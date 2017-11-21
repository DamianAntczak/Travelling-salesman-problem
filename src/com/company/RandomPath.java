package com.company;

import java.util.*;

public class RandomPath implements Algorithm
{
    private ArrayList<Point> path;
    private double profit;
    private Point startPoint;

    @Override
    public void execute(Point startPoint, Map<Integer, Point> allPoints) {
        this.path = new ArrayList<>(allPoints.size());
        TreeMap<Integer, Point> tempPoints = new TreeMap<>(allPoints);

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
}
