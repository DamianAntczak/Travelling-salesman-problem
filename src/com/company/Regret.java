package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Regret implements  Algorithm {

    private Point startPoint;
    private ArrayList<Point> path;
    private Double profit;

    @Override
    public void execute(Point startPoint, Map<Integer, Point> allPoints) {
        this.startPoint = startPoint;
        HashMap<Integer, Point> tempPoints = new HashMap<>(allPoints);
    }

    @Override
    public ArrayList<Point> getResultList() {
        return path;
    }

    @Override
    public double getProfit() {
        return profit;
    }

    @Override
    public Point getStartPoint() {
        return this.startPoint;
    }
}
