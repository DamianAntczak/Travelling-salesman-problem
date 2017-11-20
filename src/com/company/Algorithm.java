package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public interface Algorithm {
    public void execute(Point startPoint, Map<Integer, Point> allPoints);

    public ArrayList<Point> getResultList();

    double getProfit();

    Point getStartPoint();
}
