package bankio;

import bank.Account;
import bank.BankStore;

import communicationmanaging.Connector;

import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.nio.BufferOverflowException;
import java.nio.MappedByteBuffer;

import java.nio.channels.FileChannel;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


public class AccountWriter{
    Connector connector;

    public AccountWriter(Connector connector){
        this.connector = connector;
    }

    public ObjectOutputStream writeAccount(Socket socket, Account account){
        try(ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(socket.getOutputStream())) {
            objectOutputStream.writeObject(account);
            return objectOutputStream;
        } catch(NullPointerException e) {
            System.err.println("Error in print writer: Print Writer is null");
            e.printStackTrace();
            return null;
        } catch(IOException e){
            System.err.println("Error in IO");
            e.printStackTrace();
            return null;
        }
    }

    public ObjectOutputStream writeAccount(ServerSocket serverSocket, Account account){
        try(Socket socket  = connector.connect(serverSocket)) {

            try(ObjectOutputStream objectOutputStream =
                         new ObjectOutputStream(socket.getOutputStream())) {
                objectOutputStream.writeObject(account);
                return objectOutputStream;
            } catch(NullPointerException e) {
                System.err.println("Error in print writer: Print Writer is null");
                e.printStackTrace();
                return null;
            }
        } catch(Exception e){
            System.err.println("Unknown socket exception caught");
            return null;

        }
    }

    public ObjectOutputStream writeBankStore(ServerSocket serverSocket,
                                          BankStore bankStore){
        try(Socket socket = connector.connect(serverSocket)){
             try(ObjectOutputStream objectOutputStream =
                     new ObjectOutputStream(socket.getOutputStream())){
                 objectOutputStream.writeObject(bankStore);
                 return objectOutputStream;
             } catch(NullPointerException e){
                System.err.println("Error in object stream: Stream is null");
                return null;
            }
        } catch(Exception e){
            System.err.println("Unknown socket exception caught");
            return null;
        }

    }

    public void writeFile(BankStore bankStore, Path path, boolean exists){
        for(int i = 0; i < bankStore.size(); i++){
            Account account = bankStore.pollLast();
            bankStore.add(account);

            writeFile(account, path, exists);
        }
    }

    public static void writeFile(Account account, Path path, boolean exists){
        if(!exists) {
            try (FileChannel fileChannel =
                         (FileChannel) Files.newByteChannel(path,
                                 StandardOpenOption.CREATE,
                                 StandardOpenOption.WRITE
                         )) {

                int allocatedVal = 1024;

                MappedByteBuffer byteBuffer =
                        fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, allocatedVal);

                char[] accountChar = account.toString().toCharArray();

                for(int i = 0; i < allocatedVal; i++){
                    byteBuffer.put((byte) accountChar[i]);

                    if(!byteBuffer.hasRemaining()){
                        byteBuffer =
                                fileChannel
                                        .map(FileChannel.MapMode.READ_ONLY,
                                                allocatedVal,
                                                allocatedVal * 2);
                        allocatedVal = allocatedVal * 2;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error in IO: writing to file");
                e.printStackTrace();

            } catch (BufferOverflowException e) {
                System.err.println("Error in buffer: Buffer has overflown");
                e.printStackTrace();

            } catch (InvalidPathException e) {
                System.err.println("Error in path: Invalid path");
                e.printStackTrace();
            }
        } else {
            try (FileChannel fileChannel =
                         (FileChannel) Files.newByteChannel(path,
                                 StandardOpenOption.WRITE
                         )) {

                int allocatedVal = 512;

                MappedByteBuffer byteBuffer =
                        fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, allocatedVal);

                char[] accountChar = account.toString().toCharArray();

                for(int i = 0; i < allocatedVal; i++){
                    byteBuffer.put((byte) accountChar[i]);

                    if(!byteBuffer.hasRemaining()){
                        byteBuffer =
                                fileChannel
                                        .map(FileChannel.MapMode.READ_ONLY,
                                                allocatedVal,
                                                allocatedVal * 2);
                        allocatedVal = allocatedVal * 2;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error in IO: writing to file");
                e.printStackTrace();

            } catch (BufferOverflowException e) {
                System.err.println("Error in buffer: Buffer has overflown");
                e.printStackTrace();

            } catch (InvalidPathException e) {
                System.err.println("Error in path: Invalid path");
                e.printStackTrace();
            }
        }
    }

}