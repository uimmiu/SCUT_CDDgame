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

public class Desk {
    public static int winID = -1; //胜者id
    Bitmap cardImg, redoImage, passImage, chupaiImage, tishiImage, farmerImage, landlordImage;
    Context context;
    private int[] scores = new int[4];
    private int[][] timeLimitPosition = {{130, 190}, {80, 80}, {360, 80}, {150, 80}}; //剩余时间View的位置
    private int[][] passPosition = {{130, 190}, {80, 80}, {360, 80}, {150, 80}};
    private int[][] playerLatestCardsPosition = {{130, 140}, {80, 60}, {360, 60}, {250, 60}};
    private int[][] playerCardsPosition = {{30, 210}, {30, 60}, {410, 60}, {200, 60}};
    private int[][] scorePosition = {{70, 290}, {70, 30}, {340, 30}, {240, 30}};
    private int[][] iconPosition = {{30, 270}, {30, 10}, {410, 10}, {200, 10}};
    private int buttonPositionX = 240, buttonPositionY = 160;
    private boolean[] canPass = new boolean[4];
    private int[][] playerCards = new int[4][13];
    private boolean canDrawLatestCards = false;
    private int[] allCards = new int[52]; //一副扑克牌
    private int currentScore = 10; //当前分数
    private int currentID = 0; //当前操作的人的id
    private int currentRound = 0; //本次游戏轮数
    public static CardsHolder cardsOnDesktop = null; //最新的一手牌
    private int timeLimit = 300;
    private int result[] = new int[4]; //胜负得分信息
    private int op = -1; //游戏进度，-1重新开始,0游戏中，1本局游戏结束
    public static Player[] players = new Player[4];
    public static int multiple = 1; //当前倍数
    public static int boss = 0; //第一个出牌的人
    public boolean ifClickChuPai = false;

    public Desk(Context context) {
        this.context = context;
        redoImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_redo);
//        passImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_pass);
        chupaiImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_chupai);
//        tishiImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_tishi);
//        farmerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_farmer);
//        landlordImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_landlord);
    }

    public void gameLogic() {
        switch (op) {
            case -1:
                init();
                op = 0;
                break;
            case 0:
                checkGameOver();
                break;
            case 1:
                break;
        }
    }

    public void controlPaint(Canvas canvas) {
        switch (op) {
            case -1:
                break;
            case 0:
                paintGaming(canvas);
                break;
            case 1:
                paintResult(canvas);
                break;
        }
    }

    private void checkGameOver() {
        //先判断游戏是否结束
        for (int k = 0; k < 4; k++) {
            if (players[k].cards.length == 0) {
                op = 1;
                winID = k;
                for (int i = 0; i < 4; i++) {
                    if (i == k) {
                        result[i] = currentScore * multiple;
                        scores[i] += result[i];
                    } else {
                        result[i] = -currentScore * multiple;
                        scores[i] += result[i];
                    }
                }
            }
            return;
        }
        //轮到电脑出牌
        if (currentID == 1 || currentID == 2 || currentID == 3) {
            if (timeLimit <= 300 && timeLimit >= 0) {
                CardsHolder tmpcard = players[currentID].chupaiAI(cardsOnDesktop);
                if (tmpcard != null) {
                    cardsOnDesktop = tmpcard;
                    nextPerson();
                } else
                    buyao();
            }
        }
        if (currentID == 0) {
            if (timeLimit <= 300 && timeLimit >= 0) {
                if (ifClickChuPai == true) {
                    CardsHolder card = players[0].chuPai(cardsOnDesktop);
                    if (card != null) {
                        cardsOnDesktop = card;
                        nextPerson();
                    }
                    ifClickChuPai = false;
                }
            } else {
                if (currentRound != 0)
                    buyao();
                else {
                    CardsHolder autoCard = players[currentID].chupaiAI(cardsOnDesktop);
                    cardsOnDesktop = autoCard;
                    nextPerson();
                }
            }
        }
        timeLimit -= 2;
        canDrawLatestCards = true;
    }

    //初始化游戏
    public void init() {
        allCards = new int[52];
        playerCards = new int[4][13];
        winID = -1;
        currentScore = 3;
        multiple = 1;
        cardsOnDesktop = null;
        currentRound = 0;
        currentID = 0;
        //每个人的初始设置
        for (int i = 0; i < 4; i++) {
            scores[i] = 50;
            canPass[i] = false;
        }
        for (int i = 0; i < allCards.length; i++)
            allCards[i] = i;
        CardsManager.shuffleCards(allCards);
        fapai(allCards); //选定第一个发牌人也在这里做了
        CardsManager.sortCards(playerCards[0]);
        CardsManager.sortCards(playerCards[1]);
        CardsManager.sortCards(playerCards[2]);
        CardsManager.sortCards(playerCards[3]);
        players[0] = new Player(playerCards[0], playerCardsPosition[0][0], playerCardsPosition[0][1], CardsType.directionHorizontal, 0, this, context);
        players[1] = new Player(playerCards[1], playerCardsPosition[1][0], playerCardsPosition[1][1], CardsType.directionVertical, 1, this, context);
        players[2] = new Player(playerCards[2], playerCardsPosition[2][0], playerCardsPosition[2][1], CardsType.directionVertical, 2, this, context);
        players[3] = new Player(playerCards[3], playerCardsPosition[3][0], playerCardsPosition[3][1], CardsType.directionHorizontal, 3, this, context);
        players[0].setLastAndNext(players[1], players[3]);
        players[1].setLastAndNext(players[2], players[0]);
        players[2].setLastAndNext(players[3], players[1]);
        players[3].setLastAndNext(players[0], players[2]);
    }

    public void fapai(int[] cards) {
        for (int i = 0; i < 52; i++) {
            playerCards[i / 13][i % 13] = cards[i];
            //如果是方片三，确定第一个发牌人
        }
    }

    private void buyao() {
        //清空当前不要牌的人的最后一手牌
        players[currentID].latestCards = null;
        canPass[currentID] = true;
        nextPerson();
        if (cardsOnDesktop != null && currentID == cardsOnDesktop.playerId) {
            currentRound = 0;
            cardsOnDesktop = null;
            players[currentID].latestCards = null;
        }
    }

    private void nextPerson() {
        currentID = currentID == 3 ? 0 : currentID + 1;
        currentRound++;
        timeLimit=300;
    }

    private void paintGaming(Canvas canvas){
        players[0].paint(canvas);
        players[1].paint(canvas);
        players[2].paint(canvas);
        players[3].paint(canvas);
        paintIconAndScore(canvas);
        paintTimeLimit(canvas);

        //轮到玩家时画操作按钮
        if (currentID==0){
            Rect src = new Rect();
            Rect dst = new Rect();

            src.set(0, 0, chupaiImage.getWidth(), chupaiImage.getHeight());
            dst.set((int) (buttonPositionX * MainActivity.SCALE_HORIAONTAL),
                    (int) (buttonPositionY * MainActivity.SCALE_VERTICAL),
                    (int) ((buttonPositionX + 80) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((buttonPositionY + 40) * MainActivity.SCALE_VERTICAL));
            canvas.drawBitmap(chupaiImage, src, dst, null);

            if(currentRound != 0) {
                src.set(0, 0, passImage.getWidth(), passImage.getHeight());
                dst.set((int) ((buttonPositionX - 80) * MainActivity.SCALE_HORIAONTAL),
                        (int) (buttonPositionY * MainActivity.SCALE_VERTICAL),
                        (int) ((buttonPositionX) * MainActivity.SCALE_HORIAONTAL),
                        (int) ((buttonPositionY + 40) * MainActivity.SCALE_VERTICAL));
                canvas.drawBitmap(passImage, src, dst, null);
            }

            src.set(0, 0, redoImage.getWidth(), redoImage.getHeight());
            dst.set((int) ((buttonPositionX + 80) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((buttonPositionY) * MainActivity.SCALE_VERTICAL),
                    (int) ((buttonPositionX + 160) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((buttonPositionY + 40) * MainActivity.SCALE_VERTICAL));
            canvas.drawBitmap(redoImage, src, dst, null);

            src.set(0, 0, tishiImage.getWidth(), tishiImage.getHeight());
            dst.set((int) ((buttonPositionX + 160) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((buttonPositionY) * MainActivity.SCALE_VERTICAL),
                    (int) ((buttonPositionX + 240) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((buttonPositionY + 40) * MainActivity.SCALE_VERTICAL));
            canvas.drawBitmap(tishiImage, src, dst, null);
        }

        //画各自刚出的牌或“不要”
        for (int i=0;i<4;i++){
            if (currentID != i && players[i].latestCards != null && canDrawLatestCards == true) {
                players[i].latestCards.paint(canvas, playerLatestCardsPosition[i][0],
                        playerLatestCardsPosition[i][1], players[i].paintDirection);
            }
            if (currentID != i && players[i].latestCards == null && canPass[i] == true) {
                paintPass(canvas, i);
            }
        }
    }

    //画倒计时
    private void paintTimeLimit(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize((int) (16 * MainActivity.SCALE_HORIAONTAL));
        for (int i = 0; i < 3; i++) {
            if (i == currentID) {
                canvas.drawText("" + (timeLimit / 10),
                        (int) (timeLimitPosition[i][0] * MainActivity.SCALE_HORIAONTAL),
                        (int) (timeLimitPosition[i][1] * MainActivity.SCALE_VERTICAL), paint);
            }
        }
    }

    private void paintPass(Canvas canvas,int id){
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize((int) (16 * MainActivity.SCALE_HORIAONTAL));
        canvas.drawText("不要", (int) (passPosition[id][0] * MainActivity.SCALE_HORIAONTAL),
                (int) (passPosition[id][1] * MainActivity.SCALE_VERTICAL), paint);
    }

    private void paintIconAndScore(Canvas canvas){
        Paint paint = new Paint();
        paint.setTextSize((int) (16 * MainActivity.SCALE_VERTICAL));
        Rect src = new Rect();
        Rect dst = new Rect();
        for (int i = 0; i < 4; i++) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(1);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            src.set(0, 0, landlordImage.getWidth(), landlordImage.getHeight());
            dst.set((int) (iconPosition[i][0] * MainActivity.SCALE_HORIAONTAL),
                    (int) (iconPosition[i][1] * MainActivity.SCALE_VERTICAL),
                    (int) ((iconPosition[i][0] + 40) * MainActivity.SCALE_HORIAONTAL),
                    (int) ((iconPosition[i][1] + 40) * MainActivity.SCALE_VERTICAL));
            RectF rectF = new RectF(dst);
            canvas.drawRoundRect(rectF, 5, 5, paint);
            canvas.drawBitmap(landlordImage, src, dst, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawText("玩家" + i,
                    (int) (scorePosition[i][0] * MainActivity.SCALE_HORIAONTAL),
                    (int) (scorePosition[i][1] * MainActivity.SCALE_VERTICAL), paint);
            canvas.drawText("得分：" + scores[i],
                    (int) (scorePosition[i][0] * MainActivity.SCALE_HORIAONTAL),
                    (int) ((scorePosition[i][1] + 20) * MainActivity.SCALE_VERTICAL), paint);
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawText("当前底分：" + currentScore + "  当前倍数：" + multiple,
                (int) (150 * MainActivity.SCALE_HORIAONTAL),
                (int) (150 * MainActivity.SCALE_VERTICAL), paint);
    }

    private void paintResult(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize((int) (20 * MainActivity.SCALE_HORIAONTAL));
        for (int i = 0; i < 4; i++) {
            canvas.drawText("玩家" + i + ":本局得分:" + result[i] + "   总分：" + scores[i],
                    (int) (110 * MainActivity.SCALE_HORIAONTAL),
                    (int) ((96 + i * 30) * MainActivity.SCALE_VERTICAL), paint);
        }
        for (int i = 0; i < 4; i++) {
            players[i].paintResultCards(canvas);
        }
    }

    public void restart(){
        op=1;
    }

    public void onTouch(int x,int y){
        if (op == 1) {
            op = -1;
        }
        players[0].onTouch(x, y);
        if (currentID == 0) {

            if (CardsManager.inRect(x, y, (int) (buttonPositionX * MainActivity.SCALE_HORIAONTAL),
                    (int) (buttonPositionY * MainActivity.SCALE_VERTICAL),
                    (int) (80 * MainActivity.SCALE_HORIAONTAL),
                    (int) (40 * MainActivity.SCALE_VERTICAL))) {
                System.out.println("出牌");
                ifClickChuPai=true;

            }
            if (currentRound != 0) {
                if (CardsManager.inRect(x, y,
                        (int) ((buttonPositionX - 80) * MainActivity.SCALE_HORIAONTAL),
                        (int) (buttonPositionY * MainActivity.SCALE_VERTICAL),
                        (int) (80 * MainActivity.SCALE_HORIAONTAL),
                        (int) (40 * MainActivity.SCALE_VERTICAL))) {
                    System.out.println("不要");
                    buyao();
                }
            }
            if (CardsManager.inRect(x, y,
                    (int) ((buttonPositionX + 80) * MainActivity.SCALE_HORIAONTAL),
                    (int) (buttonPositionY * MainActivity.SCALE_VERTICAL),
                    (int) (80 * MainActivity.SCALE_HORIAONTAL),
                    (int) (40 * MainActivity.SCALE_VERTICAL))) {
                System.out.println("重选");
                players[0].redo();
            }
            if (CardsManager.inRect(x, y,
                    (int) ((buttonPositionX + 160) * MainActivity.SCALE_HORIAONTAL),
                    (int) (buttonPositionY * MainActivity.SCALE_VERTICAL),
                    (int) (80 * MainActivity.SCALE_HORIAONTAL),
                    (int) (40 * MainActivity.SCALE_VERTICAL))) {
                System.out.println("提示（重新）");
                restart();
            }
        }
    }
}
