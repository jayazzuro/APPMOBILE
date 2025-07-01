package com.example.app.API;

import android.content.Context;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import org.json.*;
import java.util.ArrayList;
import java.util.List;
public class APItheloai {
    private static final String URL = "http://192.168.1.129:3000/api/getTheLoaiApi";

    public static void loadSpinner(Context context, Spinner spinner) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    List<String> items = new ArrayList<>();
                    items.add("Tất cả");
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            items.add(obj.getString("tenTheLoai"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                },
                error -> Toast.makeText(context, "Lỗi tải Spinner: " + error, Toast.LENGTH_SHORT).show()
        );
        queue.add(request);
    }

}
