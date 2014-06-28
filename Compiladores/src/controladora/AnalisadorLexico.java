package controladora;

import gals.LexicalError;
import gals.Lexico;
import gals.Token;
import visao.JanelaPrincipal;

public class AnalisadorLexico {

    private final JanelaPrincipal jp;

    public AnalisadorLexico(JanelaPrincipal janelaPrincipal) {
        jp = janelaPrincipal;
    }

    public void analisarLexico(String codigo) {

        Lexico analisadorLexico = new Lexico();
        analisadorLexico.setInput(codigo);

        try {
            Token token = null;
            while ((token = analisadorLexico.nextToken()) != null) {
            }
            jp.mostrarResultadoDaAnalise("Análise léxica sem erros.\n");
        } catch (LexicalError e) {
            jp.setCursorNoErro(e.getPosition());
            jp.mostrarResultadoDaAnalise("Erro léxico na posição: " + e.getPosition() + "\n\n" + e.getMessage() + "\n\n");
        } finally {

        }
    }

}
