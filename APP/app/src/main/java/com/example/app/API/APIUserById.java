package com.example.app.API;
// CHO TRANG PROFILE
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class APIUserById {
    private static final String TAG = "ProfileUser";
    public static void getUserById(Context context, int userId, TextView idten, TextView idmail) {
        if (userId != -1) {
            String url = "http://192.168.1.129:3000/api/profileApi/" + userId;
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    response -> {
                        try {
                            if (response.length() > 0) {
                                JSONObject user = response.getJSONObject(0);
                                String ten = user.getString("hoTen");
                                String email = user.getString("email");

                                idten.setText(ten);
                                idmail.setText(email);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Lỗi JSON: " + e.getMessage());
                        }
                    },
                    error -> {
                        Log.e(TAG, "Lỗi Volley: " + error.toString());
                    }
            );
            requestQueue.add(request);
        }
    }
}
