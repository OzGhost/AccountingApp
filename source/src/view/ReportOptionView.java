package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileNameExtensionFilter;

import common.Constants;
import common.Genner;
import common.Layer;
import controller.ReportOptionController;
import model.ReportOptionModel;

public class ReportOptionView extends JFrame implements Observer {

    private static final long serialVersionUID = -5124637753509327566L;
    private JSpinner from;
    private JSpinner to;
    private JComboBox<String> gby;
    private JList<String> types;
    private JButton bt_back;
    private JButton bt_go;
    private final SpringLayout layout = new SpringLayout();
    private final JPanel pane = new JPanel();
    private final JPanel subPane = new JPanel(new GridLayout(0,1));

    public ReportOptionView () {
        init();
        setIconImage(Constants.appIcon);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Report export");
    }
    
    private void init() {

        // button
        bt_back = Genner.createButton("Back", Genner.MEDIUM_SIZE);
        bt_back.setActionCommand(Constants.ACTION_CANCEL);
        bt_go = Genner.createButton("Export Now", Genner.MEDIUM_LONG_SIZE);
        bt_go.setActionCommand(Constants.ACTION_DONE);

        // group by
        final Set<String> group = Constants.datePart.keySet();
        final String[] grp = new String[group.size()];
        int i = 0;
        for (String e: group) {
            grp[i] = e;
            i++;
        }
        gby = new JComboBox<String>(grp);

        // list type option
        types = new JList<>(Constants.PAY_LOG_TYPES);
        types.setLayoutOrientation(JList.VERTICAL);
        types.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        types.setVisibleRowCount(-1);

        final JScrollPane listHolder = new JScrollPane(types);
        listHolder.setPreferredSize(new Dimension(10, 130));

        pane.setLayout(layout);

        pane.add(bt_back);
        pane.add(bt_go);
        pane.add(listHolder);
        pane.add(subPane);

        Layer.put(bt_back).in(layout)
            .atBottomLeft(pane).withMargin(5);
        Layer.put(bt_go).in(layout)
            .atBottomRight(pane).withMargin(5);
        Layer.put(listHolder).in(layout)
            .topOf(bt_go).withMargin(5)
            .atLeft(pane).withMargin(5)
            .atRight(pane).withMargin(5);
        Layer.put(subPane).in(layout)
            .atTopLeft(pane).withMargin(5)
            .atRight(pane).withMargin(5)
            .topOf(listHolder).withMargin(5);
        
        setContentPane(pane);
        setPreferredSize(new Dimension(250, 450));
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public void setController (ReportOptionController roc) {
        bt_back.addActionListener(roc);
        bt_go.addActionListener(roc);
    }
    
    private void notice(String content, int mode) {
        String title = "";
        switch (mode) {
            case JOptionPane.WARNING_MESSAGE: title = "WARNING"; break;
            case JOptionPane.ERROR_MESSAGE: title = "ERROR!"; break;
            case JOptionPane.QUESTION_MESSAGE: title = "Answer it, do you?"; break;
            case JOptionPane.INFORMATION_MESSAGE: title = "INFORMATION"; break;
            default : title = "Message"; break;
        }
        JOptionPane.showMessageDialog(
                null,
                content,
                title,
                mode
        );
    }

    public void noticeWarning(String content) {
        notice(content, JOptionPane.WARNING_MESSAGE);
    }

    private void noticeInfo(String content) {
        notice(content, JOptionPane.INFORMATION_MESSAGE);
    }

    private void noticeError(String content) {
        notice(content, JOptionPane.ERROR_MESSAGE);
    }

    public void noticeResult(boolean rs) {
        if (rs) {
            noticeInfo("Exporting Complete!");
        } else {
            noticeError("Exporting Failure!");
        }
    }

    private File getSaveTo() {
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Pdf File", "pdf"
        );
        fc.setFileFilter(filter);
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        return null;
    }

    public Object[] getMetaData() {
        Date fdate = (Date) from.getModel().getValue();
        Date tdate = (Date) to.getModel().getValue();

        if (fdate.getTime() >= tdate.getTime()) {
            noticeWarning("From date must come before to date! Try again!");
            return null;
        }

        List<String> selectedTypes = types.getSelectedValuesList();
        if (selectedTypes.isEmpty()) {
            noticeWarning("You was missing type(s)! Try again!");
            return null;
        }

        File f = getSaveTo();
        if (f == null)
            return null;

        final Object[] rs = new Object[5];
        rs[0] = fdate;
        rs[1] = tdate;
        rs[2] = gby.getSelectedItem();
        rs[3] = selectedTypes;
        rs[4] = f;
        return rs;
    }
    
    public void beWait() {
        bt_back.setEnabled(false);
        bt_go.setEnabled(false);
        bt_go.setText("Loading...");
    }
    
    public void loaded() {
        bt_back.setEnabled(true);
        bt_go.setEnabled(true);
        bt_go.setText("Export Now");
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof ReportOptionModel)) 
            return;

        ReportOptionModel model = (ReportOptionModel) o;

        final Calendar calen = Calendar.getInstance();
        calen.setTime(model.getMaxDate());
        calen.add(Calendar.MONTH, -4);
        
        final SpinnerModel minModel = new SpinnerDateModel(
				calen.getTime(), // init number
				model.getMinDate(), // min
				model.getMaxDate(), // max
				Calendar.DAY_OF_MONTH // step
		);
        final SpinnerModel maxModel = new SpinnerDateModel(
				model.getMaxDate(), // init number
				model.getMinDate(), // min
				model.getMaxDate(), // max
				Calendar.DAY_OF_MONTH // step
		);

        from = new JSpinner(minModel);
        to = new JSpinner(maxModel);

        // subpane fill
        subPane.add(new JLabel("From date:"));
        subPane.add(from);
        subPane.add(new JLabel("To date:"));
        subPane.add(to);
        subPane.add(new JLabel("Group by:"));
        subPane.add(gby);
        subPane.add(new JLabel("Types:"));
        pack();
    }
}
