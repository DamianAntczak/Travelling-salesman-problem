package com.company;

public class Point {
    private int x;
    private int y;
    private int profit;

    public Point(int x, int y, int profit) {
        this.x = x;
        this.y = y;
        this.profit = profit * 5;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit * 5;
    }
}
