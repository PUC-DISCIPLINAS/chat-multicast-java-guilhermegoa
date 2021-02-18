import java.io.IOException;
import java.net.*;
import java.util.Scanner;


public class Client {
    private final static String HOST = "localhost";
    static boolean chatActive = false;
    static MulticastSocket mSocket = null;

    public static <ex> void main(String args[]) {

        String res = null;

        int option = 0;
        menu();


        do {

            System.out.println("Digite o numero da opcao:");
            Scanner input = new Scanner(System.in);
            option = input.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Digite um ip aima de 224 Ex.: 228.5.6.7");
                    String roomIP = input.next();
                    res = Send("criar-sala;" + roomIP + ";", HOST);
                    System.out.println("Resposta: " + res);
                    break;
                case 2:
                    res = Send("listar-salas;", HOST);
                    String[] resArray = resTractive(res);
                    for (int i = 0; i < resArray.length; i++) {
                        System.out.println("Posiçao " + i + " - " + resArray[i]);
                    }
                    break;
                case 3:
                    System.out.printf("Digite o a posicao da sala que deseja entrar:");
                    int pos = input.nextInt();
                    Room(pos);
                    break;
                default:
                    System.out.println("nada");
                    break;
            }

        } while (option != 0);

    }


    private static void Room(int pos) {
        //take room ip
        String res = Send("listar-salas;", HOST);
        String[] resArray = resTractive(res);
        String roomIP = resArray[pos];

        Scanner input = new Scanner(System.in);

        int mPort = 5678;
        byte[] message;
        DatagramPacket messageOut;

        try {
            chatActive = true;

            InetAddress groupIp = InetAddress.getByName(roomIP.trim());

            mSocket = new MulticastSocket(mPort);
            mSocket.joinGroup(groupIp);

            System.out.printf("Digite o seu nome:");
            String userName = input.nextLine();

            System.out.println("\nPara sair Digite -> sair <-");
            System.out.println("Para listar usuarios do chat -> listar <-\n");

            message = ("Usuario " + userName + " entrou no chat.").getBytes();
            messageOut = new DatagramPacket(message, message.length, groupIp, mPort);
            mSocket.send(messageOut);

            Send("adicionar-usuario;" + userName + ";" + pos + ";", HOST);

            Thread mThread = new Thread(() -> {
                while (chatActive) {
                    byte[] buffer = new byte[1000];
                    try {
                        DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                        mSocket.receive(messageIn);
                        System.out.println(new String(messageIn.getData()).trim());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            mThread.start();

            while (chatActive) {
                String text = new String();
                text = input.nextLine();
                if (text.trim().equalsIgnoreCase("sair")) {
                    Send("remover-usuario;" + userName + ";", HOST);
                    mSocket.leaveGroup(groupIp);
                    chatActive = false;
                } else if (text.trim().equalsIgnoreCase("listar")) {
                    res = Send("listar-usuarios;" + pos + ";", HOST);
                    resArray = resTractive(res);
                    for (int i = 0; i < resArray.length; i++) {
                        System.out.println(i + " - " + resArray[i]);
                    }
                } else {
                    text = userName + ": " + text;
                    message = text.getBytes();
                    messageOut = new DatagramPacket(message, message.length, groupIp, mPort);
                    mSocket.send(messageOut);
                }
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
//        finally {
//            if (mSocket != null)
//                mSocket.close();
//        }
    }

    private static String Send(String text, String host) {
        DatagramSocket aSocket = null;
        int serverPort = 6789;

        try {
            aSocket = new DatagramSocket();
            byte[] m = text.getBytes();
            InetAddress aHost = InetAddress.getByName(host);
            DatagramPacket request = new DatagramPacket(m, text.length(), aHost, serverPort);
            aSocket.send(request);

            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);

            String message = new String(reply.getData()).trim();

            return message;
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
        return text;
    }

    private static String[] resTractive(String res) {
        return res.replace("[", "").replace("]", "").trim().split(",");
    }

    private static void menu() {
        System.out.println("0 - Sair");
        System.out.println("1 - Criar sala");
        System.out.println("2 - listar salas");
        System.out.println("3 - Entrar em uma sala");
    }

}