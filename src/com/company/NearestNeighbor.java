package com.company;

import java.util.HashMap;
import java.util.Map;

public class NearestNeighbor implements Algorithm {
    private Map<Integer, Point> tempPoints;
    private double endProfit;

    public Map<Integer, Point> getTempPoints() {
        return tempPoints;
    }

    public double getEndProfit() {
        return endProfit;
    }

    public NearestNeighbor() {
        this.tempPoints = new HashMap<>(100);
        this.endProfit = 0.0;
    }

    @Override
    public void execute(Point startPoint, Map<Integer, Point> allPoints) {
        Point latestPoint = startPoint;
        Point nearestPoint;
        Integer index = 0;
        this.tempPoints.put(index, latestPoint);
        allPoints.remove(startPoint.getIndex());

        while (!allPoints.isEmpty()) {
            index++;
            nearestPoint = this.findNearest(latestPoint, allPoints);
            this.tempPoints.put(index, nearestPoint);
            allPoints.remove(nearestPoint.getIndex());
            this.countProfit(nearestPoint.getProfit(), this.getDistance(latestPoint, nearestPoint));
            latestPoint = nearestPoint;
        }

        index++;
        this.tempPoints.put(index, startPoint);
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
}
