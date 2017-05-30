package controller;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import common.Constants;
import db.db;
import model.PayLogModel;
import view.PayLogView;

public class EntryPoint {
	public static void main (String[] args) {
		
  		db.init("orcbase", "c##tester", "ngaymai");
	    
	    
	    
	    
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
		plv.setVisible(true);
		
        /*
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date from = null;
        Date to = null;
        List<String> types = new ArrayList<>();
        String gby = "MONTH";

        types.add(Constants.PAY_LOG_TYPES[0]);
        types.add(Constants.PAY_LOG_TYPES[1]);
        types.add(Constants.PAY_LOG_TYPES[2]);
        types.add(Constants.PAY_LOG_TYPES[3]);
        types.add(Constants.PAY_LOG_TYPES[4]);
        types.add(Constants.PAY_LOG_TYPES[5]);
        types.add(Constants.PAY_LOG_TYPES[6]);
        try {
            from = df.parse("2015-01-01");
            to = df.parse("2015-09-30");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ReportModel.exportNow(from, to, types, gby, 2);
        */

	}
}
