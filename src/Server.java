import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String args[]) {
        DatagramSocket aSocket = null;
        DatagramPacket reply = null;
        List rooms = new ArrayList();
        List<User> users = new ArrayList();
        String m = null;

        try {

            aSocket = new DatagramSocket(6789);

            System.out.println("Servidor: ouvindo porta UDP/6789.");

            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);

                String message = new String(request.getData()).trim();
                System.out.println("Servidor: recebido \'" + message + "\'.");

                String[] messageArray = message.split(";");

                switch (messageArray[0]) {
                    case "criar-sala":
                        rooms.add(messageArray[1]);
                        m = "Criado com sucesso";
                        SendResponse(aSocket, m.getBytes(), m.length(), request.getAddress(), request.getPort());
                        break;
                    case "listar-salas":
                        SendResponse(aSocket, rooms.toString().getBytes(), rooms.toString().length(), request.getAddress(),
                                request.getPort());
                        break;
                    case "adicionar-usuario":
                        User user = new User(messageArray[1], Integer.parseInt(messageArray[2]));
                        users.add(user);
                        m = "Adicionado com sucesso";
                        SendResponse(aSocket, m.getBytes(), m.length(), request.getAddress(), request.getPort());
                        break;
                    case "listar-usuarios":
                        ArrayList res = new ArrayList();
                        users.forEach(u -> res.add(u.name));
                        SendResponse(aSocket, res.toString().getBytes(StandardCharsets.UTF_8), res.toString().length(), request.getAddress(), request.getPort());
                        break;
                    case "remover-usuario":
                        int indexRemove = -1;
                        for (int i = 0; i < users.size(); i++) {
                            User userAux = users.get(i);
                            if (userAux.name.equalsIgnoreCase(messageArray[1])){
                                indexRemove = i;
                            }
                        }
                        users.remove(indexRemove);
                        m = "Removido com sucesso";
                        SendResponse(aSocket, m.getBytes(), m.length(), request.getAddress(), request.getPort());
                        break;
                    default:
                        SendResponse(aSocket, request.getData(), request.getLength(), request.getAddress(), request.getPort());
                }
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
    }

    private static void SendResponse(DatagramSocket aSocket, byte[] data, int length, InetAddress address, int port) {
        try {
            DatagramPacket reply = new DatagramPacket(data, length, address, port);
            aSocket.send(reply);
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}

class User implements Serializable {
    int roomPos = -1;
    String name = null;

    User(String name, int pos) {
        this.name = name;
        this.roomPos = pos;
    }
}