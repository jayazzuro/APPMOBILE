package com.example.app.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class APIinsert {

    private static final String TAG = "InsertCart";

    public static void insertToCart(Context context,
                                    String tenHang, int donGia
                                    , String hinhAnh,
                                    int soLuong , int userId) {

        String url = "http://192.168.1.129:3000/api/InsertProCartApi/" + userId;

        Map<String, Object> params = new HashMap<>();
        params.put("tenHang", tenHang);
        params.put("donGia", donGia);
        params.put("hinhAnh", hinhAnh);
        params.put("soLuong", soLuong);

        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                response -> {
                    try {
                        if (response.has("message")) {
                            Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Không thể thêm vào giỏ!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Lỗi xử lý JSON: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e(TAG, "Volley Error: " + error.toString());
                    Toast.makeText(context, "Lỗi mạng: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}
