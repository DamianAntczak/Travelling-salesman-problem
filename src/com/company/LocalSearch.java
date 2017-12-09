package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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

    public Algorithm iteratedLocalSearch(Algorithm solution, double avgTime) {
        final long startTime = System.currentTimeMillis();
        Random rand = new Random();
        do {
            Algorithm solutionCopy = solution.clone();
            for(int i=0; i < 4; i++) {
                int choice = rand.nextInt(3);
                switch (choice) {
                    case 0:
                        if ((solutionCopy.getResultList().size() - 2) > 0) {
                            int n = rand.nextInt(solutionCopy.getResultList().size() - 2 ) + 1;

                            solutionCopy.removePointFromCycle(n);
                            solutionCopy.setProfit(calculateProfitForCycle(solutionCopy.getResultList()));
                        }

                        break;
                    case 1:
                        if ((solutionCopy.getResultList().size() - 2 ) > 0 && solutionCopy.getRestList().size() > 0) {
                            int currentPosition = rand.nextInt(solutionCopy.getResultList().size() - 2 ) + 1;
                            int pointToAdd = rand.nextInt(solutionCopy.getRestList().size());

                            solutionCopy.addPointToCycle(currentPosition, solutionCopy.getRestList().get(pointToAdd));
                            solutionCopy.setProfit(calculateProfitForCycle(solutionCopy.getResultList()));
                        }
                        break;
                    case 2:
                        int currentBasePosition = rand.nextInt(solutionCopy.getResultList().size() - 1 ) + 1;
                        if ((solutionCopy.getResultList().size() - currentBasePosition - 2) > 0) {
                            int currentReplacePosition = rand.nextInt(solutionCopy.getResultList().size() - currentBasePosition - 2) + currentBasePosition + 2;
                            if (currentBasePosition == 0) {
                                System.out.print("alert1");
                            }
                            if (currentReplacePosition == solutionCopy.getResultList().size()) {
                                System.out.print("alert2");
                            }
                            if ((currentReplacePosition - currentBasePosition) < 2) {
                                System.out.print("alert3");
                            }
                            ArrayList<Point> tempList = new ArrayList<>(solutionCopy.getResultList());
                            ArrayList<Point> subList = new ArrayList<>(tempList.subList(currentBasePosition, currentReplacePosition));
                            for(int index = currentBasePosition; index <currentReplacePosition; index++) {
                                tempList.remove(currentBasePosition);
                            }
                            Collections.reverse(subList);
                            tempList.addAll(currentBasePosition, subList);

                            solutionCopy.setCycle(tempList);
                            solutionCopy.setProfit(calculateProfitForCycle(tempList));
                        }

                        break;
                }
            }

            solutionCopy = improveSolution(solutionCopy);
            if(solutionCopy.getProfit()>solution.getProfit()) {
                solution = solutionCopy;
            }

        } while ((System.currentTimeMillis() - startTime) <= avgTime);

        System.out.println(solution.getProfit());

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
        Integer LOSS_CONST = 6;
        double way = Math.sqrt((pointOne.getX() - pointTwo.getX()) * (pointOne.getX() - pointTwo.getX()) + (pointOne.getY() - pointTwo.getY()) * (pointOne.getY() - pointTwo.getY()));

        return pointTwo.getProfit() - way * LOSS_CONST;
    }
}
