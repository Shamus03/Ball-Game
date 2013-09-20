package entity;

import camera.Camera;
import shape.Polygon2D;

import java.awt.*;

public class Wall extends Entity {

    public float xMin;
    public float yMin;
    public float xMax;
    public float yMax;
    public double ULangle;
    public double URangle;
    public double LLangle;
    public double LRangle;

    public Wall(int x1, int y1, int x2, int y2) {
        boundingBox = new Polygon2D();
        color = Color.gray;

        boundingBox.addPoint(x1, y1);
        boundingBox.addPoint(x2, y1);
        boundingBox.addPoint(x2, y2);
        boundingBox.addPoint(x1, y2);

        xMin = (float) boundingBox.getBounds().getMinX();
        yMin = (float) boundingBox.getBounds().getMinY();
        xMax = (float) boundingBox.getBounds().getMaxX();
        yMax = (float) boundingBox.getBounds().getMaxY();

        xPos = (xMin + xMax) / 2;
        yPos = (yMin + yMax) / 2;

        ULangle = Math.atan2(yMax - yPos, xMin - xPos);
        URangle = Math.atan2(yMax - yPos, xMax - xPos);
        LLangle = Math.atan2(yMin - yPos, xMin - xPos);
        LRangle = Math.atan2(yMin - yPos, xMax - xPos);
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        Camera.fillRect(xMin, yMax, xMax - xMin, yMax - yMin, g);
        g.setColor(Color.black);
        Camera.drawRect(xMin, yMax, xMax - xMin, yMax - yMin, g);
    }
}