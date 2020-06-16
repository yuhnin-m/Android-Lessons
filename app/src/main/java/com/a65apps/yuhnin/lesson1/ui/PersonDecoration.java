package com.a65apps.yuhnin.lesson1.ui;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PersonDecoration extends RecyclerView.ItemDecoration {
    private int offsetPx;

    public PersonDecoration(int offset) {
        this.offsetPx = offset;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = this.offsetPx;
        outRect.right = this.offsetPx;
        outRect.top = this.offsetPx;
        outRect.bottom = this.offsetPx;
    }
}
