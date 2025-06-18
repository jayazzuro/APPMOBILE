package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.API.APIinsert;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    ImageView imgChiTiet;
    TextView txtTen, txtGia, txtChiTietmota;
    Button btnAddToCart, txtChiTietBuy, btnDecrease, btnIncrease;
    TextView txtQuantity;
    int soLuong = 1;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        imgChiTiet = findViewById(R.id.imgChiTiet);
        txtTen = findViewById(R.id.txtChiTietTen);
        txtGia = findViewById(R.id.txtChiTietGia);
        txtChiTietmota = findViewById(R.id.txtChiTietmota);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        txtChiTietBuy = findViewById(R.id.txtChiTietBuy);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        txtQuantity = findViewById(R.id.txtQuantity);

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

        userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getInt("userId", -1);

        // ✅ Gọi loadCartCount để cập nhật badge
        if (userId != -1) {
            loadCartCount(userId);
        }

        btnAddToCart.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(this, "Không tìm thấy ID người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            String tenHang = txtTen.getText().toString();
            String donGiaStr = getIntent().getStringExtra("donGia");
            String hinhAnh = getIntent().getStringExtra("hinhAnh");
            int donGia = Integer.parseInt(donGiaStr);

            APIinsert.insertToCart(
                    ChiTietSanPhamActivity.this,
                    tenHang,
                    donGia,
                    hinhAnh,
                    soLuong,
                    userId
            );

            // ✅ Gọi cập nhật sau khi thêm vào giỏ
            loadCartCount(userId);

            Intent intent = new Intent(this, Cart.class);
            startActivity(intent);
        });

        txtChiTietBuy.setOnClickListener(v -> {
            Intent intent = new Intent(ChiTietSanPhamActivity.this, Cart.class);
            intent.putExtra("tenHang", txtTen.getText().toString());
            intent.putExtra("donGia", getIntent().getStringExtra("donGia"));
            intent.putExtra("soLuong", soLuong);
            startActivity(intent);
        });
    }

    // ✅ API: Đếm sản phẩm trong giỏ và cập nhật cartBadge
    private void loadCartCount(int maKH) {
        String url = "http://10.0.2.2:3000/api/giohang/" + maKH;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    int count = 0;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            count += obj.getInt("soLuong");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    TextView cartBadge = findViewById(R.id.cartBadge);
                    if (cartBadge != null) {
                        if (count > 0) {
                            cartBadge.setVisibility(View.VISIBLE);
                            cartBadge.setText(String.valueOf(count));
                        } else {
                            cartBadge.setVisibility(View.GONE);
                        }
                    }
                },
                error -> Toast.makeText(this, "Lỗi tải số lượng giỏ hàng", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }
}
