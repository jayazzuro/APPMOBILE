package com.example.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.SanPhamAdapter.SanPhamAdapterCart;
import com.example.app.SanPham.SanPhamCart;
import com.example.app.API.APIcart;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CheckOutActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPayment;
    private TextView tvTotal;
    private RadioGroup paymentMethodGroup;
    private Button btnConfirmPayment;

    private SanPhamAdapterCart adapter;
    private List<SanPhamCart> cartList = new ArrayList<>();
    private int maKH = -1;
    private int totalPrice = 0;

    //  launcher thay cho startActivityForResult
    private ActivityResultLauncher<Intent> vnPayLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        recyclerViewPayment = findViewById(R.id.recyclerViewPayment);
        tvTotal = findViewById(R.id.tvTotal);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        maKH = prefs.getInt("userId", -1);

        adapter = new SanPhamAdapterCart(this, cartList, tvTotal);
        recyclerViewPayment.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPayment.setAdapter(adapter);

        if (maKH != -1) {
            APIcart.loadGioHang(this, maKH, cartList, adapter, tvTotal);
        }

        //  Đăng ký nhận kết quả thanh toán
        vnPayLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        boolean success = data.getBooleanExtra("paymentSuccess", false);
                        if (success) {
                            Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        }
                    }
                });
        btnConfirmPayment.setOnClickListener(v -> handlePayment());
    }

    private void handlePayment() {
        int selectedId = paymentMethodGroup.getCheckedRadioButtonId();

        if (selectedId == -1) {
            Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
            return;
        }

        calculateTotal();

        if (selectedId == R.id.radioVNPay) {
            createVNPayUrlAndOpen();
        }else if (selectedId == R.id.radioMoMo) {
            createMoMoUrlAndOpen();
        } else {
            thanhToan("COD");
        }
    }

    private void calculateTotal() {
        totalPrice = 0;
        for (SanPhamCart sp : cartList) {
            totalPrice += sp.getGia() * sp.getSoLuong(  );
        }
    }

    private String formatCurrency(int amount) {
        return String.format("%,d VNĐ", amount).replace(",", ".");
    }

    private void createVNPayUrlAndOpen() {
        String url = "http://192.168.1.129:3000/api/create_payment_url/" + maKH;

        JSONObject body = new JSONObject();
        try {
            body.put("amount", totalPrice);
            body.put("orderInfo", "Thanh toan don hang " + maKH);
            body.put("bankCode", "");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, body,
                response -> {
                    try {
                        String paymentUrl = response.getString("data");
                        if (!paymentUrl.isEmpty()) {
                            Intent intent = new Intent(CheckOutActivity.this, WebViewActivity.class);
                            intent.putExtra("paymentUrl", paymentUrl);
                            vnPayLauncher.launch(intent);  // thay đổi
                        } else {
                            Toast.makeText(this, "Không lấy được URL thanh toán", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("VNPayError", "Lỗi phản hồi từ server", e);
                        Toast.makeText(this, "Lỗi phản hồi từ server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("VNPay", "Lỗi gọi API VNPay", error);
                    Toast.makeText(this, "Lỗi gọi API VNPay", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
        Log.d("VNPayAmount", "amount = " + totalPrice + ", type = " + ((Object) totalPrice).getClass().getSimpleName());
    }

    private void createMoMoUrlAndOpen() {
        String url = "http://192.168.1.129:3000/api/create_momo_payment/ " + maKH;

        JSONObject body = new JSONObject();
        try {
            body.put("amount", totalPrice);
            body.put("orderInfo", "ThanhToan-" + maKH);  // để backend tách mã KH
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, body,
                response -> {
                    try {
                        String paymentUrl = response.getString("payUrl"); // từ backend trả về
                        if (!paymentUrl.isEmpty()) {
                            Intent intent = new Intent(CheckOutActivity.this, WebViewActivity.class);
                            intent.putExtra("paymentUrl", paymentUrl);
                            vnPayLauncher.launch(intent);
                        } else {
                            Toast.makeText(this, "Không lấy được URL thanh toán MoMo", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("MoMoError", "Lỗi phản hồi từ server", e);
                        Toast.makeText(this, "Lỗi phản hồi từ server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("MoMo", "Lỗi gọi API MoMo", error);
                    Toast.makeText(this, "Lỗi gọi API MoMo", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void thanhToan(String method) {
        String url = "http://192.168.1.129:3000/api/thanhToanApi/" + maKH;

        JSONObject body = new JSONObject();
        try {
            body.put("tongTien", totalPrice);
            body.put("phuongThuc", method);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                response -> {
                    Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();


                    cartList.clear();
                    adapter.notifyDataSetChanged();


                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                },
                error -> {
                    Log.e("CheckoutError", "Lỗi thanh toán: " + error.toString());
                    Toast.makeText(this, "Lỗi thanh toán", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }
    private void clearCartAfterPayment() {
        String url = "http://192.168.1.129:3000/api/giohang/clear/" + maKH;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                response -> {
                    Toast.makeText(this, "Đã xóa giỏ hàng", Toast.LENGTH_SHORT).show();

                    // Quay về trang chính
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                },
                error -> {
                    Log.e("ClearCart", "Lỗi xóa giỏ hàng", error);
                    Toast.makeText(this, "Lỗi khi xóa giỏ hàng", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }
}