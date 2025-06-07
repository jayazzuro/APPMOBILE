package com.example.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class dangky extends AppCompatActivity {
    TextView textView;
    EditText etTen, etDiaChi, etSdt, etMk1, etMk2, etEmail, GT;
    Button btnDangKy;
    RequestQueue requestQueue;

    private static final String TAG = "RegisterActivity";
    private static final String API_URL = "http://10.0.2.2:3000/api/registerApi";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);

        // Ánh xạ view
        textView = findViewById(R.id.textView);
        etTen = findViewById(R.id.ten);
        etDiaChi = findViewById(R.id.diachi);
        etSdt = findViewById(R.id.sdt);
        etEmail = findViewById(R.id.email);
        etMk1 = findViewById(R.id.mk1);
        etMk2 = findViewById(R.id.mk2);
        GT = findViewById(R.id.GT);
        btnDangKy = findViewById(R.id.dangky);

        requestQueue = Volley.newRequestQueue(this);

        btnDangKy.setOnClickListener(v -> dky());
    }

    private void dky() {
        String hoTen = etTen.getText().toString().trim();
        String diaChi = etDiaChi.getText().toString().trim();
        String SDT = etSdt.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String passWord = etMk1.getText().toString().trim();
        String passWord2 = etMk2.getText().toString().trim();
        String sex = GT.getText().toString().trim();

        // Kiểm tra đầu vào
        if (hoTen.isEmpty() || diaChi.isEmpty() || SDT.isEmpty() || email.isEmpty()
                || passWord.isEmpty() || passWord2.isEmpty() || sex.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!passWord.equals(passWord2)) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo JSON dữ liệu
        try {
            JSONObject dangkydata = new JSONObject();
            dangkydata.put("hoten", hoTen);
            dangkydata.put("diaChi", diaChi);
            dangkydata.put("SDT", SDT);
            dangkydata.put("email", email);
            dangkydata.put("passWord", passWord);
            dangkydata.put("sex", sex);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    API_URL,
                    dangkydata,
                    response -> {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                String userEmail = response.getJSONObject("user").getString("email");
                                Toast.makeText(this, "Đăng ký thành công: " + userEmail, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(dangky.this, login.class));
                                finish();
                            } else {
                                Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Lỗi JSON khi phân tích phản hồi: " + e.getMessage());
                            Toast.makeText(this, "Lỗi phân tích phản hồi từ server", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Log.e(TAG, "Lỗi mạng hoặc server: " + error.toString());
                        Toast.makeText(this, "Không thể kết nối tới server", Toast.LENGTH_SHORT).show();
                    }
            );

            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi tạo dữ liệu JSON để gửi đi", Toast.LENGTH_SHORT).show();
        }
    }
}
