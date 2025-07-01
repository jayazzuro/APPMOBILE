package com.example.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;


public class changePass extends AppCompatActivity {
    EditText edtOldPass, edtNewPass, edtConfirmPass;
    Button btnChangePass;


    private static final String API_URL = "http://192.168.1.129:3000/api/changePasswordApi";
    private static final String TAG = "ChangePassActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        edtOldPass = findViewById(R.id.edtOldPass);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        btnChangePass = findViewById(R.id.btnChangePass);
        Toolbar toolbar = findViewById(R.id.toolbarChangePass);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        int userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getInt("userId", -1);
        btnChangePass.setOnClickListener(v -> {
            if(userId != -1){
                changePass(userId);
            }else{
                Toast.makeText(this, "Lỗi userId", Toast.LENGTH_SHORT).show();
            }
        });



    }


    private void changePass(int userId) {
        String oldPass = edtOldPass.getText().toString().trim();
        String newPass = edtNewPass.getText().toString().trim();
        String confirmPass = edtConfirmPass.getText().toString().trim();

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = API_URL + "/" + userId;

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("curPass", oldPass);
            requestBody.put("newPass", newPass);
            requestBody.put("RenewPass", confirmPass);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    requestBody,
                    response -> {
                        if (response.has("error")) {
                            try {
                                String errorMsg = response.getString("error");
                                Toast.makeText(this, "Lỗi: " + errorMsg, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                            edtOldPass.setText("");
                            edtNewPass.setText("");
                            edtConfirmPass.setText("");
                        }
                    },
                    error -> {
                        Toast.makeText(this, "Đổi mật khẩu thất bại! (Lỗi mạng hoặc server)", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
            );

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}