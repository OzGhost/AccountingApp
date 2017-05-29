package common;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
/**
 * Class for create common view component
 * 
 * @author ducnh
 * create: 12-05-2017
 */
public abstract class Genner {
    
    public static final short MEDIUM_SIZE = 4;
    public static final short BIG_SIZE = 8;
    public static final short SMALL_SIZE = 2;
    
    /**
     * Quick new a button (for view only)
     * 
     * @param title
     * @param size
     * @return
     */
    public static JButton createButton(String title, short size){
        JButton rs = new JButton(title);
        Dimension btSize = null;
        Font btFont = null;
        if (size == MEDIUM_SIZE) {
            btSize = Constants.buttonSizeMd;
            btFont = Constants.fontSizeMd;
        } else if
        (size == BIG_SIZE) {
            btSize = Constants.buttonSizeBg;
            btFont = Constants.fontSizeBg;
        } else if
        (size == SMALL_SIZE){
            btSize = Constants.buttonSizeSm;
            btFont = Constants.fontSizeSm;
        }
        rs.setPreferredSize(btSize);
        rs.setFont(btFont);
        return rs;
    }
}
