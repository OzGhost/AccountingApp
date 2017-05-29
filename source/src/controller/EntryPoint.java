package controller;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import common.Constants;
import db.PayLog;
import db.db;
import model.PayLogModel;
import view.PayLogView;

public class EntryPoint {
	public static void main (String[] args) {
		
		db.init("orcbase", "c##tester", "ngaymai");
	    
	    URL url = ClassLoader.getSystemResource("res/icon.png");
	    Toolkit kit = Toolkit.getDefaultToolkit();
	    Image img = kit.createImage(url);
	    
	    
	    try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Font setting
        FontUIResource font = new FontUIResource(
                Constants.globalFontName,
                Font.PLAIN,
                13
        );
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
          Object key = keys.nextElement();
          Object value = UIManager.get (key);
          if (value != null && value instanceof javax.swing.plaf.FontUIResource)
            UIManager.put (key, font);
        }
        
		PayLogView plv = new PayLogView();
		PayLogController plc = new PayLogController();
		PayLogModel plm = new PayLogModel();
		
		plm.addObserver(plv);
		plc.setView(plv);
		plc.setModel(plm);
		plv.addController(plc);
		plv.setIconImage(img);
		plv.setVisible(true);
		
	}
}
