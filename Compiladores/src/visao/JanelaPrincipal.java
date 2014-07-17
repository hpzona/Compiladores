package visao;

import controle.AnalisadorLexico;
import controle.AnalisadorSemantico;
import controle.AnalisadorSintatico;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

// Classe desenvolvida por Lucas e Willian
public class JanelaPrincipal extends javax.swing.JFrame {

    AnalisadorLexico analisadorLexico;
    AnalisadorSintatico analisadorSintatico;
    AnalisadorSemantico analisadorSemantico;

    public JanelaPrincipal() {
        initComponents();
        setLocationRelativeTo(null);
        this.analisadorLexico = new AnalisadorLexico(this);
        this.analisadorSintatico = new AnalisadorSintatico(this);
        this.analisadorSemantico = new AnalisadorSemantico(this);
    }
    
    public void interaja() {
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        jTextCodigo = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextResultadoDaAnalise = new javax.swing.JTextArea();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuArquivo = new javax.swing.JMenu();
        jMenuItemNovo = new javax.swing.JMenuItem();
        jMenuItemAbrir = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenuLexico = new javax.swing.JMenu();
        jMenuItemAnalisarLexico = new javax.swing.JMenuItem();
        jMenuSintatico = new javax.swing.JMenu();
        jMenuItemAnalisarSintatico = new javax.swing.JMenuItem();
        jMenuSemantico = new javax.swing.JMenu();
        jMenuItemAnalisarSemantico = new javax.swing.JMenuItem();
        jMenuajuda = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Compilador");

        jTextCodigo.setColumns(20);
        jTextCodigo.setRows(5);
        jScrollPane.setViewportView(jTextCodigo);

        jTextResultadoDaAnalise.setColumns(20);
        jTextResultadoDaAnalise.setRows(5);
        jScrollPane1.setViewportView(jTextResultadoDaAnalise);

        jMenuArquivo.setMnemonic('A');
        jMenuArquivo.setText("Arquivo");

        jMenuItemNovo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemNovo.setLabel("Novo");
        jMenuItemNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNovoActionPerformed(evt);
            }
        });
        jMenuArquivo.add(jMenuItemNovo);

        jMenuItemAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAbrir.setText("Abrir");
        jMenuItemAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAbrirActionPerformed(evt);
            }
        });
        jMenuArquivo.add(jMenuItemAbrir);

        jMenuItemSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSalvar.setText("Salvar");
        jMenuItemSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSalvarActionPerformed(evt);
            }
        });
        jMenuArquivo.add(jMenuItemSalvar);
        jMenuArquivo.add(jSeparator1);

        jMenuItemSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSair.setText("Sair");
        jMenuItemSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSairActionPerformed(evt);
            }
        });
        jMenuArquivo.add(jMenuItemSair);

        jMenuBar.add(jMenuArquivo);

        jMenuLexico.setMnemonic('L');
        jMenuLexico.setText("Léxico");

        jMenuItemAnalisarLexico.setText("Compilar");
        jMenuItemAnalisarLexico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAnalisarLexicoActionPerformed(evt);
            }
        });
        jMenuLexico.add(jMenuItemAnalisarLexico);

        jMenuBar.add(jMenuLexico);

        jMenuSintatico.setMnemonic('S');
        jMenuSintatico.setText("Sintático");

        jMenuItemAnalisarSintatico.setText("Compilar");
        jMenuItemAnalisarSintatico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAnalisarSintaticoActionPerformed(evt);
            }
        });
        jMenuSintatico.add(jMenuItemAnalisarSintatico);

        jMenuBar.add(jMenuSintatico);

        jMenuSemantico.setMnemonic('e');
        jMenuSemantico.setText("Semântico");

        jMenuItemAnalisarSemantico.setText("Compilar");
        jMenuItemAnalisarSemantico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAnalisarSemanticoActionPerformed(evt);
            }
        });
        jMenuSemantico.add(jMenuItemAnalisarSemantico);

        jMenuBar.add(jMenuSemantico);

        jMenuajuda.setMnemonic('j');
        jMenuajuda.setText("Ajuda");
        jMenuBar.add(jMenuajuda);

        setJMenuBar(jMenuBar);
        jMenuBar.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAbrirActionPerformed
        JFileChooser jFileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                ".txt ou .lsi", "txt", "lsi");
        jFileChooser.setFileFilter(filter);
        jFileChooser.setAcceptAllFileFilterUsed(false);
        if (jFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File arquivo = jFileChooser.getSelectedFile();
            try {
                jTextCodigo.read(new FileReader(arquivo), null);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao abrir arquivo. " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jMenuItemAbrirActionPerformed

    private void jMenuItemSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalvarActionPerformed
        JFileChooser jFileChooser = new JFileChooser();
        String caminhoCompleto;
        File arquivo;
        
        if (jFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            arquivo = jFileChooser.getSelectedFile();
            caminhoCompleto = jFileChooser.getSelectedFile().getAbsolutePath();
            try {
                if ((caminhoCompleto.endsWith(".txt") || caminhoCompleto.endsWith(".lsi") || caminhoCompleto.endsWith(".TXT") || caminhoCompleto.endsWith(".LSI"))) {
                    jTextCodigo.write(new FileWriter(arquivo));
                } else {
                    JOptionPane.showMessageDialog(null, "É permitido salvar apenas com as extensões .txt ou .lsi");
                }
            } catch (IOException e) {
               JOptionPane.showMessageDialog(null, "Erro ao salvar arquivo. " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jMenuItemSalvarActionPerformed

    private void jMenuItemAnalisarLexicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAnalisarLexicoActionPerformed
        this.analisadorLexico.analisarLexico(jTextCodigo.getText());

    }//GEN-LAST:event_jMenuItemAnalisarLexicoActionPerformed

    private void jMenuItemAnalisarSintaticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAnalisarSintaticoActionPerformed
        this.analisadorSintatico.analisarSintaxe(jTextCodigo.getText());
    }//GEN-LAST:event_jMenuItemAnalisarSintaticoActionPerformed

    private void jMenuItemSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSairActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemSairActionPerformed

    private void jMenuItemNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNovoActionPerformed
        jTextCodigo.setText("");
    }//GEN-LAST:event_jMenuItemNovoActionPerformed

    private void jMenuItemAnalisarSemanticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAnalisarSemanticoActionPerformed
        this.analisadorSemantico.analisarSemantica(jTextCodigo.getText());
    }//GEN-LAST:event_jMenuItemAnalisarSemanticoActionPerformed

    public void mostrarResultadoDaAnalise(String resultado) {
        //Exibe na textArea de saída os resultado da análise
        this.jTextResultadoDaAnalise.setText(resultado);
    }

    public void setCursorNoErro(int position) {
        jTextCodigo.setCaretPosition(position);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenuArquivo;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItemAbrir;
    private javax.swing.JMenuItem jMenuItemAnalisarLexico;
    private javax.swing.JMenuItem jMenuItemAnalisarSemantico;
    private javax.swing.JMenuItem jMenuItemAnalisarSintatico;
    private javax.swing.JMenuItem jMenuItemNovo;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JMenu jMenuLexico;
    private javax.swing.JMenu jMenuSemantico;
    private javax.swing.JMenu jMenuSintatico;
    private javax.swing.JMenu jMenuajuda;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTextArea jTextCodigo;
    private javax.swing.JTextArea jTextResultadoDaAnalise;
    // End of variables declaration//GEN-END:variables
}
