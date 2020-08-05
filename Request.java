package communicationmanaging;

import bank.Account;
import bank.BankStore;
import bankio.AccountReader;
import bankio.AccountWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;

public enum Request implements DoRequest{


    WITHDRAW(){
        @Override
        public void doRequest(Account account,
                              BufferedReader bufferedReader,
                              BankStore bankStore) {
            System.out.println("Specify amount: ");

            Integer amount = null;
            try {
                amount = bufferedReader.read();
            } catch (IOException e) {
                e.printStackTrace();
            }

            account.withdraw(amount);
            bankStore.addAccount(account);
        }
    },

    DEPOSIT(){
        @Override
        public void doRequest(Account account,
                              BufferedReader bufferedReader,
                              BankStore bankStore) {
            System.out.println("Specify amount: ");

            Integer amount = null;
            try {
                amount = bufferedReader.read();
            } catch (IOException e) {
                e.printStackTrace();
            }

            account.deposit(amount);
            bankStore.addAccount(account);
        }
    },

    VIEW(){
        @Override
        public void doRequest(Account account,
                              BufferedReader bufferedReader,
                              BankStore bankStore) {

            System.out.println("Specify password: ");

            try {
                if(bufferedReader.readLine().equals("pass")){
                    System.out.println(
                            bankStore.format()
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    },

    SELECT(){
        @Override
        public void doRequest(Account account,
                              BufferedReader bufferedReader,
                              BankStore bankStore) {
            System.out.println(
                    account.toString()
            );
        }
    },

    FILE(){
        @Override
        public void doRequest(Account account,
                              BufferedReader bufferedReader,
                              BankStore bankStore) {
            File file = new File("C:\\temp\\" + account.getName() +
                    account.getCurrency().getSymbol() + ".txt");

            AccountWriter.writeFile(
                    account,
                    Path.of("C:\\temp\\" + account.getName() +
                            account.getCurrency().getSymbol() + ".txt"),
                    file.exists()
            );
        }
    },
    FILE_STORE(){
        @Override
        public void doRequest(Account account,
                              BufferedReader bufferedReader,
                              BankStore bankStore) {
            AccountWriter accountWriter =
                    new AccountWriter(new Connector());
            File file = new File("C:\\temp\\" + account.getName() +
                    account.getCurrency().getSymbol() + ".txt");

            accountWriter.writeFile(
                    bankStore,
                    Path.of("C:\\temp\\" + account.getName() +
                            account.getCurrency().getSymbol() + ".txt"),
                    file.exists()
            );

        }
    },

    NULL_REQUEST(){
        @Override
        public void doRequest(Account account,
                              BufferedReader bufferedReader,
                              BankStore bankStore){
            System.err.println("The request was null");
            System.exit(1);
        }
    },

    DELETE_STORE(){
        @Override
        public void doRequest(Account account,
                              BufferedReader bufferedReader,
                              BankStore bankStore) {
            assert bankStore.size()
                    < bankStore.destroyStore(bankStore.size());

            bankStore.add(new Account());
        }
    },

    REMOVE_ACCOUNT(){
        @Override
        public void doRequest(Account account,
                              BufferedReader bufferedReader,
                              BankStore bankStore) {
            assert bankStore.destroyAccount(account);
        }
    },

    SEE_ACCOUNT_FILE(){
        @Override
        public void doRequest(Account account,
                              BufferedReader bufferedReader,
                              BankStore bankStore) {
            System.out.println(
                    AccountReader.readFile(
                            Path.of(
                                    "C:\\temp\\" + account.getName() +
                                            account.getCurrency().getSymbol() + ".txt"
                            )
                    )
            );

            bankStore.addAccount(account);
        }
    },

    SEE_STORE_FILE(){
        @Override
        public void doRequest(Account account,
                              BufferedReader bufferedReader,
                              BankStore bankStore) {
            AccountReader accountReader;

            try {
                accountReader =
                        new AccountReader(new ServerSocket(4999),
                            new Connector());

                System.out.println(
                        accountReader.readFile(
                                bankStore,
                                Path.of(
                                        "C:\\temp\\" + account.getName() +
                                                account.getCurrency().getSymbol() + ".txt"
                                )
                        )
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}

