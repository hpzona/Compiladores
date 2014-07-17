package controle;

// Classe desenvolvida por Lucas e Willian
public class TipoDeVariavel {
    protected TipoDeVariavelEnum tipo;
    protected int tamanho;
    protected TipoPreDefinidoEnum tipoElementos;

    public TipoDeVariavel() {
    }

    public TipoDeVariavel(TipoDeVariavelEnum tipo, int tamanho, TipoPreDefinidoEnum tipoElementos) {
        this.tipo = tipo;
        this.tamanho = tamanho;
        this.tipoElementos = tipoElementos;
    }

    public TipoDeVariavelEnum getTipo() {
        return tipo;
    }

    public int getTamanho() {
        return tamanho;
    }

    public TipoPreDefinidoEnum getTipoElementos() {
        return tipoElementos;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public void setTipo(TipoDeVariavelEnum tipo) {
        this.tipo = tipo;
    }    
}
