package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Constants;
import common.PayLogDto;
import common.PayLogParamDto;
import db.db;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportModel {
    private static String mapListToCollect (List<String> list) {
        if (list == null || list.isEmpty())
            return null;
        String prefix = "";
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (String i: list) {
            sb.append(prefix);
            sb.append("'");
            sb.append(i);
            sb.append("'");
            prefix = ",";
        }
        sb.append(")");
        return sb.toString();
    }
    private static List<String> convertTypeToCode (List<String> types) {
        if (types == null || types.isEmpty())
            return null;
        final Map<String, String> cvMap = new HashMap<>();
        for (int i = Constants.PAY_LOG_TYPES.length - 1; i >= 0; i--) {
            cvMap.put(
                    Constants.PAY_LOG_TYPES[i],
                    Constants.PAY_LOG_TYPE_CODES[i]
            );
        }
        final List<String> rs = new ArrayList<>();
        types.forEach(t -> rs.add( cvMap.get(t) ));
        return rs;
    }
    
    public static void exportNow (
            Date from,
            Date to,
            String groupBy,
            List<String> types,
            File outFile
    ) throws Exception {
        if (
                from == null ||
                to == null ||
                groupBy == null
        )
            return;

        final String codes = mapListToCollect(
                convertTypeToCode (types)
        );
        if (codes == null)
            return;

        if (!Constants.datePart.containsKey(groupBy))
            return;

        final DateFormat forQ = new SimpleDateFormat("yyyy-MM-dd");

        final String retrieveDataQuery
            = "SELECT time, code, COUNT(code) as amount "
            + "FROM ( "
            + " SELECT  TO_CHAR(TRUNC(Ngay, '"
            +   Constants.datePart.get(groupBy)
            +           "'),'yyyy-mm-dd') as time,"
            + "         SUBSTR(SoPhieu,0,3) as code "
            + " FROM PhieuThuChi "
            + " WHERE SUBSTR(SoPhieu,0,3) IN " + codes + " AND "
            + "     (Ngay BETWEEN TO_DATE('"+forQ.format(from)+"', 'yyyy-mm-dd')"
            + "     AND TO_DATE('"+forQ.format(to)+"', 'yyyy-mm-dd'))"
            + ") "
            + "GROUP BY time, code "
            + "ORDER BY code, time";

        final ResultSet rs = db.sendForResult(retrieveDataQuery);
        if (rs == null)
            return;

        final List<PayLogDto> data = new ArrayList<>();

        while (rs.next()) {
            data.add(
                    new PayLogDto(
                        forQ.parse(rs.getString("time")),
                        Constants.extractCode.get( rs.getString("code") ),
                        rs.getInt("amount")
                        )
                    );
        }

        final PayLogParamDto plp = new PayLogParamDto();
        plp.setFrom(from);
        plp.setTo(to);
        plp.setTitle("BÁO CÁO SỐ PHIẾU THU/CHI");
        plp.setDate4mat(forQ);
        plp.setGroupBy(groupBy);
        plp.setTimeLabel("Thời gian");

        exportAsPdf(plp, data, outFile);
    }
    private static void exportAsPdf(
            PayLogParamDto plp,
            List<PayLogDto> data,
            File outFile
        ) throws Exception {
        if (data == null)
            return;
        
        List<String> label = new ArrayList<>(9);
        label.add(plp.getTitle());
        label.add("Kết xuất ngày:");
        label.add("Từ ngày:");
        label.add("Đến ngày:");
        label.add("Tính trên từng:");
        label.add(plp.getGroupBy());
        label.add("Thời gian");
        label.add("Loại phiếu");
        label.add("Số lượng");
        
        Map<String, Object> param = new HashMap<>();
        param.put("date4mat", plp.getDate4mat());
        param.put("from", plp.getFrom());
        param.put("to", plp.getTo());
        param.put("label", label);

        JasperPrint jPrint = null;
        jPrint = JasperFillManager.fillReport(
                JasperCompileManager.compileReport("src/res/rp.jrxml"),
                param,
                new JRBeanCollectionDataSource(data));

        final String fileName = outFile.getName();
        final int s = fileName.length();
        final String absoFile = outFile.getAbsolutePath();
        final int ss = absoFile.length();
        String prefix = null;
        String suffix = null;
        if (fileName.endsWith(".pdf") && s > 4) {
            prefix = fileName.substring(0, s - 4);
            suffix = ".pdf";
        } else {
            prefix = fileName;
            suffix = ".pdf";
        }

        final File f = new File(absoFile.substring(0, ss - s) + prefix + suffix);

        // create file by write some text to file
        final PrintWriter pw = new PrintWriter(f, "UTF-8");
        pw.write("init file");
        pw.flush();
        pw.close();

        JasperExportManager.exportReportToPdfStream(
                jPrint,
                new FileOutputStream(f)
                );
    }
}
