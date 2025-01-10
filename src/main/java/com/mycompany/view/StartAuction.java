package com.mycompany.view;

import com.mycompany.auctionserver.AuctionServer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Cristopher
 */
public class StartAuction extends javax.swing.JFrame {
    
   private final LinkedList<String> auctionItems;
    private String currentItem;
    private int remainingTime;

    public StartAuction() {
        initComponents();
        auctionItems = new LinkedList<>();
        populateAuctionItems();

        // Cria o grupo Multicast
        AuctionServer.multicastSocket = createMulticastGroup();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        startAuctionButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Iniciar Leilão");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 70, -1));

        startAuctionButton.setText("OK");
        startAuctionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startAuctionButtonActionPerformed(evt);
            }
        });
        getContentPane().add(startAuctionButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 90, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startAuctionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startAuctionButtonActionPerformed
        // Inicia threads
        startAuction(); // Thread do timer e envio de itens
        new Thread(this::receiveMessages).start(); // Thread para escutar mensagens recebidas
    }//GEN-LAST:event_startAuctionButtonActionPerformed

    // Preenche a lista de itens para leilão
    private void populateAuctionItems() {
        auctionItems.add("Item 1: Relógio de ouro");
        auctionItems.add("Item 2: Pintura rara");
        auctionItems.add("Item 3: Viagem internacional");
    }

    private void startAuction() {
        new Thread(() -> {
            while (!auctionItems.isEmpty()) {
                currentItem = auctionItems.poll(); // Obtém o próximo item
                remainingTime = 30; // Tempo inicial (30 segundos para exemplo)

                System.out.println("Novo item em leilão: " + currentItem);
                sendNewItem();

                // Contagem regressiva
                while (remainingTime > 0) {
                    sendTimeRemaining();
                    try {
                        Thread.sleep(1000); // Aguarda 1 segundo
                        remainingTime--;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Tempo esgotado para: " + currentItem);
                sendAuctionEnd();
            }

            System.out.println("Leilão finalizado!");
            closeMulticastSocket();
        }).start();
    }

    private void sendNewItem() {
        sendMulticastMessage("Novo item: " + currentItem);
    }

    private void sendTimeRemaining() {
        sendMulticastMessage("Tempo restante: " + remainingTime + " segundos");
    }

    private void sendAuctionEnd() {
        sendMulticastMessage("Leilão encerrado para: " + currentItem);
    }

    private void sendMulticastMessage(String message) {
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
                    InetAddress.getByName(AuctionServer.multicastAddress), AuctionServer.multicastPort);
            AuctionServer.multicastSocket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(StartAuction.class.getName()).log(Level.SEVERE, "Erro ao enviar mensagem multicast", ex);
        }
    }

    private void receiveMessages() {
        try {
            MulticastSocket socket = AuctionServer.multicastSocket;
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Mensagem recebida: " + message);

                // Atualiza o histórico de lances no painel
                SwingUtilities.invokeLater(() -> {
                    String currentText = ""; // Substituir pelo campo do histórico
                    currentText += "\n" + message;
                    System.out.println("Histórico atualizado: " + currentText);
                });
            }
        } catch (IOException ex) {
            Logger.getLogger(StartAuction.class.getName()).log(Level.SEVERE, "Erro ao receber mensagens multicast", ex);
        }
    }

    private void closeMulticastSocket() {
        try {
            AuctionServer.multicastSocket.leaveGroup(InetAddress.getByName(AuctionServer.multicastAddress));
            AuctionServer.multicastSocket.close();
            System.out.println("Socket multicast fechado.");
        } catch (IOException ex) {
            Logger.getLogger(StartAuction.class.getName()).log(Level.SEVERE, "Erro ao fechar socket multicast", ex);
        }
    }

    public static MulticastSocket createMulticastGroup() {
        try {
            InetAddress group = InetAddress.getByName(AuctionServer.multicastAddress);
            MulticastSocket multicastSocket = new MulticastSocket(AuctionServer.multicastPort);
            multicastSocket.joinGroup(group);
            System.out.println("Grupo multicast criado em " + AuctionServer.multicastAddress + ":" + AuctionServer.multicastPort);
            return multicastSocket;
        } catch (IOException ex) {
            Logger.getLogger(StartAuction.class.getName()).log(Level.SEVERE, "Erro ao criar grupo multicast", ex);
        }
        return null;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton startAuctionButton;
    // End of variables declaration//GEN-END:variables
}
