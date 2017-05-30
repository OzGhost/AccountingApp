package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import common.Constants;
import view.AccountListView;

public class AccountListController implements ActionListener {

    private AccountListView view;
    private PayLogController watcher;

    // Setter
    public void setView (AccountListView v) {
        this.view = v;
    }
    public void setWatcher (PayLogController w) {
        this.watcher = w;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (view == null) {
            System.out.println("---- WARNING: Event occur when view not available");
            return;
        }
        if (watcher == null) {
            System.out.println("---- WARNING: Event occur when watcher not available");
            return;
        }
        String cmd = e.getActionCommand();
        if (cmd.isEmpty())
            return;
        if (Constants.ACTION_CANCEL.equals(cmd)) {
            view.dispose();
            watcher.beNoticed(null);
            return;
        }
        if (Constants.ACTION_DONE.equals(cmd)) {
            view.dispose();
            watcher.beNoticed( view.getSelectedAccount() );
            return;
        }
    }
}
