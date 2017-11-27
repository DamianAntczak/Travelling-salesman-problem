package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public interface Algorithm {
    public void execute(Point startPoint, Map<Integer, Point> allPoints);

    public ArrayList<Point> getResultList();

    public ArrayList<Point> getRestList();

    double getProfit();

    Point getStartPoint();

    void setProfit(double profit);

    void removePointFromCycle(int index);

    void addPointToCycle(int index, Point point);

    void setCycle(ArrayList<Point> cycle);
}
