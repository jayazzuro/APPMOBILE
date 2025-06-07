package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class login extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginBtn,iddk;
    RequestQueue requestQueue;

    private static final String TAG = "LoginExample";
    private static final String API_URL = "http://10.0.2.2:3000/api/loginApi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.editTextText);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        loginBtn = findViewById(R.id.iddn);
        iddk = findViewById(R.id.iddk);

        requestQueue = Volley.newRequestQueue(this);

        loginBtn.setOnClickListener(view -> login());
        iddk.setOnClickListener(view -> signup());
    }
    private void signup(){
        Intent intent = new Intent(login.this , dangky.class);
        startActivity(intent);
    }
    private void login() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject loginData = new JSONObject();
            loginData.put("email", email);
            loginData.put("password", password);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    API_URL,
                    loginData,
                    response -> {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                String userEmail = response.getJSONObject("user").getString("email");

                                int userId = response.getJSONObject("user").getInt("maKH");
                                getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                                        .edit()
                                        .putInt("userId", userId)
                                        .apply();

                                Toast.makeText(this, "Đăng nhập thành công: " + userEmail, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(login.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Sai thông tin đăng nhập", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Lỗi JSON: " + e.getMessage());
                        }
                    },
                    error -> {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String errorBody = new String(error.networkResponse.data, "UTF-8");
                                JSONObject errorObj = new JSONObject(errorBody);
                                String message = errorObj.optString("message", "Lỗi không xác định");

                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e(TAG, "Lỗi parse lỗi response: " + e.getMessage());
                                Toast.makeText(this, "Lỗi server", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Không kết nối được server", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            requestQueue.add(request);
        } catch (JSONException e) {
            Log.e(TAG, "Lỗi tạo JSON: " + e.getMessage());
        }
    }
}
