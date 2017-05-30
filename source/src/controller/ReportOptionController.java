package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import common.Constants;
import model.ReportOptionModel;
import view.ReportOptionView;

public class ReportOptionController implements ActionListener {
    
    private ReportOptionModel model;
    private ReportOptionView view;
    private PayLogController watcher;

    public void setModel (ReportOptionModel model) {
        this.model = model;
    }
    public void setView (ReportOptionView view) {
        this.view = view;
    }
    public void setWatcher (PayLogController watcher) {
        this.watcher = watcher;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final String cmd = e.getActionCommand();
        if (cmd == null || cmd.isEmpty())
            return;
        if (Constants.ACTION_CANCEL.equals(cmd)) {
            view.dispose();
            watcher.beNoticed(new Integer(0));
            return;
        }
        if (Constants.ACTION_DONE.equals(cmd)) {
            view.beWait();
            final Object[] metaData = view.getMetaData();
            if (metaData == null) {
                view.loaded();
                return;
            }
            view.noticeResult(model.takeCare(metaData));
            view.loaded();            
        }
    }

}
