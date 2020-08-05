package execute;

import bank.Account;
import bank.BankStore;

import bankio.AccountReader;
import bankio.AccountWriter;

import communicationmanaging.Connector;
import communicationmanaging.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.ServerSocket;

import java.util.ArrayList;
import java.util.Locale;

public class ServerSide {
    private static ServerSocket serverSocket;
    private BankStore bankStore;
    private Connector connector = new Connector();
    private Runtime garbageCollection = null;

    static {
        try {
            serverSocket = new ServerSocket(4999);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        final ServerSide serverSide = new ServerSide();

        serverSide.run();
    }

    public void run(){
        BufferedReader  bufferedReader =
                new BufferedReader(
                        new InputStreamReader(
                                System.in
                        )
                );
        ArrayList<Account> accounts =
                new ArrayList<>();
        AccountWriter accountWriter =
                new AccountWriter(
                       new Connector()
                );
        while(true){
            try{
                String name = bufferedReader.readLine();
                if(name == null){
                    break;
                }
                Integer balance = bufferedReader.read();

                String country = bufferedReader.readLine();
                String language = bufferedReader.readLine();
                Locale locale = new Locale(language, country);


                Account account =
                        new Account(name,
                                balance,
                                locale);

                if (accountWriter.writeAccount(serverSocket, account) == null)
                    break;

                accounts.add(account);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        garbageCollection.gc();

        bankStore = new BankStore(accounts);
        assert accountWriter.writeBankStore(serverSocket,
                bankStore) != null;

        while(true){
            try{
                computeRequest(connector
                        .receiveRequest(serverSocket));
                accountWriter.writeBankStore(serverSocket,
                        bankStore);
                garbageCollection.gc();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void computeRequest(Request request) throws IOException {

        BufferedReader bufferedReader =
                new BufferedReader(
                        new InputStreamReader(
                                System.in
                        ));

        Account specifiedAccount =
                new AccountReader().readAccount();

        request.doRequest(specifiedAccount,
                bufferedReader,
                bankStore);
    }
}
