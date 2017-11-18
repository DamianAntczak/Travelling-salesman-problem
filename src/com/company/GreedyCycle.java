package com.company;

import javafx.util.Pair;

import java.util.*;

public class GreedyCycle implements Algorithm {

    private LinkedList<Point> cycle;
    private Point startPoint;

    public GreedyCycle() {
        this.cycle = new LinkedList<>();
    }

    @Override
    public void execute(Point startPoint, Map<Integer, Point> allPoints) {
        this.startPoint = startPoint;
        allPoints.remove(startPoint.getIndex());
        cycle.add(startPoint);

        findPath(allPoints);

        cycle.forEach(point -> System.out.println(point.getIndex()));
    }

    private Pair<Point, Double> findNearestPoint(Point forPoint,Map<Integer, Point> points) {
        double distance = 0.0;
        Point currentPoint = null;

        Iterator it = points.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Point point = (Point) pair.getValue();
            Integer key = (Integer) pair.getKey();
            double distanceToStart = forPoint.getDistance(point);

//            System.out.println(key + " = " + distanceToStart);

            if (distance == 0.0 || distance > distanceToStart) {
                distance = distanceToStart;
                currentPoint = point;
            }
        }

//        System.out.println(distance);
//        System.out.println(currentPoint.getIndex());

        return new Pair<>(currentPoint, distance);
    }

    private Map<Integer, Point> expandCycle(Map<Integer, Point> points) {
        Point currentPoint = null;
        Point precursor = null;
        double distance = 0;
        for (Point point : cycle) {
            Pair<Point, Double> nearestPoint = findNearestPoint(point, points);
            if (distance == 0 || distance > nearestPoint.getValue()) {
                distance = nearestPoint.getValue();
                currentPoint = nearestPoint.getKey();
                precursor = point;
            }
        }


        cycle.add(cycle.indexOf(precursor),currentPoint);
        points.remove(currentPoint.getIndex());
        return points;
    }

    private void  findPath(Map<Integer, Point> points){
        while (points.size() > 0){
            points = expandCycle(points);
        }
    }
}
