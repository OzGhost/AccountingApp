package view;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.table.TableModel;

import common.Constants;
import common.FinalTableModel;
import common.Genner;
import common.Layer;
import controller.CustomerListController;
import db.Customer;
import model.CustomerListModel;

public class CustomerListView extends JFrame implements Observer {
    
    private static final long serialVersionUID = -3807775799958977950L;

    
    private JTable tb_customers;
    private JButton bt_cancel;
    private JButton bt_choice;
    private JPanel panel;
    private SpringLayout layout;
    
    public CustomerListView() {
        setIconImage(Constants.appIcon);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Select customer was saved before");
    }
    
    public void init(){
        
        setPreferredSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setSize(800, 600);
        
        bt_cancel = Genner.createButton("Cancel", Genner.MEDIUM_SIZE);
        bt_cancel.setActionCommand(Constants.ACTION_CANCEL);
        bt_choice = Genner.createButton("Select", Genner.MEDIUM_SIZE);
        bt_choice.setActionCommand(Constants.ACTION_DONE);
        
        layout = new SpringLayout();
        panel = new JPanel(layout);
        panel.add(bt_cancel);
        panel.add(bt_choice);
        
        Layer.put(bt_choice).in(layout).atBottomRight(panel).withMargin(5, 20);
        Layer.put(bt_cancel).in(layout).atBottomLeft(panel).withMargin(5, 20);
        
        setContentPane(panel);
    }
    
    public void setController(CustomerListController ctler){
        if (bt_cancel == null || bt_choice == null){
            System.out.println(" ---- WARNING: Calling setter before init");
            return;
        }
        bt_cancel.addActionListener(ctler);
        bt_choice.addActionListener(ctler);
    }
    
    public Customer getSelectedCustomer() {
        Customer c = new Customer();
        if (tb_customers == null){
            System.out.println("---- WARNING: data not ready");
            return null;
        }
        
        final int row = tb_customers.getSelectedRow();
        if (row < 0) {
            warningNotice("Choosing one customer before click 'choice', Please!");
            return null;
        }
        final TableModel model = tb_customers.getModel();
        c.setId((String) model.getValueAt(row, 0) );
        c.setName((String) model.getValueAt(row, 1) );
        c.setAddress((String) model.getValueAt(row, 2) );
        c.setVatCode((String) model.getValueAt(row, 3) );
        c.setBankCode((String) model.getValueAt(row, 4) );
        c.setBankName((String) model.getValueAt(row, 5) );
        return c;
    }
    
    private void alert(String content, short mode) {
        int jmode = 0;
        String title = "";
        if (mode == Constants.ALERT_MODE_SUCCESS){
            jmode = JOptionPane.PLAIN_MESSAGE;
            title = Constants.ALERT_TITLE_SUCCESS;
        } else if
        (mode == Constants.ALERT_MODE_WARNING){
            jmode = JOptionPane.WARNING_MESSAGE;
            title = Constants.ALERT_TITLE_WARNING;
        } else if
        (mode == Constants.ALERT_MODE_ERROR){
            jmode = JOptionPane.ERROR_MESSAGE;
            title = Constants.ALERT_TITLE_ERROR;
        }
        JOptionPane.showMessageDialog(this,
                content,
                title,
                jmode);
    }
    
    public void successNotice(String content) {
        alert(content, Constants.ALERT_MODE_SUCCESS);
    }
    
    public void warningNotice(String content) {
        alert(content, Constants.ALERT_MODE_WARNING);
    }
    
    public void errorNotice(String content) {
        alert(content, Constants.ALERT_MODE_ERROR);
    }

    @Override
    public void update(Observable o, Object arg) {
        CustomerListModel model = (CustomerListModel) o;
        final String[] colname = {"Id", "Name", "Address", "VAT Code", "Bank Id", "Bank Name"};
        final Object[][] data = new Object[ model.getCustomers().size() ][6];
        int pointer = 0;
        for (Iterator<Customer> i = model.getCustomers().iterator(); i.hasNext(); ){
            Customer e = i.next();
            data[pointer][0] = e.getId();
            data[pointer][1] = e.getName();
            data[pointer][2] = e.getAddress();
            data[pointer][3] = e.getVatCode();
            data[pointer][4] = e.getBankCode();
            data[pointer][5] = e.getBankName();
            pointer++;
        }
        FinalTableModel tm = new FinalTableModel();
        tm.setColumnIdentifiers(colname);
        for (Object[] or: data){
            tm.addRow(or);
        }
        tb_customers = new JTable(tm);
        tb_customers.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane content = new JScrollPane(tb_customers);
        panel.add(content);
        Layer.put(content).in(layout)
            .atTopLeft(panel).withMargin(5)
            .atRight(panel).withMargin(5)
            .topOf(bt_cancel).withMargin(5);
        this.pack();
        this.repaint();
    }

}
