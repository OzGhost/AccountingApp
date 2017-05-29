package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import common.Constants;
import db.Customer;
import model.CustomerListModel;
import view.CustomerListView;

public class CustomerListController implements ActionListener {
    private CustomerListModel model;
    private CustomerListView view;
    private PayLogController watcher;
    
    public void setView(CustomerListView view){
        this.view = view;
    }
    public void setModel(CustomerListModel model){
        this.model = model;
    }
    public void setWatcher(PayLogController watcher){
        this.watcher = watcher;
    }
    
    public void repare(){
        view.init();
        model.init();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (view == null){
            System.out.println("---- WARNING: Event occur when view not set yet");
            return;
        }
        if (model == null){
            System.out.println("---- WARNING: Event occur when model not set yet");
            return;
        }
        final String cmd = e.getActionCommand();
        if (cmd.isEmpty())
            return;
        if (Constants.ACTION_CANCEL.equals(cmd)){
            view.dispose();
            watcher.beNoticed(null);
            return;
        }
        if (Constants.ACTION_DONE.equals(cmd)){
            if (watcher == null){
                System.out.println("---- WARNING: Event occur when watcher not set yet");
                return;
            }
            Customer c = view.getSelectedCustomer();
            if (c == null){
                return;
            }
            watcher.beNoticed(c);
            view.dispose();
            return;
        }
    }
}
