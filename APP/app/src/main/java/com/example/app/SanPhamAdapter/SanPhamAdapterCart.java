package com.example.app.SanPhamAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.SanPham.SanPhamCart;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SanPhamAdapterCart extends RecyclerView.Adapter<SanPhamAdapterCart.ViewHolder> {
    private final Context context;
    private final List<SanPhamCart> sanPhamCartList;
    private final TextView cartTotalAmount;

    // 👇 thêm hàm này vào ngay trong class SanPhamAdapterCart (sau updateSoLuongOnServer hoặc dưới cùng)
    private void deleteFromServer(int idCart) {
        String url = "http://10.0.2.2:3000/api/giohang/" + idCart;

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    // Có thể xử lý gì đó nếu cần
                },
                error -> {
                    Toast.makeText(context, "Lỗi xoá sản phẩm khỏi SQL", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(context).add(request);
    }



    public SanPhamAdapterCart(Context context, List<SanPhamCart> list, TextView totalAmountView) {
        this.context = context;
        this.sanPhamCartList = list;
        this.cartTotalAmount = totalAmountView;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPhamCart sp = sanPhamCartList.get(position);

        holder.txtTen.setText(sp.getTenSanPham());
        holder.tvQuantity.setText(String.valueOf(sp.getSoLuong()));
        holder.txtGia.setText(formatCurrency(sp.getGia() * sp.getSoLuong()));

        Glide.with(context)
                .load("http://10.0.2.2:3000/img/" + sp.getHinhAnh())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgSanPham);

        holder.btnIncrease.setOnClickListener(v -> {
            sp.setSoLuong(sp.getSoLuong() + 1);
            notifyItemChanged(holder.getAdapterPosition());
            updateTotal();
            updateSoLuongOnServer(sp.getIdCart(), sp.getSoLuong());
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (sp.getSoLuong() > 1) {
                sp.setSoLuong(sp.getSoLuong() - 1);
                notifyItemChanged(holder.getAdapterPosition());
                updateTotal();
                updateSoLuongOnServer(sp.getIdCart(), sp.getSoLuong());
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                int idCart = sanPhamCartList.get(pos).getIdCart(); // ✅ Lấy idCart để gửi API

                deleteFromServer(idCart); // ✅ Gọi hàm xoá API

                sanPhamCartList.remove(pos);
                notifyItemRemoved(pos);
                updateTotal();
            }
        });

    }

    @Override
    public int getItemCount() {
        return sanPhamCartList.size();
    }

    private void updateTotal() {
        int total = 0;
        for (SanPhamCart item : sanPhamCartList) {
            total += item.getGia() * item.getSoLuong();
        }
        cartTotalAmount.setText(formatCurrency(total));
    }

    private String formatCurrency(int amount) {
        return String.format("%,d VNĐ", amount).replace(",", ".");
    }

    // ✅ API update soLuong trong SQL
    private void updateSoLuongOnServer(int idCart, int soLuong) {
        String url = "http://10.0.2.2:3000/api/giohang/" + idCart;

        JSONObject body = new JSONObject();
        try {
            body.put("soLuong", soLuong);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, body,
                response -> {
                    // Thành công
                },
                error -> {
                    Toast.makeText(context, "Lỗi cập nhật số lượng", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(context).add(request);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSanPham;
        TextView txtTen, txtGia, tvQuantity;
        Button btnIncrease, btnDecrease, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPhamCart);
            txtTen = itemView.findViewById(R.id.txtTenCart);
            txtGia = itemView.findViewById(R.id.txtGiaCart);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
