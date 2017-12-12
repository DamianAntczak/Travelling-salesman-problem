package com.company;

import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {

    public static final double coolingRate = 0.0001;

    public SimulatedAnnealing() {
    }

    private ArrayList<Point> replacePoints(ArrayList<Point> resultList) {
        Random random = new Random();
        ArrayList<Point> tempResult = new ArrayList<>(resultList);

        int pos1  = (int)(resultList.size() * Math.random());
        int pos2 = (int) (resultList.size() * Math.random());
        Point startPoint = resultList.get(pos1);
        Point nextPoint = resultList.get(pos2);

        tempResult.set(pos1, nextPoint);
        tempResult.set(pos2, startPoint);

        return tempResult;
    }


    public Algorithm improveSolution(Algorithm solution) {
        Algorithm newSolution = solution.clone();
        ArrayList<Point> currentPath = new ArrayList<>(newSolution.getResultList());
        ArrayList<Point> best = new ArrayList<>();

        Random random = new Random();
        double temperature = 100000;
        do {
            ArrayList<Point> newPath = replacePoints(currentPath);
            double newProfit = getProfit(newPath);
            double oldProfit = getProfit(currentPath);
            if (newProfit > oldProfit) {
                currentPath = newPath;
            } else {
                if (Math.exp((newProfit - oldProfit) / temperature) > random.nextDouble()) {
                    currentPath = newPath;
                }

            }

            temperature *= 1- coolingRate;

        } while (temperature > 1);

        newSolution.setProfit(getProfit(currentPath));
        newSolution.setCycle(currentPath);
        return newSolution;
    }

    private double countCurrentProfit(Integer profit, double wayLoss) {
        Integer LOSS_CONST = 6;

        return profit - wayLoss * LOSS_CONST;
    }


    public double getProfit(ArrayList<Point> cycle) {
        double profit = 0.0;
        for (int i = 0; i < cycle.size() - 1; i++) {
            Point firstPoint = cycle.get(i);
            Point secondPoint = cycle.get(i + 1);

            double length = firstPoint.getDistance(secondPoint);
            profit += countProfit(secondPoint.getProfit(), length);
        }
        return profit;
    }

    private double countProfit(Integer profit, double wayLoss) {
        Integer LOSS_CONST = 6;

        return profit - wayLoss * LOSS_CONST;
    }

}
