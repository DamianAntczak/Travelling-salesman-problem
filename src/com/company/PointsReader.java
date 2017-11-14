package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class PointsReader {

    private List<Point> points;

    public void loadKorA100() throws IOException{
        try (BufferedReader br = new BufferedReader(new FileReader("kroA100.tsp"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
