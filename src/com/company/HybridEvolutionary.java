package com.company;

import java.util.*;
import java.util.stream.IntStream;

public class HybridEvolutionary {
    public static final int LAST_INDEX = 19;
    public static final int POPULATION_SIZE = 20;
    List<Algorithm> population;

    Map<Integer, Point> allPoints;

    public TreeMap<Double, Algorithm> improveSolution(TreeMap<Double, Algorithm> resultsFromRandom, Map<Integer, Point> allPoints, double avgTime) {
        this.allPoints = allPoints;
        population = new ArrayList<>(resultsFromRandom.values());
        population = population.subList(0, POPULATION_SIZE);
        System.out.println("Rozmiar" + population.size());

        TreeMap<Double, Algorithm> result = new TreeMap<>();
        final long startTime = System.currentTimeMillis();
        do {
            recombinate();
        } while ((System.currentTimeMillis() - startTime) <= avgTime);

        population.forEach(algorithm -> result.put(algorithm.getProfit(), algorithm));
        return result;
    }

    private void recombinate() {
        Random random = new Random();
        int firstIndex = random.nextInt(POPULATION_SIZE);
        int secondIndex = random.nextInt(POPULATION_SIZE);
        while (firstIndex == secondIndex) {
            secondIndex = random.nextInt(POPULATION_SIZE);
        }

        for (Algorithm algorithm : population) {
            System.out.println(algorithm.getProfit());
        }

        Algorithm recombination = recombination(population.get(firstIndex), population.get(secondIndex));
        Algorithm equal = population.stream().filter(algorithm -> algorithm.getProfit() == recombination.getProfit()).findFirst().orElse(null);
        if (equal == null) {
            if (recombination.getProfit() > population.get(population.size() - 1).getProfit()) {
                population.remove(LAST_INDEX);
                population.add(LAST_INDEX, recombination);
            }
        }

        final Comparator<Algorithm> c = Comparator.comparingDouble(o -> o.getProfit());
        population.sort(c.reversed());
    }

    private Algorithm recombination(Algorithm algorithm, Algorithm algorithm1) {
        System.out.println("recombination");
        Algorithm newSolution = new RandomPath();
        ArrayList<Point> resultList = new ArrayList<>();

        ArrayList<Point> path1 = algorithm.getResultList();
        ArrayList<Point> path2 = algorithm1.getResultList();

        for (int i = 0; i < path1.size() - 1; i++) {
            Point currentPoint = path1.get(i);
            int searchedIndex = IntStream.range(0, path2.size())
                    .filter(index -> currentPoint.getIndex() == path2.get(index).getIndex())
                    .findFirst().orElse(-1);

            if (searchedIndex > -1) {
                resultList.add(currentPoint);
            }
        }

        ArrayList<Point> restList = new ArrayList<>(allPoints.values());
        restList.removeAll(resultList);

        Random random = new Random();
        while (resultList.size() < 70) {
            int bound = restList.size() - 1;
            int randomInt = random.nextInt(bound);
            Point point = restList.remove(randomInt);
            resultList.add(point);
        }


        System.out.println("resultList:" + resultList.size());

        newSolution.setCycle(resultList);
        newSolution.setRestList(restList);
        LocalSearch localSearch = new LocalSearch();
        newSolution = localSearch.improveSolution(newSolution);
        System.out.println("Profit after localSearch: " + newSolution.getProfit());
        return newSolution;
    }
}
