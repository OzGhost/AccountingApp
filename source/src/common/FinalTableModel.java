package common;

import javax.swing.table.DefaultTableModel;

public class FinalTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 6065599534507916261L;
    
    public FinalTableModel(Object[] or, int nrow){
        super(or, nrow);
    }
    public FinalTableModel(){
        super();
    }
    
    @Override
    public boolean isCellEditable(int nrow, int ncol){
        return false;
    }
    
}
