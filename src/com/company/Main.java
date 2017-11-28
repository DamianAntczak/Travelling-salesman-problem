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

            Map<Integer, Point> points = pointsReader.getPoints();

            TreeMap<Double, Algorithm> pureResults = getAllResults(points);
            TreeMap<Double, Algorithm> results = new TreeMap<>(Collections.reverseOrder());

//            LocalSearch localSearch = new LocalSearch();
//
//            pureResults.forEach((aDouble, algorithm) -> {
//                System.out.print("Current profit: ");
//                System.out.println(aDouble);
//                System.out.print("Improve solution: ");
//                Algorithm solution = localSearch.improveSolution(algorithm);
//                results.put(solution.getProfit(), solution);
//            });

            final int[] i = {0};
            DrawPathHelper drawPathHelper = new DrawPathHelper();
            pureResults.forEach((aDouble, algorithm) -> {
                System.out.println(i[0] + ": "+aDouble);
                if (i[0] < 6) {
                    try {
                        String className = algorithm.getClass().getSimpleName();
                        File dir = new File("." + File.separator + className);
                        if (!dir.exists()) {
                            dir.mkdir();
                        }

//                        if(i[0] == 0) {
//                            for (int x = 0; x < algorithm.getResultList().size(); x++) {
//                                System.out.print(algorithm.getResultList().get(x).getIndex());
//                                System.out.print(" -> ");
//                            }
//                        }

                        String fileName = dir.getCanonicalPath() + File.separator + "points" + String.valueOf(aDouble).replace(".", "_");
                        drawPathHelper.saveToFile(points, algorithm.getResultList(), fileName, algorithm.getStartPoint());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                i[0]++;
            });


            //System.out.println(points.size());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static TreeMap<Double, Algorithm> getAllResults(Map<Integer, Point> points) {
        TreeMap<Double, Algorithm> results = new TreeMap<>(Collections.reverseOrder());
        for (int i = 1; i <= 100; i++) {
            Algorithm nearestNeighbor = new RandomPath();
            nearestNeighbor.execute(points.get(i), points);
            results.put(nearestNeighbor.getProfit(), nearestNeighbor);
        }
        return results;
    }

}
