drop table ChiTietThuChi;
drop table PhieuThuChi;
drop table TaiKhoan;
drop table KhachHang;


CREATE TABLE TaiKhoan (
    MaTaiKhoan      char(10) NOT NULL PRIMARY KEY,
    TenTaiKhoan     nvarchar(128)
);

CREATE TABLE KhachHang (
    MaKhachHang     int NOT NULL PRIMARY KEY,
    TenKhachHang    nvarchar(128),
    DiaChi          nvarchar(256),
    MaSoThue        char(20)
);

CREATE TABLE PhieuThuChi (
    SoPhieu         varchar(20) NOT NULL PRIMARY KEY,
    Ngay            datetime,
    MaTaiKhoanChinh char(10),
    MaKhachHang     int,
    CONSTRAINT  phieuthuchi_taikhoan
        FOREIGN KEY (MaTaiKhoanChinh)
        REFERENCES TaiKhoan(MaTaiKhoan),
    CONSTRAINT  phieuthuchi_khachhang
        FOREIGN KEY (MaKhachHang)
        REFERENCES KhachHang(MaKhachHang)
);

CREATE TABLE ChiTietThuChi (
    SoPhieu         varchar(20) NOT NULL,
    MaTaiKhoanDoiUng    char(10) NOT NULL,
    PRIMARY KEY (SoPhieu, MaTaiKhoanDoiUng),
    CONSTRAINT chitietthuchi_phieuthuchi
        FOREIGN KEY (SoPhieu)
        REFERENCES PhieuThuChi(SoPhieu),
    CONSTRAINT chitietthuchi_taikhoan
        FOREIGN KEY (MaTaiKhoanDoiUng)
        REFERENCES TaiKhoan(MaTaiKhoan)
);