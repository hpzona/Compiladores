package controle;

import java.util.List;
import java.util.Stack;

// Classe desenvolvida por Lucas e Willian
public class Metodo extends Simbolo{
    protected int enderecoDaPrimeiraInstrucao;
    protected int numeroDeParametros;
    protected List<Parametro> listaParametros;
    protected TipoPreDefinidoEnum resultado; 
    protected boolean retornoNull;

    public Metodo() {
    }

    public Metodo(String nome, CategoriaIDEnum categoria, int nivel) {
        super(nome, categoria, nivel);
    }

    public Metodo(int endereço1Instrução, int numParametros, List<Parametro> listaParametros, TipoPreDefinidoEnum resultado) {
        this.enderecoDaPrimeiraInstrucao = endereço1Instrução;
        this.numeroDeParametros = numParametros;
        this.listaParametros = listaParametros;
        this.resultado = resultado;
    }

    public Metodo(int endereço1Instrução, int numParametros, List<Parametro> listaParametros, TipoPreDefinidoEnum resultado, String nome, CategoriaIDEnum categoria, int nivel) {
        super(nome, categoria, nivel);
        this.enderecoDaPrimeiraInstrucao = endereço1Instrução;
        this.numeroDeParametros = numParametros;
        this.listaParametros = listaParametros;
        this.resultado = resultado;
    }    

    public int getEndereço1Instrução() {
        return enderecoDaPrimeiraInstrucao;
    }

    public List<Parametro> getListaParametros() {
        return listaParametros;
    }

    public int getNumParametros() {
        return numeroDeParametros;
    }

    public TipoPreDefinidoEnum getResultado() {
        return resultado;
    }

    public boolean isRetornoNull() {
        return retornoNull;
    }   

    public void setEndereço1Instrução(int endereço1Instrução) {
        this.enderecoDaPrimeiraInstrucao = endereço1Instrução;
    }

    public void setListaParametros(List<Parametro> listaParametros) {
        this.listaParametros = listaParametros;
    }

    public void setNumParametros(int numParametros) {
        this.numeroDeParametros = numParametros;
    }

    public void setResultado(TipoPreDefinidoEnum resultado) {
        this.resultado = resultado;
    }

    public void setRetornoNull(boolean retornoNull) {
        this.retornoNull = retornoNull;
    }
    
    public Stack<Parametro> getPilhaParametro()
    {
        Stack<Parametro> retorno = new Stack<Parametro>();
        for (int i = this.listaParametros.size() - 1; i >=  0; i--) {
            retorno.push(this.listaParametros.get(i));
        }
        return retorno;
    }
}
