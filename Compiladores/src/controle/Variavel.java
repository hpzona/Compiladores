package controle;

// Classe desenvolvida por Lucas e Willian
public class Variavel extends Simbolo{
    protected int deslocamento;
    protected TipoDeVariavel tipo;

    public Variavel() {
    }

    public Variavel(int deslocamento, TipoDeVariavel tipo) {
        this.deslocamento = deslocamento;
        this.tipo = tipo;
    }

    public Variavel(String nome, CategoriaIDEnum categoria, int nivel) {
        super(nome, categoria, nivel);
    }
    
    public Variavel(String nome, int nivel) {
        super(nome, nivel);
    }

    public Variavel(int deslocamento, TipoDeVariavel tipo, String nome, CategoriaIDEnum categoria, int nivel) {
        super(nome, categoria, nivel);
        this.deslocamento = deslocamento;
        this.tipo = tipo;
    }

    public int getDeslocamento() {
        return deslocamento;
    }

    public TipoDeVariavel getTipo() {
        return tipo;
    }

    public void setDeslocamento(int deslocamento) {
        this.deslocamento = deslocamento;
    }

    public void setTipo(TipoDeVariavel tipo) {
        this.tipo = tipo;
    }       
}
