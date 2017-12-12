package com.company;

import java.util.*;

public class RandomPath implements Algorithm {
    private ArrayList<Point> path;
    private double profit;
    private Point startPoint;
    private ArrayList<Point> restList;

    public RandomPath() {
        path = new ArrayList<>();
    }

    @Override
    public RandomPath clone() {
        try {
            return (RandomPath) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void execute(Point startPoint, Map<Integer, Point> allPoints) {
        this.startPoint = startPoint;
        Random random = new Random();
        int setSize = random.nextInt(50) + 50;

        Set<Integer> indexes = new HashSet<>();

        while (indexes.size() < setSize - 1) {
            indexes.add(random.nextInt(99));
        }
//        indexes.remove(startPoint.getIndex());
//        path.add(startPoint);
        indexes.forEach(integer -> {
            Point point = allPoints.get(integer);
            if (point != null)
                path.add(point);
        });

        this.restList = new ArrayList<>(allPoints.values());
        restList.removeAll(path);
    }

    @Override
    public ArrayList<Point> getResultList() {
        return path;
    }

    @Override
    public double getProfit() {
        double profit = 0.0;
        for (int i = 0; i < this.path.size() - 1; i++) {
            Point firstPoint = path.get(i);
            Point secondPoint = path.get(i + 1);

            double length = firstPoint.getDistance(secondPoint);
            profit += countProfit(secondPoint.getProfit(), length);
        }
        return profit;
    }

    private double countProfit(Integer profit, double wayLoss) {
        Integer LOSS_CONST = 6;

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
    public void removePointFromCycle(int index) {
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

    @Override
    public void setRestList(ArrayList<Point> restList) {
        this.restList = restList;
    }
}
