package bank;

import java.util.Currency;
import java.util.Locale;

public class Account {
    private Integer balance;
    private String name;
    private Currency currency;
    private String balanceStrRep;
    Boolean nullified = false;

    public Account(String name, Integer balance, Locale locale){
        this.name = name;
        this.balance = balance;
        this.currency = Currency.getInstance(locale);

        this.balanceStrRep = this.currency.getSymbol() + balance;
    }

    public Account(){
        //Defaults
        this.name = "Nullified";
        this.balance = 0;
        this.currency = Currency.getInstance(Locale.US);

        this.balanceStrRep = "null";
    }

    public boolean nullify(){
        try{
            this.name = null;
            this.balance = null;
            this.balanceStrRep = null;
            this.currency = null;
            this.nullified = true;

            return true;
        } catch(Exception e){
            return false;
        }
    }

    public String getName(){
        return this.name;
    }

    public Integer getBalance(){
        return this.balance;
    }

    public Currency getCurrency(){
        return this.currency;
    }

    public void withdraw(Integer minusVal){
        assert minusVal > 0;
        this.balance =- minusVal;
    }

    public void deposit(Integer plusVal){
        assert plusVal > 0;
        this.balance =- plusVal;
    }

    public boolean equals(Account account) {
        int equality = 0;

        for(int i = 0; i < 4; i++){
            if(i == 0 && account.getBalance().equals(this.balance)) {
                equality++;
            } else if (i == 1 && account.getName().equals(this.name)) {
                equality++;
            } else if (i == 2 && account.getCurrency().equals(this.currency)){
                equality++;
            } else if(i == 3 && account.balanceStrRep.equals(this.balanceStrRep)){
                equality++;
            }
        }

        return equality == 4;
    }


    @Override
    public String toString() {
        return name +
                ": \n" + balanceStrRep + "\n";
    }

}
