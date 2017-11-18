package com.company;

import java.io.IOException;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
	    PointsReader pointsReader = new PointsReader();
        try {
            pointsReader.loadKorA100();
            pointsReader.loadKorB100();
            pointsReader.printPoints();
            System.out.println();

            Map<Integer, Point> points = pointsReader.getPoints();

            GreedyCycle greedyCycle = new GreedyCycle();
            greedyCycle.execute(points.get(1), points);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
