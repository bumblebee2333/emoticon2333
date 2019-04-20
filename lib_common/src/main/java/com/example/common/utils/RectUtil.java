package com.example.common.utils;

import android.graphics.RectF;
import android.view.View;

public class RectUtil {

    public static void rotateRect(RectF rect, float center_x, float center_y,
                                  float rotateAngle){
        float x = rect.centerX();
        float y = rect.centerY();
        float sinA = (float) Math.sin(Math.toRadians(rotateAngle));
        float cosA = (float) Math.cos(Math.toRadians(rotateAngle));
        float newX = center_x + (x - center_x) * cosA - (y - center_y) * sinA;
        float newY = center_y + (y - center_y) * cosA + (x - center_x) * sinA;

        float dx = newX - x;
        float dy = newY - y;

        rect.offset(dx,dy);
    }

    public static void scaleRect(RectF rect, float scale){
        float width = rect.width();
        float height = rect.height();

        float newWidth = width * scale;
        float newHeight = height * scale;

        if(newWidth < 50){
            newWidth = 50;
        }
        if(newHeight < 50){
            newHeight = 50;
        }

        float dx = (newWidth - width) / 2;
        float dy = (newHeight - height) / 2;

        rect.left -= dx;
        rect.top -= dy;
        rect.right += dx;
        rect.bottom += dy;
    }

    public static void moveRect(RectF rect, float dx, float dy, int mode){
        if(mode == 1){
            rect.left -= dx;
            rect.top -= dy;
            rect.right -= dx;
            rect.bottom -= dy;
        }else if(mode == 2){
            rect.left -= dx;
            rect.top += dy;
            rect.right -= dx;
            rect.bottom += dy;
        }else if(mode == 3){
            rect.left += dx;
            rect.top -= dy;
            rect.right += dx;
            rect.bottom -= dy;
        }else if(mode == 4){
            rect.left += dx;
            rect.top += dy;
            rect.right += dx;
            rect.bottom += dy;
        }
    }
}
