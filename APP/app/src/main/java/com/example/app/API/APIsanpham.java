package com.example.app.API;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.example.app.SanPham.SanPham;
import com.example.app.SanPhamAdapter.SanPhamAdapter;
import org.json.*;
import java.util.*;

public class APIsanpham {
    private static final String URL = "http://192.168.1.129:3000/api/getProductApi";

    public static void loadProducts(Context context, List<SanPham> list, SanPhamAdapter adapter) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    list.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            list.add(new SanPham(
                                    obj.getString("tenHang"),
                                    obj.getInt("DonGia"),
                                    obj.getString("hinhAnh"),
                                    obj.getString("TenTheLoai"),
                                    obj.getString("moTa")
                            ));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(context, "Lỗi API: " + error, Toast.LENGTH_SHORT).show()
        );
        queue.add(request);
    }
    public static void filterByCategory(List<SanPham> fullList, SanPhamAdapter adapter, String category) {
        List<SanPham> filtered = new ArrayList<>();
        for (SanPham sp : fullList) {
            if (category.equals("Tất cả") || sp.getTheLoai().equals(category)) {
                filtered.add(sp);
            }
        }
        adapter.updateList(filtered);
    }
    public static void searchProducts(Context context, String keyword, List<SanPham> list, SanPhamAdapter adapter) {
        String url = "http://192.168.1.129:3000/api/search?keyword=" + Uri.encode(keyword);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    list.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            list.add(new SanPham(
                                    obj.getString("tenHang"),
                                    obj.getInt("DonGia"),
                                    obj.getString("hinhAnh"),
                                    obj.getString("TenTheLoai"),
                                    obj.getString("moTa")
                            ));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(context, "Lỗi tìm kiếm: " + error, Toast.LENGTH_SHORT).show()
        );
        queue.add(request);
    }
}