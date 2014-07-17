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

public class Semantico implements Constants
{    
    private TabelaDeSimbolos tabelaDeSimbolos;
    private Stack<Integer> pilhaDeslocamento = new Stack<Integer>();
    private int nivelAtual;
    private ContextoLIDEnum contextoLID;    
    private TipoPreDefinidoEnum tipoAtual;
    private SubCategoriaEnum subCategoria;
    private int primeiroID; 
    private int ultimoID; 
    private CategoriaIDEnum categoriaAtual;    
    private TipoPreDefinidoEnum TipoConst;
    private String ValConst;
    private int quantidadeIds;
    private int NumElementos;
    private int NPF;
    private PassagemValOuRefEnum mpp;
    private List<Parametro> listaDeParametros = new ArrayList<Parametro>();
    private Stack<Integer> pilhaIdMetodo = new Stack<Integer>();
    private TipoPreDefinidoEnum retornoMetodo;
    private boolean retornoNull;
    private Stack<Boolean> pilhaOpNega = new Stack<Boolean>();
    private Stack<Boolean> pilhaOpUnario = new Stack<Boolean>();
    private Stack<Integer> pilhaPosid = new Stack<Integer>();    
    private Stack<TipoPreDefinidoEnum> pilhaVarIndexada = new Stack<TipoPreDefinidoEnum>();
    private TipoPreDefinidoEnum TipoVar; 
    private Stack<TipoPreDefinidoEnum> pilhaTipoFator = new Stack<TipoPreDefinidoEnum>();
    private Stack<TipoPreDefinidoEnum> pilhaTipoTermo = new Stack<TipoPreDefinidoEnum>();
    private Stack<TipoPreDefinidoEnum> pilhaTipoExpSimples = new Stack<TipoPreDefinidoEnum>();
    private Stack<TipoPreDefinidoEnum> pilhaTipoExpr = new Stack<TipoPreDefinidoEnum>();
    
    
    private Stack<OperadorRelEnum> pilhaOpRel = new Stack<OperadorRelEnum>();
    private Stack<OperadorMultEnum> pilhaOpMult = new Stack<OperadorMultEnum>();
    private Stack<OperadorAddEnum> pilhaOpAdd = new Stack<OperadorAddEnum>();
    
    private Stack<ContextoExpressaoEnum> PilhaContextoEXPR = new Stack<ContextoExpressaoEnum>();
    private TipoPreDefinidoEnum TipoLadoEsq;
    
    private Stack<Boolean> pilhaRetorno = new Stack<Boolean>();
    private Stack<Integer> pilhaNPA = new Stack<Integer>();
    private Stack<Stack<Parametro>> pilhaParametrosValidar = new Stack<Stack<Parametro>>();
    
    private Stack<PassagemValOuRefEnum> pilhaERef = new Stack<PassagemValOuRefEnum>();
    
    private boolean temRetorno = false;
    
    public void executeAction(int action, Token token)	throws SemanticError
    {        
        try {
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
        }
    }
    
    
    /**
     * #101 – Insere id na TS juntamente com seus atributos: categoria (id-programa) e nível ( NA ).
     * Inicializa com zero nível atual (NA) e deslocamento.
     */
    public void executarAcaoSemantica101(Token token) {
        this.tabelaDeSimbolos = new TabelaDeSimbolos();
        this.nivelAtual = 0;
        this.pilhaDeslocamento.push(0);
        
        Simbolo idPrograma = new Simbolo(token.getLexeme(), CategoriaIDEnum.PROGRAMA, 0);
        this.nivelAtual++;
        this.tabelaDeSimbolos.addSimbolo(idPrograma);
    }
    
     /**
     * #102 –seta contextoLID para “decl” 
     *  Guarda pos. na TS do primeiro id da lista 
     */
    public void executarAcaoSemantica102(Token token) {
        this.contextoLID = ContextoLIDEnum.DECL;
        this.primeiroID = this.tabelaDeSimbolos.getTamanho();
        this.quantidadeIds = 0;
    }
    
    /**
     * 
     */
    public void executarAcaoSemantica103(Token token) {
        this.ultimoID = this.quantidadeIds + this.primeiroID;
    }
    
    /**
     * 
     */
    public void executarAcaoSemantica104(Token token) {
        int i = this.primeiroID;
        int deslocamento = this.pilhaDeslocamento.pop();
        do {            
            if(categoriaAtual == CategoriaIDEnum.CONSTANTE)
            {
                Simbolo simbolo = this.tabelaDeSimbolos.getSimbolo(i);
                Constante constante = new Constante(simbolo.getNome(), simbolo.getCategoria(), simbolo.getNivel()); 
                constante.setTipoPreDefinidoEnum(this.tipoAtual);
                constante.setValor(this.ValConst);
                constante.setCategoria(this.categoriaAtual);
                this.tabelaDeSimbolos.addSimbolo(constante, i);
            }
            if(this.categoriaAtual == CategoriaIDEnum.VARIAVEL)
            {
                Simbolo simbolo = this.tabelaDeSimbolos.getSimbolo(i);
                Variavel variavel = new Variavel(simbolo.getNome(), simbolo.getCategoria(), simbolo.getNivel());
                if(this.subCategoria == SubCategoriaEnum.preDefinido)
                {
                    TipoDeVariavel tv = new TipoDeVariavel(MudaTipo.getTipoDeVariavelEnum(this.tipoAtual), 0, null);
                    variavel.setTipo(tv);
                    variavel.setCategoria(this.categoriaAtual);
                    variavel.setDeslocamento(deslocamento);
                    deslocamento++;
                }
                if(this.subCategoria == SubCategoriaEnum.cadeia)
                {
                    TipoDeVariavel tv = new TipoDeVariavel(MudaTipo.getTipoDeVariavelEnum(this.tipoAtual), Integer.parseInt(this.ValConst), null);
                    variavel.setTipo(tv);
                    variavel.setCategoria(this.categoriaAtual);
                    variavel.setDeslocamento(deslocamento);
                    deslocamento++;
                }
                if(this.subCategoria == SubCategoriaEnum.vetor)
                {
                    TipoDeVariavel tv = new TipoDeVariavel(TipoDeVariavelEnum.VETOR, NumElementos, this.tipoAtual);
                    variavel.setTipo(tv);
                    variavel.setCategoria(this.categoriaAtual);
                    variavel.setDeslocamento(deslocamento);                   
                    deslocamento += NumElementos;
                }
                this.tabelaDeSimbolos.addSimbolo(variavel, i);
            }
            i++;
        } while (i != this.ultimoID);
        this.pilhaDeslocamento.push(deslocamento);
    }
    
    /**
     * #105 – TipoAtual := “inteiro”  
     */
    public void executarAcaoSemantica105(Token token) {
        this.tipoAtual = TipoPreDefinidoEnum.INTEIRO;        
    }
    
    /**
     * #106 – TipoAtual := “real”  
     */
    public void executarAcaoSemantica106(Token token) {
        this.tipoAtual = TipoPreDefinidoEnum.REAL;        
    }
    
    /**
     * #107 – TipoAtual := “booleano”  
     */
    public void executarAcaoSemantica107(Token token) {
        this.tipoAtual = TipoPreDefinidoEnum.BOOLEANO;        
    }
    
    /**
     * #108 – TipoAtual := “caracter”  
     */
    public void executarAcaoSemantica108(Token token) {
        this.tipoAtual = TipoPreDefinidoEnum.CARACTER;        
    }
    
    /**
     * Se TipoConst <> “inteiro”
     * então ERRO(“esperava-se uma const. inteira”)
     * senão se ValConst > 256
     * então ERRO(“cadeia > que o permitido”) 
     * senão TipoAtual := “cadeia” 
     */
    public void executarAcaoSemantica109(Token token) throws SemanticError {
        if(this.TipoConst != TipoPreDefinidoEnum.INTEIRO)
        {
            throw new SemanticError("esperava-se uma const. inteira", token.getPosition());
        }else
        {
            int tam = Integer.parseInt(this.ValConst);
            if(tam > 256)
            {
                throw new SemanticError("cadeia > que o permitido", token.getPosition());                                
            }
            else
            {
                this.tipoAtual = TipoPreDefinidoEnum.CADEIA;
            }
        }
    }
    
    public void executarAcaoSemantica110(Token token) throws SemanticError {
       if(this.tipoAtual == TipoPreDefinidoEnum.CADEIA)
       {
           throw new SemanticError("Vetor do tipo cadeia não é permitido", token.getPosition());           
       }else
       {
           this.subCategoria = SubCategoriaEnum.vetor;
       }
    }
    
    /**
     * Se TipoAtual = “cadeia”
     * Então SubCategoria := “cadeia”
     * Senão SubCategoria := “pré-definido”
     */
    public void executarAcaoSemantica111(Token token) {
       if(this.tipoAtual == TipoPreDefinidoEnum.CADEIA)
       {
           this.subCategoria = SubCategoriaEnum.cadeia;          
       }
       else
       {
           this.subCategoria = SubCategoriaEnum.preDefinido;          
       }
    }
    
    /**
     * – Se contextoLID = “decl”
     *  entao se id já declarado no NA
     *  então ERRO(“Id já declarado”)
     *  senão insere id na TS 
     *  - Se contextoLID = “par-formal”
     *  entao se id já declarado no NA
     *  então ERRO (“Id de parâmetro repetido”)
     *  senão incrementa NPF; insere id na TS
     *  - Se contextoLID = “leitura”
     *  Então se id não declarado 
     *  então ERRO (“Id não declarado”)
     *  senão se categoria ou tipo invalid
     *  paraleitura 
     *  então ERRO(“Tipo inv. p/ leitura”)
     *  senão (* Gera Cód. para LEITURA *)
     */
    public void executarAcaoSemantica112(Token token) throws SemanticError {
        if(this.contextoLID == ContextoLIDEnum.DECL)
        {
            if(tabelaDeSimbolos.jaExisteSimboloNivel(token.getLexeme(), nivelAtual))
            {
                throw new SemanticError("Id já declarado", token.getPosition());
            }else
            {
                Simbolo v = new Simbolo(token.getLexeme(), categoriaAtual, nivelAtual);                
                this.tabelaDeSimbolos.addSimbolo(v);
                this.quantidadeIds++;       
            }                
        }
        if(this.contextoLID == ContextoLIDEnum.PARAMETROFORMAL)
        {
            if(tabelaDeSimbolos.jaExisteSimboloNivel(token.getLexeme(), nivelAtual))
            {
                throw new SemanticError("Id de parâmetro repetido", token.getPosition());
            }else
            {                  
                Parametro parametro = new Parametro(token.getLexeme(), this.nivelAtual);                
                this.tabelaDeSimbolos.addSimbolo(parametro);
                this.NPF++;
                quantidadeIds++;      
            }
        }
        if(this.contextoLID == ContextoLIDEnum.LEITURA)
        {
            if(!tabelaDeSimbolos.jaExisteSimboloNesteEscopo(token.getLexeme(), nivelAtual))
            {
                throw new SemanticError("Id não declarado", token.getPosition());
            }else
            {
                Simbolo s = this.tabelaDeSimbolos.getSimboloNivel(token.getLexeme(), this.nivelAtual);
                if(s.getCategoria() == CategoriaIDEnum.VARIAVEL || s.getCategoria() == CategoriaIDEnum.PARAMETRO)
                {
                    boolean eTipoValido = false;
                    if(s.getCategoria() == CategoriaIDEnum.VARIAVEL)
                    {
                        Variavel variavel = (Variavel) s;
                        eTipoValido = !(variavel.getTipo().getTipo() == TipoDeVariavelEnum.BOOLEANO || variavel.getTipo().getTipo() == TipoDeVariavelEnum.VETOR);
                    }
                    if(s.getCategoria() == CategoriaIDEnum.PARAMETRO)
                    {
                        Parametro parametro = (Parametro) s;
                        eTipoValido = !(parametro.getTipo() == TipoPreDefinidoEnum.BOOLEANO);
                    }
                    if(!eTipoValido)
                    {
                        throw new SemanticError("Tipo inv. p/ LEITURA", token.getPosition());                        
                    }
                }else
                {
                    throw new SemanticError("Tipo inv. p/ LEITURA", token.getPosition());                    
                }
            }
        }             
    }
    
    public void executarAcaoSemantica113(Token token) throws SemanticError {
        if(this.subCategoria == SubCategoriaEnum.cadeia || this.subCategoria == SubCategoriaEnum.vetor)
        {
            throw new SemanticError("Apenas id de tipo pré-def podem ser declarados como constante", token.getPosition());            
        }else
        {
            this.categoriaAtual = CategoriaIDEnum.CONSTANTE;
        }
    }
    
    /**
     * CategoriaAtual := “variavel”     
     */
    public void executarAcaoSemantica114(Token token) {
        this.categoriaAtual = CategoriaIDEnum.VARIAVEL;    
    }
    
    public void executarAcaoSemantica115(Token token) throws SemanticError {
        if(tabelaDeSimbolos.jaExisteSimboloNivel(token.getLexeme(), this.nivelAtual))
        {
            throw new SemanticError("Id já declarado", token.getPosition());            
        }
        else
        {
            Metodo metodo = new Metodo(token.getLexeme(), CategoriaIDEnum.METODO, this.nivelAtual);
            this.tabelaDeSimbolos.addSimbolo(metodo);
            this.NPF = 0;
            this.nivelAtual++;
            this.pilhaDeslocamento.push(0);
            this.quantidadeIds = 0; 
            this.listaDeParametros = new ArrayList<>();
            this.pilhaIdMetodo.push(tabelaDeSimbolos.getTamanho() - 1);
        }
    }
    
    public void executarAcaoSemantica116(Token token) throws SemanticError {
        Metodo metodo = (Metodo) this.tabelaDeSimbolos.getSimbolo(this.pilhaIdMetodo.peek());
        metodo.setListaParametros(this.listaDeParametros);
        metodo.setNumParametros(this.NPF);        
    }
    
    public void executarAcaoSemantica117(Token token) throws SemanticError {
        Metodo metodo = (Metodo) this.tabelaDeSimbolos.getSimbolo(this.pilhaIdMetodo.peek());
        if(!retornoNull)
        {
            metodo.setResultado(this.retornoMetodo);
            this.pilhaRetorno.push(true);
        }else
        {
            this.pilhaRetorno.push(false);            
        }
        metodo.setRetornoNull(retornoNull);
                
        
    }
    
    public void executarAcaoSemantica118(Token token) throws SemanticError {
        this.tabelaDeSimbolos.removeNivelAtual(this.nivelAtual);
        this.nivelAtual--;
        pilhaDeslocamento.pop();
        Metodo metodo = (Metodo) this.tabelaDeSimbolos.getSimbolo(this.pilhaIdMetodo.peek());        
        //if(!metodo.isRetornoNull() && (!this.pilhaRetorno.empty() && this.pilhaIdMetodo.size() == 1) || this.pilhaRetorno.size() == this.pilhaIdMetodo.size())
        if(!metodo.isRetornoNull() && !this.temRetorno)
        {
            throw new SemanticError("método com tipo deve ter retorno", token.getPosition());            
        }
        this.pilhaIdMetodo.pop();
        this.pilhaRetorno.pop();
        this.temRetorno = false;
    }
    
     public void executarAcaoSemantica119(Token token){
         this.contextoLID = ContextoLIDEnum.PARAMETROFORMAL;
         this.primeiroID = this.tabelaDeSimbolos.getTamanho();
         quantidadeIds = 0;
     }
     
     public void executarAcaoSemantica120(Token token){
         this.ultimoID = this.quantidadeIds + this.primeiroID;                 
     }
     
     public void executarAcaoSemantica121(Token token) throws SemanticError{
         this.executarAcaoSemantica111(token);
         if(this.subCategoria != SubCategoriaEnum.preDefinido)
         {
             throw new SemanticError("Par devem ser de tipo pré-def.", token.getPosition());             
         }else
         {
             int pos = this.primeiroID;
             do{
                 int deslocamento = this.pilhaDeslocamento.pop(); 

                 Simbolo simbolo = this.tabelaDeSimbolos.getSimbolo((pos));
                 Parametro parametro = new Parametro(simbolo.getNome(), simbolo.getCategoria(), simbolo.getNivel()); 

                 parametro.setCategoria(CategoriaIDEnum.PARAMETRO);
                 parametro.setTipo(this.tipoAtual);
                 parametro.setPassagemValOuRefEnum(this.mpp);
                 parametro.setDeslocamento(deslocamento);

                 deslocamento++;
                 this.pilhaDeslocamento.push(deslocamento);

                 this.tabelaDeSimbolos.addSimbolo(parametro, (pos));

                 this.listaDeParametros.add(parametro);
                 pos++;
             }while(pos <= this.ultimoID - 1);
         }
     }
     
     public void executarAcaoSemantica122(Token token) throws SemanticError{
         if(this.tipoAtual == TipoPreDefinidoEnum.CADEIA)
         {
             throw new SemanticError("Métodos devem ser de tipo pré-def.", token.getPosition());                                       
         }else
         {
             this.retornoMetodo = this.tipoAtual;
             this.retornoNull = false;
         }
     }
     
     public void executarAcaoSemantica123(Token token) throws SemanticError{
         this.retornoNull = true;
     }
     
     public void executarAcaoSemantica124(Token token){
         this.mpp = PassagemValOuRefEnum.REFERENCIA;         
     }
     
     public void executarAcaoSemantica125(Token token){
         this.mpp = PassagemValOuRefEnum.VALOR;
     }
     
     public void executarAcaoSemantica126(Token token) throws SemanticError{
         if(!tabelaDeSimbolos.jaExisteSimboloNesteEscopo(token.getLexeme(), this.nivelAtual))
         {
             throw new SemanticError("Identificador não declarado", token.getPosition());             
         }else
         {            
             this.pilhaPosid.push(tabelaDeSimbolos.getPosicaoID(token.getLexeme(), this.nivelAtual));             
         }
     }
     
     public void executarAcaoSemantica127(Token token) throws SemanticError{
         TipoPreDefinidoEnum tipoExp = this.pilhaTipoExpr.pop();
         if(tipoExp != TipoPreDefinidoEnum.INTEIRO && tipoExp != TipoPreDefinidoEnum.BOOLEANO)
         {
             throw new SemanticError("Tipo inválido da expressão", token.getPosition());             
         }else
         {
             //Gerar Codigo              
         }
     }
     
     public void executarAcaoSemantica128(Token token){
         this.contextoLID = ContextoLIDEnum.LEITURA;
     }
     
     public void executarAcaoSemantica129(Token token) throws SemanticError{
         this.PilhaContextoEXPR.push(ContextoExpressaoEnum.IMPRESSAO);
         TipoPreDefinidoEnum tipoExp = this.pilhaTipoExpr.pop();
         if(tipoExp == TipoPreDefinidoEnum.BOOLEANO)
         {
             throw new SemanticError("tipo invalido para impressão", token.getPosition());             
         }else
         {
             //Gerar Codigo              
         }
     }
     
     public void executarAcaoSemantica130(Token token) throws SemanticError{
         if(!this.pilhaRetorno.isEmpty() && this.pilhaRetorno.peek())
         {
             TipoPreDefinidoEnum tipoExp = this.pilhaTipoExpr.pop();
             Simbolo simbolo  = tabelaDeSimbolos.getSimbolo(this.pilhaIdMetodo.peek());
             Metodo metodo = (Metodo) simbolo;
             TipoPreDefinidoEnum tipoMetodo = metodo.getResultado();             
             if(tipoExp != tipoMetodo)
             {
                 if(!((tipoMetodo == TipoPreDefinidoEnum.REAL && tipoExp == TipoPreDefinidoEnum.INTEIRO) || (tipoMetodo == TipoPreDefinidoEnum.CADEIA && tipoExp == TipoPreDefinidoEnum.CARACTER)))
                 {                     
                     throw new SemanticError("Tipo de exp inválido", token.getPosition());                     
                 }
                 temRetorno = true;
             }else
             {
                 temRetorno = true;
                 //gerar codigo
             }
             
         }else
         {
             throw new SemanticError("'Retorne' só pode ser usado em função", token.getPosition());             
         }
     }
     
     public void executarAcaoSemantica131(Token token) throws SemanticError{
         Simbolo simbolo  = tabelaDeSimbolos.getSimbolo(pilhaPosid.pop());
         if(simbolo.getCategoria() == CategoriaIDEnum.VARIAVEL || simbolo.getCategoria() == CategoriaIDEnum.PARAMETRO)
         {
             if(simbolo.getCategoria() == CategoriaIDEnum.VARIAVEL)
             {
                 Variavel variavel = (Variavel) simbolo;
                 if(variavel.getTipo().getTipo() == TipoDeVariavelEnum.VETOR)
                 {
                     throw new SemanticError("id. Deveria ser indexado", token.getPosition());                     
                 }
                 else
                 {
                     this.TipoLadoEsq = MudaTipo.getTipoPreDefinido(variavel.getTipo().getTipo());                     
                 }
             }
             if(simbolo.getCategoria() == CategoriaIDEnum.PARAMETRO)
             {
                 Parametro parametro = (Parametro) simbolo;
                 this.TipoLadoEsq = parametro.getTipo();
             }
         }else
         {
             throw new SemanticError("id. deveria ser var ou par", token.getPosition());              
         }
     }
     
     public void executarAcaoSemantica132(Token token) throws SemanticError{
         TipoPreDefinidoEnum tipoExpr = this.pilhaTipoExpr.pop();
         if(this.TipoLadoEsq != tipoExpr)
         {
             if(!((tipoExpr == TipoPreDefinidoEnum.INTEIRO && this.TipoLadoEsq == TipoPreDefinidoEnum.REAL) || (tipoExpr == TipoPreDefinidoEnum.CARACTER && this.TipoLadoEsq == TipoPreDefinidoEnum.CADEIA)))
             {                 
                 throw new SemanticError("tipos incompatíveis", token.getPosition());                 
             }             
         }
     }
     
     public void executarAcaoSemantica133(Token token) throws SemanticError{
         Simbolo simbolo  = tabelaDeSimbolos.getSimbolo(pilhaPosid.peek());
         if(simbolo.getCategoria() != CategoriaIDEnum.VARIAVEL)
         {
             throw new SemanticError("esperava-se uma variável", token.getPosition());             
         }else
         {
             Variavel variavel = (Variavel) simbolo;
             if(variavel.getTipo().getTipo() != TipoDeVariavelEnum.VETOR && variavel.getTipo().getTipo() != TipoDeVariavelEnum.CADEIA)
             {
                 throw new SemanticError("apenas vetores e cadeias podem ser indexados", token.getPosition());                 
             }
             else
             {
                 this.pilhaVarIndexada.push(MudaTipo.getTipoPreDefinido(variavel.getTipo().getTipo()));
             }
         } 
     }
     
     public void executarAcaoSemantica134(Token token) throws SemanticError{
         Simbolo simbolo  = tabelaDeSimbolos.getSimbolo(pilhaPosid.pop());
         Variavel variavel = (Variavel) simbolo;
         TipoPreDefinidoEnum tipoExpr = this.pilhaTipoExpr.pop();
         if(tipoExpr != TipoPreDefinidoEnum.INTEIRO)
         {
             throw new SemanticError("índice deveria ser inteiro", token.getPosition());              
         }else
         {
             TipoPreDefinidoEnum tipoVarIndexada = this.pilhaVarIndexada.pop();
             if(tipoVarIndexada == TipoPreDefinidoEnum.CADEIA)
             {
                 this.TipoLadoEsq = TipoPreDefinidoEnum.CADEIA;
             }else
             {
                 this.TipoLadoEsq = variavel.getTipo().getTipoElementos();                                  
             }
         }
     }
     
     public void executarAcaoSemantica135(Token token) throws SemanticError{
         Simbolo simbolo  = tabelaDeSimbolos.getSimbolo(pilhaPosid.peek());
         if(simbolo.getCategoria() != CategoriaIDEnum.METODO)
         {
             throw new SemanticError("id deveria ser um método", token.getPosition());              
         }else
         {
             Metodo metodo = (Metodo) simbolo;
             if(!metodo.isRetornoNull())
             {
                 throw new SemanticError("esperava-se mét sem tipo", token.getPosition()); 
             }
             this.pilhaParametrosValidar.push(metodo.getPilhaParametro());
             this.PilhaContextoEXPR.push(ContextoExpressaoEnum.PARAMETROATUAL);
         }         
     }
     
     public void executarAcaoSemantica136(Token token) throws SemanticError{
         this.pilhaNPA.push(1);
         if(!this.pilhaParametrosValidar.peek().isEmpty())
         {
             //this.ContextoEXPR = ContextoExpressaoEnum.PARAMETROATUAL; Verificar necessidade 
             TipoPreDefinidoEnum tipoExpr = this.pilhaTipoExpr.pop();
             Parametro parametro = this.pilhaParametrosValidar.peek().pop();
             PassagemValOuRefEnum eRef = this.pilhaERef.pop();

             if(tipoExpr != parametro.getTipo())
             {
                 if(!(parametro.getTipo() == TipoPreDefinidoEnum.REAL && tipoExpr == TipoPreDefinidoEnum.INTEIRO) && !(parametro.getTipo() == TipoPreDefinidoEnum.CADEIA && tipoExpr == TipoPreDefinidoEnum.CARACTER))
                 {
                     throw new SemanticError("Não há correpondecia entre parametro Atual e parametro Formal", token.getPosition());
                 }
             }

             if(parametro.getPassagemValOuRefEnum() != eRef)
             {
                 if(parametro.getPassagemValOuRefEnum() == PassagemValOuRefEnum.REFERENCIA)
                 {
                     throw new SemanticError("Esperava passagem de Parametro por Referencia", token.getPosition());
                 }
             }
         }
     }
     
     public void executarAcaoSemantica137(Token token) throws SemanticError{
        int NPA = this.pilhaNPA.pop();
        Simbolo simbolo  = tabelaDeSimbolos.getSimbolo(pilhaPosid.pop());
        Metodo metodo = (Metodo) simbolo;
        if(NPA != metodo.getNumParametros())
        {
            throw new SemanticError("Erro na quant. de parâmetros", token.getPosition());             
        }
        this.PilhaContextoEXPR.pop();
        this.pilhaParametrosValidar.pop();
     }
      
     public void executarAcaoSemantica138(Token token) throws SemanticError{
         Simbolo simbolo  = tabelaDeSimbolos.getSimbolo(pilhaPosid.pop());
         if(simbolo.getCategoria() != CategoriaIDEnum.METODO)
         {
             throw new SemanticError("id deveria ser um método", token.getPosition());              
         }else
         {
             Metodo metodo = (Metodo) simbolo;
             if(!metodo.isRetornoNull())
             {
                 throw new SemanticError("esperava-se método sem tipo", token.getPosition()); 
             }else
             {
                 if(metodo.getNumParametros() > 0)
                 {
                     throw new SemanticError("Erro na quantidade de parametros", token.getPosition());                     
                 }
                 else
                 {
                     //Gerar Codigo 
                 }
             }
         }
     }  
     
     public void executarAcaoSemantica139(Token token) throws SemanticError{
         if(this.PilhaContextoEXPR.peek()== ContextoExpressaoEnum.PARAMETROATUAL)
         {
                         
             int NPA = this.pilhaNPA.pop();
             if(!this.pilhaParametrosValidar.peek().isEmpty())
             {
                 TipoPreDefinidoEnum tipoExpr = this.pilhaTipoExpr.pop();
                 Parametro parametro = this.pilhaParametrosValidar.peek().pop();
                 PassagemValOuRefEnum eRef = this.pilhaERef.pop();

                 if(tipoExpr != parametro.getTipo())
                 {
                     if(!(parametro.getTipo() == TipoPreDefinidoEnum.REAL && tipoExpr == TipoPreDefinidoEnum.INTEIRO) && !(parametro.getTipo() == TipoPreDefinidoEnum.CADEIA && tipoExpr == TipoPreDefinidoEnum.CARACTER))
                     {
                         throw new SemanticError("Não há correpondecia entre parametro Atual e parametro Formal", token.getPosition());                         
                     }                                 
                 }

                 if(parametro.getPassagemValOuRefEnum() != eRef)
                 {
                     if(parametro.getPassagemValOuRefEnum() == PassagemValOuRefEnum.REFERENCIA)
                     {
                         throw new SemanticError("Esperava passagem por Referencia", token.getPosition());
                     }
                 }            
             }
             NPA++;             
             this.pilhaNPA.push(NPA);
         }
         if(this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.IMPRESSAO)
         {
             TipoPreDefinidoEnum tipoExpr = this.pilhaTipoExpr.pop();
             if(tipoExpr == TipoPreDefinidoEnum.BOOLEANO)
             {
                 throw new SemanticError("tipo invalido para impressão", token.getPosition());                    
             }
         }
     }  
     
     public void executarAcaoSemantica140(Token token){
         this.pilhaTipoExpr.push(this.pilhaTipoExpSimples.pop());
     }
     
     public void executarAcaoSemantica141(Token token) throws SemanticError{
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
             if(tipoExp != tipoExpSimples)
             {
                 if((tipoExp == TipoPreDefinidoEnum.INTEIRO && tipoExpSimples == TipoPreDefinidoEnum.REAL) || (tipoExp == TipoPreDefinidoEnum.REAL && tipoExpSimples == TipoPreDefinidoEnum.INTEIRO)
                         ||(tipoExp == TipoPreDefinidoEnum.CADEIA && tipoExpSimples == TipoPreDefinidoEnum.CARACTER) || (tipoExp == TipoPreDefinidoEnum.CARACTER && tipoExpSimples == TipoPreDefinidoEnum.CADEIA))
                 {
                     this.pilhaTipoExpr.push(TipoPreDefinidoEnum.BOOLEANO);                       
                 }
                 else
                 {
                     throw new SemanticError("Operandos incompatíveis", token.getPosition());
                 }
             }else
             {
                 this.pilhaTipoExpr.push(TipoPreDefinidoEnum.BOOLEANO);                 
             }
         //}
                 
         if(!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek()== ContextoExpressaoEnum.PARAMETROATUAL)
         {
             this.pilhaERef.pop();
             this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
         }
      }
     
     public void executarAcaoSemantica142(Token token){
         this.pilhaOpRel.push(OperadorRelEnum.OPERADORIGUAL);
     }
     
     public void executarAcaoSemantica143(Token token){
         this.pilhaOpRel.push(OperadorRelEnum.OPERADORMENOR);
     }
     
     public void executarAcaoSemantica144(Token token){
         this.pilhaOpRel.push(OperadorRelEnum.OPERADORMAIOR);
     }
     
     public void executarAcaoSemantica145(Token token){
         this.pilhaOpRel.push(OperadorRelEnum.OPERADORMAIORIGUAL);
     }
     
     public void executarAcaoSemantica146(Token token){
         this.pilhaOpRel.push(OperadorRelEnum.OPERADORMENORIGUAL);
     }
     
     public void executarAcaoSemantica147(Token token){
         this.pilhaOpRel.push(OperadorRelEnum.OPERADORDIFERENTE);
     }
     
     public void executarAcaoSemantica148(Token token){
         this.pilhaTipoExpSimples.push(this.pilhaTipoTermo.pop());
     }
     
     public void executarAcaoSemantica149(Token token) throws SemanticError{
         OperadorAddEnum operador = this.pilhaOpAdd.peek();
         TipoPreDefinidoEnum tipo = this.pilhaTipoExpSimples.peek();
         
         if((operador == OperadorAddEnum.OPERADORADICAO || operador == OperadorAddEnum.OPERADORSUBTRACAO) && (tipo != TipoPreDefinidoEnum.INTEIRO && tipo != TipoPreDefinidoEnum.REAL))
         {             
             throw new SemanticError("Op. e Operando incompatíveis", token.getPosition());              
         }
         
         if(operador == OperadorAddEnum.OPERADOROU && tipo != TipoPreDefinidoEnum.BOOLEANO)
         {
             throw new SemanticError("Op. e Operando incompatíveis", token.getPosition());             
         } 
         
         if(!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek()== ContextoExpressaoEnum.PARAMETROATUAL)
         {
             this.pilhaERef.pop();
             this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
         }
     }
     
     public void executarAcaoSemantica150(Token token) throws SemanticError{
         OperadorAddEnum operador = this.pilhaOpAdd.pop();
         TipoPreDefinidoEnum tipoExpSimples = this.pilhaTipoExpSimples.pop();
         TipoPreDefinidoEnum tipoTermo = this.pilhaTipoTermo.pop();
         
         if(operador == OperadorAddEnum.OPERADORADICAO || operador == OperadorAddEnum.OPERADORSUBTRACAO)
         {
             if((tipoExpSimples != TipoPreDefinidoEnum.INTEIRO && tipoExpSimples != TipoPreDefinidoEnum.REAL) || (tipoTermo != TipoPreDefinidoEnum.INTEIRO && tipoTermo != TipoPreDefinidoEnum.REAL))
             {
                 throw new SemanticError("Operandos incompatíveis", token.getPosition());                 
             }else
             {
                 if(tipoExpSimples == TipoPreDefinidoEnum.REAL || tipoTermo == TipoPreDefinidoEnum.REAL)
                 {
                     this.pilhaTipoExpSimples.push(TipoPreDefinidoEnum.REAL);                     
                 }else
                 {
                     this.pilhaTipoExpSimples.push(TipoPreDefinidoEnum.INTEIRO);                     
                 }
             }
         }
         if(operador == OperadorAddEnum.OPERADOROU)
         {
             if(tipoTermo != TipoPreDefinidoEnum.BOOLEANO)
             {
                 throw new SemanticError("Operandos incompatíveis", token.getPosition());                 
             }else
             {
                 this.pilhaTipoExpSimples.push(TipoPreDefinidoEnum.BOOLEANO);
             }             
         }
     }
     
     public void executarAcaoSemantica151(Token token){
         this.pilhaOpAdd.push(OperadorAddEnum.OPERADORADICAO);
     }
     
     public void executarAcaoSemantica152(Token token){
         this.pilhaOpAdd.push(OperadorAddEnum.OPERADORSUBTRACAO);
     }
     
     public void executarAcaoSemantica153(Token token){
         this.pilhaOpAdd.push(OperadorAddEnum.OPERADOROU);
     }
     
     public void executarAcaoSemantica154(Token token){
         this.pilhaTipoTermo.push(this.pilhaTipoFator.pop());
     }
     
     public void executarAcaoSemantica155(Token token) throws SemanticError{
         OperadorMultEnum operador = this.pilhaOpMult.peek();
         TipoPreDefinidoEnum tipo = this.pilhaTipoTermo.peek();
         if((operador == OperadorMultEnum.OPERADORMULTIPLICACAO || operador == OperadorMultEnum.OPERADORDIVISAO) && (tipo != TipoPreDefinidoEnum.INTEIRO && tipo != TipoPreDefinidoEnum.REAL))
         {             
             throw new SemanticError("Op. e Operando incompatíveis", token.getPosition());                   
         }
         if(operador == OperadorMultEnum.OPERADORDIV && tipo != TipoPreDefinidoEnum.INTEIRO)
         {
             throw new SemanticError("Op. e Operando incompatíveis", token.getPosition());                   
         }
         if(operador == OperadorMultEnum.OPERADORE && tipo != TipoPreDefinidoEnum.BOOLEANO)
         {
             throw new SemanticError("Op. e Operando incompatíveis", token.getPosition());                   
         }
         
         if(!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek()== ContextoExpressaoEnum.PARAMETROATUAL)
         {
             this.pilhaERef.pop();
             this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
         }
     }
     
     public void executarAcaoSemantica156(Token token) throws SemanticError{
         TipoPreDefinidoEnum tipoTermo = this.pilhaTipoTermo.pop();
         TipoPreDefinidoEnum tipoFator = this.pilhaTipoFator.pop();
         OperadorMultEnum operador = this.pilhaOpMult.pop();
         if(operador == OperadorMultEnum.OPERADORMULTIPLICACAO || operador == OperadorMultEnum.OPERADORDIVISAO)
         {
             if((tipoTermo != TipoPreDefinidoEnum.INTEIRO && tipoTermo != TipoPreDefinidoEnum.REAL) || (tipoFator != TipoPreDefinidoEnum.INTEIRO && tipoFator != TipoPreDefinidoEnum.REAL))
             {
                 throw new SemanticError("Operandos incompatíveis", token.getPosition());
             }
             else
             {
                 if((tipoTermo == TipoPreDefinidoEnum.REAL || tipoFator == TipoPreDefinidoEnum.REAL) || operador == OperadorMultEnum.OPERADORDIVISAO)
                 {
                     this.pilhaTipoTermo.push(TipoPreDefinidoEnum.REAL);
                 }
                 else
                 {
                     this.pilhaTipoTermo.push(TipoPreDefinidoEnum.INTEIRO);
                 }
             }
         }
         if(operador == OperadorMultEnum.OPERADORDIV)
         {
             if(tipoTermo != TipoPreDefinidoEnum.INTEIRO || tipoFator != TipoPreDefinidoEnum.INTEIRO)
             {
                 throw new SemanticError("Operandos incompatíveis", token.getPosition());                 
             }else
             {
                 this.pilhaTipoTermo.push(TipoPreDefinidoEnum.INTEIRO);
             }
         }
         if(operador == OperadorMultEnum.OPERADORE)
         {
             if(tipoTermo != TipoPreDefinidoEnum.BOOLEANO || tipoFator != TipoPreDefinidoEnum.BOOLEANO)
             {
                 throw new SemanticError("Operandos incompatíveis", token.getPosition());                  
             }else
             {
                 this.pilhaTipoTermo.push(TipoPreDefinidoEnum.BOOLEANO);                 
             }
         }
     }
     
     public void executarAcaoSemantica157(Token token){
         this.pilhaOpMult.push(OperadorMultEnum.OPERADORMULTIPLICACAO);
     }
     
     public void executarAcaoSemantica158(Token token){
         this.pilhaOpMult.push(OperadorMultEnum.OPERADORDIVISAO);
     }
     
     public void executarAcaoSemantica159(Token token){
         this.pilhaOpMult.push(OperadorMultEnum.OPERADORDIV);
     }
     
     public void executarAcaoSemantica160(Token token){
         this.pilhaOpMult.push(OperadorMultEnum.OPERADORE);
     }
     
     public void executarAcaoSemantica161(Token token) throws SemanticError{
         if(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek())
         {
             throw new SemanticError("Op. 'não' repetido – não pode!", token.getPosition());             
         }else
         {
             this.pilhaOpNega.push(true);
             if(!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek()== ContextoExpressaoEnum.PARAMETROATUAL)
             {                
                 this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
             }
         } 
     }
     
     public void executarAcaoSemantica162(Token token) throws SemanticError
     {
         TipoPreDefinidoEnum tipo = this.pilhaTipoFator.peek();
         if(tipo != TipoPreDefinidoEnum.BOOLEANO)
         {
             throw new SemanticError("Op. ‘não’ exige operando bool.", token.getPosition());            
         }
         this.pilhaOpNega.pop();
     }
     
     public void executarAcaoSemantica163(Token token) throws SemanticError{
         if(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek())
         {
             throw new SemanticError("Op. 'unário' repetido", token.getPosition());             
         }else
         {
             this.pilhaOpUnario.push(true);
             if(!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek()== ContextoExpressaoEnum.PARAMETROATUAL)
             {                 
                 this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
             }
         } 
     }     
     
     
     public void executarAcaoSemantica164(Token token) throws SemanticError
     {
         TipoPreDefinidoEnum tipo = this.pilhaTipoFator.peek();
         if(tipo != TipoPreDefinidoEnum.INTEIRO && tipo != TipoPreDefinidoEnum.REAL)
         {
             throw new SemanticError("Op. unário exige operando num.", token.getPosition());            
         }
         this.pilhaOpUnario.pop();
     }
     
     public void executarAcaoSemantica165(Token token)
     {         
         this.pilhaOpNega.push(false);         
         this.pilhaOpUnario.push(false);
     }
     
     public void executarAcaoSemantica166(Token token)
     {
         this.pilhaTipoFator.push(this.pilhaTipoExpr.pop());
         this.pilhaOpNega.pop();         
         this.pilhaOpUnario.pop();
         if(!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek()== ContextoExpressaoEnum.PARAMETROATUAL)
         {
             if((!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) || (!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek()))
            {
                this.pilhaERef.pop();
                this.pilhaERef.push(PassagemValOuRefEnum.VALOR);             
            }
             
         }
         
     }
     
     public void executarAcaoSemantica167(Token token) throws SemanticError{
         this.pilhaTipoFator.push(TipoVar);         
     }
     
     public void executarAcaoSemantica168(Token token) throws SemanticError{
         this.pilhaTipoFator.push(this.TipoConst);
         
         if(!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL)
         {
             if(!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek()))
             {
                this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
             }
         }
     }
     
     public void executarAcaoSemantica169(Token token) throws SemanticError{
         Simbolo simbolo = tabelaDeSimbolos.getSimbolo(this.pilhaPosid.peek());
         if(simbolo.getCategoria() != CategoriaIDEnum.METODO)
         {
             throw new SemanticError("id deveria ser um método", token.getPosition());              
         }else
         {
             Metodo metodo = (Metodo) simbolo;
             if(metodo.isRetornoNull())
             {
                 throw new SemanticError("esperava-se mét. com tipo", token.getPosition());                  
             }
             this.pilhaParametrosValidar.push(metodo.getPilhaParametro());
         }
         
         this.PilhaContextoEXPR.push(ContextoExpressaoEnum.PARAMETROATUAL);
     }
     
     public void executarAcaoSemantica170(Token token) throws SemanticError{
         int NPA = this.pilhaNPA.pop();
         Simbolo simbolo  = tabelaDeSimbolos.getSimbolo(pilhaPosid.pop());
         Metodo metodo = (Metodo) simbolo;
         if(NPA == metodo.getNumParametros())
         {
             this.TipoVar = metodo.getResultado();
         }else
         {
             throw new SemanticError("Erro na quant. de parâmetros", token.getPosition());
         }
         this.PilhaContextoEXPR.push(ContextoExpressaoEnum.PARAMETROATUAL);
         
         if(!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek()== ContextoExpressaoEnum.PARAMETROATUAL)
         {
             if(!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek()))
             {
                 this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
             }
         }
         
         this.pilhaParametrosValidar.pop();
     }
     
     public void executarAcaoSemantica171(Token token) throws SemanticError{
         if(this.pilhaTipoExpr.pop() != TipoPreDefinidoEnum.INTEIRO)
         {
             throw new SemanticError("índice deveria ser inteiro", token.getPosition());             
         }else
         {
             if(this.pilhaVarIndexada.pop() == TipoPreDefinidoEnum.CADEIA)
             {
                 this.TipoVar  = TipoPreDefinidoEnum.CARACTER; 
                 this.pilhaPosid.pop();
             }else
             {
                 Variavel variavel = (Variavel)this.tabelaDeSimbolos.getSimbolo(this.pilhaPosid.pop());
                 this.TipoVar = variavel.getTipo().getTipoElementos();
             }
         }
         
         if(!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek()== ContextoExpressaoEnum.PARAMETROATUAL)
         {
             if(!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek()))
             {
                 this.pilhaERef.push(PassagemValOuRefEnum.REFERENCIA);
             }
         }
     }
     
     public void executarAcaoSemantica172(Token token) throws SemanticError{
         Simbolo simbolo = this.tabelaDeSimbolos.getSimbolo(this.pilhaPosid.pop());
         if(simbolo.getCategoria() == CategoriaIDEnum.PARAMETRO || simbolo.getCategoria() == CategoriaIDEnum.VARIAVEL)
         {             
             if(simbolo.getCategoria() == CategoriaIDEnum.VARIAVEL)
             {
                 Variavel variavel = (Variavel) simbolo;
                 if(variavel.getTipo().getTipo() == TipoDeVariavelEnum.VETOR)
                 {
                     throw new SemanticError("vetor deve ser indexado", token.getPosition());                     
                 }else
                 {
                     TipoVar = MudaTipo.getTipoPreDefinido(variavel.getTipo().getTipo());
                 }                  
             }else
             {
                 Parametro parametro = (Parametro) simbolo;
                 TipoVar = parametro.getTipo();
             }
             
             if(!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek()== ContextoExpressaoEnum.PARAMETROATUAL)
             {
                 if(!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek()))
                 {
                     this.pilhaERef.push(PassagemValOuRefEnum.REFERENCIA);
                 }
             }
             
         }else
         {
             if(simbolo.getCategoria() == CategoriaIDEnum.METODO)
             {
                 Metodo metodo = (Metodo) simbolo;
                 if(metodo.isRetornoNull())
                 {
                     throw new SemanticError("Esperava-se método com tipo", token.getPosition());
                 }else
                 {
                     if(metodo.getNumParametros() != 0 )
                     {
                         throw new SemanticError("Erro na quant. de parâmetros", token.getPosition());                         
                     }else
                     {
                         TipoVar = metodo.getResultado();
                     }
                     
                     if(!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL)
                     {
                         if(!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek()))
                         {
                             this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
                         }
                     }
                 }
                 
             }else
             {
                 if(simbolo.getCategoria() == CategoriaIDEnum.CONSTANTE)
                 {
                     Constante constante = (Constante) simbolo;
                     TipoVar = constante.getTipoPreDefinidoEnum();
                     if(!this.PilhaContextoEXPR.isEmpty() && this.PilhaContextoEXPR.peek() == ContextoExpressaoEnum.PARAMETROATUAL)
                     {
                         
                         if(!(!this.pilhaOpNega.isEmpty() && this.pilhaOpNega.peek()) && !(!this.pilhaOpUnario.isEmpty() && this.pilhaOpUnario.peek()))
                         {
                             this.pilhaERef.push(PassagemValOuRefEnum.VALOR);
                         }
                     }
                 }
                 else
                 {
                     throw new SemanticError("esperava-se var,id-método ou constante", token.getPosition());                     
                 }
             }
         }
     }
    
    /**
     * TipoConst := tipo da constante
     * ValConst  := valor da constante    
     */
    public void executarAcaoSemantica173(Token token) throws SemanticError {
        Simbolo s = this.tabelaDeSimbolos.getSimboloNivel(token.getLexeme(), this.nivelAtual);
        if(s == null)
        {
            throw new SemanticError("Id não declarado", token.getPosition());            
        }
        else
        {
            if(s.getCategoria() != CategoriaIDEnum.CONSTANTE)
            {
                throw new SemanticError("id de Constante esperado", token.getPosition());
            }
            else
            {
                Constante c = (Constante) s;
                this.TipoConst = c.getTipoPreDefinidoEnum();
                this.ValConst = c.getValor();                 
            }                       
        }        
    }
    
    /**
     * TipoConst := tipo da constante
     * ValConst  := valor da constante    
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
        if(token.getLexeme().length() - 2 == 1)  //testar isso 
        {
            this.TipoConst = TipoPreDefinidoEnum.CARACTER;            
        }else
        {
            this.TipoConst = TipoPreDefinidoEnum.CADEIA;            
        }       
        this.ValConst = token.getLexeme();
    }
    
    public void executarAcaoSemantica179(Token token) throws SemanticError {
        if(this.TipoConst != TipoPreDefinidoEnum.INTEIRO)
        {
            throw new SemanticError("A dim.deve ser uma constante inteira", token.getPosition());            
        }else
        {
            this.NumElementos = Integer.parseInt(this.ValConst);                       
        }
    }
    
    public void executarAcaoSemantica180(Token token) throws SemanticError {
        if(TipoConst != tipoAtual)
        {
            if(!(tipoAtual == TipoPreDefinidoEnum.REAL && TipoConst == TipoPreDefinidoEnum.INTEIRO))
            throw new SemanticError("Tipo da constante incorreto", token.getPosition());                        
        }
    }
}
