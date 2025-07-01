package com.example.app.API;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.SanPham.SanPham;
import com.example.app.SanPham.SanPhamCart;
import com.example.app.SanPhamAdapter.SanPhamAdapter;
import com.example.app.SanPhamAdapter.SanPhamAdapterCart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class APIcart {

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
                                    obj.getString("mota")
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

    public static void loadGioHang(Context context, int maKH, List<SanPhamCart> list,
                                   SanPhamAdapterCart adapter, TextView totalAmountView) {
        String url = "http://192.168.1.129:3000/api/giohang/" + maKH;
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    list.clear();
                    int total = 0;

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            // Tạo đối tượng sản phẩm từ dữ liệu trả về
                            SanPhamCart sp = new SanPhamCart(
                                    obj.getString("tenHang"),
                                    obj.getInt("donGia"),
                                    obj.getString("hinhAnh")
                            );

                            sp.setSoLuong(obj.getInt("soLuong")); // gán số lượng

                            // ✅ Gán idCart từ API để sử dụng cho API xoá / cập nhật
                            sp.setIdCart(obj.getInt("idCart")); // bắt buộc phải có để DELETE hoạt động

                            list.add(sp);
                            total += sp.getSoLuong() * sp.getGia(); // tính tổng tiền

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    adapter.notifyDataSetChanged();
                    totalAmountView.setText(String.format("%,d VNĐ", total).replace(",", "."));
                },
                error -> Toast.makeText(context, "Lỗi tải giỏ hàng", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }

}
