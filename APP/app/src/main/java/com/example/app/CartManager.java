package com.example.app;

import com.example.app.SanPham.SanPhamCart; // ✅ Đường dẫn chính xác
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<SanPhamCart> cartList = new ArrayList<>();

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<SanPhamCart> getCartList() {
        return cartList;
    }

    public void addToCart(SanPhamCart item) {
        for (SanPhamCart sp : cartList) {
            if (sp.getTenSanPham().equals(item.getTenSanPham())) {
                sp.setSoLuong(sp.getSoLuong() + 1);
                return;
            }
        }
        cartList.add(item);
    }

    public int calculateTotal() {
        int total = 0;
        for (SanPhamCart item : cartList) {
            total += item.getGia() * item.getSoLuong();
        }
        return total;
    }
}
