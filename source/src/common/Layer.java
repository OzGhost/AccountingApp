package common;

import java.awt.Component;

import javax.swing.SpringLayout;
/**
 * Lay the component on layout with readable function name
 * use for SpringLayout only
 * 
 * @author ducnh
 * create: 12-05-2017
 */
public class Layer {
    
    // lay code: specify what to do next
    private static final short LAY_CODE_TOPLEFT = 11;
    private static final short LAY_CODE_TOPRIGHT = 12;
    private static final short LAY_CODE_BOTTOMLEFT = 21;
    private static final short LAY_CODE_BOTTOMRIGHT = 22;
    private static final short LAY_CODE_LEFTOF = 1;
    private static final short LAY_CODE_ATLEFT = 2;
    private static final short LAY_CODE_RIGHTOF = 3;
    private static final short LAY_CODE_ATRIGHT = 4;
    private static final short LAY_CODE_BOTTOMOF = 5;
    private static final short LAY_CODE_ATBOTTOM = 6;
    private static final short LAY_CODE_TOPOF = 7;
    private static final short LAY_CODE_ATTOP = 8;

    // base variable
    private Component primary;
    private Component secondary;
    private SpringLayout layout;
    private short code;

    /**
     * Private constructor
     * prevent create instance of this class
     */
    private Layer(){}

    /**
     * Setter for primary component
     * which need to be lay
     * @param c
     */
    private void setPrimary(Component c) {
        this.primary = c;
    }

    /**
     * First chain node (must have) use for open chain query
     * @param p
     * @return
     */
    public static Layer put(Component p) {
        Layer rs = new Layer();
        rs.setPrimary(p);
        return rs;
    }

    private Layer at(Component c, short code) {
        this.secondary = c;
        this.code = code;
        return this;
    }

    /**
     * Put primary component at top left corner
     * of secondary component (usually is panel)
     * @param c
     * @return
     */
    public Layer atTopLeft(Component c) {
        return at(c, LAY_CODE_TOPLEFT);
    }

    /**
     * Put primary component at top right corner
     * of secondary component (usually is panel)
     * @param c
     * @return
     */
    public Layer atTopRight(Component c) {
        return at(c, LAY_CODE_TOPRIGHT);
    }

    /**
     * Put primary component at bottom left corner
     * of secondary component (usually is panel)
     * @param c
     * @return
     */
    public Layer atBottomLeft(Component c) {
        return at(c, LAY_CODE_BOTTOMLEFT);
    }

    /**
     * Put primary component at bottom right corner
     * of secondary component (usually is panel)
     * @param c
     * @return
     */
    public Layer atBottomRight(Component c) {
        return at(c, LAY_CODE_BOTTOMRIGHT);
    }

    /**
     * Put primary component on the left of secondary component
     * @param c
     * @return
     */
    public Layer leftOf(Component c) {
        return at(c, LAY_CODE_LEFTOF);
    }

    /**
     * Put primary component on the right of secondary component
     * @param c
     * @return
     */
    public Layer rightOf(Component c) {
        return at(c, LAY_CODE_RIGHTOF);
    }
    
    /**
     * Put primary component above secondary component
     * @param c
     * @return
     */
    public Layer topOf(Component c) {
        return at(c, LAY_CODE_TOPOF);
    }
    
    /**
     * Put primary component under secondary component
     * @param c
     * @return
     */
    public Layer bottomOf(Component c) {
        return at(c, LAY_CODE_BOTTOMOF);
    }

    /**
     * Put primary component to left edge of
     * secondary component
     * @param c
     * @return
     */
    public Layer atLeft(Component c) {
        return at(c, LAY_CODE_ATLEFT);
    }
    
    /**
     * Put primary component to right edge of
     * secondary component
     * @param c
     * @return
     */
    public Layer atRight(Component c) {
        return at(c, LAY_CODE_ATRIGHT);
    }

    /**
     * Put primary component to top edge of
     * secondary component
     * @param c
     * @return
     */
    public Layer atTop(Component c) {
        return at(c, LAY_CODE_ATTOP);
    }

    /**
     * Put primary component to bottom edge of
     * secondary component
     * @param c
     * @return
     */
    public Layer atBottom(Component c) {
        return at(c, LAY_CODE_ATBOTTOM);
    }

    /**
     * Specify layout which components was lay in
     * @param s
     * @return
     */
    public Layer in(SpringLayout s) {
        this.layout = s;
        return this;
    }
    
    /**
     * Set margin between primary and secondary component
     * and put constraint to layout
     * @param m
     * @return
     */
    public Layer withMargin(int m) {
        if (primary == null || secondary == null || layout == null || code < 0)
            return null;
        if (code == LAY_CODE_TOPLEFT){
            layout.putConstraint(SpringLayout.NORTH, primary, m,
                    SpringLayout.NORTH, secondary);
            layout.putConstraint(SpringLayout.WEST, primary, m,
                    SpringLayout.WEST, secondary);
            return this;
        }
        if (code == LAY_CODE_TOPRIGHT) {
            layout.putConstraint(SpringLayout.NORTH, primary, m,
                    SpringLayout.NORTH, secondary);
            layout.putConstraint(SpringLayout.EAST, primary, (-1) * m,
                    SpringLayout.EAST, secondary);
            return this;
        }
        if (code == LAY_CODE_BOTTOMLEFT) {
            layout.putConstraint(SpringLayout.SOUTH, primary, (-1) * m,
                    SpringLayout.SOUTH, secondary);
            layout.putConstraint(SpringLayout.WEST, primary, m,
                    SpringLayout.WEST, secondary);
            return this;
        }
        if (code == LAY_CODE_BOTTOMRIGHT) {
            layout.putConstraint(SpringLayout.SOUTH, primary, (-1) * m,
                    SpringLayout.SOUTH, secondary);
            layout.putConstraint(SpringLayout.EAST, primary, (-1) * m,
                    SpringLayout.EAST, secondary);
            return this;
        }
        if (code == LAY_CODE_LEFTOF) {
            layout.putConstraint(SpringLayout.WEST, primary, m,
                    SpringLayout.EAST, secondary);
            return this;
        }
        if (code == LAY_CODE_ATLEFT) {
            layout.putConstraint(SpringLayout.WEST, primary, m,
                    SpringLayout.WEST, secondary);
            return this;
        }
        if (code == LAY_CODE_RIGHTOF) {
            layout.putConstraint(SpringLayout.EAST, primary, (-1) * m,
                    SpringLayout.WEST, secondary);
            return this;
        }
        if (code == LAY_CODE_ATRIGHT) {
            layout.putConstraint(SpringLayout.EAST, primary, (-1) * m,
                    SpringLayout.EAST, secondary);
            return this;
        }
        if (code == LAY_CODE_TOPOF) {
            layout.putConstraint(SpringLayout.SOUTH, primary, (-1) * m,
                    SpringLayout.NORTH, secondary);
            return this;
        }
        if (code == LAY_CODE_ATTOP) {
            layout.putConstraint(SpringLayout.NORTH, primary, m,
                    SpringLayout.NORTH, secondary);
            return this;
        }
        if (code == LAY_CODE_BOTTOMOF) {
            layout.putConstraint(SpringLayout.NORTH, primary, m,
                    SpringLayout.SOUTH, secondary);
            return this;
        }
        if (code == LAY_CODE_ATBOTTOM) {
            layout.putConstraint(SpringLayout.SOUTH, primary, (-1) * m,
                    SpringLayout.SOUTH, secondary);
            return this;
        }
        return this;
    }
    
    /**
     * Same as above method but have two margin
     * first for vertical margin
     * second for horizontal margin
     * @param m
     * @param n
     * @return
     */
    public Layer withMargin(int m, int n) {
        if (primary == null || secondary == null || layout == null || code < 0)
            return null;
        if (code == LAY_CODE_BOTTOMLEFT) {
            layout.putConstraint(SpringLayout.SOUTH, primary, (-1) * m,
                    SpringLayout.SOUTH, secondary);
            layout.putConstraint(SpringLayout.WEST, primary, n,
                    SpringLayout.WEST, secondary);
            return this;
        }
        if (code == LAY_CODE_TOPLEFT) {
        	layout.putConstraint(SpringLayout.NORTH, primary, m,
        			SpringLayout.NORTH, secondary);
        	layout.putConstraint(SpringLayout.WEST, primary, n,
        			SpringLayout.WEST, secondary);
        	return this;
        }
        if (code == LAY_CODE_TOPRIGHT) {
        	layout.putConstraint(SpringLayout.NORTH, primary, m,
        			SpringLayout.NORTH, secondary);
        	layout.putConstraint(SpringLayout.EAST, primary, (-1) * n,
        			SpringLayout.EAST, secondary);
        	return this;
        }
        if (code == LAY_CODE_BOTTOMRIGHT){
            layout.putConstraint(SpringLayout.SOUTH, primary, (-1) * m,
                    SpringLayout.SOUTH, secondary);
            layout.putConstraint(SpringLayout.EAST, primary, (-1) * n,
                    SpringLayout.EAST, secondary);
            return this;
        }
        return this;
    }
}
