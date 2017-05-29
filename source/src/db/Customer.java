package db;

import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class Customer {
    
    public static List<Customer> findAll(){
        List<Customer> rs = new ArrayList<>();
        ResultSet resultSet = db.sendForResult("SELECT * FROM KHACHHANG ORDER BY TenKhachHang");
        
        // connection not ready
        if (resultSet == null)
            return rs;
        try {
            while (resultSet.next()){
                final Customer c = new Customer();
                c.setId(resultSet.getString("MaKhachHang".toUpperCase()));
                c.setName(resultSet.getString("TenKhachHang".toUpperCase()));
                c.setAddress(resultSet.getString("DiaChi".toUpperCase()));
                c.setVatCode(resultSet.getString("MaSoThue".toUpperCase()));
                c.setBankCode( resultSet.getString("TKNH") );
                c.setBankName( resultSet.getString("TenNH") );
                rs.add(c);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static Customer save(Customer c) {
        if (
                c == null ||
                c.getName() == null ||
                c.getName().isEmpty()
        )
            return null;
        final ResultSet rs = db.sendForResult(
                "SELECT MaKhachHang as id FROM"
                + " (SELECT MaKhachHang FROM KhachHang ORDER BY MaKhachHang DESC)"
                + " WHERE ROWNUM=1"
        );
        String cusId = null;
        try {
            if (rs.next()) {
                cusId = String.format(
                        "%9d",
                        Integer.parseInt(rs.getString("id"))
                        ).replace(' ', '0');
            } else {
                cusId = "000000001";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("---- ERROR: Get id from database failure!");
            cusId = null;
        }

        // Get Id false
        if (cusId == null) {
            return null;
        }

        final String address = c.getAddress() == null || c.getAddress().isEmpty()
            ? "null"
            : "N'" + c.getAddress() + "'";
        final String taxCode = c.getVatCode() == null || c.getVatCode().isEmpty()
            ? "null"
            : "N'" + c.getVatCode() + "'";
        final String bankCode = c.getBankCode() == null || c.getBankCode().isEmpty()
            ? "null"
            : "'" + c.getBankCode() + "'";
        final String bankName = c.getBankName() == null || c.getBankName().isEmpty()
            ? "null"
            : "N'" + c.getBankName() + "'";
        final String q = "INSERT INTO KhachHang VALUES ("
            + " '" + cusId + "',"
            + " N'" + c.getName() + "',"
            + address + ","
            + taxCode + ","
            + bankCode + ","
            + bankName
            + ")";
        if (db.send(q)) {
            c.setId(cusId);
            return c;
        } else {
            return null;
        }
    }

    private String id;
    private String name;
    private String address;
    private String vatCode;
    private String bankCode;
    private String bankName;
    
    // Getter
    public String getId(){
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getAddress() {
        return this.address;
    }
    public String getVatCode() {
        return this.vatCode;
    }
    public String getBankCode() {
        return this.bankCode;
    }
    public String getBankName() {
        return this.bankName;
    }
    
    // Setter
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setVatCode(String vatCode){
        this.vatCode = vatCode;
    }
    public void setBankCode (String bankCode) {
        this.bankCode = bankCode;
    }
    public void setBankName (String bankName) {
        this.bankName = bankName;
    }
    
    public String toRefQuery () {
        if (id == null || id.isEmpty())
            return "null";
        return "(SELECT REF(k) FROM KhachHang k WHERE k.MaKhachHang='"+id+"')";
    }
}
