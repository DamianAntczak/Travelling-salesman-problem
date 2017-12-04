package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatedAnnealing {
    public SimulatedAnnealing(){
    }

    private ArrayList<Point> replacePoints(ArrayList<Point> resultList){
        Random random = new Random();
        double oldProfit = getProfit(resultList);
        ArrayList<Point> tempResult = new ArrayList<>(resultList);

        int randomPointIndex = random.nextInt(resultList.size() - 1);
        Point startPoint = resultList.get(randomPointIndex);
        Point nextPoint = resultList.get(randomPointIndex + 1);
        tempResult.remove(randomPointIndex);
        tempResult.add(randomPointIndex, nextPoint);
        tempResult.remove(randomPointIndex + 1);
        tempResult.add(randomPointIndex + 1, startPoint);

        double profit = getProfit(tempResult);

        if (profit > oldProfit)
            return tempResult;
        else
            return resultList;
    }

    private double getProfit(ArrayList<Point> tempResult) {
        double profit = 0.0;
        for (int i = 0; i < tempResult.size() - 1; i++){
            Point point = tempResult.get(i);
            Point point1 = tempResult.get(i + 1);
            double distance = point.getDistance(point1);
            profit += countCurrentProfit(point1.getProfit(), distance);
        }
        Point firstPoint = tempResult.get(0);
        Point lastPoint = tempResult.get(tempResult.size() - 1);
        double distance = lastPoint.getDistance(firstPoint);
        profit += countCurrentProfit(firstPoint.getProfit(), distance);
        return profit;
    }

    public Algorithm improveSolution(Algorithm solution){
        Algorithm newSolution = solution.clone();
        int i = 0;
        ArrayList<Point> currentPath = new ArrayList<>(newSolution.getResultList());
        while ( i < 1000){
            currentPath = replacePoints(currentPath);
            i++;
        }
        newSolution.setProfit(getProfit(currentPath));
        newSolution.setCycle(currentPath);
        return newSolution;
    }

    private double countCurrentProfit(Integer profit, double wayLoss) {
        Integer LOSS_CONST = 6;

        return profit - wayLoss * LOSS_CONST;
    }
}
