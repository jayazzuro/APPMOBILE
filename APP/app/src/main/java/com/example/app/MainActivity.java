package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.API.APIsanpham;
import com.example.app.API.APItheloai;
import com.example.app.SanPham.SanPham;
import com.example.app.SanPhamAdapter.SanPhamAdapter;
import com.example.app.utils.ImageSlider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView, recyclerViewSearch;
    SanPhamAdapter adapter, searchAdapter;
    List<SanPham> sanPhamList, searchList;
    Spinner mySpinner;
    TextView idten;
    ImageView imageView;
    ImageSlider imageSlider;
    EditText searchEditText;
    ImageButton btnSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewSearch = findViewById(R.id.recyclerViewSearch); // Thêm RecyclerView phụ trong XML
        mySpinner = findViewById(R.id.mySpinner);
        imageView = findViewById(R.id.id1);
        idten = findViewById(R.id.idten);
        searchEditText = findViewById(R.id.searchEditText);
        btnSearch = findViewById(R.id.btnSearch);


        // Khởi tạo danh sách
        sanPhamList = new ArrayList<>();


        // Adapter chính
        adapter = new SanPhamAdapter(this, sanPhamList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        imageSlider = new ImageSlider(imageView, idten);
        imageSlider.start();

        // Gọi API
        APIsanpham.loadProducts(this, sanPhamList, adapter);
        APItheloai.loadSpinner(this, mySpinner);

        // Lọc theo thể loại
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                APIsanpham.filterByCategory(sanPhamList, adapter, selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // nút chuyển sang trang profile cá nhân
        ImageView btnProfile = findViewById(R.id.user);
        btnProfile.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, profileUser.class);
            startActivity(intent);
        });

        // Tạo danh sách tìm kiếm
        ImageButton btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(view -> {
            String keyword = searchEditText.getText().toString().trim();
            if (!keyword.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
            }
        });
    }
}


