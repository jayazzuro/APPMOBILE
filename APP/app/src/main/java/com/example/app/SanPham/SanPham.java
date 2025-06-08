package com.example.app.SanPham;

public class SanPham {
    private String tenSanPham;
    private int gia;
    private String hinhAnh;
    private String theLoai;
    private String mota;

    public SanPham(String tenSanPham, int gia, String hinhAnh, String theLoai ) {
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.theLoai = theLoai;

    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public int getGia() {
        return gia;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public String getTheLoai() {
        return theLoai;
    }



}