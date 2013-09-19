package powerup;

import camera.Camera;
import entity.Bullet;
import entity.Entity;
import entity.Player;
import entity.Wall;
import game.BallGameStatic;

import item.OrbitalItem;
import shape.Polygon2D;

import java.awt.*;

public class Orbital extends Powerup
{	
	public static final float RESTITUTION = .5f;
	static final float ATTRACT_FORCE = .01f;
	
	static final float FRICTION = .999f;
	
	public float radius;
	
	public boolean alive = true;
	
	public int health = 10;
	
	Color color;
	
	public Orbital(Player parent, int durationSeconds, OrbitalItem item) {
		super(parent, durationSeconds);
		
		xPos = item.getxPos();
		yPos = item.getyPos();
		
		xVel = item.getxVel();
		yVel = item.getyVel();
		
		radius = 10;
		
		color = Color.gray;

        updateBoundingBox();
	}
	
	public void draw(Graphics2D g) {
		if(!colliding(parent))
		{
			double angle = Math.atan2(yPos- parent.getyPos(),xPos- parent.getxPos());
			float endX = (float)(parent.getxPos() + parent.radius *Math.cos(angle));
			float endY = (float)(parent.getyPos() + parent.radius *Math.sin(angle));
            float startX = (float)(xPos - radius * Math.cos(angle));
            float startY = (float)(yPos - radius * Math.sin(angle));

			g.setColor(Color.black);
			Camera.drawLine(startX, startY, endX, endY, g);
		}
		
		g.setColor(color);
		Camera.fillCenteredOval(xPos, yPos, radius * 2, radius * 2, g);
		g.setColor(Color.black);
		Camera.drawCenteredOval(xPos, yPos, radius * 2, radius * 2, g);
	}
	
	public void tick(int delta) {
		super.tick(delta);
		
		springToPlayer(delta);
		
		xVel *= Math.pow(FRICTION, delta);
		yVel *= Math.pow(FRICTION, delta);

		xPos+=xVel*delta;
		yPos+=yVel*delta;

        updateBoundingBox();
	}

    public void updateBoundingBox() {
        boundingBox = new Polygon2D();
        int numPoints = 12;
        for(int i = 0; i < numPoints; i++) {
            double angle = i*Math.PI*2/numPoints;
            float pointX = (float) (radius * Math.cos(angle) + xPos);
            float pointY = (float) (radius * Math.sin(angle) + yPos);
            boundingBox.addPoint(pointX, pointY);
        }
    }
	
	public void springToPlayer(int delta) {
		double angle = Math.atan2(parent.getyPos() - yPos, parent.getxPos() - xPos);
		
		double distance = distance(xPos,yPos, parent.getxPos(), parent.getyPos()) - (parent.radius *1.5);
		if(distance < 0)
			distance = 0;
		
		double force = ATTRACT_FORCE *Math.pow(distance,1.1);
		double accel = force/(Math.PI*Math.pow(radius,2));
		
		xVel += accel*Math.cos(angle)*delta;
		yVel += accel*Math.sin(angle)*delta;
	}
	
	long lastHit = System.currentTimeMillis();
	int hitDelay = 20;
	long nextHit = System.currentTimeMillis() + hitDelay;
	public void hit(Player p)
	{		
		if(System.currentTimeMillis() >= nextHit)
		{
			lastHit = System.currentTimeMillis();
			nextHit = lastHit + hitDelay;
			
			Bullet b = new Bullet(parent, parent.bulletSize, 0);
			double angle = Math.atan2(yPos- p.getyPos(),xPos- p.getxPos());
			b.setxVel(0);
            b.setyVel(0);
			b.bounces = 0;
			b.setxPos((float) (p.getxPos() + p.radius * Math.cos(angle)));
			b.setyPos((float) (p.getyPos() + p.radius * Math.sin(angle)));
			b.addToList();

			health--;
			if(health <= 0)
				removeFromList();
		}
	}

    public void onCollide(Entity other) {
        if(other instanceof Player)
            onCollide((Player)other);
        if(other instanceof Orbital)
            onCollide((Orbital)other);
        if(other instanceof Wall)
            onCollide((Wall)other);
    }

    public void onCollide(Player other) {
        if(other == parent)
            return;

        double thisAngle = Math.atan2(yPos- other.getyPos(),xPos- other.getxPos());
        double targAngle = Math.atan2(other.getyPos() -yPos, other.getxPos() -xPos);

        float inside = (float) Math.abs(distance(xPos,yPos, other.getxPos(), other.getyPos())- radius -other.radius);

        xPos += (inside/2)*Math.cos(thisAngle);
        yPos += (inside/2)*Math.sin(thisAngle);
        other.setxPos((float) ((inside / 2) * Math.cos(targAngle)) + other.getxPos());
        other.setyPos((float) ((inside / 2) * Math.sin(targAngle)) + other.getyPos());

        float thisSpeed = (float) Math.sqrt(Math.pow(xVel,2)+Math.pow(yVel,2));
        float targSpeed = (float) Math.sqrt(Math.pow(other.getxVel(),2)+Math.pow(other.getyVel(),2));

        double massRatio = Math.pow(other.radius / radius,2);

        xVel += massRatio*targSpeed*Math.cos(thisAngle)*Math.pow(RESTITUTION,2);
        yVel += massRatio*targSpeed*Math.sin(thisAngle)*Math.pow(RESTITUTION,2);

        other.setxVel((float) ((1 / massRatio) * thisSpeed * Math.cos(targAngle) * Math.pow(RESTITUTION, 2)) + other.getxVel());
        other.setyVel((float) ((1 / massRatio) * thisSpeed * Math.sin(targAngle) * Math.pow(RESTITUTION, 2)) + other.getyVel());

        hit(other);
    }

    public void onCollide(Wall other) {
        double collideAngle = Math.atan2(yPos - other.getyPos(), xPos - other.getxPos());

        if(collideAngle >= other.ULangle) {
            xVel = -Math.abs(xVel*RESTITUTION);
            xPos = other.xMin - radius;
        }
        else if(collideAngle >= other.URangle) {
            yVel = Math.abs(yVel*RESTITUTION);
            yPos = other.yMax + radius;
        }
        else if(collideAngle >= other.LRangle) {
            xVel = Math.abs(xVel*RESTITUTION);
            xPos = other.xMax + radius;
        }
        else if(collideAngle >= other.LLangle) {
            yVel = -Math.abs(yVel*RESTITUTION);
            yPos = other.yMin - radius;
        }
        else {
            xVel = -Math.abs(xVel*RESTITUTION);
            xPos = other.xMin - radius;
        }
    }
	
	public void onCollide(Orbital other)
	{		
		double thisAngle = Math.atan2(yPos-other.yPos,xPos-other.xPos);
		double targAngle = Math.atan2(other.yPos-yPos,other.xPos-xPos);

		double inside = Math.abs(distance(xPos,yPos,other.xPos,other.yPos)- radius -other.radius);

		xPos += (inside/2)*Math.cos(thisAngle);
		yPos += (inside/2)*Math.sin(thisAngle);
		other.xPos += (inside/2)*Math.cos(targAngle);
		other.yPos += (inside/2)*Math.sin(targAngle);

		double thisSpeed = Math.sqrt(Math.pow(xVel,2)+Math.pow(yVel,2));
		double targSpeed = Math.sqrt(Math.pow(other.xVel,2)+Math.pow(other.yVel,2));

		double massRatio = Math.pow(other.radius /(radius *2),2);

		xVel += massRatio*targSpeed*Math.cos(thisAngle)*Math.pow(RESTITUTION,2);
		yVel += massRatio*targSpeed*Math.sin(thisAngle)*Math.pow(RESTITUTION,2);

		other.xVel += (1/massRatio)*thisSpeed*Math.cos(targAngle)*Math.pow(RESTITUTION,2);
		other.yVel += (1/massRatio)*thisSpeed*Math.sin(targAngle)*Math.pow(RESTITUTION,2);
	}
	
	//simple distance formula.  Shouldn't really be in here.
	public double distance(double x1, double y1, double x2, double y2){return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));}
}
