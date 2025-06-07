package com.example.app.API;
// CHO TRANG PROFILE
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.app.login;

public class APIlogout {
     public static void logout (Context context){
         SharedPreferences preferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = preferences.edit();
         editor.clear();
         editor.apply();

         Intent intent = new Intent(context, login.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         context.startActivity(intent);
     }
}
