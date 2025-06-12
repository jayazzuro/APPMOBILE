package com.example.app.SanPhamAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.SanPham.SanPhamCart;

import java.util.List;

public class SanPhamAdapterCart extends RecyclerView.Adapter<SanPhamAdapterCart.ViewHolder> {
    private final Context context;
    private final List<SanPhamCart> sanPhamCartList;
    private final TextView cartTotalAmount;

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
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (sp.getSoLuong() > 1) {
                sp.setSoLuong(sp.getSoLuong() - 1);
                notifyItemChanged(holder.getAdapterPosition());
                updateTotal();
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                sanPhamCartList.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, sanPhamCartList.size());
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
