package communicationmanaging;

import bank.Account;
import bank.BankStore;

import java.io.BufferedReader;

interface DoRequest{
    void doRequest(Account account,
                   BufferedReader bufferedReader,
                   BankStore bankStore);
}

