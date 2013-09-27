package item;

import camera.Camera;
import entity.Entity;
import entity.Player;
import game.MainClass;
import shape.Polygon2D;

import java.awt.*;


public class Item extends Entity {
    float speed;
    float targxPos;
    float targyPos;

    float startDistance;

    Color color;

    float radius;

    public Item() {
        speed = (float) (Math.random() + 1) / 15;

        color = Color.white;

        radius = 10;

        startDistance = Math.max(MainClass.frame.getWidth() + 10, MainClass.frame.getHeight() + 10) + radius;
        double angle = Math.toRadians(Math.random() * 360);        //random starting location
        xPos = (float) (startDistance * Math.cos(angle));
        yPos = (float) (startDistance * Math.sin(angle));

        targxPos = (int) (Math.random() * ((MainClass.frame.getWidth()) - radius * 2) + radius);    //random location
        targyPos = (int) (Math.random() * ((MainClass.frame.getHeight()) - radius * 2) + radius);

        double travelAngle = Math.atan2(targyPos - yPos, targxPos - xPos);

        xVel = (float) (speed * Math.cos(travelAngle));
        yVel = (float) (speed * Math.sin(travelAngle));
    }

    public void supplyEffect(Player p) {
        //Does nothing.  Extend this class to add effects.
    }

    public void givePowerUp(Player p) {
        supplyEffect(p);
        removeFromList();
    }

    public void move(int delta) {
        if (isOutofView())
            removeFromList();

        xPos += xVel * delta;
        yPos += yVel * delta;
    }

    boolean isOutofView() {
        if (xPos - radius > MainClass.frame.getWidth() / 2 + 10 && xVel > 0)
            return true;
        if (xPos + radius < -MainClass.frame.getWidth() / 2 - 10 && xVel < 0)
            return true;
        if (yPos - radius > MainClass.frame.getHeight() / 2 + 10 && yVel > 0)
            return true;
        if (yPos + radius < -MainClass.frame.getHeight() / 2 - 10 && yVel < 0)
            return true;
        return false;
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        Camera.fillCenteredOval(xPos, yPos, radius * 2, radius * 2, g);
        g.setColor(Color.black);
        Camera.drawCenteredOval(xPos, yPos, radius * 2, radius * 2, g);
        drawEffect(g);
    }

    public void onCollide(Entity e) {
        if (e instanceof Player)
            onCollide((Player) e);
    }

    public void onCollide(Player p) {
        givePowerUp(p);
    }

    public void drawEffect(Graphics2D g) {
        //Add effect to extended classes
    }

    public void updateBoundingBox() {
        boundingBox = new Polygon2D();
        int numPoints = 12;
        for (int i = 0; i < numPoints; i++) {
            double angle = i * Math.PI * 2 / numPoints;
            float pointX = (float) (radius * Math.cos(angle) + xPos);
            float pointY = (float) (radius * Math.sin(angle) + yPos);
            boundingBox.addPoint(pointX, pointY);
        }
    }
}
