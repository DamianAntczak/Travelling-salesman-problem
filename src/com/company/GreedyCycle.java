package com.company;

import com.sun.javafx.geom.Edge;
import javafx.util.Pair;

import java.util.*;

public class GreedyCycle implements Algorithm {

    private ArrayList<Point> cycle;
    private Point startPoint;
    private double profit;
    private ArrayList<Point> restList;

    private List<Edge> edges;

    public GreedyCycle() {
        this.restList = new ArrayList<>();
        this.cycle = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.profit = 0.0;
    }

    @Override
    public GreedyCycle clone() {
        try {
            return (GreedyCycle) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void execute(Point startPoint, Map<Integer, Point> allPoints) {
        Map<Integer, Point> tempPoints = new HashMap<>(allPoints);

        this.startPoint = startPoint;
        cycle.add(startPoint);
        tempPoints.remove(startPoint.getIndex());
        findPath(tempPoints);
        //addLastEdgeProfit();
    }

    private void addLastEdgeProfit() {
        Point point1 = startPoint;
        Point point2 = cycle.get(cycle.size() - 1);
        Double distance = point1.getDistance(point2);
        profit += countProfit(point1.getProfit(), distance);
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


        Point lastPoint = null;
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
        Integer LOSS_CONST = 6;

        return profit - wayLoss * LOSS_CONST;
    }

    public double getProfit() {
        return profit;
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
        Point remove = this.cycle.remove(index);
        this.restList.add(remove);
    }

    @Override
    public void addPointToCycle(int index, Point point) {
        this.cycle.add(index, point);
        this.restList.remove(point);
    }

    @Override
    public void setCycle(ArrayList<Point> cycle) {
        this.cycle = cycle;
    }

    @Override
    public void setRestList(ArrayList<Point> restList) {

    }
}
