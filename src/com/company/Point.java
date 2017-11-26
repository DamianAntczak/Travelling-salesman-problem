package com.company;

public class Point {
    private int index;
    private int x;
    private int y;
    private int profit;


    public Point(int index, int x, int y) {
        this.index = index;
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
        this.profit = profit;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getDistance(Point point){
        return Math.sqrt((this.getX() - point.getX()) * (this.getX() - point.getX()) + (this.getY() - point.getY()) *  (this.getY() - point.getY()));
    }
}
