package com.example.qrcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.common.app.ResourcesManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/10/23.
 * PS:
 */
public class QrCodeUtils {
    public static Bitmap createQrCode(String content, int width, int height, Bitmap logo) {
        int backColor = Color.WHITE;
        int pointColor = ResourcesManager.getRes().getColor(R.color.colorBlue);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, "1");
        hints.put(EncodeHintType.ERROR_CORRECTION, "H");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = pointColor;
                    } else {
                        pixels[i * width + j] = backColor;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            //Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
            if (logo != null) return addLogo(bitmap, logo, 0.2F);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap createPoster(String content){
        int width = 1080;
        width /= 2;
        int height = width;
        Bitmap logo = BitmapFactory.decodeResource(ResourcesManager.getRes(), R.mipmap.app_logo);
        Bitmap qrCode = QrCodeUtils.createQrCode(content, width, height, logo);
        return createPoster(qrCode);
    }

    private static Bitmap createPoster(Bitmap p) {
        int width = 1080;
        int height;// = ScreenUtils.getScreenHeight(ResourcesManager.getAppContext());
        height = (int) (width*1.5);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        String text = "分享自《表情工坊》APP";

        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //textPaint设置
        textPaint.setTextSize(25);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextAlign(Paint.Align.LEFT);

        bgPaint.setColor(Color.WHITE);
        bgPaint.setShadowLayer(15, 0, 0, Color.GRAY);
        Canvas canvas = new Canvas(bitmap);
        int margin = 40;
        int marginTop = margin * 2;
        int marginBottom = margin * 2;
        RectF rectF = new RectF(margin, marginTop, width - margin, height - marginBottom);
        canvas.drawColor(Color.WHITE);
        canvas.drawRoundRect(rectF, 20, 20, bgPaint);

        canvas.drawBitmap(p, (width - p.getWidth()) / 2, (height - p.getHeight()) / 2, bgPaint);
        canvas.drawText(text, margin + 50, height - marginBottom - 50, textPaint);
//        canvas.save();
        Bitmap b = createQrCode("http://skit.vip/", 180,180, null);

        Bitmap logo = BitmapFactory.decodeResource(ResourcesManager.getRes(), R.mipmap.app_logo);
        b = addLogo(b,logo,0.2f);
        canvas.drawBitmap(b, width - margin - b.getWidth(),height - marginBottom - b.getHeight(), null);
        return bitmap;
    }

    private static Bitmap addLogo(Bitmap srcBitmap, Bitmap logoBitmap, float logoPercent) {
        if (srcBitmap == null || logoBitmap == null) return null;
        if (logoPercent < 0f || logoPercent > 1f) {
            logoPercent = 0.2f;
        }

        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int logoWidth = logoBitmap.getWidth();
        int logoHeight = logoBitmap.getHeight();

        //计算画布缩放的宽高比
        float scaleWidth = srcWidth * logoPercent / logoWidth;
        float scaleHeight = srcHeight * logoPercent / logoHeight;

        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        canvas.scale(scaleWidth, scaleHeight, srcWidth / 2, srcHeight / 2);
        canvas.drawBitmap(logoBitmap, srcWidth / 2 - logoWidth / 2, srcHeight / 2 - logoHeight / 2, null);

        return bitmap;
    }
}
