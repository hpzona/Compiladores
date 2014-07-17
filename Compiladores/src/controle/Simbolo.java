package controle;

import java.util.Objects;

// Classe desenvolvida por Lucas e Willian
public class Simbolo {
    protected String nome;
    protected CategoriaIDEnum categoria;
    protected int nivel;

    public Simbolo() {
    }
    
    public Simbolo(String nome, int nivel) {
        this.nome = nome;        
        this.nivel = nivel;
    }

    public Simbolo(String nome, CategoriaIDEnum categoria, int nivel) {
        this.nome = nome;
        this.categoria = categoria;
        this.nivel = nivel;
    }

    public CategoriaIDEnum getCategoria() {
        return categoria;
    }

    public int getNivel() {
        return nivel;
    }

    public String getNome() {
        return nome;
    }

    public void setCategoria(CategoriaIDEnum categoria) {
        this.categoria = categoria;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object obj) {
        if ( !(obj instanceof Simbolo) ) {
            return false;
        }
        return this.nome.equals(((Simbolo)obj).nome);
    }   

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.nome);
        hash = 89 * hash + (this.categoria != null ? this.categoria.hashCode() : 0);
        hash = 89 * hash + this.nivel;
        return hash;
    }
}
