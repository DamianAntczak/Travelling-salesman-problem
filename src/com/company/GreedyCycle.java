package com.company;

import javafx.util.Pair;

import java.util.*;

public class GreedyCycle implements Algorithm {

    private ArrayList<Point> cycle;
    private Point startPoint;
    private double profit;

    public GreedyCycle() {
        this.cycle = new ArrayList<>();
        this.profit = 0.0;
    }

    @Override
    public void execute(Point startPoint, Map<Integer, Point> allPoints) {
        Map<Integer, Point> tempPoints = new HashMap<>(allPoints);

        this.startPoint = startPoint;
        cycle.add(startPoint);
        tempPoints.remove(startPoint.getIndex());
        findPath(tempPoints);
    }

    private Pair<Point, Double> findNearestPoint(Point forPoint, Map<Integer, Point> points) {
        double actualProfit = 0.0;
        Point currentPoint = null;

        Iterator it = points.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Point point = (Point) pair.getValue();
            Integer key = (Integer) pair.getKey();
            double profitToStart = countProfit(point.getProfit(), forPoint.getDistance(point));

//            System.out.println(key + " = " + distanceToStart);

            if (actualProfit == 0.0 || profitToStart > actualProfit ) {
                actualProfit = profitToStart;
                currentPoint = point;
            }
        }

//        System.out.println(distance);
//        System.out.println(currentPoint.getIndex());

        return new Pair<>(currentPoint, actualProfit);
    }

    private Map<Integer, Point> expandCycle(Map<Integer, Point> points) {
        Point currentPoint = null;
        Point precursor = null;
        double actualProfit = 0;
        for (Point point : cycle) {
            Pair<Point, Double> nearestPoint = findNearestPoint(point, points);
            if (nearestPoint.getValue() > actualProfit) {
                actualProfit = nearestPoint.getValue();
                currentPoint = nearestPoint.getKey();
                precursor = point;
            }
        }

        if (actualProfit > 0) {
            this.profit += actualProfit;
            cycle.add(cycle.indexOf(precursor), currentPoint);
            points.remove(currentPoint.getIndex());
            return points;
        } else {
            return null;
        }

    }

    private void findPath(Map<Integer, Point> points) {
        while (points.size() > 0) {
            Map<Integer, Point> integerPointMap = expandCycle(points);
            if (integerPointMap == null)
                break;
        }
    }

    public ArrayList<Point> getResultList() {
        return cycle;
    }

    private double countProfit(Integer profit, double wayLoss) {
        Integer LOSS_CONST = 5;

        return profit - wayLoss * LOSS_CONST;
    }

    public double getProfit() {
        return profit;
    }

    @Override
    public Point getStartPoint() {
        return startPoint;
    }
}
