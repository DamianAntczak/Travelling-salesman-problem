package com.company;

import java.util.*;

public class RandomPath implements Algorithm
{
    private ArrayList<Point> path;
    private double profit;
    private Point startPoint;
    private ArrayList<Point> restList;

    @Override
    public void execute(Point startPoint, Map<Integer, Point> allPoints) {
        this.path = new ArrayList<>(allPoints.size());
        TreeMap<Integer, Point> tempPoints = new TreeMap<>(allPoints);
        restList = new ArrayList<>(100);

        Random random = new Random();
        while (tempPoints.size() > 1){
            int index = random.nextInt(100);
            Point point = tempPoints.get(index);

            if(point != null){
                if(tempPoints.size() == 100){
                    this.startPoint = point;
                }
                path.add(point);
                tempPoints.remove(point.getIndex());
            }
        }
        path.add(tempPoints.firstEntry().getValue());
    }

    @Override
    public ArrayList<Point> getResultList() {
        return path;
    }

    @Override
    public double getProfit() {

        double profit = 0.0;
        for (int i = 0; i < this.path.size() - 1; i++){
            Point firstPoint = path.get(i);
            Point secoundPoint = path.get(i + 1);

            double length = firstPoint.getDistance(secoundPoint);
            profit += countProfit(secoundPoint.getProfit(), length);
        }

        return profit;
    }

    private double countProfit(Integer profit, double wayLoss) {
        Integer LOSS_CONST = 5;

        return profit - wayLoss * LOSS_CONST;
    }

    @Override
    public Point getStartPoint() {
        return startPoint;
    }

    @Override
    public ArrayList<Point> getRestList() {
        return this.restList;
    }

    @Override
    public void setProfit(double profit) {
        this.profit = profit;
    }

    @Override
    public void removePointFromCycle(int index){
        Point point = this.path.remove(index);
        this.restList.add(point);
    }

    @Override
    public void addPointToCycle(int index, Point point) {
        this.path.add(index, point);
        this.restList.remove(point);
    }

    @Override
    public void setCycle(ArrayList<Point> cycle) {
        this.path = cycle;
    }
}
