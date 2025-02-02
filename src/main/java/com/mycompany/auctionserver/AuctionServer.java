package com.mycompany.auctionserver;

import com.mycompany.utils.AuctionServerHelpers;
import com.mycompany.view.StartAuction;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.json.JSONArray;
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

    private static Item currentItem;
    private static int currentItemIndex = 0;  // Índice do item atual
    private static int timeLeft = 60;  // Duração de cada item (em segundos)
    private static Timer auctionTimer;
    public static List<Item> auctionItems = new ArrayList<>();

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
        try (ServerSocket serverSocket = new ServerSocket(portaServidor, 0, ipServidor)) {
            System.out.println("Servidor TCP iniciado no endereço " + ipServidor + ", porta " + portaServidor);

            // Conectar ao grupo multicast
            InetAddress multicastGroup = InetAddress.getByName(multicastAddress);
            multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.joinGroup(multicastGroup);
            System.out.println("Servidor conectado ao grupo multicast.");
            populateAuctionItems();
            openServerModal();

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
                System.out.println("[SERVER] - Mensagem recebida do grupo multicast: " + message);

                // Chama a função para tratar a mensagem
                handleReceivedMessage(decryptedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Função para popular a lista ficticia de itens do leilão
    public static void populateAuctionItems() {
        auctionItems.add(new Item(
                "Mona Lisa",
                7800000.00,
                "A obra-prima de Leonardo da Vinci, pintada entre 1503 e 1506. Este quadro icônico, conhecido por seu sorriso enigmático, é uma das pinturas mais valiosas e reconhecíveis do mundo.",
                "/imagens/monalisa.png"
        ));
        auctionItems.add(new Item(
                "Álbum 'The Beatles - Abbey Road'",
                500000.00,
                "Uma edição rara e autografada do álbum icônico 'Abbey Road', lançado pelos Beatles em 1969. Inclui anotações originais de Paul McCartney.",
                "/imagens/abbey_road.png"
        ));
//        auctionItems.add(new Item(
//                "Manuscrito de Albert Einstein",
//                150000.00,
//                "Uma coleção de anotações e fórmulas escritas à mão por Albert Einstein, incluindo rascunhos da famosa equação E=mc². Uma peça rara de história científica.",
//                "/imagens/manuscrito_einstein.png"
//        ));
//        auctionItems.add(new Item(
//                "Espada Excalibur",
//                500000.00,
//                "A lendária espada do Rei Arthur, com lâmina forjada por magia e empunhadura adornada com pedras preciosas. Um artefato místico repleto de histórias.",
//                "/imagens/excalibur.png"
//        ));
//        auctionItems.add(new Item(
//                "Chevrolet Impala 1967",
//                120000.00,
//                "Um clássico americano, o Chevrolet Impala 1967 é uma lenda entre os carros clássicos. Totalmente restaurado, com pintura preta impecável e motor V8.",
//                "/imagens/chevrolet_impala.png"
//        ));
//        auctionItems.add(new Item(
//                "Fóssil de Tiranossauro Rex",
//                85000.00,
//                "Um fóssil completo de Tiranossauro Rex, datado de 66 milhões de anos atrás. Inclui ossos preservados e crânio quase intacto. Uma janela para o passado pré-histórico.",
//                "/imagens/fossil_t_rex.png"
//        ));
//        auctionItems.add(new Item(
//                "Camisa de Pelé - Final de 1970",
//                750000.00,
//                "A lendária camisa usada por Pelé durante a final da Copa do Mundo de 1970, onde o Brasil conquistou o tricampeonato. Um pedaço da história do futebol.",
//                "/imagens/camisa_pele.png"
//        ));
    }

    // Função para tratar as ações de mensagem recebida
    public static void handleReceivedMessage(String message) throws Exception {
        JSONObject jsonMessage = new JSONObject(message);
        // Verificar o tipo da mensagem pelo campo "@type"
        if (jsonMessage.has("@type")) {
            String actionType = jsonMessage.getString("@type");

            switch (actionType) {
                case "enter":
                    handleEnterMessage(jsonMessage);
                    break;
                case "bid":
                    handleBidMessage(jsonMessage);
                    break;
                case "exit":
                    handleExitMessage(jsonMessage);
                    break;
                default:
                    System.out.println("");
            }
        } else {
            System.out.println("[SERVER] Mensagem recebida não possui o campo '@type'.");
        }
    }

    // Ação para processar uma mensagem de lance
    public static void handleBidMessage(JSONObject jsonMessage) throws Exception {
        // Extrai informações do JSON
        String user = jsonMessage.getString("user");
        double userBid = jsonMessage.getDouble("userBid");
        String item = jsonMessage.getString("item");
        double currentBid = Double.parseDouble(jsonMessage.getString("currentBid"));

        if (userBid > currentBid && userBid > currentItem.getPrice()) {
            System.out.println("[SERVER] Lance recebido: " + userBid + " no item: " + item + " por " + user);

            // Atualizando o lance do usuário
            currentItem.setCurrentBid(userBid);
            currentItem.setCurrentWinner(user);

            // Resposta para confirmar o lance
            JSONObject responseJson = new JSONObject();
            responseJson.put("@type", "success");
            responseJson.put("bidValue", userBid);
            responseJson.put("item", item);
            responseJson.put("currentWinner", user);

            // Envia a resposta criptografada
            String response = AuctionServerHelpers.encrypt(responseJson.toString(), symmetricKey, IV);
            sendMulticastMessage(response);
        } else {
            JSONObject responseJson = new JSONObject();
            responseJson.put("@type", "error");

            // Envia a resposta criptografada
            String response = AuctionServerHelpers.encrypt(responseJson.toString(), symmetricKey, IV);
            sendMulticastMessage(response);
        }
    }

    // Ação para processar quando alguém sair
    public static void handleExitMessage(JSONObject jsonMessage) {
        System.out.println("[SERVER] Um participante saiu no grupo.");
        System.out.println("[SERVER] " + jsonMessage);
    }

    // Ação para processar quando alguém sair
    public static void handleEnterMessage(JSONObject jsonMessage) {
        System.out.println("[SERVER] Um participante entrou no grupo.");
        System.out.println("[SERVER] " + jsonMessage);
    }

    public static void handleTimerMessage(JSONObject jsonMessage) {
        System.out.println("[SERVER] Tempo restante para o item: " + jsonMessage.getString("timer"));
    }

    // Inicia o leilão e envia o primeiro item
    public static void startAuction() throws Exception {
        System.out.println("[SERVER] - Leilão iniciado!");

        // Envia o primeiro item e começa o timer
        sendNextItem();
    }

    // Envia o próximo item com o timer
    private static void sendNextItem() throws Exception {
        if (currentItemIndex < auctionItems.size()) {
            currentItem = auctionItems.get(currentItemIndex);

            // Cria o JSON com todas as informações do item
            JSONObject itemJson = new JSONObject();
            itemJson.put("@type", "item");
            itemJson.put("name", currentItem.getName());
            itemJson.put("price", currentItem.getPrice());
            itemJson.put("description", currentItem.getDescription());
            itemJson.put("imagePath", currentItem.getImagePath());
            itemJson.put("currentBid", currentItem.getCurrentBid());

            // Criptografa o JSON inteiro
            String jsonString = itemJson.toString();
            String encryptedJsonString = AuctionServerHelpers.encrypt(jsonString, symmetricKey, IV);

            // Envia a mensagem criptografada para os clientes
            sendMulticastMessage(encryptedJsonString);

            // Inicia o timer para o item atual
            startItemTimer();
        } else {
            System.out.println("[SERVER] - Leilão terminou!");
            
            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("@type", "end");
            JSONArray itemsArray = new JSONArray();
            for (Item item : auctionItems) {
                JSONObject itemJson = new JSONObject();
                itemJson.put("name", item.getName());
                itemJson.put("description", item.getDescription());
                itemJson.put("price", item.getPrice());
                itemJson.put("currentWinner", item.getCurrentWinner() != null ? item.getCurrentWinner() : "Não houve vencedor para este item");
                itemJson.put("currentBid", item.getCurrentBid());
                itemsArray.put(itemJson);
            }

            jsonMessage.put("auctionItems", itemsArray);
            
            String encryptedMessage = AuctionServerHelpers.encrypt(jsonMessage.toString(), symmetricKey, IV);
            sendMulticastMessage(encryptedMessage);
        }
    }

    // Controla o timer para cada item
    private static void startItemTimer() {
        timeLeft = 20;  // Redefine o tempo para o novo item
        auctionTimer = new Timer(1000, e -> {
            timeLeft--;
            JSONObject timeJson = new JSONObject();
            timeJson.put("@type", "timer");
            timeJson.put("timeLeft", timeLeft);

            try {
                String encryptedTimeString = AuctionServerHelpers.encrypt(timeJson.toString(), symmetricKey, IV);
                sendMulticastMessage(encryptedTimeString);
            } catch (Exception ex) {
                Logger.getLogger(AuctionServer.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("Tempo restante para o item: " + timeLeft + "s");

            // Quando o tempo acabar, avança para o próximo item
            if (timeLeft <= 0) {
                auctionTimer.stop();
                currentItemIndex++;  // Avança para o próximo item
                try {
                    JSONObject itemResultJson = new JSONObject();
                    if (currentItem.getCurrentWinner() != null) {
                        itemResultJson.put("@type", "winner");
                        itemResultJson.put("currentWinner", currentItem.getCurrentWinner());
                    } else {
                        itemResultJson.put("@type", "noWinner");
                        itemResultJson.put("currentWinner", "Não houve um vencedor para " + currentItem.getName() + ".");
                    }
                    itemResultJson.put("item", currentItem.getName());
                    itemResultJson.put("bidValue", currentItem.getCurrentBid());

                    String encryptedTimeString = AuctionServerHelpers.encrypt(itemResultJson.toString(), symmetricKey, IV);
                    sendMulticastMessage(encryptedTimeString);

                    sendNextItem();  // Envia o próximo item
                } catch (Exception ex) {
                    Logger.getLogger(AuctionServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        auctionTimer.start();
    }

    // Fazer uma função para abrir o jframe com um botão de começar / resetar leilão já com a tela STARTAUCTION pronta.
    public static void openServerModal() {
        StartAuction startFrame = new StartAuction();
        startFrame.setVisible(true);
        startFrame.setLocationRelativeTo(null);
    }
}
