/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventanas;

import java.awt.event.KeyEvent;
import java.util.List;
import javafx.util.Pair;
import virusexpansion.Simulacion;

/**
 *
 * @author Santiago Pérez Gómez
 */
public class CargarParametrosComunidad extends javax.swing.JDialog {

    private java.awt.Frame parent;
    private int numComunidades;
    private int numComunidad;
    private Simulacion simulacion;
    private List<Pair<String, Double>> pair;

    /**
     * Creates new form CargarParametrosComunidad
     */
    public CargarParametrosComunidad(java.awt.Frame parent, int numComunidades, int numComunidad, Simulacion simulacion, List<Pair<String, Double>> pair) {
        //super(parent, modal);
        this.parent = parent;
        this.numComunidad = numComunidad;
        this.numComunidades = numComunidades;
        this.simulacion = simulacion;
        this.pair = pair;
        
        initComponents();
        
        String nombre = this.TituloParametrosComunidad.getText();
        this.TituloParametrosComunidad.setText(nombre+" "+(numComunidad+1));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        botonOk = new javax.swing.JButton();
        labelNombreComunidad = new javax.swing.JLabel();
        labelPoblacionTotal = new javax.swing.JLabel();
        textFieldNomnbreComunidad = new javax.swing.JTextField();
        textFieldPoblacion = new javax.swing.JTextField();
        TituloParametrosComunidad = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        botonOk.setText("Aceptar");
        botonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonOkActionPerformed(evt);
            }
        });

        labelNombreComunidad.setText("Nombre Comunidad");

        labelPoblacionTotal.setText("Población Total");

        textFieldPoblacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textFieldPoblacionKeyTyped(evt);
            }
        });

        TituloParametrosComunidad.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        TituloParametrosComunidad.setText("Introduzca los datos indicados de la comunidad");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(TituloParametrosComunidad))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(botonOk)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelPoblacionTotal)
                                    .addComponent(labelNombreComunidad))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textFieldNomnbreComunidad, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textFieldPoblacion, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(TituloParametrosComunidad)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNombreComunidad)
                    .addComponent(textFieldNomnbreComunidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldPoblacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPoblacionTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonOk)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        parent.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void botonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonOkActionPerformed
        String nombreComunidad = this.textFieldNomnbreComunidad.getText();
        String poblacionString = this.textFieldPoblacion.getText();
        if (!nombreComunidad.isEmpty() && !poblacionString.isEmpty()) {
            double poblacion = Double.valueOf(poblacionString);
            pair.add(new Pair<>(nombreComunidad, poblacion));
            this.numComunidad++;
            if (this.numComunidad < this.numComunidades) {
                CargarParametrosComunidad formVerParametrosComunidad
                        = new CargarParametrosComunidad(parent, numComunidades, (numComunidad), simulacion, pair);
                formVerParametrosComunidad.setVisible(true);
                formVerParametrosComunidad.setLocationRelativeTo(null);
                this.setVisible(false);
            } else {
                simulacion.cargarParametrosFrontComunidad(pair);
                ((Inicio) parent).botonIniciarEnabled();
                parent.setVisible(true);
                this.dispose();
            }
        }
    }//GEN-LAST:event_botonOkActionPerformed

    private void textFieldPoblacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldPoblacionKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
            if (c == '.') {
                int cont = 0;
                String text = this.textFieldPoblacion.getText();
                for (int i = 0; i < text.length(); i++) {
                    if (text.charAt(i) == '.') {
                        cont++;
                    }
                }
                if (cont > 0 || text.isEmpty()) {
                    getToolkit().beep();
                    evt.consume();
                }
            } else {
                getToolkit().beep();
                evt.consume();
            }
        }
    }//GEN-LAST:event_textFieldPoblacionKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel TituloParametrosComunidad;
    private javax.swing.JButton botonOk;
    private javax.swing.JLabel labelNombreComunidad;
    private javax.swing.JLabel labelPoblacionTotal;
    private javax.swing.JTextField textFieldNomnbreComunidad;
    private javax.swing.JTextField textFieldPoblacion;
    // End of variables declaration//GEN-END:variables
}
