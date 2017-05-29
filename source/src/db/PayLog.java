package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import common.Constants;


public class PayLog {

    private static String getTypeCode (String type) {
        if (Constants.PAY_LOG_TYPES[0].equals(type)) {
            return Constants.PAY_LOG_TYPE_CODES[0];
        }
        if (Constants.PAY_LOG_TYPES[1].equals(type)) {
            return Constants.PAY_LOG_TYPE_CODES[1];
        }
        if (Constants.PAY_LOG_TYPES[2].equals(type)) {
            return Constants.PAY_LOG_TYPE_CODES[2];
        }
        if (Constants.PAY_LOG_TYPES[3].equals(type)) {
            return Constants.PAY_LOG_TYPE_CODES[3];
        }
        if (Constants.PAY_LOG_TYPES[4].equals(type)) {
            return Constants.PAY_LOG_TYPE_CODES[4];
        }
        if (Constants.PAY_LOG_TYPES[5].equals(type)) {
            return Constants.PAY_LOG_TYPE_CODES[5];
        }
        return "";
    }

    /**
     * Aka auto id genner
     */
    public static PayLog getByType (String type, Date cdate) {
        if (type == null || type.isEmpty())
            return null;
        PayLog rs = new PayLog();
        final DateFormat df = new SimpleDateFormat("-MM-yy");
        final String typeCode = getTypeCode(type);
        final ResultSet resultSet = db.sendForResult(
                "SELECT SoPhieu as code FROM ("
                + " SELECT SoPhieu FROM PhieuThuChi"
                + " WHERE SoPhieu Like '"+typeCode+"%"+df.format(cdate)+"'"
                + " ORDER BY SoPhieu DESC"
                + ") WHERE ROWNUM = 1"
        );
        try {
            if (resultSet.next()) {
                String lastCode = resultSet.getString("code");
                int current = Integer.parseInt(
                    lastCode
                        .substring(0, lastCode.length() - 6)
                        .replaceAll("[^\\d]", "")
                );
                current++;
                rs.setId(
                        typeCode
                        + String.format("%6d", current).replace(" ", "0")
                        + df.format(cdate)
                );
            } else {
                rs.setId(typeCode + "000001" + df.format(cdate));
            }
        } catch (SQLException e) {
            rs = null;
            e.printStackTrace();
        }
        rs.setCreateDate(cdate);
        return rs;
    }

    private String id;
    private Date createDate;
    private int vatPercent;
    private String reason;
    private Account mainAccount;
    private Customer customer;
    private List<Account> coAcc;

    // Getter
    public String getId () {
        return this.id;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public int getVatPercent() {
        return this.vatPercent;
    }
    public String getReason() {
        return this.reason;
    }
    public Account getMainAccount () {
        return this.mainAccount;
    }
    public Customer getCustomer () {
        return this.customer;
    }
    public List<Account> getCoAcc() {
        return this.coAcc;
    }

    // Setter
    public void setId (String id) {
        this.id = id;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public void setVatPercent (int vatPercent) {
        this.vatPercent = vatPercent;
    }
    public void setReason (String reason) {
        this.reason = reason;
    }
    public void setMainAccount (Account mainAccount) {
        this.mainAccount = mainAccount;
    }
    public void setCustomer (Customer customer) {
        this.customer = customer;
    }
    public void setCoAcc (List<Account> coAcc) {
        this.coAcc = coAcc;
    }

    private String getCoAccREF() {
        if (this.coAcc == null || coAcc.isEmpty())
            return "null";
        String prefix = "";
        final StringBuilder sb = new StringBuilder("ChiTietThuChi_ntabtyp(");
        for (Account a: coAcc){
            String refQ = a.toRefQuery();
            if (!"null".equals(refQ)) {
                sb.append(prefix + "ChiTietThuChi_objtyp(");
                sb.append(refQ);
                sb.append(")");
                prefix = ",";
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public String toQuery() {
        if (
                createDate == null ||
                id == null || id.isEmpty()
        ) {
            return ";";
        }
        final String dateString = new SimpleDateFormat("yyyy-MM-dd")
            .format(this.createDate);
        final String reasonString = reason == null || reason.isEmpty() ? "null" : "N'"+reason+"'";
        final String mainAccREF = this.mainAccount == null ? "null" : mainAccount.toRefQuery();
        final String customerREF = this.customer == null ? "null" : customer.toRefQuery();
        return "INSERT INTO PhieuThuChi VALUES ("
                + "'" +this.id + "', "
                + "TO_DATE('" + dateString + "', 'yyyy-mm-dd'), "
                + this.vatPercent + ", "
                + reasonString + ", "
                + mainAccREF + ", "
                + customerREF + ", "
                + getCoAccREF() + ")";
    }
}

