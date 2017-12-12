package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MeasuresOfSimilarity {

    public static final int INITIAL_CAPACITY = 10;

    public void execute(Map<Integer, Point> points){
        List<Algorithm> algorithms = new ArrayList<>(INITIAL_CAPACITY);

        for (int i = 0; i < INITIAL_CAPACITY; i++){
            RandomPath randomPath = new RandomPath();
            randomPath.execute(points.get(0), points);
            algorithms.add(randomPath);
            System.out.println("Add i: "+ i);
        }
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

    public double commonEdges(Algorithm solution1, Algorithm solution2) {
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

        double avgEdgesCount = (solution1.getResultList().size() + solution2.getResultList().size()) / 2;
        return howManyCommon / avgEdgesCount;
    }
}
