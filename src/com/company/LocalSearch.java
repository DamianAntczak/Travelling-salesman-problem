package com.company;

import java.util.ArrayList;

public class LocalSearch {
    public void improveSolution(Algorithm solution) {
        solution = this.removeMethod(solution);
        solution = this.addMethod(solution);
//        solution = this.replaceMethod(solution);
        System.out.println(solution.getProfit());
        System.out.println(solution.getResultList());
    }

    // usuniecie wierzchołka
    public Algorithm removeMethod(Algorithm solution) {
        double newProfit;
        for (int i = 1; i < solution.getResultList().size(); i++) {
            ArrayList<Point> tempList = new ArrayList<>(solution.getResultList());
            tempList.remove(i);
            newProfit = calculateProfitForCycle(tempList);
            if (newProfit > solution.getProfit()) {
                solution.removePointFromCycle(i);
                solution.setProfit(newProfit);

                return solution;
            }
        }

        return solution;
    }

    // dodanie wierzcholka
    public Algorithm addMethod(Algorithm solution) {
        double newProfit;

        ArrayList<Point> restList = new ArrayList<>(solution.getRestList());
        ArrayList<Point> solutionPointList = new ArrayList<>(solution.getResultList());

        for (int currentPosition = 0; currentPosition < solutionPointList.size(); currentPosition++) {
            for(Point pointToAdd :restList) {
                ArrayList<Point> tempList = new ArrayList<>(solutionPointList);
                tempList.add(currentPosition, pointToAdd);
                newProfit = calculateProfitForCycle(tempList);
                if (newProfit > solution.getProfit()) {
                    solution.addPointToCycle(currentPosition, pointToAdd);
                    solution.setProfit(newProfit);

                    return solution;
                }
            }
        }

        return solution;
    }

    // wymiana dwoch łuków
    public Algorithm replaceMethod(Algorithm solution) {
        double newProfit;

        return solution;
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
        Integer LOSS_CONST = 5;
        double way = Math.sqrt((pointOne.getX() - pointTwo.getX()) * (pointOne.getX() - pointTwo.getX()) + (pointOne.getY() - pointTwo.getY()) * (pointOne.getY() - pointTwo.getY()));

        return pointTwo.getProfit() - way * LOSS_CONST;
    }
}
