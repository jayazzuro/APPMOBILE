package com.example.app.SanPhamAdapter;

import com.example.app.SanPham.SanPham;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.ChiTietSanPhamActivity;

import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {
    private Context context;
    private List<SanPham> sanPhamList;

    public SanPhamAdapter(Context context, List<SanPham> sanPhamList) {
        this.context = context;
        this.sanPhamList = sanPhamList;
    }

    @NonNull
    @Override
    public SanPhamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_sanpham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamAdapter.ViewHolder holder, int position) {
        SanPham sp = sanPhamList.get(position);
        holder.txtTen.setText(sp.getTenSanPham());
        String giaFormatted = String.format("%,d", sp.getGia()).replace(',', '.') + " VNĐ";
        holder.txtGia.setText(giaFormatted);

        String imageUrl = "http://192.168.1.129:3000/img/" + sp.getHinhAnh();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.left)
                .error(R.drawable.right)
                .into(holder.imgSanPham);
        // THÊM xử lý sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
            intent.putExtra("tenHang", sp.getTenSanPham());
            intent.putExtra("donGia", String.valueOf(sp.getGia()));
            intent.putExtra("hinhAnh", sp.getHinhAnh());
            intent.putExtra("mota", sp.getMota());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sanPhamList.size();
    }

    public void updateList(List<SanPham> filteredList) {
        sanPhamList = filteredList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSanPham;
        TextView txtTen, txtGia ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            txtTen = itemView.findViewById(R.id.txtTen);
            txtGia = itemView.findViewById(R.id.txtGia);

        }
    }

}