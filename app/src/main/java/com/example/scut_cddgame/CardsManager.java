package com.example.scut_cddgame;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class CardsManager {
    public static Random rand = new Random();

    public static boolean inReat(int x, int y, int rectX, int rectY, int rectW, int rectH) {
        if (x <= rectX || x >= rectX + rectW || y <= rectY || y >= rectY + rectH)
            return false;
        else
            return true;
    }

    //洗牌
    public static void shuffleCards(int[] cards) {
        int len = cards.length;
        for (int i = 0; i < len; i++) {
            int des = rand.nextInt(52), tmp = cards[i];
            cards[i] = cards[des];
            cards[des] = tmp;
        }
    }

    //对牌进行排序
    public static void sortCards(int[] cards) {
        Arrays.sort(cards);
    }

    public static int getImageRow(int poke) {
        return poke / 4;
    }

    //返回的int对应黑桃、红心、梅花、方片
    public static int getImageCol(int poke) {
        return poke % 4;
    }

    //获取某张牌的大小
    public static int getCardNumber(int card) {
        return getImageRow(card) + 3;
    }

    //判断是否为顺子
    public static boolean isShun(int[] cards) {
        int curr = getCardNumber(cards[0]);
        //特判某些特殊顺子
        if (getCardNumber(cards[0]) == 15
                && getCardNumber(cards[1]) == 14
                && getCardNumber(cards[2]) == 5
                && getCardNumber(cards[3]) == 4
                && getCardNumber(cards[4]) == 3)
            return true;
        if (getCardNumber(cards[0]) == 15
                && getCardNumber(cards[1]) == 6
                && getCardNumber(cards[2]) == 5
                && getCardNumber(cards[3]) == 4
                && getCardNumber(cards[4]) == 3)
            return true;
        for (int i = 1; i < cards.length; i++) {
            int next = getCardNumber(cards[i]);
            if (curr - next != 1)
                return false;
            curr = next;
        }
        return true;
    }

    //判断是否为不同花的顺子
    public static boolean isZaShun(int[] cards) {
        if (!isShun(cards))
            return false;
        for (int i = 0; i < cards.length - 1; i++) {
            if (getImageCol(cards[i]) == getImageCol(cards[i + 1]))
                return false;
        }
        return true;
    }

    //判断是否为同花顺
    public static boolean isTongHuaShun(int[] cards) {
        if (!isShun(cards))
            return false;
        for (int i = 0; i < cards.length - 1; i++) {
            if (getImageCol(cards[i]) != getImageCol(cards[i + 1]))
                return false;
        }
        return true;
    }

    //判断是否为同花
    public static boolean isTongHua(int[] cards) {
        for (int i = 0; i < cards.length - 1; i++) {
            if (getImageCol(cards[i]) != getImageCol(cards[i + 1]))
                return false;
        }
        return true;
    }

    //判断传进来的牌属于哪一个牌型
    public static int getType(int[] cards) {
        int len = cards.length;
        if (len == 1)
            return CardsType.DanZhang;
        if (len == 2)
            if (getCardNumber(cards[0]) == getCardNumber(cards[1]))
                return CardsType.Dui;
        if (len == 3)
            if (getCardNumber(cards[0]) == getCardNumber(cards[2]))
                return CardsType.SanGe;
        if (len == 5) {
            if (isTongHuaShun(cards))
                return CardsType.TongHuaShun;
            if (isZaShun(cards))
                return CardsType.ZaShun;
            if ((getCardNumber(cards[0]) == getCardNumber(cards[1])
                    && getCardNumber(cards[2]) == getCardNumber(cards[4]))
                    || (getCardNumber(cards[0]) == getCardNumber(cards[2])
                    && getCardNumber(cards[3]) == getCardNumber(cards[4])))
                return CardsType.HuLu;
            if (getCardNumber(cards[0]) == getCardNumber(cards[3])
                    || getCardNumber(cards[1]) == getCardNumber(cards[4]))
                return CardsType.JinGang;
        }
        return CardsType.error;
    }

    //返回的int包含花色和数值
    public static int getValue(int[] cards) {
        int type = getType(cards);
        if (type == CardsType.DanZhang || type == CardsType.Dui || type == CardsType.SanGe)
            return cards[0];
        //特判2或A开头做顺子的情况
        if (type == CardsType.ZaShun || type == CardsType.TongHuaShun) {
            if (getCardNumber(cards[0]) == 15) {
                if (getCardNumber(cards[1]) == 14)
                    return cards[2];
                else
                    return cards[1];
            } else
                return cards[0];
        }
        if (type == CardsType.TongHua)
            return cards[0];
        if (type == CardsType.SanGe) {
            if (getCardNumber(cards[0]) == getCardNumber(cards[2]))
                return cards[2];
            else
                return cards[4];
        }
        if (type == CardsType.JinGang) {
            if (getCardNumber(cards[0]) == getCardNumber(cards[3]))
                return cards[3];
            else
                return cards[4];
        }
        return 0;
    }

    //判断牌型是否合法
    public static boolean isLegel(int[] cards) {
        if (getType(cards) != CardsType.error)
            return true;
        return false;
    }

    //判断哪手牌大
    public static int compare(CardsHolder f, CardsHolder s) {
        if (f.cards.length != s.cards.length)
            return -1;
        if (getCardNumber(f.value) > getCardNumber(s.value))
            return 1;
        if (getCardNumber(f.value) == getCardNumber(s.value)) {
            if (getImageCol(f.value) < getImageCol(s.value))
                return 1;
            if (getImageCol(f.value) > getImageCol(s.value))
                return 0;
        }
        if (getCardNumber(f.value) < getCardNumber(s.value))
            return 0;
        return -1;
    }

    public static int[] outCardByItself(int cards[], Player last, Player next) {
        CardsAnalyzer analyzer = CardsAnalyzer.getInstance();
        analyzer.setPokes(cards);
        Vector<int[]> cardDanZhang = analyzer.getCardDanZhang();
        if (cardDanZhang.size() > 0)
            return cardDanZhang.elementAt(0);
        return new int[]{cards[0]};
    }

    //智能出牌
    public static int[] findTheRightCard(CardsHolder card,int cards[],Player last,Player next){
        CardsAnalyzer cardsAnalyzer=CardsAnalyzer.getInstance();
        cardsAnalyzer.setPokes(cards);
        return findBigerCards(card,cards);
    }

    public static int[] findBigerCards(CardsHolder card, int cards[]) {
        //获取card的信息，牌值，牌型
        int[] cardPokes = card.cards;
        //cardValue包含值和花色
        int cardValue = card.value, cardType = card.cardsType, cardLength = cardPokes.length, size = 0;
        CardsAnalyzer analyzer = CardsAnalyzer.getInstance();
        analyzer.setPokes(cards);
        Vector<int[]> tmp;
        switch (cardType) {
            case CardsType.DanZhang:
                tmp = analyzer.getCardDanZhang();
                size = tmp.size();
                for (int i = 0; i < size; i++) {
                    int[] cardArray = tmp.get(i);
                    int v = CardsManager.getCardNumber(cardArray[0]), h = CardsManager.getImageCol(cardArray[0]);
                    if (v > getCardNumber(cardValue))
                        return cardArray;
                    if (v == getCardNumber(cardValue) && h < getImageCol(cardValue))
                        return cardArray;
                }
                break;
            case CardsType.Dui:
                tmp = analyzer.getCardDui();
                size = tmp.size();
                for (int i = 0; i < size; i++) {
                    int[] cardArray = tmp.get(i);
                    int v = CardsManager.getCardNumber(cardArray[1]), h = CardsManager.getImageCol(cardArray[1]);
                    if (v > getCardNumber(cardValue))
                        return cardArray;
                    if (v == getCardNumber(cardValue) && h < getImageCol(cardValue))
                        return cardArray;
                }
                break;
            case CardsType.SanGe:
                tmp = analyzer.getCardSange();
                size = tmp.size();
                for (int i = 0; i < size; i++) {
                    int[] cardArray = tmp.get(i);
                    int v = CardsManager.getCardNumber(cardArray[0]), h = CardsManager.getImageCol(cardArray[2]);
                    if (v > getCardNumber(cardValue))
                        return cardArray;
                    if (v == getCardNumber(cardValue) && h < getImageCol(cardValue))
                        return cardArray;
                }
                break;
            case CardsType.ZaShun:
            case CardsType.TongHuaShun:
            case CardsType.TongHua:
                tmp = analyzer.getCardZaShun();
                size = tmp.size();
                for (int i = 0; i < size; i++) {
                    int[] cardArray = tmp.get(i);
                    int v = CardsManager.getCardNumber(CardsManager.getValue(cardArray)), h = CardsManager.getImageCol(CardsManager.getValue(cardArray));
                    if (v > getCardNumber(cardValue))
                        return cardArray;
                    if (v == getCardNumber(cardValue) && h < getImageCol(cardValue))
                        return cardArray;
                }
                break;
            case CardsType.JinGang:
                if (cards.length < 5) break;
                tmp = analyzer.getCardJinGang();
                size = tmp.size();
                for (int i = 0; i < size; i++) {
                    int[] cardArray = tmp.get(i);
                    int v = CardsManager.getCardNumber(cardArray[3]), h = CardsManager.getImageCol(cardArray[3]);
                    if (v > getCardNumber(cardValue)) {
                        int[] jinGang = new int[5];
                        for (int j = 0; j < cardArray.length; j++)
                            jinGang[j] = cardArray[j];
                        tmp = analyzer.getCardDanZhang();
                        size = tmp.size();
                        if (size > 0) {
                            int[] t = tmp.get(0);
                            jinGang[4] = t[0];
                            return jinGang;
                        }
                    }
                    if (v == getCardNumber(cardValue) && h < getImageCol(cardValue)) {
                        int[] jinGang = new int[5];
                        for (int j = 0; j < cardArray.length; j++)
                            jinGang[j] = cardArray[j];
                        tmp = analyzer.getCardDanZhang();
                        size = tmp.size();
                        if (size > 0) {
                            int[] t = tmp.get(0);
                            jinGang[4] = t[0];
                            return jinGang;
                        }
                    }

                }
                break;
            case CardsType.HuLu:
                if (cards.length < 5) break;
                tmp = analyzer.getCardHuLu();
                size = tmp.size();
                for (int i = 0; i < size; i++) {
                    int[] cardArray = tmp.get(i);
                    int v = CardsManager.getCardNumber(cardArray[2]);
                    int h = CardsManager.getImageCol(cardArray[2]);
                    if (v > getCardNumber(cardValue)) {
                        int[] huLu = new int[5];
                        for (int j = 0; j < cardArray.length; j++)
                            huLu[j] = cardArray[j];
                        tmp = analyzer.getCardHuLu();
                        size = tmp.size();
                        if (size > 0) {
                            int[] t = tmp.get(0);
                            huLu[4] = t[0];
                            return huLu;
                        }
                    }
                    if (v == getCardNumber(cardValue) && h < getImageCol(cardValue)) {
                        int[] huLu = new int[5];
                        for (int j = 0; j < cardArray.length; j++)
                            huLu[j] = cardArray[j];
                        tmp = analyzer.getCardDanZhang();
                        size = tmp.size();
                        if (size > 0) {
                            int[] t = tmp.get(0);
                            huLu[4] = t[0];
                            return huLu;
                        }
                    }
                }
                break;
        }
        return cardPokes;
    }
}
