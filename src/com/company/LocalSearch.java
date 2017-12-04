package com.company;

import java.util.ArrayList;
import java.util.Collections;

public class LocalSearch {
    public Algorithm improveSolution(Algorithm solution) {
        Algorithm removeSolution;
        Algorithm addSolution;
        Algorithm replaceSolution;
        double oldProfit;
        double newProfit;
        do {
            oldProfit = solution.getProfit();
            addSolution = this.addMethod(solution.clone());
            removeSolution = this.removeMethod(solution.clone());
            replaceSolution = this.replaceMethod(solution.clone());
            if (removeSolution.getProfit() > addSolution.getProfit()) {
                if(removeSolution.getProfit() > replaceSolution.getProfit()) {
                    solution = removeSolution;
                } else {
                    solution = replaceSolution;
                }
            } else if (addSolution.getProfit() > replaceSolution.getProfit()) {
                solution = addSolution;
            } else {
                solution = replaceSolution;
            }

            newProfit = solution.getProfit();
        } while (newProfit > oldProfit);

//        System.out.println(solution.getProfit());
//        System.out.println(solution.getResultList());

        return solution;
    }

    // usuniecie wierzchołka
    public Algorithm removeMethod(Algorithm solution) {
        double newProfit;
        for (int i = 1; i < solution.getResultList().size() - 1; i++) {
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

        for (int currentPosition = 1; currentPosition < solutionPointList.size() - 1; currentPosition++) {
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
        ArrayList<Point> solutionPointList = new ArrayList<>(solution.getResultList());

        for (int currentBasePosition = 1; currentBasePosition < solutionPointList.size() - 2; currentBasePosition++) {
            for (int currentReplacePosition = currentBasePosition + 2; currentReplacePosition < solutionPointList.size(); currentReplacePosition++) {
                ArrayList<Point> tempList = new ArrayList<>(solutionPointList);
                ArrayList<Point> subList = new ArrayList<>(tempList.subList(currentBasePosition, currentReplacePosition));
                for(int index = currentBasePosition; index <currentReplacePosition; index++) {
                    tempList.remove(currentBasePosition);
                }
                Collections.reverse(subList);
                tempList.addAll(currentBasePosition, subList);

                newProfit = calculateProfitForCycle(tempList);
                double oldProfit = solution.getProfit();

                if (newProfit > oldProfit) {
                    solution.setCycle(tempList);
                    solution.setProfit(newProfit);

                    return solution;
                }
            }
        }

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
