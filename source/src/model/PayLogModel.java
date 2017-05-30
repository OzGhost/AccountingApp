package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import db.Account;
import db.Customer;
import db.PayLog;
import db.db;

public class PayLogModel extends Observable {

    public static final short CHANGED_CUSTOMER = 1;
    public static final short CHANGED_ADD_ACC = 2;
    public static final short CHANGED_DEL_ACC = 3;
    public static final short CHANGED_MAIN_ACC = 4;
    
    private Customer customer;
    private Account mainAcc;
    private int[] indexs_taken;
    final private List<Account> accounts = new ArrayList<>();
    
    // Getters
    public Customer getCustomer(){
        return this.customer;
    }
    public int[] getIndexsTaken () {
        return this.indexs_taken;
    }
    public List<Account> getAccounts() {
        return this.accounts;
    }
    public Account getMainAcc() {
        return this.mainAcc;
    }
    
    public void specifyCustomer(Customer c){
        this.customer = c;
        setChanged();
        notifyObservers(CHANGED_CUSTOMER);
    }

    public void specifyMainAccount(Account a) {
        this.mainAcc = a;
        setChanged();
        notifyObservers(CHANGED_MAIN_ACC);
    }
    
    public void newCustomer() {
        this.customer = null;
    }
    
    public void removeCoAcc (int[] indexs) {
        if (indexs.length < 1)
            return;
        this.indexs_taken = indexs;
        Arrays.sort(indexs_taken);
        for (int i = indexs_taken.length - 1; i >= 0; i--) {
            accounts.remove(indexs[i]);
        }
        setChanged();
        notifyObservers(CHANGED_DEL_ACC);
    }

    public void addCoAcc (List<Account> accList) {
        if (accList == null || accList.isEmpty())
            return;
        indexs_taken = new int[accList.size()];
        int iden = accounts.size();
        for (int i = 0; i < accList.size(); i++) {
            indexs_taken[i] = iden;
            iden++;
        }
        accounts.addAll(accList);
        setChanged();
        notifyObservers(CHANGED_ADD_ACC);
    }

    public boolean saveDown(Object[] metaData) {
        if (customer == null) {
            customer = new Customer();
            customer.setName((String)metaData[4]);
            customer.setAddress((String)metaData[5]);
            customer.setVatCode((String)metaData[6]);
            customer.setBankCode((String)metaData[7]);
            customer.setBankName((String)metaData[8]);
            customer = Customer.save(customer);
            if (customer == null) {
                return false;
            }
        }
        PayLog p = PayLog.getByType( (String) metaData[1], (Date) metaData[0] );
        p.setReason( (String) metaData[2] );
        p.setCustomer(customer);
        p.setCoAcc(accounts);
        p.setVatPercent(10);
        p.setMainAccount(new Account("1111", ""));
        return db.send(p.toQuery());
    }

    public boolean verifyData () {
        if (accounts.isEmpty())
            return false;
        if (
                mainAcc == null ||
                mainAcc.getCode() == null ||
                mainAcc.getCode().isEmpty()
        )
            return false;
        return true;
    }
}
