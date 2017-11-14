package com.company;

public class PointUtils {

    public static double getDistance(Point point1, Point point2){
        double deltaX = point2.getX() - point1.getX();
        double deltaY = point2.getY() - point1.getY();
        return Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2));
    }
}
