package item;

import camera.Camera;
import entity.Player;
import shape.Polygon2D;

import java.awt.*;


public class HealthItem extends Item {
    static double refillMax = .2;
    static double refillMin = .1;

    static final float CROSS_OUTSIDE = .5f;
    static final float CROSS_INSIDE = .2f;

    double percentRefill;

    public HealthItem() {
        super();
        percentRefill = Math.random() * (refillMax - refillMin) + refillMin;
        color = Color.red;
    }

    public void supplyEffect(Player p) {
        p.addHealth((int) (p.HEALTHMAX * percentRefill));
    }

    public void drawEffect(Graphics2D g) {
        Polygon2D cross = new Polygon2D();        //will be the health cross

        cross.addPoint(xPos - radius * CROSS_INSIDE, yPos - radius * CROSS_INSIDE);
        cross.addPoint(xPos - radius * CROSS_INSIDE, yPos - radius * CROSS_OUTSIDE);
        cross.addPoint(xPos + radius * CROSS_INSIDE, yPos - radius * CROSS_OUTSIDE);

        cross.addPoint(xPos + radius * CROSS_INSIDE, yPos - radius * CROSS_INSIDE);
        cross.addPoint(xPos + radius * CROSS_OUTSIDE, yPos - radius * CROSS_INSIDE);
        cross.addPoint(xPos + radius * CROSS_OUTSIDE, yPos + radius * CROSS_INSIDE);

        cross.addPoint(xPos + radius * CROSS_INSIDE, yPos + radius * CROSS_INSIDE);
        cross.addPoint(xPos + radius * CROSS_INSIDE, yPos + radius * CROSS_OUTSIDE);
        cross.addPoint(xPos - radius * CROSS_INSIDE, yPos + radius * CROSS_OUTSIDE);

        cross.addPoint(xPos - radius * CROSS_INSIDE, yPos + radius * CROSS_INSIDE);
        cross.addPoint(xPos - radius * CROSS_OUTSIDE, yPos + radius * CROSS_INSIDE);
        cross.addPoint(xPos - radius * CROSS_OUTSIDE, yPos - radius * CROSS_INSIDE);

        g.setColor(Color.white);
        Camera.fillPolygon2D(cross, g);
    }
}
