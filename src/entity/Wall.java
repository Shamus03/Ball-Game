package entity;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import camera.Camera;
import powerup.Orbital;
import shape.Polygon2D;

public class Wall extends Entity
{

	float xMin,yMin,xMax,yMax;
    double ULangle, URangle, LLangle, LRangle;

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