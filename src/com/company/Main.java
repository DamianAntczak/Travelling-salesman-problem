package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        PointsReader pointsReader = new PointsReader();
        try {
            pointsReader.loadKorA100();
            pointsReader.loadKorB100();
            pointsReader.printPoints();
            System.out.println();

            Map<Integer, Point> points = pointsReader.getPoints();



            TreeMap<Double, Algorithm> results = getAllResults(points);

            final int[] i = {0};
            results.forEach((aDouble, algorithm) -> {
                System.out.println(aDouble);
                if(i[0] < 6) {
                    try {
                        saveToFile(points, algorithm.getResultList(), ".\\greedyCycle\\points" + String.valueOf(aDouble).replace(".", "_"), algorithm.getStartPoint());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                i[0]++;
            });



            System.out.println(points.size());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveToFile(Map<Integer, Point> points, ArrayList<Point> path, String fileName, Point startPoint) throws IOException {
        int maxX = getMaxX(points);
        int maxY = getMaxY(points);
        BufferedImage img = new BufferedImage(maxX + 150, maxY + 150, BufferedImage.TYPE_INT_RGB);

        fillBackground(img);

        Graphics2D graphics = img.createGraphics();
        graphics.drawRect(0, 0, img.getWidth(), img.getHeight());
        graphics.setStroke(new BasicStroke(5));
        graphics.setFont(new Font("Tahoma", Font.PLAIN, 50));

        drawPoints(points, graphics);
        drawLines(graphics, path);
        drawStartPoint(startPoint, graphics);
        graphics.dispose();

        ImageIO.write(img, "PNG", new File(fileName+".png"));
    }

    private static void drawStartPoint(Point startPoint, Graphics2D graphics) {
        graphics.setStroke(new BasicStroke(30));
        graphics.setColor(Color.BLUE);
        graphics.drawOval(startPoint.getX(), startPoint.getY(), 10, 10);

    }

    private static TreeMap<Double, Algorithm> getAllResults(Map<Integer, Point> points) {
        TreeMap<Double, Algorithm> results = new TreeMap<>(Collections.reverseOrder());
        for (int i = 1; i <= 100; i++) {
            Algorithm greedyCycle = new GreedyCycle();
            greedyCycle.execute(points.get(i), points);
            ArrayList<Point> path = greedyCycle.getResultList();
            results.put(greedyCycle.getProfit(), greedyCycle);
        }
        return results;
    }

    private static int getMaxY(Map<Integer, Point> points) {
        return Collections.max(points.entrySet(), Comparator.comparingInt(entry -> entry.getValue().getY())).getValue().getY();
    }

    private static int getMaxX(Map<Integer, Point> points) {
        return Collections.max(points.entrySet(), Comparator.comparingInt(entry -> entry.getValue().getX())).getValue().getX();
    }

    private static void drawLines(Graphics2D graphics, ArrayList<Point> path) {
        graphics.setStroke(new BasicStroke(25));
        graphics.setStroke(new BasicStroke(3));
        graphics.setColor(Color.black);
        for (int i = 0; i < path.size() - 1; i++) {
            Point point1 = path.get(i);
            Point point2 = path.get(i + 1);
            graphics.drawLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());

        }
        graphics.drawLine(path.get(0).getX(), path.get(0).getY(), path.get(path.size() - 1).getX(), path.get(path.size() - 1).getY());
    }

    private static void drawPoints(Map<Integer, Point> points, Graphics2D graphics) {
        graphics.setStroke(new BasicStroke(25));
        points.forEach((integer, point) -> {
            graphics.setColor(new Color(135, 135, 135));
            graphics.drawOval(point.getX(), point.getY(), 10, 10);
            graphics.setColor(new Color(137, 0, 0));
            graphics.drawString(String.valueOf(point.getIndex()), point.getX(), point.getY() - 20);
            graphics.setColor(new Color(18, 132, 1));
            graphics.drawString(String.valueOf(point.getProfit()), point.getX(), point.getY() + 60);
        });
    }

    private static void fillBackground(BufferedImage img) {
        int pixelWhite[] = {255, 255, 255};
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                img.getRaster().setPixel(i, j, pixelWhite);
            }
        }
    }

}
