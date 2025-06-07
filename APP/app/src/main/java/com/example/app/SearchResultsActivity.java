package com.example.app;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.API.APIsanpham;
import com.example.app.SanPham.SanPham;
import com.example.app.SanPhamAdapter.SanPhamAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<SanPham> searchResults;
    SanPhamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        recyclerView = findViewById(R.id.recyclerViewSearchResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResults = new ArrayList<>();
        adapter = new SanPhamAdapter(this, searchResults);
        recyclerView.setAdapter(adapter);

        String keyword = getIntent().getStringExtra("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            APIsanpham.searchProducts(this, keyword, searchResults, adapter);
        } else {
            Toast.makeText(this, "Không có từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
        }
    }
}
