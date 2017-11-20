package com.company;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NearestNeighbor implements Algorithm {
    private ArrayList<Point> tempPoints;
    private Point startPoint;
    private double endProfit;

    public ArrayList<Point> getPoints() {
        return tempPoints;
    }

    public double getEndProfit() {
        return endProfit;
    }

    public NearestNeighbor() {
        this.tempPoints = new ArrayList<>();
        this.endProfit = 0.0;
    }

    @Override
    public void execute(Point startPoint, Map<Integer, Point> allPoints) {
        this.startPoint = startPoint;
        Map<Integer, Point> allPointsTemp = new HashMap<>(allPoints);
        Point latestPoint = startPoint;
        Point nearestPoint;
        this.tempPoints.add(0, latestPoint);

        allPointsTemp.remove(startPoint.getIndex());
        ArrayList<Point> arrList = new ArrayList<>();
        arrList.addAll(allPointsTemp.values());

        while (!arrList.isEmpty()) {
            Pair<Integer, Point> nearestPointPair = this.findNearest(latestPoint, arrList);
            nearestPoint = nearestPointPair.getValue();
            arrList.remove((int)nearestPointPair.getKey());

            this.tempPoints.add(nearestPoint);

            this.countProfit(nearestPoint.getProfit(), this.getDistance(latestPoint, nearestPoint));
            latestPoint = nearestPoint;
        }

        this.tempPoints.add(startPoint);
        this.countProfit(startPoint.getProfit(), this.getDistance(latestPoint, startPoint));
    }

    private Pair<Integer, Point> findNearest(Point point, ArrayList<Point> allPoints) {
        Point nearestPoint = point;
        double min = 1000000000;
        int index = 0;
        int endIndex = 0;

        for(Point secondPoint : allPoints)
        {
            if(this.getDistance(point, secondPoint) < min) {
                min = this.getDistance(point, secondPoint);
                nearestPoint = secondPoint;
                endIndex = index;
            }
            index++;
        }

        return new Pair<Integer, Point>(endIndex, nearestPoint);
    }

    private void countProfit(Integer profit, double wayLoss) {
        Integer LOSS_CONST = 5;

        this.endProfit += profit - wayLoss * LOSS_CONST;
    }

    private double getDistance(Point pointOne, Point pointTwo) {
        return Math.sqrt((pointOne.getX() - pointTwo.getX()) * (pointOne.getX() - pointTwo.getX()) + (pointOne.getY() - pointTwo.getY()) *  (pointOne.getY() - pointTwo.getY()));
    }

    @Override
    public ArrayList<Point> getResultList() {
        return this.tempPoints;
    }

    @Override
    public double getProfit() {
        return this.endProfit;
    }

    @Override
    public Point getStartPoint() {
        return this.startPoint;
    }
}
