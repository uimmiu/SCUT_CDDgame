package com.example.scut_cddgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class CardsHolder {
    int value = 0, cardsType = 0, playerId;
    int[] cards;
    Bitmap cardImage;
    Context context;

    public CardsHolder(int[] cards, int id, Context context) {
        this.playerId = id;
        this.cards = cards;
        this.context = context;
        cardsType = CardsManager.getType(cards);
        value = CardsManager.getValue(cards);
    }

    public void paint(Canvas canvas, int left, int top, int dir) {
        Rect src = new Rect(), des = new Rect();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        for (int i = 0; i < cards.length; i++) {
            int row = CardsManager.getImageRow(cards[i]);
            int col = CardsManager.getImageCol(cards[i]);
            cardImage = BitmapFactory.decodeResource(context.getResources(), CardImage.cardImages[row][col]);
            if (dir == CardsType.directionVertical) {
                //下面两行代码貌似没用
                row = CardsManager.getImageRow(cards[i]);
                col = CardsManager.getImageCol(cards[i]);
                src.set(0, 0, cardImage.getWidth(), cardImage.getHeight());
                des.set((int) (left * MainActivity.SCALE_HORIAONTAL),
                        (int) ((i * 45 + top) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((left + 40) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((i * 45 + top + 60) * MainActivity.SCALE_VERTICAL));
            } else {
                row = CardsManager.getImageRow(cards[i]);
                col = CardsManager.getImageCol(cards[i]);
                src.set(0, 0, cardImage.getWidth(), cardImage.getHeight());
                des.set((int) ((left + i * 20) * MainActivity.SCALE_HORIAONTAL),
                        (int) (top * MainActivity.SCALE_VERTICAL),
                        (int) ((left + 40 + i * 20) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((top + 60) * MainActivity.SCALE_VERTICAL));
            }
            RectF rectF = new RectF(des);
            canvas.drawRoundRect(rectF, 5, 5, paint);
            canvas.drawBitmap(cardImage, src, des, paint);
        }
    }
}
