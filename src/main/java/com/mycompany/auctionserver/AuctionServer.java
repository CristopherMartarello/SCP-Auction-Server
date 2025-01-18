package com.mycompany.auctionserver;

import com.mycompany.utils.AuctionServerHelpers;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.JOptionPane;
import org.json.JSONObject;

public class AuctionServer {

    public static final String serverPublicKeyBase64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAotfntXqe1eKG9LsERj+PUuMM4BfyHfxJe42wB4/1DDLf+32/DybXjj2/I4uLXZY68o3KKYoc1/t6ACkvD58Jcr7v8t4sbmpDjiXGg8tcCw7GBoJd0fSXF1ZQjYdc7SnHnuT1kiF0VOcVsABRD7xqd7+RZeOufQP+b1edI5L9gQQpAOG/jVN0ZgGJ1WOthbxMYsfnstoEicwYYE/XFiz0LJV9CdaZnRhY8xKYM+JHpDcyLHR5+tpToEjlOlZlB1l3+/1oOMy48JIoPp+svzCi3ISs1SpazrAjy5rO3vpevg5TBLAiQbasb+Z6yCs6v9dhJF+KxxOaKddyx/7Gj+X8uwIDAQAB";
    public static final String serverPrivateKeyBase64 = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCi1+e1ep7V4ob0uwRGP49S4wzgF/Id/El7jbAHj/UMMt/7fb8PJteOPb8ji4tdljryjcopihzX+3oAKS8Pnwlyvu/y3ixuakOOJcaDy1wLDsYGgl3R9JcXVlCNh1ztKcee5PWSIXRU5xWwAFEPvGp3v5Fl4659A/5vV50jkv2BBCkA4b+NU3RmAYnVY62FvExix+ey2gSJzBhgT9cWLPQslX0J1pmdGFjzEpgz4kekNzIsdHn62lOgSOU6VmUHWXf7/Wg4zLjwkig+n6y/MKLchKzVKlrOsCPLms7e+l6+DlMEsCJBtqxv5nrIKzq/12EkX4rHE5op13LH/saP5fy7AgMBAAECggEAQXtjhAEKdQSR80Bu4Ba3+gMuTsCiTjeAjwvzQJ89UUn18onmtd+PJ3Yh8dev05LRIm8s35ZXsexAa2ckMvpnULqB/54irMXTY4pCMQYp/ZJTxBNRSde6mBpP6xGHJyE5UeZM75W/U1LXUD5YWmUQRRYGEDxFkqTz/+7x0/fMM8AE+QwvND1DoULcmjlqYBpJP5NEwBMrglRZz+5h7Vi72bHFsol+uWSU1/OCbX3EwGgBJ6xKLtgONGFtY0k7ujh5uw302HPkjC1P5EwkbYiUeuCguQ9hTJ6raFLMMBGyLtDy7C3GPr+mDPgWZ5G3EQPcapnPdAqIGJlOVW3DfcY9yQKBgQDhx7tJZSgYVX3EA4HJXk7R1Lz9ZfxRjEP2jcb+tP4KSiLyrYt1PjaxIlz8pBdcGGsDZfJJ/hTfyRc9Ygv1d7mZP8FP8p8KJoK2K/Kyo6DkuQIycZL1ONndo6ef10oJFuCRa7F59YWs6HbpKfhpMzQ1+UymQlO+msn+NY6hgMslWQKBgQC4o6sf0aQ6dBvBdRfkDUVbLgJTJ9J4CR3+pR1OkFG7WFiUpQzUHS6jJvZPOT0BN2xn+Z1nIMiv017FI/ZXQv9zxLacGZVqISc9VEx/2XKISMWXeJ3UMxRBJMa7Yp0c4kNqk+IbxMc1fBkf3BXzrDV2nO9oFmXMDcnK51QWH4xsMwKBgQDb450IfydMg76Hr26wglO7Ujh5heD2PuhV8ICUwgsEVG2y8cf3eI7ldvUe7GT/wZw/ZANTgswrovoqQxooh+DPWuNXjJDN3vHAoA6vYmMpPvHf1PLuNt8gV+nB53foYEp39m2TvMXiv0hIDyMqub6orlKzPbe306LUHK77paaziQKBgDeY4PBl2gPX7nukXJtI+7dm9UBA33lRlXyWD2sWveWhxpqL0H8WgnKSSty0KZByNexhF2p0TrnS9dh66bSA8hbUBwCeG4Wnkf8/oQFmYrxy3TytDylUcCblggnuucx2vUIcYZtm9209fvs+9EU5d6fNvbEj/WciR78XRRScT1ZNAoGBAIPYAiUHcFw7jaZymP0NGGNV/wD/Oh59HwFdkyN1UIidY2VQS1xL/XA5/QY2oonsYPbL4ia85e+3COGhFiD95v4m5/YyfZ38xpBe5lR4HxedToWtYA13Qc7ikqU1crYSHpC3TG+c5ZMozPVdmdNHx2+FaBzkQDBA94HQ1jJeh74C";
    public static PublicKey serverPublicKey = AuctionServerHelpers.decodePublicKey(serverPublicKeyBase64);
    public static PrivateKey serverPrivateKey = AuctionServerHelpers.decodePrivateKey(serverPrivateKeyBase64);

    public static int multicastPort = 50000;
    public static String multicastAddress = "230.0.0.1";
    public static MulticastSocket multicastSocket;

    public static SecretKey symmetricKey = AuctionServerHelpers.generateSymmetricKey();
    public static String symmetricKeyBase64 = AuctionServerHelpers.encodeSymmetricKey(symmetricKey);
    public static IvParameterSpec IV = AuctionServerHelpers.generateIV(symmetricKey);

    public static LinkedList<Item> auctionItems;

    public static void main(String[] args) throws IOException {
        int portaServidor = 0; // Porta na qual ocorrerá a conexão TCP
        InetAddress ipServidor = null;

        // Validando argumentos de entrada
        if (args.length != 2) {
            JOptionPane.showMessageDialog(null, "Argumentos inválidos.\n Utilize: <IP> <Porta>", "Erro!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } else {
            try {
                // Obtendo o IP e a Porta nos argumentos
                ipServidor = (InetAddress) InetAddress.getByName(args[0]);
                portaServidor = Integer.parseInt(args[1]);
                JOptionPane.showMessageDialog(null, "Servidor será iniciado no endereço " + ipServidor + ", porta " + portaServidor);
                runServer(ipServidor, portaServidor);

            } catch (UnknownHostException ex) {
                JOptionPane.showMessageDialog(null, "Endereço IP inválido: " + ex.getMessage(), "Erro!", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Porta inválida: " + ex.getMessage(), "Erro!", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }

    }

    public static void runServer(InetAddress ipServidor, int portaServidor) {
        try ( ServerSocket serverSocket = new ServerSocket(portaServidor, 0, ipServidor)) {
            System.out.println("Servidor TCP iniciado no endereço " + ipServidor + ", porta " + portaServidor);

            // Conectar ao grupo multicast
            InetAddress multicastGroup = InetAddress.getByName(multicastAddress);
            multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.joinGroup(multicastGroup);
            System.out.println("Servidor conectado ao grupo multicast.");
            populateAuctionItems();

            // Iniciar a thread para receber mensagens do multicast
            Thread multicastReceiver = new Thread(() -> {
                try {
                    receiveMulticastMessages();
                } catch (Exception ex) {
                    Logger.getLogger(AuctionServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            multicastReceiver.start();

            // Loop principal para aceitar conexões de clientes (TCP)
            while (true) {
                System.out.println("Aguardando conexões...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Conexão recebida de: " + clientSocket.getInetAddress());

                // nova thread para tratar a conexão do cliente (múltiplas conexões simultaneas)
                Thread clientHandler = new Thread(() -> JSONcommunication(clientSocket));
                clientHandler.start();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao iniciar o servidor: " + e.getMessage(), "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void JSONcommunication(Socket clientSocket) {
        try {
            // Streams de entrada e saída usando DataInputStream e DataOutputStream
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            // Leitura do JSON enviado pelo cliente
            String jsonMessage = in.readUTF(); // Alinha com writeUTF do cliente
            System.out.println("Mensagem Recebida [TCP]: " + jsonMessage);

            // Parse do JSON
            JSONObject json = new JSONObject(jsonMessage);
            String cpf = json.getString("CPF");
            String name = json.getString("name");
            String signatureBase64 = json.getString("signature");
            String text = json.getString("text");

            byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);

            // Obter chave pública do cliente
            String clientPublicKeyBase64 = loadPublicKey(cpf);
            JSONObject response = new JSONObject();

            if (clientPublicKeyBase64.contains("não encontrada")) {
                response.put("error", "CPF do cliente não é válido no servidor.");
                out.writeUTF(response.toString());
                return;
            }

            PublicKey clientPublicKey = AuctionServerHelpers.decodePublicKey(clientPublicKeyBase64);

            // Verificar assinatura
            boolean isValid = AuctionServerHelpers.verifySignature(text, signatureBytes, clientPublicKey);

            if (isValid) {

                // Servidor assina a resposta também
                text = "Usuário foi autenticado pelo servidor.";
                signatureBytes = AuctionServerHelpers.signMessage(text, serverPrivateKey);
                signatureBase64 = Base64.getEncoder().encodeToString(signatureBytes);

                // Codificar chave simétrica com chave pública do cliente
                String encodedSecretKey = AuctionServerHelpers.encryptWithPublicKey(symmetricKeyBase64, clientPublicKey);
                String encodedAddress = AuctionServerHelpers.encryptWithPublicKey(multicastAddress, clientPublicKey);
                String encodedPort = AuctionServerHelpers.encryptWithPublicKey(Integer.toString(multicastPort), clientPublicKey);

                // Criar resposta JSON
                response.put("address", encodedAddress);
                response.put("port", encodedPort);
                response.put("secretKey", encodedSecretKey);
                response.put("text", text);
                response.put("signature", signatureBase64);
            } else {
                response.put("error", "Usuário não autenticado no servidor, por favor verifique as credenciais e tente novamente.");
            }

            // Enviar a resposta ao cliente
            out.writeUTF(response.toString());

            // Fechar streams e socket
            in.close();
            out.close();
            clientSocket.close();
            System.out.println("Conexão encerrada.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String loadPublicKey(String cpf) {
        String resourcePath = "com/mycompany/keys/" + cpf + ".txt";
        String resourceNotFoundMessage = "Chave pública para o CPF " + cpf + " não encontrada.";

        try {
            InputStream inputStream = AuctionServer.class.getClassLoader().getResourceAsStream(resourcePath);

            if (inputStream == null) {
                System.err.println(resourceNotFoundMessage);
                return resourceNotFoundMessage;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();

            String publicKeyContent = content.toString().replaceAll("\\s+", "");
            System.out.println("Conteúdo da chave pública: " + publicKeyContent);

            return publicKeyContent;
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro na leitura da chave pública.";
        }
    }

    // Função para enviar uma mensagem ao grupo
    public static void sendMulticastMessage(String message) {
        try {
            InetAddress group = InetAddress.getByName(AuctionServer.multicastAddress);
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), group, AuctionServer.multicastPort);
            AuctionServer.multicastSocket.send(packet);
            System.out.println("[SERVER] - Mensagem enviada ao grupo multicast: " + message);
        } catch (IOException ex) {
            Logger.getLogger(AuctionServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Função para escutar as mensagens do grupo multicast
    public static void receiveMulticastMessages() throws Exception {
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(packet);  // Recebe a mensagem

                String message = new String(packet.getData(), 0, packet.getLength());
                String decryptedMessage = AuctionServerHelpers.decrypt(message, symmetricKey, IV);
                System.out.println("[SERVER] - Mensagem recebida do grupo multicast: " + decryptedMessage);

                // Chama a função para tratar a mensagem
                handleReceivedMessage(decryptedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Função para popular a lista ficticia de itens do leilão
    public static void populateAuctionItems() {
        auctionItems.add(new Item("Pintura de Leonardo Da Vinci", 5000000.00, "Uma obra rara de Da Vinci, pintada em 1503, é considerada uma das mais valiosas do mundo.", "imagens/tao deixando a gente sonhar.png"));
        auctionItems.add(new Item("Relógio de Ouro de Napoleão", 3500000.00, "Relógio de ouro usado por Napoleão Bonaparte durante suas campanhas, com gravuras detalhadas e história fascinante.", "imagens/tao deixando a gente sonhar.png"));
        auctionItems.add(new Item("Cálice Sagrado", 250000.00, "Cálice antigo que supostamente foi usado durante a Última Ceia, feito de ouro e pedras preciosas.", "imagens/tao deixando a gente sonhar.png"));
        auctionItems.add(new Item("Trono Medieval", 100000.00, "Trono de madeira nobre, feito à mão, usado em uma corte medieval durante o século XIII.", "imagens/tao deixando a gente sonhar.png"));
        auctionItems.add(new Item("Fósseis de Dinossauro", 150000.00, "Fósseis completos de um dinossauro da era Cretácea, incluindo crânio e ossos raros.", "imagens/dinosaur_fossils.jpg"));
        auctionItems.add(new Item("Espada Samurai Original", 300000.00, "Espada samurai original, forjada em ferro de alta qualidade durante o período Edo no Japão, com inscrições.", "imagens/samurai_sword.jpg"));
        auctionItems.add(new Item("Lâmpada Mágica de Aladdin", 500000.00, "Uma réplica encantadora da famosa lâmpada mágica de Aladdin, com detalhes em ouro e pedras preciosas.", "imagens/aladdin_lamp.jpg"));
    }

    // Função para tratar as ações de mensagem recebida
    public static void handleReceivedMessage(String message) throws Exception {
        System.out.println(message);
        if (message.startsWith("ENTROU")) {
            handleEnterMessage();
        } else if (message.startsWith("LANCE")) {
            handleBidMessage(message);
        } else if (message.startsWith("SAIR")) {
            handleExitMessage();
        }
    }

    // Ação para processar uma mensagem de lance
    public static void handleBidMessage(String message) throws Exception {
        // Exemplo: LANCE: valor;item
        String[] parts = message.split(";");
        String bidValue = parts[1];  // valor do lance
        String item = parts[2];  // item relacionado ao lance

        System.out.println(" [SERVER] Lance recebido: " + bidValue + " no item: " + item);

        // Lógica para processar o lance e encriptar
        String response = "[SERVER] Lance registrado: " + bidValue + " no item " + item;
        String encryptedResponse = AuctionServerHelpers.encrypt(response, symmetricKey, IV);
        sendMulticastMessage(encryptedResponse);
    }

    // Ação para processar quando alguém sair
    public static void handleExitMessage() {
        System.out.println("[SERVER] Um participante saiu no grupo.");
    }

    // Ação para processar quando alguém sair
    public static void handleEnterMessage() {
        System.out.println("[SERVER] Um participante entrou no grupo.");
    }
    
    // Fazer uma função para abrir o jframe com um botão de começar / resetar leilão já com a tela STARTAUCTION pronta.
    public static void openServerModal() {
        
    }
}
