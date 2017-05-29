package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import common.Constants;
import common.FinalTableModel;
import common.Genner;
import common.Layer;
import controller.PayLogController;
import db.Account;
import db.Customer;
import model.PayLogModel;

public class PayLogView extends JFrame implements Observer {
	private static final long serialVersionUID = 3640173455167857444L;
	
	private JComboBox<String> types;
	private JSpinner date;
    private JTextArea reason;
	private JButton bt_customerSelect;
	private JButton bt_newCustomer;
	private JButton bt_addCoAcc;
	private JButton bt_delCoAcc;
	private JTable tb_coAcc;
	private JTextField customerName;
	private JTextField customerCode;
	private JTextField customerAddress;
	private JTextField customerVatCode;
    private JTextField customerBankCode;
    private JTextField customerBankName;
	private JButton bt_save;
	private JButton bt_cancel;
	private JButton bt_export;
	private JButton bt_reset;
    private JTextField mainAccCode;
    private JTextField mainAccName;
    private JButton bt_mainAcc;
    private JFormattedTextField vatPer;
	private final SpringLayout mainLayout = new SpringLayout();
	private final JPanel mainFrame = new JPanel();
	
	public PayLogView() {
		init();
	}
	
	private void init() {
		JPanel kind = initKindChoice();
		JPanel datePart = initDateChoice();
        JPanel reasonPart = initReason();
		JPanel customerChoice = initCustomerChoice();
		JPanel coAccChoice = initCoAccChoice();
        JPanel mainAccChoice = initMainAccChoice();
        JPanel vatPerChoice = initVatPercent();
		initButton();
		
		// pack middle part
        final SpringLayout rml = new SpringLayout();
        final JPanel rightMiddle = new JPanel(rml);
        rightMiddle.add(mainAccChoice);
        rightMiddle.add(coAccChoice);
        rightMiddle.add(vatPerChoice);
        Layer.put(mainAccChoice).in(rml)
            .atTopLeft(rightMiddle).withMargin(0)
            .atRight(rightMiddle).withMargin(0);
        Layer.put(vatPerChoice).in(rml)
            .atBottomLeft(rightMiddle).withMargin(0)
            .atRight(rightMiddle).withMargin(0);
        Layer.put(coAccChoice).in(rml)
            .bottomOf(mainAccChoice).withMargin(10)
            .topOf(vatPerChoice).withMargin(10)
            .atLeft(rightMiddle).withMargin(0)
            .atRight(rightMiddle).withMargin(0);

		final JPanel middlePart = new JPanel();
		middlePart.setLayout(new GridLayout(0,2));
		middlePart.add(customerChoice);
		middlePart.add(rightMiddle);
		
		// add each part into main container
		mainFrame.setLayout(mainLayout);
		mainFrame.add(kind);
		mainFrame.add(datePart);
		mainFrame.add(reasonPart);
		mainFrame.add(middlePart);
		mainFrame.add(bt_save);
		mainFrame.add(bt_cancel);
		mainFrame.add(bt_export);
		mainFrame.add(bt_reset);
		
		// lay each part
		Layer.put(datePart).in(mainLayout).atTopLeft(mainFrame).withMargin(10,5);
		Layer.put(kind).in(mainLayout)
			.leftOf(datePart).withMargin(5)
			.atTop(mainFrame).withMargin(10);
        Layer.put(reasonPart).in(mainLayout)
            .atTopRight(mainFrame).withMargin(10, 5)
            .leftOf(kind).withMargin(5)
            .topOf(middlePart).withMargin(10);
		Layer.put(middlePart).in(mainLayout)
			.atLeft(mainFrame).withMargin(5)
			.atRight(mainFrame).withMargin(5)
			.bottomOf(kind).withMargin(10)
			.topOf(bt_save).withMargin(5);
		Layer.put(bt_save).in(mainLayout)
			.atBottomRight(mainFrame).withMargin(5);
		Layer.put(bt_cancel).in(mainLayout)
			.rightOf(bt_reset).withMargin(5)
			.atBottom(mainFrame).withMargin(5);
		Layer.put(bt_export).in(mainLayout)
			.rightOf(bt_save).withMargin(5)
			.atBottom(mainFrame).withMargin(5);
		Layer.put(bt_reset).in(mainLayout)
			.rightOf(bt_export).withMargin(5)
			.atBottom(mainFrame).withMargin(5);
		
		// put all in frame
		setContentPane(mainFrame);
		
		// init size and position
        setPreferredSize(new Dimension(800, 600));
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
	}
	
	private JPanel initKindChoice() {
		types = new JComboBox<>(Constants.PAY_LOG_TYPES);
		// pack as kind part of screen
		final TitledBorder kindBorder = BorderFactory.createTitledBorder(
				Constants.BORDER_LINE, 
				"Pay type"
		);
		final JPanel kind = new JPanel();
		kind.setBorder(kindBorder);
		kind.setLayout(new BorderLayout());
		kind.setPreferredSize(new Dimension(220, 80));
		kind.add(types, BorderLayout.CENTER);
		return kind;
	}
	
	private JPanel initDateChoice() {
		Calendar calen = Calendar.getInstance();
		Date today = calen.getTime();
		calen.add(Calendar.YEAR, -10);
		Date minDay = calen.getTime();
		calen.add(Calendar.YEAR, 12);
		Date maxDay = calen.getTime();
		SpinnerModel dayModel = new SpinnerDateModel(
				today, // init number
				minDay, // min
				maxDay, // max
				Calendar.DAY_OF_MONTH // step
		);
		date = new JSpinner(dayModel);
		// pack as date choice part
		final TitledBorder dateBorder = BorderFactory.createTitledBorder(
				Constants.BORDER_LINE, 
				"Occur date"
		);
		final JPanel datePart = new JPanel(new GridLayout(0,1));
		datePart.setBorder(dateBorder);
		datePart.setPreferredSize(new Dimension(220, 80));
		datePart.add(new JLabel(" Format: mm/dd/yy hh:mm aa"));
		datePart.add(date);
		return datePart;
	}

    private JPanel initReason () {
        reason = new JTextArea();
        final JPanel rs = new JPanel(new BorderLayout());
        final TitledBorder tbd = BorderFactory.createTitledBorder(
                Constants.BORDER_LINE,
                "Reason"
        );
        rs.setBorder(tbd);
        rs.add(reason, BorderLayout.CENTER);
        return rs;
    }
	
	private JPanel initCustomerChoice() {
		bt_customerSelect = new JButton("Select");
		bt_customerSelect.setActionCommand(Constants.ACTION_SELECT_CUSTOMER);
		bt_newCustomer = new JButton("New");
		bt_newCustomer.setActionCommand(Constants.ACTION_NEW_CUSTOMMER);
		customerName = new JTextField();
		customerCode = new JTextField();
		customerCode.setEnabled(false);
		customerAddress = new JTextField();
		customerVatCode = new JTextField();
        customerBankCode = new JTextField();
        customerBankName = new JTextField();
		// pack as customer choice
		final TitledBorder customerBorder = BorderFactory.createTitledBorder(
				Constants.BORDER_LINE, 
				"Customer"
		);
		final JPanel btg_button = new JPanel(new GridLayout(0,2));
		btg_button.add(bt_customerSelect);
		btg_button.add(bt_newCustomer);
		final JPanel customerChoice = new JPanel(new GridLayout(0,1));
		customerChoice.setBorder(customerBorder);
		customerChoice.add(btg_button);
		customerChoice.add(new JLabel("Customer code: "));
		customerChoice.add(customerCode);
		customerChoice.add(new JLabel("Customer name: "));
		customerChoice.add(customerName);
		customerChoice.add(new JLabel("Customer address: "));
		customerChoice.add(customerAddress);
		customerChoice.add(new JLabel("Customer tax code: "));
		customerChoice.add(customerVatCode);
		customerChoice.add(new JLabel("Customer bank code: "));
		customerChoice.add(customerBankCode);
		customerChoice.add(new JLabel("Customer bank name: "));
		customerChoice.add(customerBankName);
		return customerChoice;
	}
	
	private JPanel initCoAccChoice() {
	    
	    bt_addCoAcc = Genner.createButton("Add", Genner.MEDIUM_SIZE);
        bt_addCoAcc.setActionCommand(Constants.ACTION_ADD_COACC);
        bt_delCoAcc = Genner.createButton("Remove", Genner.MEDIUM_SIZE);
        bt_delCoAcc.setActionCommand(Constants.ACTION_DEL_COACC);

        // group add and remove button
        final JPanel btGroup = new JPanel(new GridLayout(0,2));
        btGroup.add(bt_addCoAcc);
        btGroup.add(bt_delCoAcc);
	    
	    tb_coAcc = new JTable();
        tb_coAcc.setModel(new FinalTableModel(new Object[]{"Code", "Name"}, 0));
	    tb_coAcc.setFillsViewportHeight(true);
	    JScrollPane tableHolder = new JScrollPane(tb_coAcc);
	    
	    final JPanel rs = new JPanel();
	    rs.setBorder(BorderFactory.createTitledBorder(
                Constants.BORDER_LINE, 
                "Co-Relate Account"
        ));
	    final SpringLayout l = new SpringLayout();
	    rs.setLayout(l);
	    rs.add(btGroup);
	    rs.add(tableHolder);
	    
	    Layer.put(btGroup).in(l)
	        .atTopLeft(rs).withMargin(2)
	        .atRight(rs).withMargin(2);
	    Layer.put(tableHolder).in(l)
	        .atLeft(rs).withMargin(2)
	        .atRight(rs).withMargin(2)
	        .bottomOf(btGroup).withMargin(2)
	        .atBottom(rs).withMargin(2);
	        
	    return rs;
	}

    private JPanel initMainAccChoice () {
        bt_mainAcc = Genner.createButton("Select main account", Genner.MEDIUM_SIZE);
        bt_mainAcc.setActionCommand(Constants.ACTION_SELECT_MAIN_ACC);
        mainAccCode = new JTextField();
        mainAccCode.setEditable(false);
        mainAccCode.setColumns(6);
        mainAccName = new JTextField();
        mainAccName.setEditable(false);
        final SpringLayout l = new SpringLayout();
        final JPanel p = new JPanel(l);
        p.setBorder(BorderFactory.createTitledBorder(
                    Constants.BORDER_LINE,
                    "Main Account"
        ));
        p.setPreferredSize(new Dimension(200, 96));
        p.add(bt_mainAcc);
        p.add(mainAccCode);
        p.add(mainAccName);

        Layer.put(bt_mainAcc).in(l)
            .atTopLeft(p).withMargin(2)
            .atRight(p).withMargin(2);
        Layer.put(mainAccCode).in(l)
            .bottomOf(bt_mainAcc).withMargin(5)
            .atBottom(p).withMargin(2)
            .atLeft(p).withMargin(2);
        Layer.put(mainAccName).in(l)
            .bottomOf(bt_mainAcc).withMargin(5)
            .atBottom(p).withMargin(2)
            .leftOf(mainAccCode).withMargin(5)
            .atRight(p).withMargin(2);
        return p;
    }

    private JPanel initVatPercent () {
        final NumberFormat nf = NumberFormat.getNumberInstance();
        vatPer = new JFormattedTextField(nf);
        vatPer.setText("0");
        final JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(
                    Constants.BORDER_LINE,
                    "Tax PerCent"
        ));
        p.setPreferredSize(new Dimension(200, 54));
        p.add(vatPer, BorderLayout.CENTER);
        p.add(new JLabel(" % "), BorderLayout.EAST);
        return p;
    }
	
	private void initButton() {
		bt_save = Genner.createButton("Save", Genner.BIG_SIZE);
		bt_save.setActionCommand(Constants.ACTION_DONE);
		bt_cancel = Genner.createButton("Exit", Genner.MEDIUM_SIZE);
		bt_cancel.setActionCommand(Constants.ACTION_EXIT);
		bt_export = Genner.createButton("Export", Genner.MEDIUM_SIZE);
		bt_export.setActionCommand("export");
		bt_reset = Genner.createButton("Reset", Genner.MEDIUM_SIZE);
		bt_reset.setActionCommand("reset");
	}
	
	public void addController(PayLogController ctrler) {
	    if (
            ctrler == null ||
            bt_customerSelect == null ||
            bt_newCustomer == null ||
            bt_save == null ||
            bt_cancel == null ||
            bt_export == null ||
            bt_reset == null ||
            bt_addCoAcc == null ||
            bt_delCoAcc == null ||
            bt_mainAcc == null
        ) {
            System.out.println("---- WARNING: add controller while not init yet");
	        return;
        }
	    bt_customerSelect.addActionListener(ctrler);
	    bt_newCustomer.addActionListener(ctrler);
	    bt_save.addActionListener(ctrler);
	    bt_cancel.addActionListener(ctrler);
	    bt_export.addActionListener(ctrler);
	    bt_reset.addActionListener(ctrler);
        bt_addCoAcc.addActionListener(ctrler);
        bt_delCoAcc.addActionListener(ctrler);
        bt_mainAcc.addActionListener(ctrler);
	}
	
	public void resetCustomerForm(){
	    if (
            customerName == null ||
            customerCode == null ||
            customerAddress == null ||
            customerVatCode == null
        )
	        return;
	    customerName.setText("");
	    customerName.setEnabled(true);
	    customerCode.setText("");
	    customerAddress.setText("");
	    customerAddress.setEnabled(true);
	    customerVatCode.setText("");
	    customerVatCode.setEnabled(true);
	    customerBankCode.setText("");
	    customerBankCode.setEnabled(true);
	    customerBankName.setText("");
	    customerBankName.setEnabled(true);
	}
	
	public void setSelectedCustomer(Customer c){
	    if (c == null) {
	        return;
	    }
	    customerName.setText(c.getName());
	    customerName.setEnabled(false);
        customerCode.setText(c.getId());
        customerAddress.setText(c.getAddress());
        customerAddress.setEnabled(false);
        customerVatCode.setText(c.getVatCode());
        customerVatCode.setEnabled(false);
        customerBankCode.setText(c.getBankCode());
        customerBankCode.setEnabled(false);
        customerBankName.setText(c.getBankName());
        customerBankName.setEnabled(false);
	}

    public void addCoAcc(Object[] row) {
        ((DefaultTableModel) this.tb_coAcc.getModel()).addRow(row);
    }

    public int[] getSelectedCoAccRows () {
        return tb_coAcc.getSelectedRows();
    }

    public Object[] getMetaData () {
        Object[] rs = new Object[9];
        rs[0] = date.getModel().getValue();
        rs[1] = types.getSelectedItem();
        rs[2] = reason.getText();
        rs[3] = vatPer.getText();
        rs[4] = customerName.getText();
        rs[5] = customerAddress.getText();
        rs[6] = customerVatCode.getText();
        rs[7] = customerBankCode.getText();
        rs[8] = customerBankName.getText();
        return rs;
    }

    public void noticeWarning(String content) {
        JOptionPane.showMessageDialog(
                null,
                content,
                "WARNING",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public boolean verifyData (boolean newCustom) {
        if (newCustom && customerName.getText().isEmpty())
            return false;
        if (vatPer.getText().isEmpty())
            return false;
        return true;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof PayLogModel)) {
            return;
        }
        final short code = (short) arg;
        PayLogModel md = (PayLogModel) o;
        if (code == PayLogModel.CHANGED_CUSTOMER) {
            setSelectedCustomer(md.getCustomer());
            return;
        }
        if (code == PayLogModel.CHANGED_DEL_ACC) {
            int[] indexs = md.getIndexsTaken();
            DefaultTableModel dtm = (DefaultTableModel) tb_coAcc.getModel();
            for (int i = indexs.length - 1; i >= 0; i--) {
                dtm.removeRow(indexs[i]);
            }
            return;
        }
        if (code == PayLogModel.CHANGED_ADD_ACC) {
            int[] indexs = md.getIndexsTaken();
            List<Account> accs = md.getAccounts();
            DefaultTableModel dtm = (DefaultTableModel) tb_coAcc.getModel();
            for (int i: indexs) {
                dtm.addRow( accs.get(i).castToObjects() );
            }
            return;
        }
        if (code == PayLogModel.CHANGED_MAIN_ACC) {
            Account c = md.getMainAcc();
            mainAccCode.setText(c.getCode());
            mainAccName.setText(c.getName());
            return;
        }
    }
}
