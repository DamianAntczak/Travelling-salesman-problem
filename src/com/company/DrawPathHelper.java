package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class DrawPathHelper {

    private int height;


    public void drawBestResults(Map<Integer, Point> points, TreeMap<Double, Algorithm> results) {
        final int[] i = {0};
        results.forEach((aDouble, algorithm) -> {
            System.out.println(aDouble);
            if (i[0] < 6) {
                try {
                    String className = algorithm.getClass().getSimpleName();
                    File dir = new File("." + File.separator + className);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    if(i[0] == 0) {
                        for (int x = 0; x < algorithm.getResultList().size(); x++) {
                            System.out.print(algorithm.getResultList().get(x).getIndex());
                            System.out.print(" -> ");
                        }
                    }

                    System.out.println();
                    String fileName = dir.getCanonicalPath() + File.separator + "points" + String.valueOf(aDouble).replace(".", "_");
                    saveToFile(points, algorithm.getResultList(), fileName, algorithm.getStartPoint());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            i[0]++;
        });
    }


    private void saveToFile(Map<Integer, Point> points, ArrayList<Point> path, String fileName, Point startPoint) throws IOException {
        int maxX = getMaxX(points);
        int maxY = getMaxY(points);
        BufferedImage img = new BufferedImage(maxX + 150, maxY + 150, BufferedImage.TYPE_INT_RGB);
        height = img.getHeight();
        fillBackground(img);

        Graphics2D graphics = img.createGraphics();
        graphics.drawRect(0, 0, img.getWidth(), img.getHeight());
        graphics.setStroke(new BasicStroke(5));
        graphics.setFont(new Font("Tahoma", Font.PLAIN, 50));

        drawPoints(points, graphics);
        drawLines(graphics, path);
        drawStartPoint(startPoint, graphics);
        graphics.dispose();

        ImageIO.write(img, "PNG", new File(fileName + ".png"));
    }

    private void drawStartPoint(Point startPoint, Graphics2D graphics) {
        graphics.setStroke(new BasicStroke(30));
        graphics.setColor(Color.BLUE);
        graphics.drawOval(startPoint.getX(), yToScreen(startPoint.getY()), 10, 10);

    }

    private int getMaxY(Map<Integer, Point> points) {
        return Collections.max(points.entrySet(), Comparator.comparingInt(entry -> entry.getValue().getY())).getValue().getY();
    }

    private int getMaxX(Map<Integer, Point> points) {
        return Collections.max(points.entrySet(), Comparator.comparingInt(entry -> entry.getValue().getX())).getValue().getX();
    }

    private void drawLines(Graphics2D graphics, ArrayList<Point> path) {
        graphics.setStroke(new BasicStroke(25));
        graphics.setStroke(new BasicStroke(3));
        graphics.setColor(Color.black);
        for (int i = 0; i < path.size() - 1; i++) {
            Point point1 = path.get(i);
            Point point2 = path.get(i + 1);
            graphics.drawLine(point1.getX(), yToScreen(point1.getY()), point2.getX(), yToScreen(point2.getY()));

        }
        graphics.drawLine(path.get(0).getX(), yToScreen(path.get(0).getY()), path.get(path.size() - 1).getX(), yToScreen(path.get(path.size() - 1).getY()));
    }

    private int yToScreen(int y) {
        return height - y;
    }

    private void drawPoints(Map<Integer, Point> points, Graphics2D graphics) {
        graphics.setStroke(new BasicStroke(25));
        points.forEach((integer, point) -> {
            graphics.setColor(new Color(135, 135, 135));
            graphics.drawOval(point.getX(), yToScreen(point.getY()), 10, 10);
            graphics.setColor(new Color(137, 0, 0));
            graphics.drawString(String.valueOf(point.getIndex()), point.getX(), yToScreen(point.getY() - 20));
            graphics.setColor(new Color(18, 132, 1));
            graphics.drawString(String.valueOf(point.getProfit()), point.getX(), yToScreen(point.getY() + 60));
        });
    }

    private  void fillBackground(BufferedImage img) {
        int pixelWhite[] = {255, 255, 255};
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                img.getRaster().setPixel(i, j, pixelWhite);
            }
        }
    }
}
