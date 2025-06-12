package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.SanPhamAdapter.SanPhamAdapterCart;
import com.example.app.SanPham.SanPhamCart;
import java.util.List;

public class Cart extends AppCompatActivity {
    private RecyclerView recyclerViewCart;
    private SanPhamAdapterCart adapter;
    private TextView cartTotalAmount;
    Button idtt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart); // XML layout của giỏ hàng
        idtt=findViewById(R.id.idtt);
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        cartTotalAmount = findViewById(R.id.cart_total_amount);

        List<SanPhamCart> cartList = CartManager.getInstance().getCartList();
        adapter = new SanPhamAdapterCart(this, cartList, cartTotalAmount);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(adapter);

        updateTotal();
        idtt.setOnClickListener(v -> {
        Intent intent = new Intent(Cart.this, MainActivity.class);
        startActivity(intent);
        });
    }

    private void updateTotal() {
        int total = CartManager.getInstance().calculateTotal();
        cartTotalAmount.setText(String.format("%,d VNĐ", total).replace(",", "."));
    }
}
