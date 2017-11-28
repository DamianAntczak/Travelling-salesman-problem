package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    private static int height;

    public static void main(String[] args) {
        PointsReader pointsReader = new PointsReader();
        try {
            pointsReader.loadKorA100();
            pointsReader.loadKorB100();
            pointsReader.printPoints();
            System.out.println();
            System.out.println("Choose algorithm:");
            System.out.println("R/r - RandomPath");
            System.out.println("G/g - GreedyCycle");
            System.out.println("N/n - NearestNeighbor");

            Scanner s = new Scanner(System.in);
            String str = s.nextLine();

            Map<Integer, Point> points = pointsReader.getPoints();
            TreeMap<Double, Algorithm> pureResults = null;
            try {
                switch (str){
                    case "r":
                    case "R":
                        pureResults = getAllResults(points, "RandomPath");
                        break;
                    case "G":
                    case "g":
                        pureResults = getAllResults(points, "GreedyCycle");
                        break;
                    case "N":
                    case "n":
                        pureResults = getAllResults(points, "NearestNeighbor");
                        break;
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            TreeMap<Double, Algorithm> results = new TreeMap<>(Collections.reverseOrder());

            LocalSearch localSearch = new LocalSearch();

            pureResults.forEach((aDouble, algorithm) -> {
                System.out.print("Current profit: ");
                System.out.println(aDouble);
                System.out.print("Improve solution: ");
                Algorithm solution = localSearch.improveSolution(algorithm);
                results.put(solution.getProfit(), solution);
            });


            DrawPathHelper drawPathHelper = new DrawPathHelper();
            drawPathHelper.drawBestResults(points, results);


            //System.out.println(points.size());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static TreeMap<Double, Algorithm> getAllResults(Map<Integer, Point> points, String algorithmName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        algorithmName = "com.company." + algorithmName;
        TreeMap<Double, Algorithm> results = new TreeMap<>(Collections.reverseOrder());
        for (int i = 1; i <= 100; i++) {
            Algorithm algorithm = (Algorithm) Class.forName(algorithmName).newInstance();
            algorithm.execute(points.get(i), points);
            results.put(algorithm.getProfit(), algorithm);
        }
        return results;
    }

}
