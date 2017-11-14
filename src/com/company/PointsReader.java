package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PointsReader {

    private Map<Integer, Point> points;

    public PointsReader() {
        points = new HashMap<>(100);
    }

    public void loadKorA100() throws IOException{
        try (BufferedReader br = new BufferedReader(new FileReader("kroA100.tsp"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.matches("\\d* \\d* \\d*")){
                    String[] args = line.split(" ");
                    Point point = new Point(Integer.parseInt(args[0]), Integer.parseInt(args[1]),Integer.parseInt(args[2]));
                    points.put(point.getIndex(), point);
                }
            }
        }
    }

    public void loadKorB100() throws IOException{
        try (BufferedReader br = new BufferedReader(new FileReader("kroB100.tsp"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.matches("\\d* \\d* \\d*")){
                    String[] args = line.split(" ");
                    Point point = points.get(Integer.parseInt(args[0]));
                    point.setProfit(Integer.parseInt(args[1]));
                    points.put(point.getIndex(), point);
                }
            }
        }
    }

    public void printPoints(){
        points.forEach((integer, point) -> {
            System.out.println(String.format("index:%5d  x:%5d  y:%5d   profit:%5d", point.getIndex(), point.getX(), point.getY(), point.getProfit()));
        });
    }

    public Map<Integer, Point> getPoints() {
        return points;
    }
}
