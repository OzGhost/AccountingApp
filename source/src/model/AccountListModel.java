package model;

import java.util.List;
import java.util.Observable;

import db.Account;

public class AccountListModel extends Observable {

    private List<Account> accs;

    public void init() {
        this.accs = Account.findAll();
        setChanged();
        notifyObservers();
    }
    
    public List<Account> getAccounts() {
        return this.accs;
    }
}
