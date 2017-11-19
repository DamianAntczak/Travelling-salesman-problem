package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
	    PointsReader pointsReader = new PointsReader();
        try {
            pointsReader.loadKorA100();
            pointsReader.loadKorB100();
            pointsReader.printPoints();
            System.out.println();

            Map<Integer, Point> points = pointsReader.getPoints();

            int maxX = Collections.max(points.entrySet(), Comparator.comparingInt(entry -> entry.getValue().getX())).getValue().getX();
            int maxY = Collections.max(points.entrySet(), Comparator.comparingInt(entry -> entry.getValue().getY())).getValue().getY();
            BufferedImage img = new BufferedImage(maxX + 50, maxY + 50, BufferedImage.TYPE_INT_RGB);

            int pixelWhite[] = {255,255,255};
            for (int i = 0 ; i < img.getWidth(); i++){
                for (int j = 0 ; j < img.getHeight(); j++){
                    img.getRaster().setPixel(i,j,pixelWhite);
                }
            }

            Graphics2D graphics = img.createGraphics();
            graphics.setStroke(new BasicStroke(5));
            graphics.setFont(new Font("Tahoma", Font.PLAIN, 50));

            points.forEach((integer, point) -> {
                int pixel[] = {0,0,0};
                for (int size = 1 ; size < 12; size++) {
                    graphics.setColor(Color.BLUE);
                    graphics.drawOval(point.getX(), point.getY(), 10, 10);
                    graphics.setColor(Color.RED);
                    graphics.setStroke(new BasicStroke(25));
                    graphics.drawString(String.valueOf(point.getIndex()), point.getX(),point.getY());
                }
                img.getRaster().setPixel(point.getX(), point.getY(), pixel);
            });
            GreedyCycle greedyCycle = new GreedyCycle();
            greedyCycle.execute(points.get(1),points);
            LinkedList<Point> path = greedyCycle.getCycle();

            graphics.setStroke(new BasicStroke(3));
            graphics.setColor(Color.black);
            for (int i = 0; i < path.size() - 2; i++){
                Point point1 = path.get(i);
                Point point2 = path.get(i + 1);
                graphics.drawLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());
            }
            graphics.drawLine(path.get(0).getX(),path.get(0).getY(), path.get(path.size()-1).getX(), path.get(path.size()-1).getY());
            graphics.dispose();


            ImageIO.write(img, "BMP", new File("points.bmp"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
