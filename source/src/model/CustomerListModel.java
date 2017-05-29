package model;

import java.util.List;
import java.util.Observable;

import db.Customer;

public class CustomerListModel extends Observable {
    
    private List<Customer> customers;
    
    public void init(){
        this.customers = Customer.findAll();
        setChanged();
        notifyObservers();
    }
    
    public List<Customer> getCustomers() {
        return customers;
    }
}
