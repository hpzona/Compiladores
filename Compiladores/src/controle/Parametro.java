package controle;

// Classe desenvolvida por Lucas e Willian
public class Parametro extends Simbolo{
    protected int deslocamento;
    protected PassagemValOuRefEnum passagemValOuRefEnum;
    protected TipoPreDefinidoEnum tipoPreDefinidoEnum;

    public Parametro() {
    }

    public Parametro(String nome, CategoriaIDEnum categoria, int nivel) {
        super(nome, categoria, nivel);
    }
    
    public Parametro(String nome, int nivel) {
        super(nome, nivel);
    }

    public Parametro(int deslocamento, PassagemValOuRefEnum passagemValOuRefEnum, TipoPreDefinidoEnum tipoPreDefinidoEnum) {
        this.deslocamento = deslocamento;
        this.passagemValOuRefEnum = passagemValOuRefEnum;
        this.tipoPreDefinidoEnum = tipoPreDefinidoEnum;
    }

    public Parametro(int deslocamento, PassagemValOuRefEnum passagemValOuRefEnum, TipoPreDefinidoEnum tipoPreDefinidoEnum, String nome, CategoriaIDEnum categoria, int nivel) {
        super(nome, categoria, nivel);
        this.deslocamento = deslocamento;
        this.passagemValOuRefEnum = passagemValOuRefEnum;
        this.tipoPreDefinidoEnum = tipoPreDefinidoEnum;
    }   

    public int getDeslocamento() {
        return deslocamento;
    }

    public PassagemValOuRefEnum getPassagemValOuRefEnum() {
        return passagemValOuRefEnum;
    }

    public TipoPreDefinidoEnum getTipo() {
        return tipoPreDefinidoEnum;
    }

    public void setDeslocamento(int deslocamento) {
        this.deslocamento = deslocamento;
    }

    public void setPassagemValOuRefEnum(PassagemValOuRefEnum passagemValOuRefEnum) {
        this.passagemValOuRefEnum = passagemValOuRefEnum;
    }

    public void setTipo(TipoPreDefinidoEnum tipoPreDefinidoEnum) {
        this.tipoPreDefinidoEnum = tipoPreDefinidoEnum;
    }    
}
