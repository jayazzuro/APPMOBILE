package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app.API.APIlogout;
import com.example.app.API.APIUserById;

public class profileUser extends AppCompatActivity {
    ImageView imageView;
    TextView idten , idmail;
    Button btnChangePass, btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        imageView = findViewById(R.id.imgAvatar);
        idten = findViewById(R.id.tvUserName);
        idmail = findViewById(R.id.tvEmail);
        btnChangePass = findViewById(R.id.btnChangePass);
        btnLogout = findViewById(R.id.btnLogout);
        int userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getInt("userId", -1);
        APIUserById.getUserById(this, userId, idten, idmail);
        btnLogout.setOnClickListener(view -> {
            APIlogout.logout(profileUser.this);
        });
        btnChangePass.setOnClickListener(view ->{
            Intent intent = new Intent(profileUser.this, changePass.class);
            startActivity(intent);
        } );
    }

}