package bankio;

import bank.Account;
import bank.BankStore;

import communicationmanaging.Connector;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.BufferOverflowException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class AccountReader {
    ServerSocket serverSocket;
    Connector connector;

    public AccountReader(ServerSocket serverSocket, Connector connector){
        this.serverSocket = serverSocket;
        this.connector = connector;
    }

    public AccountReader(){
        try {
            this.serverSocket = new ServerSocket(4999);
            this.connector = new Connector();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Account readAccount() throws IOException {
        try(Socket socket = connector.connect(serverSocket)){
            try(ObjectInputStream objectInputStream =
                        new ObjectInputStream(socket.getInputStream())) {

                return (Account) objectInputStream.readObject();
            } catch(NullPointerException e) {
                System.err.println("Error in print writer: Print Writer is null");
                e.printStackTrace();

                return null;
            } catch (ClassNotFoundException e){
                System.err.println("Error in reading Object: Class Does Not Exists!");
                e.printStackTrace();

                return null;
            }
        }
    }

    public BankStore readStore() throws IOException{
        try (Socket socket = connector.connect(serverSocket)){
            try (ObjectInputStream objectInputStream =
                    new ObjectInputStream(socket.getInputStream())){


                return (BankStore) objectInputStream.readObject();

            } catch (ClassNotFoundException e) {
                System.err.println("Error in reading Store: Store Does Not Exist");
                e.printStackTrace();

                return null;
            } catch (NullPointerException e){
                System.err.println("Error in reading Store: Store is null");
                e.printStackTrace();

                return null;
            }
        }
    }

    public String readFile(BankStore bankStore, Path path){
        StringBuffer stringBuffer = new StringBuffer();

        for(int i = 0; i < bankStore.size(); i++){
            Account account = bankStore.pollLast();
            bankStore.add(account);

            stringBuffer.append(readFile(path));
        }

        return String.valueOf(stringBuffer);
    }


    public static String readFile(Path path){
        StringBuilder fileContents = new StringBuilder();

        try(FileChannel fileChannel =
                    (FileChannel) Files.newByteChannel(
                            path,
                            StandardOpenOption.READ
                    )){
            int allocatedVal = 1024;

            MappedByteBuffer byteBuffer =
                    fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, allocatedVal);

            for(int i = 0; i < allocatedVal; i++){
                fileContents.append((char) byteBuffer.get(i));

                if(!byteBuffer.hasRemaining()){
                    byteBuffer =
                            fileChannel
                                    .map(FileChannel.MapMode.READ_ONLY,
                                            allocatedVal,
                                            allocatedVal * 2);
                    allocatedVal = allocatedVal * 2;
                }
            }

            return String.valueOf(fileContents);
        } catch (IOException e) {
            System.err.println("Error in IO: IO failure");
            e.printStackTrace();

            return null;
        } catch(BufferOverflowException e){
            System.err.println("Error in Buffer: Buffer has Overflown");
            e.printStackTrace();

            return null;
        } catch (InvalidPathException e){
            System.err.println("Error in path: Invalid Path");
            e.printStackTrace();

            return null;
        } catch (Exception e){
            System.err.println("Unknown Exception");
            e.printStackTrace();

            return null;
        }
    }

}
