package gals;

import controle.CategoriaIDEnum;
import controle.Constante;
import controle.ContextoExpressaoEnum;
import controle.ContextoLIDEnum;
import controle.PassagemValOuRefEnum;
import controle.Metodo;
import controle.MudaTipo;
import controle.OperadorAddEnum;
import controle.OperadorMultEnum;
import controle.OperadorRelEnum;
import controle.Parametro;
import controle.Simbolo;
import controle.SubCategoriaEnum;
import controle.TabelaDeSimbolos;
import controle.TipoPreDefinidoEnum;
import controle.TipoDeVariavel;
import controle.TipoDeVariavelEnum;
import controle.Variavel;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Semantico implements Constants {

    private TabelaDeSimbolos tabSimbolos;

    private int NA;
    private Stack<Integer> deslocamento = new Stack<>();
    private int primeiroID;
    private int ultimoID;
    private String ValConst;
    private int quantidadeID;
    private int NumElementos;
    private TipoPreDefinidoEnum tipoAtual;
    private SubCategoriaEnum subCategoria;
    private CategoriaIDEnum categoriaAtual;
    private ContextoLIDEnum contextoLID;
    private TipoPreDefinidoEnum TipoConst;
    private int NPF;
    private PassagemValOuRefEnum mpp;
    private List<Parametro> listaDeParametros = new ArrayList<>();
    private TipoPreDefinidoEnum TipoVar;
    private TipoPreDefinidoEnum TipoLadoEsq;
    private TipoPreDefinidoEnum retornoMetodo;
    private boolean retornoNull;
    private boolean temRetorno = false;

    //PILHAS
    private Stack<Integer> pilhaPosID = new Stack<>();
    private Stack<Integer> pilhaIdMetodo = new Stack<>();
    private Stack<Boolean> pilhaOpNega = new Stack<>();
    private Stack<Boolean> pilhaOpUnario = new Stack<>();
    private Stack<OperadorAddEnum> pilhaOpAdd = new Stack<>();
    private Stack<OperadorRelEnum> pilhaOpRel = new Stack<>();
    private Stack<OperadorMultEnum> pilhaOpMult = new Stack<>();
    private Stack<TipoPreDefinidoEnum> pilhaTipoTermo = new Stack<>();
    private Stack<TipoPreDefinidoEnum> pilhaTipoExpSimples = new Stack<>();
    private Stack<TipoPreDefinidoEnum> pilhaTipoExpr = new Stack<>();
    private Stack<ContextoExpressaoEnum> PilhaContextoEXPR = new Stack<>();
    private Stack<TipoPreDefinidoEnum> pilhaVarIndexada = new Stack<>();
    private Stack<TipoPreDefinidoEnum> pilhaTipoFator = new Stack<>();
    private Stack<Integer> pilhaNPA = new Stack<>();
    private Stack<PassagemValOuRefEnum> pilhaERef = new Stack<>();
    private Stack<Stack<Parametro>> pilhaParametrosValidar = new Stack<>();
    private Stack<Boolean> pilhaRetorno = new Stack<>();

    public void executeAction(int action, Token token) throws SemanticError {
        /* try {
         System.out.println("Ação " + action + " - token: " + token.getLexeme());
         Method executarAcaoSemantica = this.getClass().getMethod("executarAcaoSemantica" + action, Token.class);
         executarAcaoSemantica.invoke(this, token);
         } catch (InvocationTargetException e) {
         Throwable targetException = e.getTargetException();
         if (targetException instanceof SemanticError) {
         SemanticError semanticError = (SemanticError) targetException;
         //semanticError.setPosition(token.getPosition()); verificar este caso 
         throw semanticError;
         }
         e.printStackTrace();
         SemanticError semanticError = new SemanticError("Erro SEMÂNTICO: " + targetException);
         throw semanticError;
         } catch (Exception e) {
         throw new RuntimeException(e);
         }*/

        switch (action) {
            /*
             #101 – Inicializa com zero nível atual (NA) e deslocamento
             - Insere id na TS juntamente com seus atributos categoria (id - programa) e nível (NA )
             */
            case 101:
                this.tabSimbolos = new TabelaDeSimbolos();
                this.NA = 0;
                this.deslocamento.push(0);
                Simbolo id = new Simbolo(token.getLexeme(), CategoriaIDEnum.PROGRAMA, NA);
                this.NA++;
                this.tabSimbolos.addSimbolo(id);
                break;
            /*
             #102 - seta contextoLID para “decl” 
             Guarda pos. na TS do primeiro id da lista 
             */
            case 102:
                this.contextoLID = ContextoLIDEnum.DECL;
                this.primeiroID = this.tabSimbolos.getTamanho();
                this.quantidadeID = 0;
                break;
            /*
             #103 - Guarda pos. na TS do último id da lista 
             */
            case 103:
                this.ultimoID = this.primeiroID + this.quantidadeID;
                break;
            /*
             #104 - Atualiza atributos dos id de <lid> de acordo com a CategoriaAtual e com a SubCategoria. 
             Para cálculo do Deslocamento de variáveis, considere que toda variável ocupa 1 célula de memória 
             (exceto vetor que ocupa 1 célula para cada elemento)                  
             */
            case 104:
                int pos = this.primeiroID;
                int desloc = this.deslocamento.pop();
                do {
                    if (categoriaAtual == CategoriaIDEnum.CONSTANTE) {

                        Simbolo simbolo = this.tabSimbolos.getSimbolo(pos);
                        Constante nova = new Constante(simbolo.getNome(), this.categoriaAtual, simbolo.getNivel());
                        nova.setTipoPreDefinidoEnum(this.tipoAtual);
                        nova.setValor(this.ValConst);
                        this.tabSimbolos.addSimbolo(nova, pos);
                        desloc++;

                    } else if (categoriaAtual == CategoriaIDEnum.VARIAVEL) {

                        Simbolo simbolo = this.tabSimbolos.getSimbolo(pos);
                        TipoDeVariavel tipo;
                        Variavel nova = new Variavel(simbolo.getNome(), simbolo.getCategoria(), simbolo.getNivel());
                        nova.setCategoria(this.categoriaAtual);
                        nova.setDeslocamento(desloc);
                        if (this.subCategoria == SubCategoriaEnum.preDefinido) {
                            tipo = new TipoDeVariavel(MudaTipo.getTipoDeVariavelEnum(this.tipoAtual), 0, null);
                            nova.setTipo(tipo);
                            desloc++;

                        } else if (this.subCategoria == SubCategoriaEnum.cadeia) {
                            tipo = new TipoDeVariavel(MudaTipo.getTipoDeVariavelEnum(this.tipoAtual), Integer.parseInt(this.ValConst), null);
                            nova.setTipo(tipo);
                            desloc++;

                        } else if (this.subCategoria == SubCategoriaEnum.vetor) {
                            tipo = new TipoDeVariavel(TipoDeVariavelEnum.VETOR, NumElementos, this.tipoAtual);
                            nova.setTipo(tipo);
                            desloc += NumElementos;
                        }

                    }
                } while (pos != this.ultimoID);
                deslocamento.push(desloc);
                break;

            /*
             #105 - TipoAtual := “inteiro”                
             */
            case 105:
                this.tipoAtual = TipoPreDefinidoEnum.INTEIRO;
                break;
            /*
             #106 - TipoAtual := “real”                
             */
            case 106:
                this.tipoAtual = TipoPreDefinidoEnum.REAL;
                break;
            /*
             #107 - TipoAtual := “booleano”                
             */
            case 107:
                this.tipoAtual = TipoPreDefinidoEnum.BOOLEANO;
                break;
            /*
             #108 - TipoAtual := “caracter”                
             */
            case 108:
                this.tipoAtual = TipoPreDefinidoEnum.CARACTER;
                break;

            /*
             #109 - Se TipoConst <> “inteiro” então ERRO(“esperava - se uma const. inteira”)
             senão se ValConst > 256 então ERRO(“cadeia > que o permitido”) 
             senão TipoAtual := “cadeia”                
             */
            case 109:
                if (TipoConst != TipoPreDefinidoEnum.INTEIRO) {
                    throw new SemanticError("Esperava-se uma constante inteira", token.getPosition());
                } else if (Integer.parseInt(ValConst) > 256) {
                    throw new SemanticError("Cadeia maior do que o permitido", token.getPosition());
                } else {
                    this.tipoAtual = TipoPreDefinidoEnum.CADEIA;
                }
                break;
            /*
             #110 - Se TipoAtual = “cadeia” Então ERRO(“Vetor do tipo cadeia não é permitido”)
             senão SubCategoria := “vetor”                
             */
            case 110:
                if (tipoAtual == TipoPreDefinidoEnum.CADEIA) {
                    throw new SemanticError("Vetor do tipo cadeia não é permitido", token.getPosition());
                } else {
                    this.subCategoria = SubCategoriaEnum.vetor;
                }
                break;
            /*
             #111 -  Se TipoConst <> inteiro Então ERRO (“A dim.deve ser uma constante inteira”)
             Senão Seta NumElementos para ValConst            
             */
            case 111:
                if (TipoConst != TipoPreDefinidoEnum.INTEIRO) {
                    throw new SemanticError("A dimensão deve ser uma constante inteira", token.getPosition());
                } else {
                    this.NumElementos = Integer.parseInt(ValConst);
                }
                break;
            /*
             #112 -  Se TipoAtual = “cadeia” Então SubCategoria := “cadeia”
             Senão SubCategoria := “pré-definido”            
             */
            case 112:
                if (this.tipoAtual == TipoPreDefinidoEnum.CADEIA) {
                    this.subCategoria = SubCategoriaEnum.cadeia;
                } else {
                    this.subCategoria = SubCategoriaEnum.preDefinido;
                }
                break;
            /*
             #113 - Se contextoLID = “decl” entao se id já declarado no NA então ERRO(“Id já declarado”) senão insere id na TS
             - Se contextoLID = “par-formal” entao se id já declarado no NA então ERRO (“Id de parâmetro repetido”) senão incrementa NPF; insere id na TS
             - Se contextoLID = “leitura” Então se id não declarado então ERRO (“Id não declarado”) senão se categoria ou tipo invalido para leitura então ERRO(“Tipo inv. p/ leitura”)
             senão (* Gera Cód. para leitura *)            
             */
            case 113:
                if (this.contextoLID == ContextoLIDEnum.DECL) {
                    if (tabSimbolos.jaExisteSimboloNesteEscopo(token.getLexeme(), NA)) {
                        throw new SemanticError("Id já declado", token.getPosition());
                    } else {
                        Simbolo novo = new Simbolo(token.getLexeme(), categoriaAtual, NA);
                        tabSimbolos.addSimbolo(novo);
                        this.quantidadeID++;
                    }
                } else if (this.contextoLID == ContextoLIDEnum.PAR_FORMAL) {
                    if (tabSimbolos.jaExisteSimboloNesteEscopo(token.getLexeme(), NA)) {
                        throw new SemanticError("Id de parametro repetido", token.getPosition());
                    } else {
                        this.NPF++;
                        Parametro novo = new Parametro(token.getLexeme(), NA);
                        tabSimbolos.addSimbolo(novo);
                        this.quantidadeID++;
                    }
                } else if (this.contextoLID == ContextoLIDEnum.LEITURA) {
                    if (!tabSimbolos.jaExisteSimboloNesteEscopo(token.getLexeme(), NA)) {
                        throw new SemanticError("Id não declado", token.getPosition());
                    } else {
                        Simbolo novo = this.tabSimbolos.getSimboloNivel(token.getLexeme(), this.NA);
                        if (novo.getCategoria() == CategoriaIDEnum.VARIAVEL || novo.getCategoria() == CategoriaIDEnum.PARAMETRO) {
                            boolean valido = false;

                            if (novo.getCategoria() == CategoriaIDEnum.VARIAVEL) {
                                Variavel variavel = (Variavel) novo;
                                valido = !(variavel.getTipo().getTipo() == TipoDeVariavelEnum.BOOLEANO || variavel.getTipo().getTipo() == TipoDeVariavelEnum.VETOR);
                            } else if (novo.getCategoria() == CategoriaIDEnum.PARAMETRO) {
                                Parametro parametro = (Parametro) novo;
                                valido = !(parametro.getTipo() == TipoPreDefinidoEnum.BOOLEANO);
                            }

                            if (!valido) {
                                throw new SemanticError("Tipo inválido para a leitura", token.getPosition());
                            } else {
                                ///////////////////// GERA CÓDIGO PARA LEITURA /////////////////////////
                            }
                        }
                    }
                }
                break;
            /*
             #114 - Se SubCategoria = “cadeia” ou “vetor” Então ERRO (“Apenas id de tipo pré-def podem ser declarados como constante”) senão CategoriaAtual := “constante”            
             */
            case 114:
                if (this.subCategoria == SubCategoriaEnum.cadeia || this.subCategoria == SubCategoriaEnum.vetor) {
                    throw new SemanticError("Apenas id de tipo pré-definido podem ser declarados como constantes", token.getPosition());
                } else {
                    this.categoriaAtual = CategoriaIDEnum.CONSTANTE;
                }
                break;
            /*
             #115 - Se TipoConst <> TipoAtual Então ERRO (“Tipo da constante incorreto”)            
             */
            case 115:
                if (this.TipoConst != this.tipoAtual) {
                    throw new SemanticError("Tipo da constante incorreto", token.getPosition());
                }
                break;
            /*
             #116 - CategoriaAtual := “variavel”            
             */
            case 116:
                this.categoriaAtual = CategoriaIDEnum.CONSTANTE;
                break;
            /*
             #117 - Se id já está declarado no NA, então ERRO(“Id já declarado”) 
             senão insere id na TS, junto com NA e categ. zera número de parâmetros Formais (NPF) incrementa nível atual (NA := NA + 1)            
             */
            case 117:
                if (tabSimbolos.jaExisteSimboloNivel(token.getLexeme(), this.NA)) {
                    throw new SemanticError("Id já declarado", token.getPosition());
                } else {
                    Metodo novo = new Metodo(token.getLexeme(), CategoriaIDEnum.METODO, this.NA);
                    this.tabSimbolos.addSimbolo(novo);
                    this.pilhaIdMetodo.push(tabSimbolos.getTamanho() - 1);
                    this.NPF = 0;
                    this.deslocamento.push(0);
                    this.quantidadeID = 0;
                    this.listaDeParametros = new ArrayList<>();
                    this.NA++;
                }
                break;
            /*
             #118 - Atualiza num. de par. Formais (NPF) na TS          
             */
            case 118:

                break;

            /*  
             #119 - Atualiza tipo do método na TS          
             */
            case 119:

                break;

            /*  
             #120 - Atualiza tipo do método na TS          
             */
            case 120:
                this.tabSimbolos.removeNivelAtual(this.NA);
                this.NA--;
                deslocamento.pop();

                Metodo metodoAtual = (Metodo) this.tabSimbolos.getSimbolo(this.pilhaIdMetodo.peek());
                if (!metodoAtual.isRetornoNull() && !this.temRetorno) {
                    throw new SemanticError("Método sem declaração de retorno", token.getPosition());
                }

                this.pilhaRetorno.pop();
                this.temRetorno = false;
                this.pilhaIdMetodo.pop();
                break;
            /*  
             #121 - Seta contextoLID para “par-formal” Marca pos.na TS do primeiro id da lista           
             */
            case 121:
                this.contextoLID = ContextoLIDEnum.PAR_FORMAL;
                this.primeiroID = this.tabSimbolos.getTamanho();
                this.quantidadeID = 0;
                break;
            /*  
             #122 - Marca pos. na TS do último id da lista            
             */
            case 122:
                this.ultimoID = this.primeiroID + this.quantidadeID;
                break;
            /*  
             #123 - Se TipoAtual diferente de “pre-definido” Então ERRO (“Par. devem ser de tipo pré-def.”) 
             Senão Atualiza atributos dos id’s de <lid> : Cat.(“Parâmetro”), TipoAtual e MPP. Insere os par em uma lista auxiliar (ListaPar) a ser usada na chamada do método.            
             */
            case 123:
                //AQUI CHAMARIA O CASE 112, se aceitasse cadeia também!!
                if (this.subCategoria != SubCategoriaEnum.preDefinido) {
                    throw new SemanticError("Paramentros devem ser do tipo pré-definido", token.getPosition());
                } else {
                    pos = this.primeiroID;
                    do {
                        desloc = this.deslocamento.pop();

                        Simbolo novo = this.tabSimbolos.getSimbolo(pos);
                        Parametro par = new Parametro(novo.getNome(), novo.getCategoria(), novo.getNivel());
                        par.setCategoria(CategoriaIDEnum.PARAMETRO);
                        par.setTipo(this.tipoAtual);
                        par.setPassagemValOuRefEnum(mpp);
                        par.setDeslocamento(desloc);
                        desloc++;

                        this.deslocamento.push(desloc);
                        this.tabSimbolos.addSimbolo(par, pos);
                        pos++;
                        this.listaDeParametros.add(par);

                    } while (pos != this.ultimoID);
                }
                break;
            /*  
             #124 - Se TipoAtual = “cadeia” Então ERRO (“Métodos devem ser de tipo pré-def.”) Senão Seta tipo do método para TipoAtual
             */
            case 124:
                if (this.tipoAtual == TipoPreDefinidoEnum.CADEIA) {
                    throw new SemanticError("Métodos devem ser de tipo pré-definido, não cadeia", token.getPosition());
                } else {
                    this.retornoMetodo = this.tipoAtual;
                    this.retornoNull = false;
                }
                break;
            /*  
             #125 - Seta tipo do método para “nulo”
             */
            case 125:
                this.retornoNull = true;
                break;
            /*  
             #126 - Seta MPP para “referência” 
             */
            case 126:
                this.mpp = PassagemValOuRefEnum.REFERENCIA;
                break;
            /*
             #127 - Seta MPP para “valor” 
             */
            case 127:
                this.mpp = PassagemValOuRefEnum.VALOR;
                break;
            /*
             #128 - Se id não está declarado (não esta na TS) então ERRO(“Identificador não declarado”) 
             senão guarda pos ocup por id na TS em POSID
             */
            case 128:
                if (!tabSimbolos.jaExisteSimboloNesteEscopo(token.getLexeme(), this.NA)) {
                    throw new SemanticError("Id não declarado", token.getPosition());
                } else {
                    this.pilhaPosID.push(tabSimbolos.getPosicaoID(token.getLexeme(), this.NA));
                }
                break;
        }
    }

    /**
     * ***********************************************************************************************************************************************************
     */
    public void executarAcaoSemantica116(Token token) throws SemanticError {
        Metodo metodo = (Metodo) this.tabSimbolos.getSimbolo(this.pilhaIdMetodo.peek());
        metodo.setListaParametros(this.listaDeParametros);
        metodo.setNumParametros(this.NPF);
    }

    public void executarAcaoSemantica117(Token token) throws SemanticError {
        Metodo metodo = (Metodo) this.tabSimbolos.getSimbolo(this.pilhaIdMetodo.peek());
        if (!retornoNull) {
            metodo.setResultado(this.retornoMetodo);
            this.pilhaRetorno.push(true);
        } else {
            this.pilhaRetorno.push(false);
        }
        metodo.setRetornoNull(retornoNull);

    }

    public void executarAcaoSemantica127(Token token) throws SemanticError {
        TipoPreDefinidoEnum tipoExp = this.pilhaTipoExpr.pop();
        if (tipoExp != TipoPreDefinidoEnum.INTEIRO && tipoExp != TipoPreDefinidoEnum.BOOLEANO) {
            throw new SemanticError("Tipo inválido da expressão", token.getPosition());
        } else {
            //Gerar Codigo              
        }
    }

    public void executarAcaoSemantica128(Token token) {
        this.contextoLID = ContextoLIDEnum.LEITURA;
    }

    public void executarAcaoSemantica129(Token token) throws SemanticError {
        this.PilhaContextoEXPR.push(ContextoExpressaoEnum.IMPRESSAO);
        TipoPreDefinidoEnum tipoExp = this.pilhaTipoExpr.pop();
        if (tipoExp == TipoPreDefinidoEnum.BOOLEANO) {
            throw new SemanticError("tipo invalido para impressão", token.getPosition());
        } else {
            //Gerar Codigo              
        }
    }

    public void executarAcaoSemantica130(Token token) throws SemanticError {
        if (!this.pilhaRetorno.isEmpty() && this.pilhaRetorno.peek()) {
            TipoPreDefinidoEnum tipoExp = this.pilhaTipoExpr.pop();
            Simbolo simbolo = tabSimbolos.getSimbolo(this.pilhaIdMetodo.peek());
            Metodo metodo = (Metodo) simbolo;
            TipoPreDefinidoEnum tipoMetodo = metodo.getResultado();
            if (tipoExp != tipoMetodo) {
                if (!((tipoMetodo == TipoPreDefinidoEnum.REAL && tipoExp == TipoPreDefinidoEnum.INTEIRO) || (tipoMetodo == TipoPreDefinidoEnum.CADEIA && tipoExp == TipoPreDefinidoEnum.CARACTER))) {
                    throw new SemanticError("Tipo de exp inválido", token.getPosition());
                }
                temRetorno = true;
            } else {
                temRetorno = true;
                //gerar codigo
            }

        } else {
            throw new SemanticError("'Retorne' só pode ser usado em função", token.getPosition());
        }
    }

    public void executarAcaoSemantica131(Token token) throws SemanticError {
        Simbolo simbolo = tabSimbolos.getSimbolo(pilhaPosID.pop());
        if (simbolo.getCategoria() == CategoriaIDEnum.VARIAVEL || simbolo.getCategoria() == CategoriaIDEnum.PARAMETRO) {
            if (simbolo.getCategoria() == CategoriaIDEnum.VARIAVEL) {
                Variavel variavel = (Variavel) simbolo;
                if (variavel.getTipo().getTipo() == TipoDeVariavelEnum.VETOR) {
                    throw new SemanticError("id. Deveria ser indexado", token.getPosition());
                } else {
                    this.TipoLadoEsq = MudaTipo.getTipoPreDefinido(variavel.getTipo().getTipo());
                }
            }
            if (simbolo.getCategoria() == CategoriaIDEnum.PARAMETRO) {
                Parametro parametro = (Parametro) simbolo;
                this.TipoLadoEsq = parametro.getTipo();
            }
        } else {
            throw new SemanticError("id. deveria ser var ou par", token.getPosition());
        }
    }

    public void executarAcaoSemantica132(Token token) throws SemanticError {
        TipoPreDefinidoEnum tipoExpr = this.pilhaTipoExpr.pop();
        if (this.TipoLadoEsq != tipoExpr) {
            if (!((tipoExpr == TipoPreDefinidoEnum.INTEIRO && this.TipoLadoEsq == TipoPreDefinidoEnum.REAL) || (tipoExpr == TipoPreDefinidoEnum.CARACTER && this.TipoLadoEsq == TipoPreDefinidoEnum.CADEIA))) {
                throw new SemanticError("tipos incompatíveis", token.getPosition());
            }
        }
    }

    public void executarAcaoSemantica133(Token token) throws SemanticError {
        Simbolo simbolo = tabSimbolos.getSimbolo(pilhaPosID.peek());
        if (simbolo.getCategoria() != CategoriaIDEnum.VARIAVEL) {
            throw new SemanticError("esperava-se uma variável", token.getPosition());
        } else {
            Variavel variavel = (Variavel) simbolo;
            if (variavel.getTipo().getTipo() != TipoDeVariavelEnum.VETOR && variavel.getTipo().getTipo() != TipoDeVariavelEnum.CADEIA) {
                throw new SemanticError("apenas vetores e cadeias podem ser indexados", token.getPosition());
            } else {
                this.pilhaVarIndexada.push(MudaTipo.getTipoPreDefinido(variavel.getTipo().getTipo()));
            }
        }
    }

    public void executarAcaoSemantica134(Token token) throws SemanticError {
        Simbolo simbolo = tabSimbolos.getSimbolo(pilhaPosID.pop());
        Variavel variavel = (Variavel) simbolo;
        TipoPreDefinidoEnum tipoExpr = this.pilhaTipoExpr.pop();
        if (tipoExpr != TipoPreDefinidoEnum.INTEIRO) {
            throw new SemanticError("índice deveria ser inteiro", token.getPosition());
        } else {
            TipoPreDefinidoEnum tipoVarIndexada = this.pilhaVarIndexada.pop();
            if (tipoVarIndexada == TipoPreDefinidoEnum.CADEIA) {
                this.TipoLadoEsq = TipoPreDefinidoEnum.CADEIA;
            } else {
                this.TipoLadoEsq = variavel.getTipo().getTipoElementos();
            }
        }
    }

    public void executarAcaoSemantica135(Token token) throws SemanticError {
        Simbolo simbolo = tabSimbolos.getSimbolo(pilhaPosID.peek());
        if (simbolo.getCategoria() != CategoriaIDEnum.METODO) {
            throw new SemanticError("id deveria ser um método", token.getPosition());
        } else {
            Metodo metodo = (Metodo) simbolo;
            if (!metodo.isRetornoNull()) {
                throw new SemanticError("esperava-se mét sem tipo", token.getPosition());
            }
            this.pilhaParametrosValidar.push(metodo.getPilhaParametro());
            this.PilhaContextoEXPR.push(ContextoExpressaoEnum.PARAMETROATUAL);
        }
    }

    public void executarAcaoSemantica136(Token token) throws SemanticError {
        this.pilhaNPA.push(1);
        if (!this.pilhaParametrosValidar.peek().isEmpty()) {
            //this.ContextoEXPR = ContextoExpressaoEnum.PARAMETROATUAL; Verificar necessidade 
            TipoPreDefinidoEnum tipoExpr = this.pilhaTipoExpr.pop();
            Parametro parametro = this.pilhaParametrosValidar.peek().pop();
            PassagemValOuRefEnum eRef = this.pilhaERef.pop();

            if (tipoExpr != parametro.getTipo()) {
                if (!(parametro.getTipo() == TipoPreDefinidoEnum.REAL && tipoExpr == TipoPreDefinidoEnum.INTEIRO) && !(parametro.getTipo() == TipoPreDefinidoEnum.CADEIA && tipoExpr == TipoPreDefinidoEnum.CARACTER)) {
                    throw new SemanticError("Não há correpondecia entre parametro Atual e parametro Formal", token.getPosition());
                }
            }

            if (parametro.getPassagemValOuRefEnum() != eRef) {
                if (parametro.getPassagemValOuRefEnum() == PassagemValOuRefEnum.REFERENCIA) {
                    throw new SemanticError("Esperava passagem de Parametro por Referencia", token.getPosition());
                }
            }
        }
    }

    public void executarAcaoSemantica137(Token token) throws SemanticError {
        int NPA = this.pilhaNPA.pop();
        Simbolo simbolo = tabSimbolos.getSimbolo(pilhaPosID.pop());
        Metodo metodo = (Metodo) simbolo;
        if (NPA != metodo.getNumParametros()) {
            throw new SemanticError("Erro na quant. de parâmetros", token.getPosition());
        }
        this.PilhaContextoEXPR.pop();
        this.pilhaParametrosValidar.pop();
    }

    public void executarAcaoSemantica138(Token token) throws SemanticError {
        Simbolo simbolo = tabSimbolos.getSimbolo(pilhaPosID.pop());
        if (simbolo.getCategoria() != CategoriaIDEnum.METODO) {
            throw new SemanticError("id deveria ser um método", token.getPosition());
        } else {
            Metodo metodo = (Metodo) simbolo;
            if (!metodo.isRetornoNull()) {
                throw new SemanticError("esperava-se método sem tipo", token.getPosition());
            } else {
                if (metodo.getNumParametros() > 0) {
                    throw new SemanticError("Erro na quantidade de parametros", token.getPosition());
                } else {
                    //Gerar Codigo 
                }
            }
        }
    }

    public void executarAcaoSemantica139(Token token) throws SemanticError {
        if (this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL) {

            int NPA = this.pilhaNPA.pop();
            if (!this.pilhaParametrosValidar.peek().isEmpty()) {
                TipoPreDefinidoEnum tipoExpr = this.pilhaTipoExpr.pop();
                Parametro parametro = this.pilhaParametrosValidar.peek().pop();
                PassagemValOuRefEnum eRef = this.pilhaERef.pop();

                if (tipoExpr != parametro.getTipo()) {
                    if (!(parametro.getTipo() == TipoPreDefinidoEnum.REAL && tipoExpr == TipoPreDefinidoEnum.INTEIRO) && !(parametro.getTipo() == TipoPreDefinidoEnum.CADEIA && tipoExpr == TipoPreDefinidoEnum.CARACTER)) {
                        throw new SemanticError("Não há correpondecia entre parametro Atual e parametro Formal", token.getPosition());
                    }
                }

                if (parametro.getPassagemValOuRefEnum() != eRef) {
                    if (parametro.getPassagemValOuRefEnum() == PassagemValOuRefEnum.REFERENCIA) {
                        throw new SemanticError("Esperava passagem por Referencia", token.getPosition());
                    }
                }
            }
            NPA++;
            this.pilhaNPA.push(NPA);
        }
        if (this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.IMPRESSAO) {
            TipoPreDefinidoEnum tipoExpr = this.pilhaTipoExpr.pop();
            if (tipoExpr == TipoPreDefinidoEnum.BOOLEANO) {
                throw new SemanticError("tipo invalido para impressão", token.getPosition());
            }
        }
    }

    public void executarAcaoSemantica140(Token token) {
        this.pilhaTipoExpr.push(this.pilhaTipoExpSimples.pop());
    }

    public void executarAcaoSemantica141(Token token) throws SemanticError {
        OperadorRelEnum operador = this.pilhaOpRel.pop();
        TipoPreDefinidoEnum tipoExp = this.pilhaTipoExpr.pop();
        TipoPreDefinidoEnum tipoExpSimples = this.pilhaTipoExpSimples.pop();

        /*if(operador == OperadorRelEnum.Operador_Maior || operador == OperadorRelEnum.Operador_Maior_Igual || operador == OperadorRelEnum.Operador_Menor || operador == OperadorRelEnum.Operador_Menor_Igual)
         {
         if(((tipoExp != TipoPreDefinidoEnum.INTEIRO && tipoExpSimples != TipoPreDefinidoEnum.REAL) || (tipoExp != TipoPreDefinidoEnum.INTEIRO && tipoExpSimples != TipoPreDefinidoEnum.REAL)) && (tipoExp != TipoPreDefinidoEnum.BOOLEANO && tipoExpSimples != TipoPreDefinidoEnum.BOOLEANO))
         {
         throw new SemanticError("Operandos incompatíveis", token.getPosition());
         }
         else
         {
         this.pilhaTipoExpr.push(TipoPreDefinidoEnum.BOOLEANO);
         }  
         }*/
        //if(operador == OperadorRelEnum.OPERADORIGUAL || operador == OperadorRelEnum.Operador_Diferente)
        //{
        if (tipoExp != tipoExpSimples) {
            if ((tipoExp == TipoPreDefinidoEnum.INTEIRO && tipoExpSimples == TipoPreDefinidoEnum.REAL) || (tipoExp == TipoPreDefinidoEnum.REAL && tipoExpSimples == TipoPreDefinidoEnum.INTEIRO)
                    || (tipoExp == TipoPreDefinidoEnum.CADEIA && tipoExpSimples == TipoPreDefinidoEnum.CARACTER) || (tipoExp == TipoPreDefinidoEnum.CARACTER && tipoExpSimples == TipoPreDefinidoEnum.CADEIA)) {
                this.pilhaTipoExpr.push(TipoPreDefinidoEnum.BOOLEANO);
            } else {
                throw new SemanticError("Operandos incompatíveis", token.getPosition());
            }
        } else {
            this.pilhaTipoExpr.push(TipoPreDefinidoEnum.BOOLEANO);
        }
        //}

        if (!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL) {
            this.pilhaERef.pop();
            this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
        }
    }

    public void executarAcaoSemantica142(Token token) {
        this.pilhaOpRel.push(OperadorRelEnum.OPERADORIGUAL);
    }

    public void executarAcaoSemantica143(Token token) {
        this.pilhaOpRel.push(OperadorRelEnum.OPERADORMENOR);
    }

    public void executarAcaoSemantica144(Token token) {
        this.pilhaOpRel.push(OperadorRelEnum.OPERADORMAIOR);
    }

    public void executarAcaoSemantica145(Token token) {
        this.pilhaOpRel.push(OperadorRelEnum.OPERADORMAIORIGUAL);
    }

    public void executarAcaoSemantica146(Token token) {
        this.pilhaOpRel.push(OperadorRelEnum.OPERADORMENORIGUAL);
    }

    public void executarAcaoSemantica147(Token token) {
        this.pilhaOpRel.push(OperadorRelEnum.OPERADORDIFERENTE);
    }

    public void executarAcaoSemantica148(Token token) {
        this.pilhaTipoExpSimples.push(this.pilhaTipoTermo.pop());
    }

    public void executarAcaoSemantica149(Token token) throws SemanticError {
        OperadorAddEnum operador = this.pilhaOpAdd.peek();
        TipoPreDefinidoEnum tipo = this.pilhaTipoExpSimples.peek();

        if ((operador == OperadorAddEnum.OPERADORADICAO || operador == OperadorAddEnum.OPERADORSUBTRACAO) && (tipo != TipoPreDefinidoEnum.INTEIRO && tipo != TipoPreDefinidoEnum.REAL)) {
            throw new SemanticError("Op. e Operando incompatíveis", token.getPosition());
        }

        if (operador == OperadorAddEnum.OPERADOROU && tipo != TipoPreDefinidoEnum.BOOLEANO) {
            throw new SemanticError("Op. e Operando incompatíveis", token.getPosition());
        }

        if (!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL) {
            this.pilhaERef.pop();
            this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
        }
    }

    public void executarAcaoSemantica150(Token token) throws SemanticError {
        OperadorAddEnum operador = this.pilhaOpAdd.pop();
        TipoPreDefinidoEnum tipoExpSimples = this.pilhaTipoExpSimples.pop();
        TipoPreDefinidoEnum tipoTermo = this.pilhaTipoTermo.pop();

        if (operador == OperadorAddEnum.OPERADORADICAO || operador == OperadorAddEnum.OPERADORSUBTRACAO) {
            if ((tipoExpSimples != TipoPreDefinidoEnum.INTEIRO && tipoExpSimples != TipoPreDefinidoEnum.REAL) || (tipoTermo != TipoPreDefinidoEnum.INTEIRO && tipoTermo != TipoPreDefinidoEnum.REAL)) {
                throw new SemanticError("Operandos incompatíveis", token.getPosition());
            } else {
                if (tipoExpSimples == TipoPreDefinidoEnum.REAL || tipoTermo == TipoPreDefinidoEnum.REAL) {
                    this.pilhaTipoExpSimples.push(TipoPreDefinidoEnum.REAL);
                } else {
                    this.pilhaTipoExpSimples.push(TipoPreDefinidoEnum.INTEIRO);
                }
            }
        }
        if (operador == OperadorAddEnum.OPERADOROU) {
            if (tipoTermo != TipoPreDefinidoEnum.BOOLEANO) {
                throw new SemanticError("Operandos incompatíveis", token.getPosition());
            } else {
                this.pilhaTipoExpSimples.push(TipoPreDefinidoEnum.BOOLEANO);
            }
        }
    }

    public void executarAcaoSemantica151(Token token) {
        this.pilhaOpAdd.push(OperadorAddEnum.OPERADORADICAO);
    }

    public void executarAcaoSemantica152(Token token) {
        this.pilhaOpAdd.push(OperadorAddEnum.OPERADORSUBTRACAO);
    }

    public void executarAcaoSemantica153(Token token) {
        this.pilhaOpAdd.push(OperadorAddEnum.OPERADOROU);
    }

    public void executarAcaoSemantica154(Token token) {
        this.pilhaTipoTermo.push(this.pilhaTipoFator.pop());
    }

    public void executarAcaoSemantica155(Token token) throws SemanticError {
        OperadorMultEnum operador = this.pilhaOpMult.peek();
        TipoPreDefinidoEnum tipo = this.pilhaTipoTermo.peek();
        if ((operador == OperadorMultEnum.OPERADORMULTIPLICACAO || operador == OperadorMultEnum.OPERADORDIVISAO) && (tipo != TipoPreDefinidoEnum.INTEIRO && tipo != TipoPreDefinidoEnum.REAL)) {
            throw new SemanticError("Op. e Operando incompatíveis", token.getPosition());
        }
        if (operador == OperadorMultEnum.OPERADORDIV && tipo != TipoPreDefinidoEnum.INTEIRO) {
            throw new SemanticError("Op. e Operando incompatíveis", token.getPosition());
        }
        if (operador == OperadorMultEnum.OPERADORE && tipo != TipoPreDefinidoEnum.BOOLEANO) {
            throw new SemanticError("Op. e Operando incompatíveis", token.getPosition());
        }

        if (!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL) {
            this.pilhaERef.pop();
            this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
        }
    }

    public void executarAcaoSemantica156(Token token) throws SemanticError {
        TipoPreDefinidoEnum tipoTermo = this.pilhaTipoTermo.pop();
        TipoPreDefinidoEnum tipoFator = this.pilhaTipoFator.pop();
        OperadorMultEnum operador = this.pilhaOpMult.pop();
        if (operador == OperadorMultEnum.OPERADORMULTIPLICACAO || operador == OperadorMultEnum.OPERADORDIVISAO) {
            if ((tipoTermo != TipoPreDefinidoEnum.INTEIRO && tipoTermo != TipoPreDefinidoEnum.REAL) || (tipoFator != TipoPreDefinidoEnum.INTEIRO && tipoFator != TipoPreDefinidoEnum.REAL)) {
                throw new SemanticError("Operandos incompatíveis", token.getPosition());
            } else {
                if ((tipoTermo == TipoPreDefinidoEnum.REAL || tipoFator == TipoPreDefinidoEnum.REAL) || operador == OperadorMultEnum.OPERADORDIVISAO) {
                    this.pilhaTipoTermo.push(TipoPreDefinidoEnum.REAL);
                } else {
                    this.pilhaTipoTermo.push(TipoPreDefinidoEnum.INTEIRO);
                }
            }
        }
        if (operador == OperadorMultEnum.OPERADORDIV) {
            if (tipoTermo != TipoPreDefinidoEnum.INTEIRO || tipoFator != TipoPreDefinidoEnum.INTEIRO) {
                throw new SemanticError("Operandos incompatíveis", token.getPosition());
            } else {
                this.pilhaTipoTermo.push(TipoPreDefinidoEnum.INTEIRO);
            }
        }
        if (operador == OperadorMultEnum.OPERADORE) {
            if (tipoTermo != TipoPreDefinidoEnum.BOOLEANO || tipoFator != TipoPreDefinidoEnum.BOOLEANO) {
                throw new SemanticError("Operandos incompatíveis", token.getPosition());
            } else {
                this.pilhaTipoTermo.push(TipoPreDefinidoEnum.BOOLEANO);
            }
        }
    }

    public void executarAcaoSemantica157(Token token) {
        this.pilhaOpMult.push(OperadorMultEnum.OPERADORMULTIPLICACAO);
    }

    public void executarAcaoSemantica158(Token token) {
        this.pilhaOpMult.push(OperadorMultEnum.OPERADORDIVISAO);
    }

    public void executarAcaoSemantica159(Token token) {
        this.pilhaOpMult.push(OperadorMultEnum.OPERADORDIV);
    }

    public void executarAcaoSemantica160(Token token) {
        this.pilhaOpMult.push(OperadorMultEnum.OPERADORE);
    }

    public void executarAcaoSemantica161(Token token) throws SemanticError {
        if (!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) {
            throw new SemanticError("Op. 'não' repetido – não pode!", token.getPosition());
        } else {
            this.pilhaOpNega.push(true);
            if (!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL) {
                this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
            }
        }
    }

    public void executarAcaoSemantica162(Token token) throws SemanticError {
        TipoPreDefinidoEnum tipo = this.pilhaTipoFator.peek();
        if (tipo != TipoPreDefinidoEnum.BOOLEANO) {
            throw new SemanticError("Op. ‘não’ exige operando bool.", token.getPosition());
        }
        this.pilhaOpNega.pop();
    }

    public void executarAcaoSemantica163(Token token) throws SemanticError {
        if (!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek()) {
            throw new SemanticError("Op. 'unário' repetido", token.getPosition());
        } else {
            this.pilhaOpUnario.push(true);
            if (!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL) {
                this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
            }
        }
    }

    public void executarAcaoSemantica164(Token token) throws SemanticError {
        TipoPreDefinidoEnum tipo = this.pilhaTipoFator.peek();
        if (tipo != TipoPreDefinidoEnum.INTEIRO && tipo != TipoPreDefinidoEnum.REAL) {
            throw new SemanticError("Op. unário exige operando num.", token.getPosition());
        }
        this.pilhaOpUnario.pop();
    }

    public void executarAcaoSemantica165(Token token) {
        this.pilhaOpNega.push(false);
        this.pilhaOpUnario.push(false);
    }

    public void executarAcaoSemantica166(Token token) {
        this.pilhaTipoFator.push(this.pilhaTipoExpr.pop());
        this.pilhaOpNega.pop();
        this.pilhaOpUnario.pop();
        if (!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL) {
            if ((!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) || (!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek())) {
                this.pilhaERef.pop();
                this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
            }

        }

    }

    public void executarAcaoSemantica167(Token token) throws SemanticError {
        this.pilhaTipoFator.push(TipoVar);
    }

    public void executarAcaoSemantica168(Token token) throws SemanticError {
        this.pilhaTipoFator.push(this.TipoConst);

        if (!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL) {
            if (!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek())) {
                this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
            }
        }
    }

    public void executarAcaoSemantica169(Token token) throws SemanticError {
        Simbolo simbolo = tabSimbolos.getSimbolo(this.pilhaPosID.peek());
        if (simbolo.getCategoria() != CategoriaIDEnum.METODO) {
            throw new SemanticError("id deveria ser um método", token.getPosition());
        } else {
            Metodo metodo = (Metodo) simbolo;
            if (metodo.isRetornoNull()) {
                throw new SemanticError("esperava-se mét. com tipo", token.getPosition());
            }
            this.pilhaParametrosValidar.push(metodo.getPilhaParametro());
        }

        this.PilhaContextoEXPR.push(ContextoExpressaoEnum.PARAMETROATUAL);
    }

    public void executarAcaoSemantica170(Token token) throws SemanticError {
        int NPA = this.pilhaNPA.pop();
        Simbolo simbolo = tabSimbolos.getSimbolo(pilhaPosID.pop());
        Metodo metodo = (Metodo) simbolo;
        if (NPA == metodo.getNumParametros()) {
            this.TipoVar = metodo.getResultado();
        } else {
            throw new SemanticError("Erro na quant. de parâmetros", token.getPosition());
        }
        this.PilhaContextoEXPR.push(ContextoExpressaoEnum.PARAMETROATUAL);

        if (!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL) {
            if (!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek())) {
                this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
            }
        }

        this.pilhaParametrosValidar.pop();
    }

    public void executarAcaoSemantica171(Token token) throws SemanticError {
        if (this.pilhaTipoExpr.pop() != TipoPreDefinidoEnum.INTEIRO) {
            throw new SemanticError("índice deveria ser inteiro", token.getPosition());
        } else {
            if (this.pilhaVarIndexada.pop() == TipoPreDefinidoEnum.CADEIA) {
                this.TipoVar = TipoPreDefinidoEnum.CARACTER;
                this.pilhaPosID.pop();
            } else {
                Variavel variavel = (Variavel) this.tabSimbolos.getSimbolo(this.pilhaPosID.pop());
                this.TipoVar = variavel.getTipo().getTipoElementos();
            }
        }

        if (!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL) {
            if (!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek())) {
                this.pilhaERef.push(PassagemValOuRefEnum.REFERENCIA);
            }
        }
    }

    public void executarAcaoSemantica172(Token token) throws SemanticError {
        Simbolo simbolo = this.tabSimbolos.getSimbolo(this.pilhaPosID.pop());
        if (simbolo.getCategoria() == CategoriaIDEnum.PARAMETRO || simbolo.getCategoria() == CategoriaIDEnum.VARIAVEL) {
            if (simbolo.getCategoria() == CategoriaIDEnum.VARIAVEL) {
                Variavel variavel = (Variavel) simbolo;
                if (variavel.getTipo().getTipo() == TipoDeVariavelEnum.VETOR) {
                    throw new SemanticError("vetor deve ser indexado", token.getPosition());
                } else {
                    TipoVar = MudaTipo.getTipoPreDefinido(variavel.getTipo().getTipo());
                }
            } else {
                Parametro parametro = (Parametro) simbolo;
                TipoVar = parametro.getTipo();
            }

            if (!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL) {
                if (!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek())) {
                    this.pilhaERef.push(PassagemValOuRefEnum.REFERENCIA);
                }
            }

        } else {
            if (simbolo.getCategoria() == CategoriaIDEnum.METODO) {
                Metodo metodo = (Metodo) simbolo;
                if (metodo.isRetornoNull()) {
                    throw new SemanticError("Esperava-se método com tipo", token.getPosition());
                } else {
                    if (metodo.getNumParametros() != 0) {
                        throw new SemanticError("Erro na quant. de parâmetros", token.getPosition());
                    } else {
                        TipoVar = metodo.getResultado();
                    }

                    if (!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL) {
                        if (!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek())) {
                            this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
                        }
                    }
                }

            } else {
                if (simbolo.getCategoria() == CategoriaIDEnum.CONSTANTE) {
                    Constante constante = (Constante) simbolo;
                    TipoVar = constante.getTipoPreDefinidoEnum();
                    if (!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL) {

                        if (!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek())) {
                            this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
                        }
                    }
                } else {
                    throw new SemanticError("esperava-se var,id-método ou constante", token.getPosition());
                }
            }
        }
    }

    /**
     * TipoConst := tipo da constante ValConst := valor da constante
     */
    public void executarAcaoSemantica173(Token token) throws SemanticError {
        Simbolo s = this.tabSimbolos.getSimboloNivel(token.getLexeme(), this.NA);
        if (s == null) {
            throw new SemanticError("Id não declarado", token.getPosition());
        } else {
            if (s.getCategoria() != CategoriaIDEnum.CONSTANTE) {
                throw new SemanticError("id de Constante esperado", token.getPosition());
            } else {
                Constante c = (Constante) s;
                this.TipoConst = c.getTipoPreDefinidoEnum();
                this.ValConst = c.getValor();
            }
        }
    }

    /**
     * TipoConst := tipo da constante ValConst := valor da constante
     */
    public void executarAcaoSemantica174(Token token) {
        this.TipoConst = TipoPreDefinidoEnum.INTEIRO;
        this.ValConst = token.getLexeme();
    }

    public void executarAcaoSemantica175(Token token) {
        this.TipoConst = TipoPreDefinidoEnum.REAL;
        this.ValConst = token.getLexeme();
    }

    public void executarAcaoSemantica176(Token token) {
        this.TipoConst = TipoPreDefinidoEnum.BOOLEANO;
        this.ValConst = token.getLexeme();
    }

    public void executarAcaoSemantica177(Token token) {
        this.TipoConst = TipoPreDefinidoEnum.BOOLEANO;
        this.ValConst = token.getLexeme();
    }

    public void executarAcaoSemantica178(Token token) {
        if (token.getLexeme().length() - 2 == 1) //testar isso 
        {
            this.TipoConst = TipoPreDefinidoEnum.CARACTER;
        } else {
            this.TipoConst = TipoPreDefinidoEnum.CADEIA;
        }
        this.ValConst = token.getLexeme();
    }
}
