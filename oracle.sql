
DROP TABLE PhieuThuChi;
DROP TABLE KhachHang;
DROP TABLE TaiKhoan;

DROP TYPE PhieuThuChi_objtyp;
DROP TYPE ChiTietThuChi_ntabtyp;
DROP TYPE ChiTietThuChi_objtyp;
DROP TYPE KhachHang_objtyp;
DROP TYPE TaiKhoan_objtyp;

CREATE TYPE TaiKhoan_objtyp AS OBJECT (
    MaTaiKhoan      Varchar2(15),
    TenTaiKhoan     NVarchar2(128)
)
/
CREATE TYPE KhachHang_objtyp AS OBJECT (
    MaKhachHang     Varchar2(10),
    TenKhachHang    NVarchar2(128),
    DiaChi          NVarchar2(256),
    MaSoThue        NVarchar2(20),
    TKNH            Varchar2(20),
    TenNH           NVarchar2(128)
)
/
CREATE TYPE ChiTietThuChi_objtyp AS OBJECT (
    TaiKhoan_ref    REF TaiKhoan_objtyp
)
/
CREATE TYPE ChiTietThuChi_ntabtyp AS TABLE OF ChiTietThuChi_objtyp
/
CREATE TYPE PhieuThuChi_objtyp AS OBJECT (
    SoPhieu         Varchar2(20),
    Ngay            Date,
    ThueXuat        Number(3),
    LyDo            NVarchar2(128),
    TaiKhoanChinh   REF TaiKhoan_objtyp,
    KhachHang       REF KhachHang_objtyp,
    TaiKhoanDoiUng  ChiTietThuChi_ntabtyp
)
/

CREATE TABLE TaiKhoan OF TaiKhoan_objtyp (MaTaiKhoan PRIMARY KEY)
    OBJECT ID PRIMARY KEY
/
CREATE TABLE KhachHang OF KhachHang_objtyp (MaKhachHang PRIMARY KEY)
    OBJECT ID PRIMARY KEY
/
CREATE TABLE PhieuThuChi OF PhieuThuChi_objtyp (
    PRIMARY KEY (SoPhieu),
    FOREIGN KEY (TaiKhoanChinh) REFERENCES TaiKhoan,
    FOREIGN KEY (KhachHang) REFERENCES KhachHang
) OBJECT ID PRIMARY KEY
    NESTED TABLE TaiKhoanDoiUng STORE AS tkdu
/
ALTER TABLE tkdu
    ADD (SCOPE FOR (TaiKhoan_ref) IS TaiKhoan);

