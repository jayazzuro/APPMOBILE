package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.app.API.APIinsert;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    ImageView imgChiTiet;
    TextView txtTen, txtGia , txtChiTietmota;
    Button txtChiTietAdd , txtChiTietBuy , btnDecrease, btnIncrease;
    TextView txtQuantity;
    int soLuong = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        imgChiTiet = findViewById(R.id.imgChiTiet);
        txtTen = findViewById(R.id.txtChiTietTen);
        txtGia = findViewById(R.id.txtChiTietGia);
        txtChiTietmota = findViewById(R.id.txtChiTietmota);
        txtChiTietAdd = findViewById(R.id.txtChiTietAdd);
        txtChiTietBuy = findViewById(R.id.txtChiTietBuy);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        txtQuantity = findViewById(R.id.txtQuantity);

        // Nhận dữ liệu từ Intent
        String ten = getIntent().getStringExtra("tenHang");
        String gia = getIntent().getStringExtra("donGia");
        String hinh = getIntent().getStringExtra("hinhAnh");
        String mota = getIntent().getStringExtra("mota");

        txtTen.setText(ten);
        txtGia.setText(gia + " VNĐ");
        txtChiTietmota.setText(mota);

        String imageUrl = "http://10.0.2.2:3000/img/" + hinh;
        Glide.with(this).load(imageUrl).into(imgChiTiet);

       btnDecrease.setOnClickListener(v -> {
           if (soLuong > 1) {
               soLuong--;
               txtQuantity.setText(String.valueOf(soLuong));
           }
       });

        btnIncrease.setOnClickListener(v -> {
            soLuong++;
            txtQuantity.setText(String.valueOf(soLuong));
        });
        int userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getInt("userId", -1);
        // Thêm sản phẩm vào giỏ hàng
        txtChiTietAdd.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(this, "Không tìm thấy ID người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            String tenHang = txtTen.getText().toString();
            String donGiaStr = getIntent().getStringExtra("donGia");
            String hinhAnh = getIntent().getStringExtra("hinhAnh");


            int donGia= Integer.parseInt(donGiaStr);

            APIinsert.insertToCart(
                    ChiTietSanPhamActivity.this,
                    tenHang,
                    donGia,
                    hinhAnh,
                    soLuong,
                    userId
            );
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        txtChiTietBuy.setOnClickListener(v->{
            Intent intent = new Intent(ChiTietSanPhamActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}

