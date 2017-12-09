package com.company;

import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {
    public SimulatedAnnealing(){
    }

    private ArrayList<Point> replacePoints(ArrayList<Point> resultList){
        Random random = new Random();
        double oldProfit = getProfit(resultList);
        ArrayList<Point> tempResult = new ArrayList<>(resultList);

        int randomPointIndex = random.nextInt(resultList.size() - 1);
        int nextRandomPointIndex = random.nextInt(resultList.size() - 1);
        Point startPoint = resultList.get(randomPointIndex);
        Point nextPoint = resultList.get(nextRandomPointIndex);
        tempResult.remove(randomPointIndex);
        tempResult.add(randomPointIndex, nextPoint);
        tempResult.remove(nextRandomPointIndex);
        tempResult.add(nextRandomPointIndex, startPoint);

        double profit = getProfit(tempResult);

        return tempResult;
    }

    private double getProfit(ArrayList<Point> tempResult) {
//        double profit = 0.0;
//        for (int i = 0; i < tempResult.size() - 1; i++){
//            Point point = tempResult.get(i);
//            Point point1 = tempResult.get(i + 1);
//            double distance = point.getDistance(point1);
//            profit += countCurrentProfit(point1.getProfit(), distance);
//        }
//        Point firstPoint = tempResult.get(0);
//        Point lastPoint = tempResult.get(tempResult.size() - 1);
//        double distance = lastPoint.getDistance(firstPoint);
//        profit += countCurrentProfit(firstPoint.getProfit(), distance);
//        return profit;

        return calculateProfitForCycle(tempResult);
    }

    public Algorithm improveSolution(Algorithm solution){
        Algorithm newSolution = solution.clone();
        ArrayList<Point> currentPath = new ArrayList<>(newSolution.getResultList());

        Random random = new Random();
        double temperature = 2000.0;
        do {
            int i = 0;
            while (i < 100) {
                ArrayList<Point> tempPath = replacePoints(currentPath);
                double newProfit = getProfit(tempPath);
                double oldProfit = getProfit(currentPath);
                if (newProfit > oldProfit) {
                    currentPath = tempPath;
                } else {
                    if (Math.exp((newProfit - oldProfit) / temperature) > random.nextDouble()) {
                        currentPath = tempPath;
                    }

                }
                i++;
            }
            temperature -= 100.0;

        }while (temperature >= 0.0);

        newSolution.setProfit(getProfit(currentPath));
        newSolution.setCycle(currentPath);
        return newSolution;
    }

    private double countCurrentProfit(Integer profit, double wayLoss) {
        Integer LOSS_CONST = 6;

        return profit - wayLoss * LOSS_CONST;
    }

    private double calculateProfitForCycle(ArrayList<Point> cycle) {
        double profit = 0;
        Point latestPoint = cycle.get(0);
        for (int i = 1; i < cycle.size(); i++) {
            profit += this.countCurrentProfit(latestPoint, cycle.get(i));
            latestPoint = cycle.get(i);
        }

        return profit;
    }

    private double countCurrentProfit(Point pointOne, Point pointTwo) {
        Integer LOSS_CONST = 6;
        double way = Math.sqrt((pointOne.getX() - pointTwo.getX()) * (pointOne.getX() - pointTwo.getX()) + (pointOne.getY() - pointTwo.getY()) * (pointOne.getY() - pointTwo.getY()));

        return pointTwo.getProfit() - way * LOSS_CONST;
    }
}
