package common;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ButtonColumn extends JLabel implements TableCellRenderer {
    
    private static final long serialVersionUID = 5435388286903913947L;

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row, int colum
    ) {
        if (isSelected) {
            System.out.println("is selected");
        }
        setToolTipText("remove this");
        return this;
    }

}
