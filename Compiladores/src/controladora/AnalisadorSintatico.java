package controladora;

import gals.LexicalError;
import gals.Lexico;
import gals.SemanticError;
import gals.Sintatico;
import gals.SyntaticError;
import visao.JanelaPrincipal;

public class AnalisadorSintatico {

    private final JanelaPrincipal jp;

    public AnalisadorSintatico(JanelaPrincipal janelaPrincipal) {
        jp = janelaPrincipal;
    }

    public void analisarSintaxe(String codigo) {
        Lexico analisadorLexico = new Lexico();
        Sintatico analisadorSintatico = new Sintatico();

        analisadorLexico.setInput(codigo);

        try {
            analisadorSintatico.parse(analisadorLexico, null);
            jp.mostrarResultadoDaAnalise("Análise sintática sem erros.\n");
        } catch (LexicalError | SyntaticError ex) {
            jp.setCursorNoErro(ex.getPosition());
            jp.mostrarResultadoDaAnalise("Erro sintático na posição: " + ex.getPosition() + "\n\n" + ex.getMessage() + "\n\n");
        } catch (SemanticError ex) {
            jp.mostrarResultadoDaAnalise("Erro Semântico na posição: " + ex.getPosition() + "\n\n" + ex.getMessage() + "\n\n");
        }
    }
}
