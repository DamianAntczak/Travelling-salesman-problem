package com.company;

import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        PointsReader pointsReader = new PointsReader();
        LocalSearch localSearch = new LocalSearch();
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing();
        HybridEvolutionary hybridEvolutionary = new HybridEvolutionary();

        try {
            pointsReader.loadKorA100();
            pointsReader.loadKorB100();
            pointsReader.printPoints();

            Scanner s = new Scanner(System.in);

            Map<Integer, Point> points = pointsReader.getPoints();
            TreeMap<Double, Algorithm> pureResults = null;
            System.out.println();
            System.out.println("Multiple Start Local Search? (y - yes / whatever - no)");
            String str = s.nextLine();
            switch (str){
                case "Y":
                case "y":
                    Algorithm algorithm = new RandomPath();
                    algorithm.execute(points.get(1), points);
                    Pair<Double, TreeMap<Double, Algorithm>> bestResultAndAvgTime = Msls(points);
                    double averageTime = bestResultAndAvgTime.getKey();
                    TreeMap<Double, Algorithm> bestResults = bestResultAndAvgTime.getValue();
                    DrawPathHelper drawPathHelper = new DrawPathHelper();
//                    drawPathHelper.drawBestResults(points, bestResults);

                    TreeMap<Double, Algorithm> ILSarray = new TreeMap<>(Collections.reverseOrder());
                    for(int i = 0; i<20; i++) {
                        algorithm = localSearch.iteratedLocalSearch(algorithm, averageTime);
                        ILSarray.put(algorithm.getProfit(), algorithm);
                    }

                    drawPathHelper.drawBestResults(points, ILSarray);
                    break;
            }

            TreeMap<Double, Algorithm> results = new TreeMap<>(Collections.reverseOrder());
            System.out.println();
            System.out.println("Choose algorithm:");
            System.out.println("R/r - RandomPath");
            System.out.println("G/g - GreedyCycle");
            System.out.println("N/n - NearestNeighbor");
            System.out.println("T/t - Regret");
            System.out.println("H/h - Hybrid");
            str = s.nextLine();
            try {
                switch (str) {
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
                    case "T":
                    case "t":
                        pureResults = getAllResults(points, "Regret");
                        break;
                    case "S":
                    case "s":
                        pureResults = getAllResults(points, "RandomPath");
                        pureResults.forEach((aDouble, algorithm) -> {
                            Algorithm solution = simulatedAnnealing.improveSolution(algorithm);
                            results.put(solution.getProfit(), solution);
                        });
                        break;
                    case "H":
                    case "h":
                        pureResults = hybridEvolutionary.improveSolution(getAllResults(points, "RandomPath"), points, 60590);
                        break;
                }

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }


//            pureResults.forEach((aDouble, algorithm) -> {
//                System.out.print("Current profit: ");
//                System.out.println(aDouble);
//                System.out.print("Improve solution: ");
//                Algorithm solution = localSearch.improveSolution(algorithm);
//                results.put(solution.getProfit(), solution);

//                results.put(algorithm.getProfit(), algorithm);
//            });
//            SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing();
//            pureResults.forEach((aDouble, algorithm) -> {
//                Algorithm solution = simulatedAnnealing.improveSolution(algorithm);
//                results.put(solution.getProfit(), solution);
//            });


            pureResults.forEach((aDouble, algorithm) -> {
//                System.out.print("Current profit: ");
//                System.out.println(aDouble);
//                System.out.print("Improve solution: ");
//                Algorithm solution = localSearch.improveSolution(algorithm);
//                Algorithm solution = simulatedAnnealing.improveSolution(algorithm);
                results.put(algorithm.getProfit(), algorithm);
            });

            DrawPathHelper drawPathHelper = new DrawPathHelper();
            drawPathHelper.drawBestResults(points, results);

            //System.out.println(points.size());
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }

    }


    private static TreeMap<Double, Algorithm> getAllResults(Map<Integer, Point> points, String algorithmName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        algorithmName = "com.company." + algorithmName;
        TreeMap<Double, Algorithm> results = new TreeMap<>(Collections.reverseOrder());
        for (int i = 1; i <= 100; i++) {
            Algorithm algorithm = (Algorithm) Class.forName(algorithmName).newInstance();
            long startTime = System.nanoTime();
            algorithm.execute(points.get(i), points);
            long stopTime = System.nanoTime();
            results.put(algorithm.getProfit(), algorithm);
//            System.out.println("Execution time " + (stopTime - startTime));
        }
        return results;
    }

    private static Pair<Double, TreeMap<Double, Algorithm>> Msls(Map<Integer, Point> points) {
        List<Long> timeArray = new ArrayList<Long>();
        TreeMap<Double, Algorithm> best20results = new TreeMap<>(Collections.reverseOrder());

        try {
            TreeMap<Double, Algorithm> pureResults = null;
            LocalSearch localSearch = new LocalSearch();
            for(int i = 0; i < 20; i++) {
                TreeMap<Double, Algorithm> results = new TreeMap<>(Collections.reverseOrder());
                pureResults = getAllResults(points, "RandomPath");
                final long startTime = System.currentTimeMillis();
                pureResults.forEach((aDouble, algorithm) -> {
//                    final long startLocalTime = System.currentTimeMillis();
                    Algorithm solution = localSearch.improveSolution(algorithm);
//                    final long endLocalTime = System.currentTimeMillis();
//                    System.out.println("local search execution time: " + (endLocalTime - startLocalTime) + " milliseconds");
                    results.put(solution.getProfit(), solution);
                });
                final long endTime = System.currentTimeMillis();

                System.out.println("Total execution time: " + (endTime - startTime) + " milliseconds");
                timeArray.add((endTime - startTime));
                best20results.put(results.firstKey(), results.get(results.firstKey()));
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        best20results.forEach((aDouble, algorithm) -> {
            System.out.println(aDouble);
        });

        System.out.print("avg time: ");
        System.out.println(timeArray.stream().mapToDouble(val -> val).average().getAsDouble());
        return new Pair<Double, TreeMap<Double, Algorithm>>(timeArray.stream().mapToDouble(val -> val).average().getAsDouble(), best20results);
    }
}
