package com.example.app.utils;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.app.R;

public class ChiTietSanPhamActivity extends AppCompatActivity {

    ImageView imgChiTiet;
    TextView txtTen, txtGia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        imgChiTiet = findViewById(R.id.imgChiTiet);
        txtTen = findViewById(R.id.txtChiTietTen);
        txtGia = findViewById(R.id.txtChiTietGia);

        // Nhận dữ liệu từ Intent
        String ten = getIntent().getStringExtra("ten");
        String gia = getIntent().getStringExtra("gia");
        String hinh = getIntent().getStringExtra("hinh");

        txtTen.setText(ten);
        txtGia.setText(gia + " VNĐ");

        String imageUrl = "http://10.0.2.2:3000/img/" + hinh;
        Glide.with(this).load(imageUrl).into(imgChiTiet);
    }
}

