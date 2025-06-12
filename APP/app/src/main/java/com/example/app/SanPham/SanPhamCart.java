package com.example.app.SanPham;

public class SanPhamCart {
    private String tenSanPham;
    private int gia;
    private String hinhAnh;
    private int soLuong;

    public SanPhamCart(String tenSanPham, int gia, String hinhAnh) {
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.soLuong = 1; // mặc định 1 sản phẩm khi thêm vào giỏ
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

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
