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

import java.net.Socket;

public class ClientSide {
    private static Socket socket;
    private static BankStore bankStore;
    private Runtime garbageCollection = null;

    static {
        try{
            socket =
                    new Socket("localhost",
                            4999);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        final ClientSide clientSide = new ClientSide();

        clientSide.run();
    }

    public void run(){
        AccountReader accountReader =
                new AccountReader();

        while(true){
            try {
                Account account = accountReader.readAccount();

                System.out.println(account.toString());
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch(NullPointerException e){
                break;
            }
        }

        while(true) {
            try {
                garbageCollection.gc();
                bankStore = new AccountReader().readStore();
                options();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void options() throws IOException {
        BufferedReader bufferedReader =
                new BufferedReader(
                        new InputStreamReader(
                                System.in
                        )
                );
        Request request;
        Connector connector = new Connector();

        assert new AccountWriter(connector)
                .writeAccount(socket, bankStore
                        .getAccount(bufferedReader.readLine())) != null;


        switch (bufferedReader.readLine()){
            case "Withdraw":
                request = Request.WITHDRAW;
                connector.sendRequest(request);
                break;

            case "Deposit":
                request = Request.DEPOSIT;
                connector.sendRequest(request);
                break;

            case "View":
                request = Request.VIEW;
                connector.sendRequest(request);
                break;

            case "Select":
                request = Request.SELECT;
                connector.sendRequest(request);
                break;

            case "File":
                request = Request.FILE;
                connector.sendRequest(request);
                break;

            case "File Store":
                request = Request.FILE_STORE;
                connector.sendRequest(request);

            case "Remove Account":
                request = Request.REMOVE_ACCOUNT;
                connector.sendRequest(request);
                break;

            case "Delete Store":
                request = Request.DELETE_STORE;
                connector.sendRequest(request);
                break;

            case "See Account File":
                request = Request.SEE_ACCOUNT_FILE;
                connector.sendRequest(request);
                break;

            case "See Store File":
                request = Request.SEE_STORE_FILE;
                connector.sendRequest(request);
                break;
        }
    }

}
