package com.company;

import java.util.*;

public class Regret implements Algorithm {

    public static final int K = 4;
    private Point startPoint;
    private ArrayList<Point> path;
    private Double profit;
    private ArrayList<Point> restList;

    public Regret(){
        this.profit = 0.0;
        path = new ArrayList<>(100);
    }

    @Override
    public Regret clone() {
        try {
            return (Regret) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void execute(Point startPoint, Map<Integer, Point> allPoints) {
        this.startPoint = startPoint;
        path.add(startPoint);

        HashMap<Integer, Point> tempPoints = new HashMap<>(allPoints);
        tempPoints.remove(startPoint.getIndex());
        findPath(tempPoints);
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

    private TreeMap<Double, Point> findBestPoints(Point forPoint, Map<Integer, Point> points) {
        double actualProfit = 0.0;
        Point currentPoint = null;
        TreeMap<Double, Point> pointByProfit = new TreeMap<>(Collections.reverseOrder());

        Iterator it = points.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Point point = (Point) pair.getValue();
            double profitToStart = countProfit(point.getProfit(), forPoint.getDistance(point));

            pointByProfit.put(profitToStart, point);
        }

        return pointByProfit;
    }

    private double countK_Regret(int k, TreeMap<Double, Point> pointByProfit) {
        final double[] kRegret = {0.0};
        final int[] counter = {0};
        pointByProfit.forEach((aDouble, point) -> {
            if (counter[0] < k) {
                kRegret[0] += aDouble;
            }
            counter[0] += 1;
        });
        return kRegret[0];
    }

    private Map<Integer, Point> expandPath(Map<Integer, Point> points) {

        double kRegret = 0.0;
        Point chosenPoint = null;
        Point precursorPoint = null;

        Iterator it = path.iterator();
        while (it.hasNext()) {
            Point point = (Point) it.next();

            TreeMap<Double, Point> bestPointList = findBestPoints(point, points);
            double currentRegret = countK_Regret(K, bestPointList);
            if (currentRegret > kRegret) {
                kRegret = currentRegret;
                chosenPoint = bestPointList.firstEntry().getValue();
                precursorPoint = point;
            }
        }

        if (kRegret > 0) {
            this.profit += countProfit(chosenPoint.getProfit(), precursorPoint.getDistance(chosenPoint));
//            System.out.println(profit);
            path.add(path.indexOf(precursorPoint), chosenPoint);
            points.remove(chosenPoint.getIndex());
            return points;
        }
        else {
            return null;
        }
    }

    private void findPath(Map<Integer, Point> points) {
        while (points.size() > 0) {
            Map<Integer, Point> integerPointMap = expandPath(points);
            if (integerPointMap == null)
                break;
        }
    }

    private double countProfit(Integer profit, double wayLoss) {
        Integer LOSS_CONST = 6;

        return profit - wayLoss * LOSS_CONST;
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
        this.path.remove(index);
    }

    @Override
    public void addPointToCycle(int index, Point point) {
        this.path.add(index, point);
    }

    @Override
    public void setCycle(ArrayList<Point> cycle) {
        this.path = cycle;
    }
}
