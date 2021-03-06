package com.example.scut_cddgame;

import android.annotation.SuppressLint;
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

import javax.crypto.spec.DESKeySpec;

public class Player {
    int[] cards; //玩家手里的牌
    boolean[] cardsFlag; //玩家选中牌的标识
    int playerID; //玩家ID
    int currentID; //当前玩家
    int currentRound; //当前轮次
    int top, left; //玩家在桌面上的坐标
    Desk desk;
    CardsHolder latestCards; //玩家最新的一手牌
    CardsHolder cardsOnDesktop; //桌上最新的一手牌
    Context context;
    int paintDirection = CardsType.directionVertical;
    Bitmap cardImage;
    private Player last, next;

    public Player(int[] cards, int left, int top, int paintDir, int id, Desk desk, Context context) {
        this.cards = cards;
        this.left = left;
        this.top = top;
        this.paintDirection = paintDir;
        this.playerID = id;
        this.desk = desk;
        this.context = context;
        cardsFlag = new boolean[cards.length];
    }

    public void setLeftAndTop(int left, int top) {
        this.left = left;
        this.top = top;
    }

    // 设置上下家关系
    public void setLastAndNext(Player last, Player next) {
        this.last = last;
        this.next = next;
    }

    //绘制玩家手中的牌
    public void paint(Canvas canvas) {
        Rect src = new Rect(), des = new Rect();
        //电脑的牌全是背面
        if (playerID == 1 || playerID == 2 || playerID == 3) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(1);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            Bitmap backImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.card_bg);
            src.set(0, 0, backImage.getWidth(), backImage.getHeight());
            des.set((int) (left * MainActivity.SCALE_HORIAONTAL),
                    (int) (top * MainActivity.SCALE_VERTICAL),
                    (int) ((left + 40) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((top + 60) * MainActivity.SCALE_VERTICAL));
            RectF rectF = new RectF(des);
            canvas.drawRoundRect(rectF, 5, 5, paint);
            canvas.drawBitmap(backImage, src, des, paint);
            //显示剩余牌数量
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            paint.setTextSize((int) (20 * MainActivity.SCALE_HORIAONTAL));
            canvas.drawText("" + cards.length, (int) (left * MainActivity.SCALE_HORIAONTAL), (int) ((top + 80) * MainActivity.SCALE_VERTICAL), paint);
        } else { //绘制玩家手里的牌
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(1);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            for (int i = 0; i < cards.length; i++) {
                int row = CardsManager.getImageRow(cards[i]), col = CardsManager.getImageCol(cards[i]);
                cardImage = BitmapFactory.decodeResource(context.getResources(), CardImage.cardImages[row][col]);
                int select = cardsFlag[i] ? 10 : 0;
                src.set(0, 0, cardImage.getWidth(), cardImage.getHeight());
                des.set((int) ((left + i * 20) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((top - select) * MainActivity.SCALE_VERTICAL),
                        (int) ((left + 40 + i * 20) * MainActivity.SCALE_HORIAONTAL), (int) ((top - select + 60) * MainActivity.SCALE_VERTICAL));
                RectF rectF = new RectF(des);
                canvas.drawRoundRect(rectF, 5, 5, paint);
                canvas.drawBitmap(cardImage, src, des, paint);
            }
        }
    }

    //游戏结束时绘制所有玩家手里的牌
    public void paintResultCards(Canvas canvas) {
        Rect src = new Rect(), des = new Rect();
        for (int i = 0; i < cards.length; i++) {
            int row = CardsManager.getImageRow(cards[i]), col = CardsManager.getImageCol(cards[i]);
            cardImage = BitmapFactory.decodeResource(context.getResources(),
                    CardImage.cardImages[row][col]);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(1);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            // 当玩家是1、2时，竖向绘制
            if (playerID == 1 || playerID == 2) {
                src.set(0, 0, cardImage.getWidth(), cardImage.getHeight());
                des.set((int) (left * MainActivity.SCALE_HORIAONTAL),
                        (int) ((top - 40 + i * 15) * MainActivity.SCALE_VERTICAL),
                        (int) ((left + 40) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((top + 20 + i * 15) * MainActivity.SCALE_VERTICAL));
                RectF rectF = new RectF(des);
                canvas.drawRoundRect(rectF, 5, 5, paint);
                canvas.drawBitmap(cardImage, src, des, paint);

            } else {
                src.set(0, 0, cardImage.getWidth(), cardImage.getHeight());
                des.set((int) ((left + 40 + i * 20) * MainActivity.SCALE_HORIAONTAL),
                        (int) (top * MainActivity.SCALE_VERTICAL),
                        (int) ((left + 80 + i * 20) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((top + 60) * MainActivity.SCALE_VERTICAL));
                RectF rectF = new RectF(des);
                canvas.drawRoundRect(rectF, 5, 5, paint);
                canvas.drawBitmap(cardImage, src, des, paint);
            }
        }
    }

    //电脑出牌AI
    public CardsHolder chupaiAI(CardsHolder card) {
        int[] pokeWanted = null;
        if (card == null)  //随意出牌
            pokeWanted = CardsManager.outCardByItself(cards, last, next);
        else
            pokeWanted = CardsManager.findTheRightCard(card, cards, last, next);
        if (pokeWanted == null)
            return null;
        //出牌后把牌从玩家手里剔除
        for (int i = 0; i < pokeWanted.length; i++) {
            for (int j = 0; j < cards.length; j++)
                if (cards[j] == pokeWanted[i]) {
                    cards[j] = -1;
                    break;
                }
        }
        int[] newpokes = new int[0];
        if (cards.length - pokeWanted.length > 0)
            newpokes = new int[cards.length - pokeWanted.length];
        int j = 0;
        for (int i = 0; i < cards.length; i++)
            if (cards[i] != -1)
                newpokes[j++] = cards[i];
        this.cards = newpokes;
        CardsHolder thisCard = new CardsHolder(pokeWanted, playerID, context);
        Desk.cardsOnDesktop = thisCard;
        this.latestCards = thisCard;
        return thisCard;
    }

    //玩家出牌
    @SuppressLint("ShowToast")
    public CardsHolder chuPai(CardsHolder card) {
        int cnt = 0;
        for (int i = 0; i < cards.length; i++)
            if (cardsFlag[i]) cnt++;
        int[] chupaiPokes = new int[cnt];
        int j = 0;
        for (int i = 0; i < cards.length; i++)
            if (cardsFlag[i])
                chupaiPokes[j++] = cards[i];
        int cardType = CardsManager.getType(chupaiPokes);
        if (cardType == CardsType.error) {
            if (chupaiPokes.length != 0)
                MainActivity.handler.sendEmptyMessage(MainActivity.WRONG_CARD);
            else MainActivity.handler.sendEmptyMessage(MainActivity.EMPTY_CARD);
            return null;
        }
        CardsHolder newLatestCardsHolder = new CardsHolder(chupaiPokes, playerID, context);
        if (card == null) {
            Desk.cardsOnDesktop = newLatestCardsHolder;
            this.latestCards = newLatestCardsHolder;
            int[] newPokes = new int[cards.length - cnt];
            int k = 0;
            for (int i = 0; i < cards.length; i++)
                if (!cardsFlag[i])
                    newPokes[k++] = cards[i];
            this.cards = newPokes;
            this.cardsFlag = new boolean[cards.length];
        } else {
            if (CardsManager.compare(newLatestCardsHolder, card) == 1) {
                Desk.cardsOnDesktop = newLatestCardsHolder;
                this.latestCards = newLatestCardsHolder;
                int[] newPokes = new int[cards.length - cnt];
                for (int i = 0; i < cards.length; i++)
                    if (!cardsFlag[i])
                        newPokes[j++] = cards[i];
                this.cards = newPokes;
                this.cardsFlag = new boolean[cards.length];
            }
            if (CardsManager.compare(newLatestCardsHolder, card) == 0) {
                MainActivity.handler.sendEmptyMessage(MainActivity.SMALL_CARD);
                return null;
            }
            if (CardsManager.compare(newLatestCardsHolder, card) == -1) {
                MainActivity.handler.sendEmptyMessage(MainActivity.WRONG_CARD);
                return null;
            }
        }
        return newLatestCardsHolder;
    }

    //当玩家操作时，触摸屏事件处理
    public void onTouch(int x,int y){
        for(int i=0;i<cards.length;i++){
            if (i != cards.length - 1) {
                if (CardsManager.inRect(x, y,
                        (int) ((left + i * 20) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((top - (cardsFlag[i] ? 10 : 0)) * MainActivity.SCALE_VERTICAL),
                        (int) (20 * MainActivity.SCALE_HORIAONTAL),
                        (int) (60 * MainActivity.SCALE_VERTICAL))) {
                    cardsFlag[i] = !cardsFlag[i];
                    break;
                }
            }else{
                if (CardsManager.inRect(x, y,
                        (int) ((left + i * 20) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((top - (cardsFlag[i] ? 10 : 0)) * MainActivity.SCALE_VERTICAL),
                        (int) (40 * MainActivity.SCALE_HORIAONTAL),
                        (int) (60 * MainActivity.SCALE_VERTICAL))) {
                    cardsFlag[i] = !cardsFlag[i];
                    break;
                }
            }
        }
    }

    //unfinished
    public void redo(){
        for (int i=0;i<cardsFlag.length;i++)
            cardsFlag[i]=false;
    }
}
