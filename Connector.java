package communicationmanaging;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;

public class Connector {
    public Socket connect(ServerSocket serverSocket) throws IOException{
        return serverSocket.accept();
    }

    public void sendRequest(Request request){
        try(Socket socket = connect(new ServerSocket(4999))) {
            try (ObjectOutputStream objectOutputStream =
                         new ObjectOutputStream(socket.getOutputStream())) {
                objectOutputStream.writeObject(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Request receiveRequest(ServerSocket serverSocket){
        try(Socket socket = connect(serverSocket)){
            try(ObjectInputStream objectInputStream =
                        new ObjectInputStream(socket.getInputStream())) {

                return (Request) objectInputStream.readObject();
            } catch(NullPointerException e) {
                System.err.println("Error in print writer: Print Writer is null");
                e.printStackTrace();

                return null;
            } catch (ClassNotFoundException e){
                System.err.println("Error in reading Object: Class Does Not Exists!");
                e.printStackTrace();

                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Request.NULL_REQUEST;
    }

}
