package com.mycompany.view;

import com.mycompany.auctionserver.AuctionServer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author Cristopher
 */
public class StartAuction extends javax.swing.JFrame {
    
    public StartAuction() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        startAuctionButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Iniciar Leilão");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 110, 70, -1));

        startAuctionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/start.png"))); // NOI18N
        startAuctionButton.setText("OK");
        startAuctionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startAuctionButtonActionPerformed(evt);
            }
        });
        getContentPane().add(startAuctionButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 420, 370));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startAuctionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startAuctionButtonActionPerformed
        try {
            AuctionServer.startAuction();
        } catch (Exception ex) {
            Logger.getLogger(StartAuction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_startAuctionButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton startAuctionButton;
    // End of variables declaration//GEN-END:variables
}
