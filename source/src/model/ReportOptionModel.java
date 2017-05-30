package model;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Observable;

public class ReportOptionModel extends Observable {
    private Date max;
    private Date min;

    public void init() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*
        ResultSet rs = db.sendForResult(
                "SELECT max(Ngay) as ma ,min(Ngay) as mi FROM PhieuThuChi"
        );
        */
        try {
            // rs.next();
            // max = df.parse(rs.getString("ma"));
            // min = df.parse(rs.getString("mi"));
            max = df.parse("2017-12-01 00:00:00");
            min = df.parse("2015-01-01 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }

    // Getter
    public Date getMaxDate() {
        return this.max;
    }
    public Date getMinDate() {
        return this.min;
    }

    @SuppressWarnings("unchecked")
    public boolean takeCare(Object[] meta) {
        boolean rs = true;
        try {
            ReportModel.exportNow(
                    (Date) meta[0],
                    (Date) meta[1],
                    (String) meta[2],
                    (List<String>) meta[3],
                    (File) meta[4]
            );
        } catch (Exception e) {
            e.printStackTrace();
            rs = false;
        }
        return rs;
    }
}
