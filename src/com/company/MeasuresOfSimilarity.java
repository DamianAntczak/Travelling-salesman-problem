package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MeasuresOfSimilarity {

    public static final int INITIAL_CAPACITY = 100;

    public void execute(Map<Integer, Point> points){
        List<Algorithm> algorithms = new ArrayList<>(INITIAL_CAPACITY);

        for (int i = 0; i < INITIAL_CAPACITY; i++){
            RandomPath randomPath = new RandomPath();
            randomPath.execute(points.get(0), points);
            algorithms.add(randomPath);
            System.out.println("Add i: "+ i);
        }

    }
}
