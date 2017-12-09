package com.company;

import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NearestNeighbor implements Algorithm {
    private ArrayList<Point> tempPoints;
    private ArrayList<Point> restPoints;
    private Point startPoint;
    private double endProfit;

    public NearestNeighbor() {
        this.tempPoints = new ArrayList<>();
        this.endProfit = 0.0;
    }

    @Override
    public NearestNeighbor clone() {
        try {
            return (NearestNeighbor) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
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
        ArrayList<Point> unUsed = new ArrayList<>();
        arrList.addAll(allPointsTemp.values());

        while (!arrList.isEmpty()) {
            Pair<Integer, Point> nearestPointPair = this.findNearest(latestPoint, arrList);
            nearestPoint = nearestPointPair.getValue();

            double currentProfit = this.countCurrentProfit(nearestPoint.getProfit(), getDistance(latestPoint, nearestPoint));
            if (currentProfit <= 0) {
                unUsed.add(nearestPoint);
            } else {
                this.tempPoints.add(nearestPoint);

                this.countProfit(nearestPoint.getProfit(), this.getDistance(latestPoint, nearestPoint));
                latestPoint = nearestPoint;
            }

            arrList.remove((int)nearestPointPair.getKey());
        }

        this.restPoints = unUsed;

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
        Integer LOSS_CONST = 6;

        this.endProfit += profit - wayLoss * LOSS_CONST;
    }

    private double countCurrentProfit(Integer profit, double wayLoss) {
        Integer LOSS_CONST = 6;

        return profit - wayLoss * LOSS_CONST;
    }

    private double getDistance(Point pointOne, Point pointTwo) {
        return Math.sqrt((pointOne.getX() - pointTwo.getX()) * (pointOne.getX() - pointTwo.getX()) + (pointOne.getY() - pointTwo.getY()) * (pointOne.getY() - pointTwo.getY()));
    }

    @Override
    public ArrayList<Point> getResultList() {
        return this.tempPoints;
    }

    @Override
    public ArrayList<Point> getRestList() {
        return this.restPoints;
    }

    @Override
    public double getProfit() {
        return this.endProfit;
    }

    @Override
    public Point getStartPoint() {
        return this.startPoint;
    }

    @Override
    public void setProfit(double profit) {
        this.endProfit = profit;
    }

    @Override
    public void removePointFromCycle(int index){
        Point point = this.tempPoints.remove(index);
        this.restPoints.add(point);
    }

    @Override
    public void addPointToCycle(int index, Point point) {
        this.tempPoints.add(index, point);
        this.restPoints.remove(point);
    }

    @Override
    public void setCycle(ArrayList<Point> cycle) {
        this.tempPoints = cycle;
    }

    @Override
    public void setRestList(ArrayList<Point> restList) {

    }
}
