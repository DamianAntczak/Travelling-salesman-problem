package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Comparator;
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

            points.forEach((integer, point) -> {
                int pixel[] = {0,0,0};
                for (int size = 1 ; size < 12; size++) {
                    img.getRaster().setPixel(point.getX() - size, point.getY() - size, pixel);
                    img.getRaster().setPixel(point.getX() + size, point.getY() + size, pixel);
                    img.getRaster().setPixel(point.getX() - size, point.getY() + size, pixel);
                    img.getRaster().setPixel(point.getX() + size, point.getY() - size, pixel);
                }
                img.getRaster().setPixel(point.getX(), point.getY(), pixel);
            });

            ImageIO.write(img, "BMP", new File("points.bmp"));

            GreedyCycle greedyCycle = new GreedyCycle();
            greedyCycle.execute(points.get(1), points);






        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
