package controle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Classe desenvolvida por Lucas e Willian
public class TabelaDeSimbolos {
    private List<Simbolo> tabela;

    public TabelaDeSimbolos() {
        tabela = new ArrayList<Simbolo>();
    }

    public TabelaDeSimbolos(List<Simbolo> tabela) {
        this.tabela = tabela;
    }

    public List<Simbolo> getTabela() {
        return tabela;
    }

    public void setTabela(List<Simbolo> tabela) {
        this.tabela = tabela;
    }
    
    public void addSimbolo(Simbolo s)
    {
        tabela.add(s);            
    }
    
    public void addSimbolo(Simbolo s, int posicao)
    {
        tabela.set(posicao, s);
    }
    
    public void removerSimbolo(Simbolo s)
    {
        tabela.remove(s);    
    }
    
    public boolean jaExisteSimbolo(Simbolo s)
    {
        if(tabela.contains(s))   
        {
            return true;
        }
        else
        {
            return false;
        }
    }   
    
    
    public boolean jaExisteSimboloNivel(String s, int nivel)
    {
        for (Iterator<Simbolo> it = tabela.iterator(); it.hasNext();) {
            Simbolo simbolo = it.next();
            if(simbolo.getNome().equals(s) && (simbolo.getNivel() == nivel || simbolo.getNivel() == 0))
            {
                return true;
            }
        }
        return false;
    }
    
    public Simbolo getSimboloNivel(String s, int nivel)
    {
        Simbolo resultado = new Simbolo("", 0);
        for (Iterator<Simbolo> it = tabela.iterator(); it.hasNext();) {
            Simbolo simbolo = it.next();
            if((simbolo.getNome().equals(s) && simbolo.getNivel() <= nivel) && simbolo.getNivel() > resultado.nivel)
            {
                resultado = simbolo;
            }
        }
        if(resultado.getNome().equals(""))
        {
            return null;
        }
        return resultado ;
    }
    
    public void removerSimboloNoNivel(Simbolo s, int nivel)
    {
        Simbolo aRemover =  new Simbolo();
        for (Iterator<Simbolo> it = tabela.iterator(); it.hasNext();) {
            Simbolo simbolo = it.next();
            if(simbolo.equals(s) && simbolo.getNivel() == nivel)
            {
                aRemover = simbolo;
            }
        }
        tabela.remove(aRemover); 
    }
    
    public int getTamanho() {
        return tabela.size();
    }
    
    public Simbolo getSimbolo(int i) {
        return tabela.get(i);
    }

    public void removeNivelAtual(int nivelAtual) {
        for (int i = 0; i < tabela.size(); i++) {
            if (tabela.get(i).getNivel() == nivelAtual) {
                tabela.remove(i);
                i--;
            }
        }
    }

    public Integer getPosicaoID(String nome, int nivel) {
        Simbolo simbolo = new Simbolo("", 0);
        int resultado = 0;
        for (int i = 0; i < tabela.size(); i++) {
            if((tabela.get(i).getNome().equals(nome) && tabela.get(i).getNivel() <= nivel) && (tabela.get(i).getNivel() > simbolo.nivel))
            {
                simbolo = tabela.get(i);
                resultado = i;
            }
        }
        return resultado;
    }

    public boolean jaExisteSimboloNesteEscopo(String s, int nivel) { //validar
        for (Iterator<Simbolo> it = tabela.iterator(); it.hasNext();) {
            Simbolo simbolo = it.next();
            if(simbolo.getNome().equals(s) && simbolo.getNivel() <= nivel)
            {
                return true;
            }
        }
        return false;
    }
}