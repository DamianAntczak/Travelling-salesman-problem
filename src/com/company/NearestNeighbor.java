package com.company;

import java.util.Map;

public class NearestNeighbor {
    private static Map<Integer, Point> tempPoints;
    public static void execute(Integer startPointIndex, Map<Integer, Point> allPoints) {
        Point latestPoint = allPoints.get(startPointIndex);
        tempPoints.put(0, latestPoint);
        allPoints.remove(startPointIndex);

        allPoints.forEach((integer, point) -> {
            NearestNeighbor.getDistance(latestPoint, point);
        });
    }

    private static Integer findNearest(Point point, Map<Integer, Point> allPoints) {
        Integer min = 10000;
        allPoints.forEach((integer, secondPoint) -> {
            if(NearestNeighbor.getDistance(point, secondPoint) < min) {
                min = NearestNeighbor.getDistance(point, secondPoint);
            }
        });

        return min;
    }

    private static Integer countProfit(Integer profit, Integer wayLoss) {
        Integer LOSS_CONST = 5;

        return profit - wayLoss * LOSS_CONST;
    }

    private static double getDistance(Point pointOne, Point pointTwo) {
        return Math.sqrt((pointOne.getX() - pointTwo.getX()) *  (pointOne.getX() - pointTwo.getX()) + (pointOne.getY() - pointTwo.getY()) *  (pointOne.getY() - pointTwo.getY()));
    }
}
