package com.example.common.utils;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;

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
