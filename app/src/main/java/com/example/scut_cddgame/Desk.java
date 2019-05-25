package com.example.scut_cddgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

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
        passImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_pass);
        chupaiImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_chupai);
        tishiImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_tishi);
        farmerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_farmer);
        landlordImage = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.icon_landlord);
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
                    if (i==k){
                        result[i]=currentScore*multiple;
                        scores[i]+=result[i];
                    }else{
                        result[i]=-currentScore*multiple;
                        scores[i]+=result[i];
                    }
                }
            }
            return;
        }
        //轮到电脑出牌
        if (currentID==1 || currentID==2 || currentID==3){
            if (timeLimit<=300 && timeLimit>=0){
                // TODO: 19-5-25 继续写Player类，要实现电脑出牌AI
            }
        }
    }
}
