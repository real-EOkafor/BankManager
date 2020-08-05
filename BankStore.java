package bank;

import java.util.ArrayList;
import java.util.TreeSet;

public class BankStore extends TreeSet<Account>{
    TreeSet<Account> accountTreeSet =
            new TreeSet<>((o1, o2) -> {
                int i, j;

                i = o1.getName().lastIndexOf(' ');
                j = o2.getName().lastIndexOf(' ');

                return o1.getName()
                        .substring(i)
                        .compareToIgnoreCase(o2.getName()
                                .substring(j));
            });

    public BankStore(ArrayList<Account> accounts){
        accountTreeSet.addAll(accounts);
    }

    public int size(){
        return accountTreeSet.size();
    }

    public TreeSet<String> format(){
        TreeSet<String> formattedBank =
                new TreeSet<>((acc1, acc2) -> {
                    int i, j;

                    i = acc1.indexOf(' ');
                    j = acc2.indexOf(' ');

                    return acc1
                            .substring(i)
                            .compareToIgnoreCase(acc2
                            .substring(j));
                });

        for(int i = 0; i < accountTreeSet.size(); i++){
            Account acc = accountTreeSet.pollLast();
            accountTreeSet.add(acc);

            assert acc != null;
            formattedBank.add(acc.toString());
        }

        return formattedBank;
    }

    public int destroyStore(int limit){
        int destroySuccess = 0;

        for (int i = 0; i < limit; i++){
            Account account = accountTreeSet.pollLast();

            if (account != null) {
                boolean boolSuccess = account.nullify();
                assert boolSuccess;
                destroySuccess++;
            } else {
                destroySuccess--;
            }
        }

        accountTreeSet.clear();
        return destroySuccess;
    }

    public Account getAccount(String name){
        for(int i = 0; i < accountTreeSet.size(); i++){
            Account getAcc = accountTreeSet.pollLast();
            assert getAcc != null;
            if(getAcc.getName().equals(name)){
                return getAcc;
            } else {
                accountTreeSet.add(getAcc);
            }
        }
        return null;
    }

    public void addAccount(Account account){
        accountTreeSet.add(account);
    }

    public boolean destroyAccount(Account account){
        if (!account.nullified)
            return false;

        for(int i = 0; i < accountTreeSet.size(); i++){
            Account accountPoll = accountTreeSet.pollLast();

            if(account.equals(accountPoll)) {
                account.nullify();
                return true;
            } else {
                accountTreeSet.add(accountPoll);
            }
        }

        return false;
    }
}
