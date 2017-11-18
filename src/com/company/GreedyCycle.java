package com.company;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GreedyCycle implements Algorithm{

    private List<Point> cycle;
    private Point startPoint;

    public GreedyCycle() {
        this.cycle = new ArrayList<>();
    }

    @Override
    public void execute(Point startPoint, Map<Integer, Point> allPoints) {
        this.startPoint = startPoint;
        allPoints.remove(startPoint);

        findStartCycle(allPoints);
    }

    private void findStartCycle(Map<Integer, Point> points){
        double distance = 0.0;
        Point currentPoint = null;

        Iterator it = points.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Point point = (Point) pair.getValue();
            Integer key = (Integer) pair.getKey();
            double distanceToStart = startPoint.getDistance(point);

            System.out.println(key + " = " + distanceToStart);

            if(distance == 0.0 || distance > distanceToStart) {
                distance = distanceToStart;
                currentPoint = point;
            }

            it.remove();
        }

        System.out.println(distance);
        System.out.println(currentPoint.getIndex());
        cycle.add(currentPoint);
    }
}
