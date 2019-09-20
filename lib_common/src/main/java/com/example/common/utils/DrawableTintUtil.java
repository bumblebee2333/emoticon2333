package com.example.common.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;

public class DrawableTintUtil {
    public static Drawable tintDrawable(@NonNull Drawable drawable,int color){
        Drawable d = getCanTintDrawable(drawable);
        DrawableCompat.setTint(d, color);
        return d;
    }
    private static Drawable getCanTintDrawable(Drawable drawable) {
        Drawable.ConstantState state = drawable.getConstantState();
        return DrawableCompat.wrap(state == null ? drawable : state.newDrawable().mutate());
    }
}
