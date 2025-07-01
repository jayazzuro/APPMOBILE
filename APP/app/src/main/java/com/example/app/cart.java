package com.example.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.API.APIcart;
import com.example.app.SanPham.SanPhamCart;
import com.example.app.SanPhamAdapter.SanPhamAdapterCart;

import java.util.ArrayList;
import java.util.List;

public class cart extends AppCompatActivity {
    private RecyclerView recyclerViewCart;
    private SanPhamAdapterCart adapter;
    private TextView cartTotalAmount;
    Button idtt,btnCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        idtt = findViewById(R.id.idtt);
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        cartTotalAmount = findViewById(R.id.cart_total_amount);
        btnCheckOut = findViewById(R.id.cart_checkout_button);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int maKH = prefs.getInt("userId", -1);

        List<SanPhamCart> cartList = new ArrayList<>();
        adapter = new SanPhamAdapterCart(this, cartList, cartTotalAmount);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(adapter);

        if (maKH != -1) {
            APIcart.loadGioHang(this, maKH, cartList, adapter, cartTotalAmount);
        }

        idtt.setOnClickListener(v -> {
            Intent intent1 = new Intent(cart.this, MainActivity.class);
            startActivity(intent1);
        });
        btnCheckOut.setOnClickListener(v -> {
            Intent intent = new Intent(cart.this, CheckOutActivity.class);
            startActivity(intent);
        });
    }
}
