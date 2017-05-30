package view;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import common.Constants;
import common.FinalTableModel;
import common.Genner;
import common.Layer;
import controller.AccountListController;
import db.Account;
import model.AccountListModel;

public class AccountListView extends JFrame implements Observer {

    public static final short SINGLE_MODE = 1;
    public static final short MULTI_MODE = 2;

    private static final long serialVersionUID = -6825721126852337766L;
    private JButton bt_cancel;
    private JButton bt_choice;
    private JTable tb_acc;
    private boolean singleMode;
    private  final JPanel panel = new JPanel();
    private final SpringLayout layout = new SpringLayout();

    public AccountListView (short mode) {
        if (mode == SINGLE_MODE) {
            singleMode = true;
        } else {
            singleMode = false;
        }
        init();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Select Account(s)");
        setContentPane(panel);
        setPreferredSize(new Dimension(800, 600));
        setIconImage(Constants.appIcon);
        pack();
        setLocationRelativeTo(null);
    }
    
    private void init () {
        bt_cancel = Genner.createButton("Cancel", Genner.MEDIUM_SIZE);
        bt_cancel.setActionCommand(Constants.ACTION_CANCEL);
        bt_choice = Genner.createButton("Select", Genner.MEDIUM_SIZE);
        bt_choice.setActionCommand(Constants.ACTION_DONE);

        panel.setLayout(layout);
        panel.add(bt_choice);
        panel.add(bt_cancel);

        Layer.put(bt_choice).in(layout)
            .atBottomRight(panel).withMargin(5);
        Layer.put(bt_cancel).in(layout)
            .atBottomLeft(panel).withMargin(5);

    }

    private void fillTable (List<Account> al) {
        final FinalTableModel ftm = new FinalTableModel();
        ftm.setColumnIdentifiers(new Object[]{"Code", "Name"});
        al.forEach(a -> ftm.addRow( a.castToObjects() ));
        tb_acc = new JTable(ftm);
        if (singleMode) {
            tb_acc.getSelectionModel()
                .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        JScrollPane tb_holder = new JScrollPane(tb_acc);

        panel.add(tb_holder);

        Layer.put(tb_holder).in(layout)
            .atTopLeft(panel).withMargin(5)
            .atRight(panel).withMargin(5)
            .topOf(bt_choice).withMargin(5);
    }

    public void setController(AccountListController alc) {
        bt_cancel.addActionListener(alc);
        bt_choice.addActionListener(alc);
    }

    public Object getSelectedAccount() {
        if (tb_acc == null) {
            System.out.println("---- WARNING: Data not ready");
            return new ArrayList<>();
        }
        final DefaultTableModel tm = (DefaultTableModel) tb_acc.getModel();
        final int[] indexs = tb_acc.getSelectedRows();
        final List<Account> rs = new ArrayList<>();

        for (int i: indexs) {
            rs.add(
                new Account(
                    (String) tm.getValueAt(i, 0),
                    (String) tm.getValueAt(i, 1)
                )
            );

        }
        if (singleMode) {
            if (!rs.isEmpty()) {
                return rs.get(0);
            } else {
                return null;
            }
        } else {
            return rs;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof AccountListModel)) {
            return;
        }
        AccountListModel m = (AccountListModel) o;
        fillTable(m.getAccounts());
        pack();
    }
}
