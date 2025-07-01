// ✅ MainActivity.java với xử lý cartBadge đã được sửa đúng
package com.example.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.API.APIsanpham;
import com.example.app.API.APItheloai;
import com.example.app.SanPham.SanPham;
import com.example.app.SanPhamAdapter.SanPhamAdapter;
import com.example.app.utils.ImageSlider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView, recyclerViewSearch;
    SanPhamAdapter adapter;
    List<SanPham> sanPhamList;
    Spinner mySpinner;
    TextView idten, cartBadge;
    ImageView imageView;
    ImageSlider imageSlider;
    EditText searchEditText;
    ImageButton btnSearch, btnCart;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewSearch = findViewById(R.id.recyclerViewSearch);
        mySpinner = findViewById(R.id.mySpinner);
        imageView = findViewById(R.id.id1);
        idten = findViewById(R.id.idten);
        searchEditText = findViewById(R.id.searchEditText);
        btnSearch = findViewById(R.id.btnSearch);
        cartBadge = findViewById(R.id.cartBadge);
        btnCart = findViewById(R.id.btnCart);

        sanPhamList = new ArrayList<>();
        adapter = new SanPhamAdapter(this, sanPhamList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        imageSlider = new ImageSlider(imageView, idten);
        imageSlider.start();

        APIsanpham.loadProducts(this, sanPhamList, adapter);
        APItheloai.loadSpinner(this, mySpinner);

        btnCart.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, cart.class);
            startActivity(intent);
        });

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                APIsanpham.filterByCategory(sanPhamList, adapter, selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ImageView btnProfile = findViewById(R.id.user);
        btnProfile.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, profileUser.class);
            startActivity(intent);
        });

        btnSearch.setOnClickListener(view -> {
            String keyword = searchEditText.getText().toString().trim();
            if (!keyword.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
            }
        });

        int userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getInt("userId", -1);
        if (userId != -1) {
            loadCartCount(userId); // ✅ gọi lần đầu khi mở app
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getInt("userId", -1);
        if (userId != -1) {
            loadCartCount(userId); // ✅ cập nhật mỗi khi quay lại MainActivity
        }
    }

    private void loadCartCount(int maKH) {
        String url = "http://192.168.1.129:3000/api/giohang/" + maKH;
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

                    if (cartBadge != null) {
                        if (count > 0) {
                            cartBadge.setVisibility(View.VISIBLE);
                            cartBadge.setText(String.valueOf(count));
                        } else {
                            cartBadge.setVisibility(View.GONE);
                        }
                    }
                },
                error -> Toast.makeText(this, "Lỗi tải giỏ hàng", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }
}