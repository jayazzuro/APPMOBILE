package com.example.app.utils;

import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.app.R;

public class ImageSlider {
    private int[] images = {R.drawable.bg, R.drawable.ao2, R.drawable.ao3};
    private String[] texts = {"Chất Liệu Tốt", "Giá Tốt", "Bao Bền"};
    private int currentIndex = 0;
    private Handler handler = new Handler();
    private ImageView imageView;
    private TextView textView;

    public ImageSlider(ImageView imageView, TextView textView) {
        this.imageView = imageView;
        this.textView = textView;
        update();
    }

    private void update() {
        imageView.setImageResource(images[currentIndex]);
        textView.setText(texts[currentIndex]);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            currentIndex = (currentIndex + 1) % images.length;
            update();
            handler.postDelayed(this, 5000);
        }
    };

    public void start() {
        handler.postDelayed(runnable, 3000);
    }
}
