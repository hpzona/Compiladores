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
    private int NPA;
    private PassagemValOuRefEnum mpp;
    private List<Parametro> listaDeParametros = new ArrayList<>();
    private TipoPreDefinidoEnum TipoVar;
    private TipoPreDefinidoEnum TipoLadoEsq;
    private TipoPreDefinidoEnum retornoMetodo;
    private TipoPreDefinidoEnum tipoExp;
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
    private Stack<ContextoExpressaoEnum> pilhaContextoExpr = new Stack<>();
    private Stack<TipoPreDefinidoEnum> pilhaTipoVarIndexada = new Stack<>();
    private Stack<TipoPreDefinidoEnum> pilhaTipoFator = new Stack<>();
    private Stack<Integer> pilhaNPA = new Stack<>();
    private Stack<PassagemValOuRefEnum> pilhaERef = new Stack<>();
    private Stack<Stack<Parametro>> pilhaParametrosValidar = new Stack<>();
    private Stack<Boolean> pilhaRetorno = new Stack<>();

    //AUX
    private Simbolo simboloAux;
    private Parametro parametroAux;
    private Metodo metodoAux;
    private Variavel variavelAux;
    private Constante constanteAux;
    private OperadorRelEnum operadorAux;
    private TipoPreDefinidoEnum tipoExprAux;
    private TipoPreDefinidoEnum tipoTermoAux;
    private TipoPreDefinidoEnum tipoFatorAux;

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

                        this.simboloAux = this.tabSimbolos.getSimbolo(pos);
                        this.constanteAux = new Constante(this.simboloAux.getNome(), this.categoriaAtual, this.simboloAux.getNivel());
                        this.constanteAux.setTipoPreDefinidoEnum(this.tipoAtual);
                        this.constanteAux.setValor(this.ValConst);
                        this.tabSimbolos.addSimbolo(this.constanteAux, pos);
                        desloc++;

                    } else if (categoriaAtual == CategoriaIDEnum.VARIAVEL) {

                        this.simboloAux = this.tabSimbolos.getSimbolo(pos);
                        TipoDeVariavel tipo;
                        this.variavelAux = new Variavel(this.simboloAux.getNome(), this.simboloAux.getCategoria(), this.simboloAux.getNivel());
                        this.variavelAux.setCategoria(this.categoriaAtual);
                        this.variavelAux.setDeslocamento(desloc);
                        if (this.subCategoria == SubCategoriaEnum.preDefinido) {
                            tipo = new TipoDeVariavel(MudaTipo.getTipoDeVariavelEnum(this.tipoAtual), 0, null);
                            this.variavelAux.setTipo(tipo);
                            desloc++;

                        } else if (this.subCategoria == SubCategoriaEnum.cadeia) {
                            tipo = new TipoDeVariavel(MudaTipo.getTipoDeVariavelEnum(this.tipoAtual), Integer.parseInt(this.ValConst), null);
                            this.variavelAux.setTipo(tipo);
                            desloc++;

                        } else if (this.subCategoria == SubCategoriaEnum.vetor) {
                            tipo = new TipoDeVariavel(TipoDeVariavelEnum.VETOR, NumElementos, this.tipoAtual);
                            this.variavelAux.setTipo(tipo);
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
                this.tipoAtual = TipoPreDefinidoEnum.NUM_INT;
                break;
            /*
             #106 - TipoAtual := “real”                
             */
            case 106:
                this.tipoAtual = TipoPreDefinidoEnum.NUM_REAL;
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
                if (TipoConst != TipoPreDefinidoEnum.NUM_INT) {
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
                if (TipoConst != TipoPreDefinidoEnum.NUM_INT) {
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
                        this.simboloAux = new Simbolo(token.getLexeme(), categoriaAtual, NA);
                        tabSimbolos.addSimbolo(this.simboloAux);
                        this.quantidadeID++;
                    }
                } else if (this.contextoLID == ContextoLIDEnum.PAR_FORMAL) {
                    if (tabSimbolos.jaExisteSimboloNesteEscopo(token.getLexeme(), NA)) {
                        throw new SemanticError("Id de parametro repetido", token.getPosition());
                    } else {
                        this.NPF++;
                        this.parametroAux = new Parametro(token.getLexeme(), NA);
                        tabSimbolos.addSimbolo(this.parametroAux);
                        this.quantidadeID++;
                    }
                } else if (this.contextoLID == ContextoLIDEnum.LEITURA) {
                    if (!tabSimbolos.jaExisteSimboloNesteEscopo(token.getLexeme(), NA)) {
                        throw new SemanticError("Id não declado", token.getPosition());
                    } else {
                        this.simboloAux = this.tabSimbolos.getSimboloNivel(token.getLexeme(), this.NA);
                        if (this.simboloAux.getCategoria() == CategoriaIDEnum.VARIAVEL || this.simboloAux.getCategoria() == CategoriaIDEnum.PARAMETRO) {
                            boolean valido = false;

                            if (this.simboloAux.getCategoria() == CategoriaIDEnum.VARIAVEL) {
                                this.variavelAux = (Variavel) this.simboloAux;
                                valido = !(this.variavelAux.getTipo().getTipo() == TipoDeVariavelEnum.BOOLEANO || this.variavelAux.getTipo().getTipo() == TipoDeVariavelEnum.VETOR);
                            } else if (this.simboloAux.getCategoria() == CategoriaIDEnum.PARAMETRO) {
                                this.parametroAux = (Parametro) this.simboloAux;
                                valido = !(this.parametroAux.getTipo() == TipoPreDefinidoEnum.BOOLEANO);
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
                    this.metodoAux = new Metodo(token.getLexeme(), CategoriaIDEnum.METODO, this.NA);
                    this.tabSimbolos.addSimbolo(this.metodoAux);
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
                this.metodoAux = (Metodo) this.tabSimbolos.getSimbolo(this.pilhaIdMetodo.peek());
                this.metodoAux.setListaParametros(this.listaDeParametros);
                this.metodoAux.setNumParametros(this.NPF);
                break;

            /*  
             #119 - Atualiza tipo do método na TS          
             */
            case 119:
                this.metodoAux = (Metodo) this.tabSimbolos.getSimbolo(this.pilhaIdMetodo.peek());
                if (!retornoNull) {
                    this.metodoAux.setResultado(this.retornoMetodo);
                    this.pilhaRetorno.push(true);
                } else {
                    this.pilhaRetorno.push(false);
                }
                this.metodoAux.setRetornoNull(retornoNull);
                break;

            /*  
             #120 - Atualiza tipo do método na TS          
             */
            case 120:
                this.tabSimbolos.removeNivelAtual(this.NA);
                this.NA--;
                deslocamento.pop();

                this.metodoAux = (Metodo) this.tabSimbolos.getSimbolo(this.pilhaIdMetodo.peek());
                if (!this.metodoAux.isRetornoNull() && !this.temRetorno) {
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

                        this.simboloAux = this.tabSimbolos.getSimbolo(pos);
                        this.parametroAux = new Parametro(this.simboloAux.getNome(), this.simboloAux.getCategoria(), this.simboloAux.getNivel());
                        this.parametroAux.setCategoria(CategoriaIDEnum.PARAMETRO);
                        this.parametroAux.setTipo(this.tipoAtual);
                        this.parametroAux.setPassagemValOuRefEnum(mpp);
                        this.parametroAux.setDeslocamento(desloc);
                        desloc++;

                        this.deslocamento.push(desloc);
                        this.tabSimbolos.addSimbolo(this.parametroAux, pos);
                        pos++;
                        this.listaDeParametros.add(this.parametroAux);

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

            /*
             #129 - Se TipoExpr <> “booleano” e <> “inteiro” então ERRO(“Tipo inválido da expressão”) 
             senao (* ação de G. Código *)
             */
            case 129:
                this.tipoExp = this.pilhaTipoExpr.pop();
                if (this.tipoExp != TipoPreDefinidoEnum.BOOLEANO && this.tipoExp != TipoPreDefinidoEnum.NUM_INT) {
                    throw new SemanticError("Tipo inválido da expressão", token.getPosition());
                } else {
                    ///////////////////// GERA CÓDIGO /////////////////////////
                }
                break;
            /*
             #130 - Seta ContextoLID para “Leitura”
             */
            case 130:
                this.contextoLID = ContextoLIDEnum.LEITURA;
                break;
            /*
             #131 - Seta ContextoEXPR para “impressão”
             */
            case 131:
                this.pilhaContextoExpr.push(ContextoExpressaoEnum.IMPRESSAO);
                break;
            /*
             #132 - Se está fora do escopo de um método com tipo Então ERRO (“Retorne” só pode ser usado em Método com tipo”) 
             Senão se TipoExpr <> tipo do método então ERRO(“Tipo de retorno inválido”)
             senao (* ação de Geração de Código *)
             */
            case 132:
                if (this.pilhaRetorno.isEmpty() && !this.pilhaRetorno.peek()) {
                    throw new SemanticError("'Retorne' só pode ser usado em Metodo com tipo", token.getPosition());
                } else {
                    this.tipoExprAux = this.pilhaTipoExpr.pop();
                    this.simboloAux = tabSimbolos.getSimbolo(this.pilhaIdMetodo.peek());
                    Metodo metodo = (Metodo) this.simboloAux;
                    TipoPreDefinidoEnum tipoMetodo = metodo.getResultado();
                    if (this.tipoExprAux != tipoMetodo) {
                        if (!((tipoMetodo == TipoPreDefinidoEnum.NUM_REAL && this.tipoExprAux == TipoPreDefinidoEnum.NUM_INT) || (tipoMetodo == TipoPreDefinidoEnum.CADEIA && this.tipoExprAux == TipoPreDefinidoEnum.CARACTER))) {
                            throw new SemanticError("Tipo de retorno inválido", token.getPosition());
                        }
                        temRetorno = true;
                    } else {
                        temRetorno = true;
                        ///////////////////// GERA CÓDIGO /////////////////////////
                    }
                }
                break;

            /*
             #133 - Se categ. de id = “Variável” ou “Parâmetro” então se tipo de id = “vetor” então ERRO (“id. Deveria ser indexado”)
             senão TipoLadoEsq := tipo de id
             senão ERRO (“id. deveria ser var ou par”)       
             */
            case 133:
                this.simboloAux = tabSimbolos.getSimbolo(pilhaPosID.pop());
                if (this.simboloAux.getCategoria() == CategoriaIDEnum.VARIAVEL || this.simboloAux.getCategoria() == CategoriaIDEnum.PARAMETRO) {
                    if (this.simboloAux.getCategoria() == CategoriaIDEnum.VARIAVEL) {
                        this.variavelAux = (Variavel) this.simboloAux;
                        if (this.variavelAux.getTipo().getTipo() == TipoDeVariavelEnum.VETOR) {
                            throw new SemanticError("Id deveria ser indexado", token.getPosition());
                        } else {
                            this.TipoLadoEsq = MudaTipo.getTipoPreDefinido(this.variavelAux.getTipo().getTipo());
                        }
                    }
                    if (this.simboloAux.getCategoria() == CategoriaIDEnum.PARAMETRO) {
                        this.parametroAux = (Parametro) this.simboloAux;
                        this.TipoLadoEsq = this.parametroAux.getTipo();
                    }
                } else {
                    throw new SemanticError("Id deveria ser variável ou parametro", token.getPosition());
                }
                break;

            /*
             #134 – se TipoExpr não compatível com tipoLadoesq então ERRO (“tipos incompatíveis”) 
             senão (* G. Código *)
             */
            case 134:
                if (this.TipoLadoEsq != this.tipoExp) {
                    if (!((this.tipoExp == TipoPreDefinidoEnum.NUM_INT && this.TipoLadoEsq == TipoPreDefinidoEnum.NUM_REAL) || (this.tipoExp == TipoPreDefinidoEnum.CARACTER && this.TipoLadoEsq == TipoPreDefinidoEnum.CADEIA))) {
                        throw new SemanticError("Tipos incompatíveis na expressão", token.getPosition());
                    }
                } else {
                    ///////////////////// GERA CÓDIGO /////////////////////////                    
                }
                break;

            /*
             #135 – se categoria de id <> “variável” então ERRO (“esperava-se uma variável”) 
             senao se tipo de id <> vetor e <> de cadeia então ERRO(“apenas vetores e cadeias podem ser indexados”) 
             senão TipoVarIndexada = tipo de id (vetor ou cadeia) 
             */
            case 135:
                this.simboloAux = tabSimbolos.getSimbolo(pilhaPosID.peek());
                if (this.simboloAux.getCategoria() != CategoriaIDEnum.VARIAVEL) {
                    throw new SemanticError("esperava-se uma variável", token.getPosition());
                } else {
                    this.variavelAux = (Variavel) this.simboloAux;
                    if (this.variavelAux.getTipo().getTipo() != TipoDeVariavelEnum.VETOR && this.variavelAux.getTipo().getTipo() != TipoDeVariavelEnum.CADEIA) {
                        throw new SemanticError("apenas vetores e cadeias podem ser indexados", token.getPosition());
                    } else {
                        this.pilhaTipoVarIndexada.push(MudaTipo.getTipoPreDefinido(this.variavelAux.getTipo().getTipo()));
                    }
                }
                break;

            /*
             #136 – se TipoExpr <> “inteiro” então ERRO(“índice deveria ser inteiro”) 
             senão se TipoVarIndexada = cadeia então TipoLadoEsq := “caracter” 
             senao TipoLadoEsq := TipoElementos do vetor
             */
            case 136:
                this.simboloAux = tabSimbolos.getSimbolo(pilhaPosID.pop());
                this.variavelAux = (Variavel) this.simboloAux;
                this.tipoExp = this.pilhaTipoExpr.pop();
                if (this.tipoExp != TipoPreDefinidoEnum.NUM_INT) {
                    throw new SemanticError("índice deveria ser inteiro", token.getPosition());
                } else {
                    TipoPreDefinidoEnum tipoVarIndexada = this.pilhaTipoVarIndexada.pop();
                    if (tipoVarIndexada == TipoPreDefinidoEnum.CADEIA) {
                        this.TipoLadoEsq = TipoPreDefinidoEnum.CADEIA;
                    } else {
                        this.TipoLadoEsq = this.variavelAux.getTipo().getTipoElementos();
                    }
                }
                break;

            /*
             #137 – se categoria de id <> método então ERRO(“id deveria ser um método”) 
             senão se tipo do método <> nulo então ERRO(“esperava-se mét sem tipo”)
             */
            case 137:
                this.simboloAux = tabSimbolos.getSimbolo(pilhaPosID.peek());
                if (this.simboloAux.getCategoria() != CategoriaIDEnum.METODO) {
                    throw new SemanticError("Id deveria ser um método", token.getPosition());
                } else {
                    Metodo metodo = (Metodo) this.simboloAux;
                    if (!metodo.isRetornoNull()) {
                        throw new SemanticError("Esperava-se método sem tipo", token.getPosition());
                    }
                    this.pilhaParametrosValidar.push(metodo.getPilhaParametro());
                    this.pilhaContextoExpr.push(ContextoExpressaoEnum.PAR_ATUAL);
                }
                break;

            /*
             #138 – NPA := 0 (Número de Parâmetros Atuais) seta contextoEXPR para “par-atual” 
             */
            case 138:
                this.pilhaNPA.clear();
                this.NPA = 0;
                this.pilhaContextoExpr.push(ContextoExpressaoEnum.PAR_ATUAL);
                break;

            /*
             #139 – se NPA <> NPF então ERRO(“Erro na quant.de parâmetros”)
             senao (* G. Código para chamada de proc*)
             */
            case 139:
                this.NPA = this.pilhaNPA.pop();
                this.simboloAux = tabSimbolos.getSimbolo(pilhaPosID.pop());
                this.metodoAux = (Metodo) this.simboloAux;
                this.NPF = this.metodoAux.getNumParametros();

                if (NPA != NPF) {
                    throw new SemanticError("Erro na quant. de parâmetros", token.getPosition());
                } else {
                    ///////////////////// GERA CÓDIGO PARA CHAMADA DE PROC /////////////////////////                    
                }
                this.pilhaContextoExpr.pop();
                this.pilhaParametrosValidar.pop();
                break;

            /*
             #140 - se categoria de id <> método então ERRO(“id deveria ser um método”) 
             senão se tipo do método <> nulo então ERRO(“esperava-se método sem tipo”) 
             senão se NPF <> 0 então ERRO(“Erro na quantidade de parametros”) 
             senão(*GC p/ chamada de método *)
             */
            case 140:
                this.simboloAux = tabSimbolos.getSimbolo(pilhaPosID.pop());
                if (this.simboloAux.getCategoria() != CategoriaIDEnum.METODO) {
                    throw new SemanticError("id deveria ser um método", token.getPosition());
                } else {
                    this.metodoAux = (Metodo) this.simboloAux;
                    if (!this.metodoAux.isRetornoNull()) {
                        throw new SemanticError("esperava-se método sem tipo", token.getPosition());
                    } else {
                        if (this.metodoAux.getNumParametros() > 0) {
                            throw new SemanticError("Erro na quantidade de parametros", token.getPosition());
                        } else {
                            ///////////////////// GERA CÓDIGO PARA CHAMADA DE PROC /////////////////////////  
                        }
                    }
                }
                break;
            /*
             #141 - se ContextoEXPR = “par-atual” então incrementa NPA e Verifica se existe ParâmetroFormal correspondente e se o tipo e o MPP são compatíveis
             - se ContextoEXPR = “impressão”entao se TipoExpr = booleano então ERRO(“tipo invalido para impressão”)
             senão (* G. Código para impressão *)                 
             */
            case 141:
                if (this.pilhaContextoExpr.peek() == ContextoExpressaoEnum.PAR_ATUAL) {

                    this.NPA = this.pilhaNPA.pop();
                    if (!this.pilhaParametrosValidar.peek().isEmpty()) {
                        this.parametroAux = this.pilhaParametrosValidar.peek().pop();
                        this.tipoExprAux = this.pilhaTipoExpr.pop();

                        PassagemValOuRefEnum eRef = this.pilhaERef.pop();

                        if (this.tipoExprAux != this.parametroAux.getTipo()) {
                            if (!(this.parametroAux.getTipo() == TipoPreDefinidoEnum.NUM_REAL && this.tipoExprAux == TipoPreDefinidoEnum.NUM_INT)
                                    && !(this.parametroAux.getTipo() == TipoPreDefinidoEnum.CADEIA && this.tipoExprAux == TipoPreDefinidoEnum.CARACTER)) {

                                throw new SemanticError("Parametro Formal e Atual não correspondem", token.getPosition());
                            }
                        }

                        if (this.parametroAux.getPassagemValOuRefEnum() != eRef) {
                            if (this.parametroAux.getPassagemValOuRefEnum() == PassagemValOuRefEnum.REFERENCIA) {
                                throw new SemanticError("Esperava passagem por Referencia", token.getPosition());
                            }
                        }
                    }
                    NPA++;
                    this.pilhaNPA.push(NPA);
                }
                if (this.pilhaContextoExpr.peek() == ContextoExpressaoEnum.IMPRESSAO) {
                    this.tipoExprAux = this.pilhaTipoExpr.pop();
                    if (this.tipoExprAux == TipoPreDefinidoEnum.BOOLEANO) {
                        throw new SemanticError("tipo invalido para impressão", token.getPosition());
                    }
                }
                break;

            /*
             #142 - TipoExpr := TipoExpSimples
             */
            case 142:
                this.pilhaTipoExpr.push(this.pilhaTipoExpSimples.pop());
                break;

            /*
             #143 – Se TipoExpSimples incompatível com TipoExpr então ERRO (“Operandos incompatíveis”)
             senão TipoExpr := “booleano”
             */
            case 143:
                this.tipoExprAux = this.pilhaTipoExpr.pop();
                TipoPreDefinidoEnum tipoExpSimples = this.pilhaTipoExpSimples.pop();

                if (this.tipoExprAux != tipoExpSimples) {
                    if ((this.tipoExprAux == TipoPreDefinidoEnum.NUM_INT && tipoExpSimples == TipoPreDefinidoEnum.NUM_REAL)
                            || (this.tipoExprAux == TipoPreDefinidoEnum.NUM_REAL && tipoExpSimples == TipoPreDefinidoEnum.NUM_INT)
                            || (this.tipoExprAux == TipoPreDefinidoEnum.CADEIA && tipoExpSimples == TipoPreDefinidoEnum.CARACTER)
                            || (this.tipoExprAux == TipoPreDefinidoEnum.CARACTER && tipoExpSimples == TipoPreDefinidoEnum.CADEIA)) {
                        this.pilhaTipoExpr.push(TipoPreDefinidoEnum.BOOLEANO);
                    } else {
                        throw new SemanticError("Operandos incompatíveis", token.getPosition());
                    }
                } else {
                    this.pilhaTipoExpr.push(TipoPreDefinidoEnum.BOOLEANO);
                }
                if (!this.pilhaContextoExpr.isEmpty() && this.pilhaContextoExpr.peek() == ContextoExpressaoEnum.PAR_ATUAL) {
                    this.pilhaERef.pop();
                    this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
                }
                break;

            /*
             #144 a #149 – Guarda Operador Relacional para futura Geração de Código
             */
            case 144:
                this.pilhaOpRel.push(OperadorRelEnum.OPERADORIGUAL);
                break;

            case 145:
                this.pilhaOpRel.push(OperadorRelEnum.OPERADORMENOR);
                break;

            case 146:
                this.pilhaOpRel.push(OperadorRelEnum.OPERADORMAIOR);
                break;

            case 147:
                this.pilhaOpRel.push(OperadorRelEnum.OPERADORMAIORIGUAL);
                break;

            case 148:
                this.pilhaOpRel.push(OperadorRelEnum.OPERADORMENORIGUAL);
                break;

            case 149:
                this.pilhaOpRel.push(OperadorRelEnum.OPERADORDIFERENTE);
                break;

            /*
             #150 - TipoExpSimples := TipoTermo
             */
            case 150:
                this.pilhaTipoExpSimples.push(this.pilhaTipoTermo.pop());
                break;

            /*
             #151 – Se operador não se aplica a TipoExpSimples então ERRO(“Op. e Operando incompatíveis”) 
             */
            case 151:
                OperadorAddEnum operadorAddAux = this.pilhaOpAdd.peek();
                TipoPreDefinidoEnum tipo = this.pilhaTipoExpSimples.peek();

                if ((operadorAddAux == OperadorAddEnum.OPERADORADICAO || operadorAddAux == OperadorAddEnum.OPERADORSUBTRACAO)
                        && (tipo != TipoPreDefinidoEnum.NUM_INT && tipo != TipoPreDefinidoEnum.NUM_REAL)) {
                    throw new SemanticError("Op. e Operando incompatíveis", token.getPosition());
                }

                if (operadorAddAux == OperadorAddEnum.OPERADOROU && tipo != TipoPreDefinidoEnum.BOOLEANO) {
                    throw new SemanticError("Op. e Operando incompatíveis", token.getPosition());
                }

                if (!this.pilhaContextoExpr.isEmpty() && this.pilhaContextoExpr.peek() == ContextoExpressaoEnum.PAR_ATUAL) {
                    this.pilhaERef.pop();
                    this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
                }
                break;

            /*
             #152 - Se TipoTermo incompatível com TipoExpSimples então ERRO (“Operandos incompatíveis”) 
             senão TipoExpSimples := tipo do resultado da operação(* Gera Código de acordo com oppad *)
             */
            case 152:
                TipoPreDefinidoEnum tipoTermo = this.pilhaTipoTermo.pop();
                TipoPreDefinidoEnum tipoExprSimp = this.pilhaTipoExpSimples.pop();
                OperadorAddEnum op = this.pilhaOpAdd.pop();

                //Se for Adição ou Subtração:
                if (op == OperadorAddEnum.OPERADORADICAO || op == OperadorAddEnum.OPERADORSUBTRACAO) {
                    if ((tipoExprSimp != TipoPreDefinidoEnum.NUM_INT && tipoExprSimp != TipoPreDefinidoEnum.NUM_REAL)
                            || (tipoTermo != TipoPreDefinidoEnum.NUM_INT && tipoTermo != TipoPreDefinidoEnum.NUM_REAL)) {
                        throw new SemanticError("Operandos incompatíveis", token.getPosition());
                    } else {
                        //Se um dos dois forem Real, o resultado é Real
                        if (tipoExprSimp == TipoPreDefinidoEnum.NUM_REAL || tipoTermo == TipoPreDefinidoEnum.NUM_REAL) {
                            this.pilhaTipoExpSimples.push(TipoPreDefinidoEnum.NUM_REAL);
                        } else {
                            this.pilhaTipoExpSimples.push(TipoPreDefinidoEnum.NUM_INT);
                        }
                    }
                }

                //Se for operação lógica
                if (op == OperadorAddEnum.OPERADOROU) {
                    if (tipoTermo != TipoPreDefinidoEnum.BOOLEANO) {
                        throw new SemanticError("Operandos incompatíveis", token.getPosition());
                    } else {
                        this.pilhaTipoExpSimples.push(TipoPreDefinidoEnum.BOOLEANO);
                    }
                }
                break;

            /*
             #153 a #155 – guarda operador para futura G. código
             */
            case 153:
                this.pilhaOpAdd.push(OperadorAddEnum.OPERADORADICAO);
                break;

            case 154:
                this.pilhaOpAdd.push(OperadorAddEnum.OPERADORSUBTRACAO);
                break;

            case 155:
                this.pilhaOpAdd.push(OperadorAddEnum.OPERADOROU);
                break;

            /*
             #156 – TipoTermo := TipoFator
             */
            case 156:
                this.pilhaTipoTermo.push(this.pilhaTipoFator.pop());
                break;

            /*
             #157 – Se operador não se aplica a TipoTermo então ERRO(“Op. e Operando incompatíveis”) 
             */
            case 157:
                OperadorMultEnum operadorMult = this.pilhaOpMult.peek();
                this.tipoTermoAux = this.pilhaTipoTermo.peek();

                if ((operadorMult == OperadorMultEnum.OPERADORMULTIPLICACAO || operadorMult == OperadorMultEnum.OPERADORDIVISAO)
                        && (this.tipoTermoAux != TipoPreDefinidoEnum.NUM_INT && this.tipoTermoAux != TipoPreDefinidoEnum.NUM_REAL)) {

                    throw new SemanticError("Op. e Operando incompatíveis", token.getPosition());
                }
                if (operadorMult == OperadorMultEnum.OPERADORDIV && this.tipoTermoAux != TipoPreDefinidoEnum.NUM_INT) {
                    throw new SemanticError("Op. e Operando incompatíveis", token.getPosition());
                }
                if (operadorMult == OperadorMultEnum.OPERADORE && this.tipoTermoAux != TipoPreDefinidoEnum.BOOLEANO) {
                    throw new SemanticError("Op. e Operando incompatíveis", token.getPosition());
                }

                if (!this.pilhaContextoExpr.isEmpty() && this.pilhaContextoExpr.peek() == ContextoExpressaoEnum.PAR_ATUAL) {
                    this.pilhaERef.pop();
                    this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
                }
                break;

            /*
             #158 - Se TipoFator incompatível com TipoTermo então ERRO (“Operandos incompatíveis”) 
             senão TipoTermo := tipo do res. da operação (* G. Código de acordo com opmult *)    
             */
            case 158:
                this.tipoTermoAux = this.pilhaTipoTermo.pop();
                this.tipoFatorAux = this.pilhaTipoFator.pop();
                OperadorMultEnum opMult = this.pilhaOpMult.pop();
                if (opMult == OperadorMultEnum.OPERADORMULTIPLICACAO || opMult == OperadorMultEnum.OPERADORDIVISAO) {

                    if ((this.tipoTermoAux != TipoPreDefinidoEnum.NUM_INT && this.tipoTermoAux != TipoPreDefinidoEnum.NUM_REAL)
                            || (this.tipoFatorAux != TipoPreDefinidoEnum.NUM_INT && this.tipoFatorAux != TipoPreDefinidoEnum.NUM_REAL)) {
                        throw new SemanticError("Operandos incompatíveis", token.getPosition());
                    } else {
                        if ((this.tipoTermoAux == TipoPreDefinidoEnum.NUM_REAL || this.tipoFatorAux == TipoPreDefinidoEnum.NUM_REAL) || opMult == OperadorMultEnum.OPERADORDIVISAO) {
                            this.pilhaTipoTermo.push(TipoPreDefinidoEnum.NUM_REAL);
                        } else {
                            this.pilhaTipoTermo.push(TipoPreDefinidoEnum.NUM_INT);
                        }
                        ///////////////////// GERA CÓDIGO DE ACORDO COM OPMULT ///////////////////////// 
                    }
                }
                break;

            /*
             #159 a #162 – guarda operador para futura G. código 
             */
            case 159:
                this.pilhaOpMult.push(OperadorMultEnum.OPERADORMULTIPLICACAO);
                break;

            case 160:
                this.pilhaOpMult.push(OperadorMultEnum.OPERADORDIVISAO);
                break;

            case 161:
                this.pilhaOpMult.push(OperadorMultEnum.OPERADORE);
                break;

            case 162:
                this.pilhaOpMult.push(OperadorMultEnum.OPERADORDIV);
                break;

            /*
                
             */
            case 163:

                break;

            /*
                
             */
            case 164:

                break;

            /*
                
             */
            case 165:

                break;

            /*
                
             */
            case 166:

                break;

            /*
             #167 - OpNega := OpUnario := false
             */
            case 167:
                this.pilhaOpNega.push(false);
                this.pilhaOpUnario.push(false);
                break;

            /*
             #168 – TipoFator := TipoExpr 
             */
            case 168:
                this.pilhaTipoFator.push(this.pilhaTipoExpr.pop());
                /*   this.pilhaOpNega.pop();
                 this.pilhaOpUnario.pop();
                 if (!this.pilhaContextoExpr.isEmpty() && this.pilhaContextoExpr.peek() == ContextoExpressaoEnum.PAR_ATUAL) {
                 if ((!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) || (!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek())) {
                 this.pilhaERef.pop();
                 this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
                 }

                 }*/
                break;

            /*
             #169 – TipoFator := TipoVar
             */
            case 169:
                this.pilhaTipoFator.push(TipoVar);
                break;

            /*
             #170 – TipoFator := TipoCte
             */
            case 170:
                this.pilhaTipoFator.push(this.TipoConst);
                /*
                 if (!this.pilhaContextoExpr.isEmpty() && this.pilhaContextoExpr.peek() == ContextoExpressaoEnum.PAR_ATUAL) {
                 if (!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek())) {
                 this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
                 }
                 }*/
                break;

            /*
                
             */
            case 171:

                break;

            /*
                
             */
            case 172:

                break;

            /*
                
             */
            case 173:

                break;

            /*
                
             */
            case 174:

                break;

            /*
                
             */
            case 175:

                break;

            /*
             #176 a #180 – TipoConst:= tipo da constante
             ValConst:= valor da constante
             */
            case 176:
                this.TipoConst = TipoPreDefinidoEnum.NUM_INT;
                this.ValConst = token.getLexeme();
                break;

            case 177:
                this.TipoConst = TipoPreDefinidoEnum.NUM_REAL;
                this.ValConst = token.getLexeme();
                break;

            case 178:
                this.TipoConst = TipoPreDefinidoEnum.BOOLEANO;
                this.ValConst = token.getLexeme();
                break;

            case 179:
                this.TipoConst = TipoPreDefinidoEnum.BOOLEANO;
                this.ValConst = token.getLexeme();
                break;

            case 180:
                if (token.getLexeme().length() - 2 == 1) {
                    // OU 
                    //if (token.getLexeme().length() == 1) {
                    //TESTAR O CERTO!!!
                    this.TipoConst = TipoPreDefinidoEnum.CARACTER;
                } else {
                    this.TipoConst = TipoPreDefinidoEnum.CADEIA;
                }
                this.ValConst = token.getLexeme();
                break;

        }
    }

    /**
     * ***********************************************************************************************************************************************************
     */

public void executarAcaoSemantica161(Token token) throws SemanticError {
        if (!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) {
            throw new SemanticError("Op. 'não' repetido – não pode!", token.getPosition());
        } else {
            this.pilhaOpNega.push(true);
            if (!this.pilhaContextoExpr.isEmpty() && this.pilhaContextoExpr.peek() == ContextoExpressaoEnum.PAR_ATUAL) {
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
            if (!this.pilhaContextoExpr.isEmpty() && this.pilhaContextoExpr.peek() == ContextoExpressaoEnum.PAR_ATUAL) {
                this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
            }
        }
    }

    public void executarAcaoSemantica164(Token token) throws SemanticError {
        TipoPreDefinidoEnum tipo = this.pilhaTipoFator.peek();
        if (tipo != TipoPreDefinidoEnum.NUM_INT && tipo != TipoPreDefinidoEnum.NUM_REAL) {
            throw new SemanticError("Op. unário exige operando num.", token.getPosition());
        }
        this.pilhaOpUnario.pop();
    }

    public void executarAcaoSemantica169(Token token) throws SemanticError {
        this.simboloAux = tabSimbolos.getSimbolo(this.pilhaPosID.peek());
        if (this.simboloAux.getCategoria() != CategoriaIDEnum.METODO) {
            throw new SemanticError("id deveria ser um método", token.getPosition());
        } else {
            Metodo metodo = (Metodo) this.simboloAux;
            if (metodo.isRetornoNull()) {
                throw new SemanticError("esperava-se mét. com tipo", token.getPosition());
            }
            this.pilhaParametrosValidar.push(metodo.getPilhaParametro());
        }

        this.pilhaContextoExpr.push(ContextoExpressaoEnum.PAR_ATUAL);
    }

    public void executarAcaoSemantica170(Token token) throws SemanticError {
        this.NPA = this.pilhaNPA.pop();
        this.simboloAux = tabSimbolos.getSimbolo(pilhaPosID.pop());
        this.metodoAux = (Metodo) this.simboloAux;
        if (NPA == this.metodoAux.getNumParametros()) {
            this.TipoVar = this.metodoAux.getResultado();
        } else {
            throw new SemanticError("Erro na quant. de parâmetros", token.getPosition());
        }
        this.pilhaContextoExpr.push(ContextoExpressaoEnum.PAR_ATUAL);

        if (!this.pilhaContextoExpr.isEmpty() && this.pilhaContextoExpr.peek() == ContextoExpressaoEnum.PAR_ATUAL) {
            if (!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek())) {
                this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
            }
        }

        this.pilhaParametrosValidar.pop();
    }

    public void executarAcaoSemantica171(Token token) throws SemanticError {
        if (this.pilhaTipoExpr.pop() != TipoPreDefinidoEnum.NUM_INT) {
            throw new SemanticError("índice deveria ser inteiro", token.getPosition());
        } else {
            if (this.pilhaTipoVarIndexada.pop() == TipoPreDefinidoEnum.CADEIA) {
                this.TipoVar = TipoPreDefinidoEnum.CARACTER;
                this.pilhaPosID.pop();
            } else {
                this.variavelAux = (Variavel) this.tabSimbolos.getSimbolo(this.pilhaPosID.pop());
                this.TipoVar = this.variavelAux.getTipo().getTipoElementos();
            }
        }

        if (!this.pilhaContextoExpr.isEmpty() && this.pilhaContextoExpr.peek() == ContextoExpressaoEnum.PAR_ATUAL) {
            if (!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek())) {
                this.pilhaERef.push(PassagemValOuRefEnum.REFERENCIA);
            }
        }
    }

    public void executarAcaoSemantica172(Token token) throws SemanticError {
        this.simboloAux = this.tabSimbolos.getSimbolo(this.pilhaPosID.pop());
        if (this.simboloAux.getCategoria() == CategoriaIDEnum.PARAMETRO || this.simboloAux.getCategoria() == CategoriaIDEnum.VARIAVEL) {
            if (this.simboloAux.getCategoria() == CategoriaIDEnum.VARIAVEL) {
                this.variavelAux = (Variavel) this.simboloAux;
                if (this.variavelAux.getTipo().getTipo() == TipoDeVariavelEnum.VETOR) {
                    throw new SemanticError("vetor deve ser indexado", token.getPosition());
                } else {
                    TipoVar = MudaTipo.getTipoPreDefinido(this.variavelAux.getTipo().getTipo());
                }
            } else {
                this.parametroAux = (Parametro) this.simboloAux;
                TipoVar = this.parametroAux.getTipo();
            }

            if (!this.pilhaContextoExpr.isEmpty() && this.pilhaContextoExpr.peek() == ContextoExpressaoEnum.PAR_ATUAL) {
                if (!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek())) {
                    this.pilhaERef.push(PassagemValOuRefEnum.REFERENCIA);
                }
            }

        } else {
            if (this.simboloAux.getCategoria() == CategoriaIDEnum.METODO) {
                this.metodoAux = (Metodo) this.simboloAux;
                if (this.metodoAux.isRetornoNull()) {
                    throw new SemanticError("Esperava-se método com tipo", token.getPosition());
                } else {
                    if (this.metodoAux.getNumParametros() != 0) {
                        throw new SemanticError("Erro na quant. de parâmetros", token.getPosition());
                    } else {
                        TipoVar = this.metodoAux.getResultado();
                    }

                    if (!this.pilhaContextoExpr.isEmpty() && this.pilhaContextoExpr.peek() == ContextoExpressaoEnum.PAR_ATUAL) {
                        if (!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek())) {
                            this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
                        }
                    }
                }

            } else {
                if (this.simboloAux.getCategoria() == CategoriaIDEnum.CONSTANTE) {
                    this.constanteAux = (Constante) this.simboloAux;
                    TipoVar = this.constanteAux.getTipoPreDefinidoEnum();
                    if (!this.pilhaContextoExpr.isEmpty() && this.pilhaContextoExpr.peek() == ContextoExpressaoEnum.PAR_ATUAL) {

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

}
