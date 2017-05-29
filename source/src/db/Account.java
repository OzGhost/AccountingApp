package db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Account {

    public static List<Account> findAll() {
        List<Account> rs = new ArrayList<>();
        ResultSet resultSet = db.sendForResult("SELECT * FROM TaiKhoan ORDER BY MaTaiKhoan");
        
        // connection not ready
        if (resultSet == null)
            return rs;
        try {
            while (resultSet.next()){
                final Account a = new Account();
                a.setCode(resultSet.getString("MaTaiKhoan".toUpperCase()));
                a.setName(resultSet.getString("TenTaiKhoan".toUpperCase()));
                rs.add(a);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    private String code;
    private String name;

    /**
     * Constructor no argument
     */
    public Account () {
        this.code = "";
        this.name = "";
    }

    /**
     * Constructor all argument
     */
    public Account (String code, String name) {
        this.code = code ;
        this.name = name;
    }

    // Getters
    public String getCode () {
        return this.code;
    }
    public String getName () {
        return this.name;
    }

    // Setters
    public void setCode (String code) {
        this.code = code;
    }
    public void setName (String name) {
        this.name = name;
    }

    public Object[] castToObjects () {
        Object[] o = new Object[2];
        o[0] = this.code;
        o[1] = this.name;
        return o;
    }

    public String toRefQuery () {
        if (code == null || code.isEmpty())
            return "null";
        return "(SELECT REF(t) FROM TaiKhoan t WHERE t.MaTaiKhoan='"+code+"')";
    }

}

