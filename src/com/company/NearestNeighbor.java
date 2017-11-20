package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class NearestNeighbor implements Algorithm {
    private ArrayList<Point> tempPoints;
    private double endProfit;
    private Map<Integer, Point> allPoints;

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
        this.allPoints = allPoints;
        Point latestPoint = startPoint;
        Point nearestPoint;
        Integer index = 0;
        this.tempPoints.add(index, latestPoint);
        System.out.println(this.allPoints.size());
        this.allPoints.remove(startPoint.getIndex());

        while (!this.allPoints.isEmpty()) {
            index++;
            System.out.println(this.allPoints.size());
            nearestPoint = this.findNearest(latestPoint, this.allPoints);
            this.tempPoints.add(index, nearestPoint);
            this.allPoints.remove(nearestPoint.getIndex());
            this.countProfit(nearestPoint.getProfit(), this.getDistance(latestPoint, nearestPoint));
            latestPoint = nearestPoint;
        }

        index++;
        this.tempPoints.add(index, startPoint);
        this.countProfit(startPoint.getProfit(), this.getDistance(latestPoint, startPoint));
    }

    private Point findNearest(Point point, Map<Integer, Point> allPoints) {
        Point nearestPoint = new Point(0, 0, 0);
        double min = 1000000;

        for(Map.Entry<Integer, Point> secondPoint : allPoints.entrySet())
        {
            if(this.getDistance(point, secondPoint.getValue()) < min) {
                min = this.getDistance(point, secondPoint.getValue());
                nearestPoint = point;
            }
        }

        return nearestPoint;
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
        return 0;
    }

    @Override
    public Point getStartPoint() {
        return null;
    }
}
