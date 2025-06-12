package com.example.app.API;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.SanPham.SanPham;
import com.example.app.SanPhamAdapter.SanPhamAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class APIcart {

    private static final String URL = "http://10.0.2.2:3000/api/getProductApi";

    // ✅ Giữ nguyên API lấy danh sách sản phẩm
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

    // ✅ HÀM MỚI: Gửi dữ liệu lên API để thêm vào bảng giohang
    public static void themVaoGioHang(Context context,
                                      String tenHang,
                                      int donGia,
                                      String dvt,
                                      String hinhAnh,
                                      int soLuong,
                                      String maKH) {

        String url = "http://10.0.2.2:3000/api/giohang"; // endpoint bạn đã tạo bên Node.js
        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject body = new JSONObject();
        try {
            body.put("tenHang", tenHang);
            body.put("donGia", donGia);
            body.put("DVT", dvt);
            body.put("hinhAnh", hinhAnh);
            body.put("soLuong", soLuong);
            body.put("maKH", maKH);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Lỗi tạo dữ liệu JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                response -> Toast.makeText(context, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(context, "Lỗi thêm vào giỏ hàng: " + error, Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }
}
