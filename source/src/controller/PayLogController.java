package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import common.Constants;
import db.Account;
import db.Customer;
import model.AccountListModel;
import model.CustomerListModel;
import model.PayLogModel;
import view.AccountListView;
import view.CustomerListView;
import view.PayLogView;

public class PayLogController implements ActionListener {
    
    private PayLogView view;
    private PayLogModel model;
    
    public void setView(PayLogView view){
        this.view = view;
    }
    
    public void setModel(PayLogModel model) {
        this.model = model;
    }
    
    public void beNoticed(Object c){
        view.setVisible(true);
        if (c == null) {
            return;
        }
        if (c instanceof Customer) {
            model.specifyCustomer(  (Customer) c );
            return;
        }
        if (c instanceof List) {
            model.addCoAcc( (List<Account>) c );
            return;
        }
        if (c instanceof Account) {
            model.specifyMainAccount( (Account) c);
            return;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (view == null){
            System.out.println("view not set yet");
            return;
        }
        String cmd = e.getActionCommand();
        System.out.println("signal: " + cmd);
        if (cmd.isEmpty()) return;
        if (cmd.equals(Constants.ACTION_NEW_CUSTOMMER)){
            view.resetCustomerForm();
            model.newCustomer();
            return;
        }
        if (cmd.equals(Constants.ACTION_SELECT_CUSTOMER)){
            view.setVisible(false);
            CustomerListModel clm = new CustomerListModel();
            CustomerListView clv = new CustomerListView();
            CustomerListController clc = new CustomerListController();
            clc.setModel(clm);
            clc.setView(clv);
            clc.setWatcher(this);
            
            clm.addObserver(clv);
            clc.repare();
            clv.setController(clc);
            
            clv.setLocationRelativeTo(view);
            clv.pack();
            clv.setVisible(true);
            return;
        }
        if (cmd.equals(Constants.ACTION_EXIT)){
            System.exit(0);
        }
        if (Constants.ACTION_SELECT_MAIN_ACC.equals(cmd)) {
            final AccountListView alv = new AccountListView(
                    AccountListView.SINGLE_MODE
            );
            final AccountListModel alm = new AccountListModel();
            final AccountListController alc = new AccountListController();

            alm.addObserver(alv);
            alm.init();
            alv.setController(alc);

            alc.setModel(alm);
            alc.setView(alv);
            alc.setWatcher(this);

            view.setVisible(false);
            alv.setVisible(true);
            
            return;
        }
        if (Constants.ACTION_ADD_COACC.equals(cmd)) {
            final AccountListView alv = new AccountListView(
                    AccountListView.MULTI_MODE
            );
            final AccountListModel alm = new AccountListModel();
            final AccountListController alc = new AccountListController();

            alm.addObserver(alv);
            alm.init();
            alv.setController(alc);

            alc.setModel(alm);
            alc.setView(alv);
            alc.setWatcher(this);

            view.setVisible(false);
            alv.setVisible(true);
            
            return;
        }
        if (Constants.ACTION_DEL_COACC.equals(cmd)) {
            model.removeCoAcc(view.getSelectedCoAccRows());
            return;
        }
        if (Constants.ACTION_DONE.equals(cmd)) {
            boolean sati = model.verifyData();
            sati = sati && view.verifyData(model.getCustomer() == null);
            if (!sati) {
                view.noticeWarning("Make sure you do not missing every thing! Try again!");
                return;
            }
            Object[] metaData = view.getMetaData();
            model.saveDown(metaData);
            return;
        }
    }

}
