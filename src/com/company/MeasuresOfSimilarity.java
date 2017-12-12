package com.company;

import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class MeasuresOfSimilarity {

    private static final int INITIAL_CAPACITY = 100;

    private double bestProfit;
    private Algorithm bestSolution;

    public void execute(Map<Integer, Point> points){
        List<Algorithm> algorithms = new ArrayList<>(INITIAL_CAPACITY);

        for (int i = 0; i < INITIAL_CAPACITY; i++){
            RandomPath randomPath = new RandomPath();
            LocalSearch localSearch = new LocalSearch();

            randomPath.execute(points.get(0), points);
            Algorithm afterLocalSearch = localSearch.improveSolution(randomPath);
            algorithms.add(afterLocalSearch);
            System.out.println(afterLocalSearch.getProfit());
        }

        bestSolution =  findBest(algorithms);
        bestProfit = bestSolution.getProfit();
        System.out.println(bestProfit);

        System.out.println();
        List<Pair<Double, BigDecimal>> pairs = compareWithBestSolution(algorithms);
        pairs.forEach(pair -> {
            System.out.println(String.format("%f;%f", pair.getKey(),  pair.getValue().floatValue()));
        });

       try(PrintWriter pw = new PrintWriter(new FileWriter("results.txt"))){
           pairs.forEach(pair -> {
               String line = String.format("%f;%f", pair.getKey(), pair.getValue().floatValue());
               System.out.println(line);
               pw.println(line);
           });
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    private Algorithm findBest(List<Algorithm> algorithms) {
        ArrayList<Algorithm> collect = algorithms.stream().sorted(Comparator.comparingDouble(o -> o.getProfit())).collect(Collectors.toCollection(ArrayList::new));
        Collections.reverse(collect);
        return collect.get(0);
    }

    public double commonVertexs(Algorithm solution1, Algorithm solution2) {
        final int[] howManyCommon = {0};
        solution1.getResultList().forEach((point) -> {
            if (solution2.getResultList().indexOf(point) != -1) {
                howManyCommon[0] += 1;
            }
        });

        double avgVertexsCount = (solution1.getResultList().size() + solution2.getResultList().size()) / 2;
        return howManyCommon[0] / avgVertexsCount;
    }

    private BigDecimal commonEdges(Algorithm solution1, Algorithm solution2) {
        int howManyCommon = 0;
        for(int index1 = 0; index1 < solution1.getResultList().size()-1; index1++) {
            for(int index2 = 0; index2 < solution2.getResultList().size()-1; index2++) {
                if(solution1.getResultList().get(index1).getIndex() == solution2.getResultList().get(index2).getIndex() &&
                        solution1.getResultList().get(index1+1).getIndex() == solution2.getResultList().get(index2+1).getIndex()) {
                    howManyCommon += 1;
                    break;
                }
            }
        }

        if(solution1.getResultList().get(solution1.getResultList().size()-1).getIndex() == solution2.getResultList().get(solution2.getResultList().size()-1).getIndex() &&
                solution1.getResultList().get(0).getIndex() == solution2.getResultList().get(0).getIndex()) {
            howManyCommon += 1;
        }

        BigDecimal numerator = BigDecimal.valueOf(solution1.getResultList().size() + solution2.getResultList().size());
        BigDecimal avgEdgesCount = numerator.divide(BigDecimal.valueOf(2));
        return BigDecimal.valueOf(howManyCommon).divide(avgEdgesCount, 8, RoundingMode.HALF_UP);
    }

    private List<Pair<Double, BigDecimal>> compareWithBestSolution(List<Algorithm> algorithms){
        List<Pair<Double, BigDecimal>> results = new ArrayList<>(INITIAL_CAPACITY);
        algorithms.forEach(algorithm -> {
            BigDecimal value = commonEdges(algorithm, bestSolution);
            results.add(new Pair<>(algorithm.getProfit(), value));
        });
        return results;
    }
}
