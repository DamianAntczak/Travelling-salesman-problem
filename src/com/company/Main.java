package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	    PointsReader pointsReader = new PointsReader();
        try {
            pointsReader.loadKorA100();
            pointsReader.loadKorB100();
            pointsReader.printPoints();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}