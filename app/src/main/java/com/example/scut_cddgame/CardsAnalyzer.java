package com.example.scut_cddgame;

import java.util.Vector;

public class CardsAnalyzer {
    private int[] cards;
    private int[] countCards = new int[12];
    private int count2;
    private Vector<int[]> cardDanZhang = new Vector<int[]>(4);
    private Vector<int[]> cardDui = new Vector<int[]>(4);
    private Vector<int[]> cardSange = new Vector<int[]>(4);
    private Vector<int[]> cardZaShun = new Vector<int[]>(4);
    private Vector<int[]> cardTongHuaShun = new Vector<int[]>(4);
    private Vector<int[]> cardTongHua = new Vector<int[]>(4);
    private Vector<int[]> cardJinGang = new Vector<int[]>(4);
    private Vector<int[]> cardHuLu = new Vector<int[]>(4);

    private CardsAnalyzer(){}

    public int[] getCountPokes() {
        return countCards;
    }

    public int getCount2() {
        return count2;
    }

    public Vector<int[]> getCardDanZhang() {
        return cardDanZhang;
    }

    public Vector<int[]> getCardDui() {
        return cardDui;
    }

    public Vector<int[]> getCardSange() {
        return cardSange;
    }

    public Vector<int[]> getCardZaShun() {
        return cardZaShun;
    }

    public Vector<int[]> getCardTongHuaShun() {
        return cardTongHuaShun;
    }

    public Vector<int[]> getCardTongHua() {
        return cardTongHua;
    }

    public Vector<int[]> getCardJinGang() {
        return cardJinGang;
    }

    public Vector<int[]> getCardHuLu() {
        return cardHuLu;
    }

    public static CardsAnalyzer getInstance(){
        return new CardsAnalyzer();
    }

    private void init(){
        for (int i=0;i<countCards.length;i++) countCards[i]=0;
        count2=0;
        cardDanZhang.clear();
        cardDui.clear();
        cardSange.clear();
        cardZaShun.clear();
        cardTongHuaShun.clear();
        cardTongHua.clear();
        cardJinGang.clear();
        cardHuLu.clear();
    }

    public int remainCount(){
        return cardDanZhang.size()+cardDui.size()+cardSange.size()
                +cardZaShun.size()+cardTongHuaShun.size()+cardTongHua.size()
                +cardJinGang.size()+cardHuLu.size();
    }

    public boolean lastCardTypeEq(int pokeType){
        if (remainCount()>1) return false;
        switch (pokeType){
            case CardsType.SanGe:
                return cardSange.size()==1;
            case CardsType.Dui:
                return cardDui.size()==1;
            case CardsType.DanZhang:
                return cardDanZhang.size()==1;
        }
        return false;
    }

    public int[] getPokes(){
        return cards;
    }

    public void setPokes(int[] pokes){
        CardsManager.sortCards(pokes);
        this.cards=pokes;
        try {
            this.analyze(); //这是什么神仙函数
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int[] getMinType(Player last,Player next){
        CardsAnalyzer lastAna=CardsAnalyzer.getInstance();
        lastAna.setPokes(last.cards);
        CardsAnalyzer nextAna=CardsAnalyzer.getInstance();
        nextAna.setPokes(next.cards);
        int needSmart=-1;
        if (Desk.boss==next.playerID || (Desk.boss!=next.playerID && Desk.boss != last.playerID)){
            //这里的功能估计没做完
        }
        return null; //随便加的
    }

    private void analyze(){} //随便加的
}
