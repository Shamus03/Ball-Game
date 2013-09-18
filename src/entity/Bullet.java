package entity;
import camera.Camera;
import game.BallGameStatic;

import java.awt.*;

import powerup.Orbital;
import powerup.Powerup;
import shape.Polygon2D;

public class Bullet extends Entity
{
	private Color color;
	public float radius;	//radius of bullet
    
	public double speed;	//Constant speed.  No FRICTION for bullets.
	Player parent;	//Sets parent player.  Will not interact with parent.
	boolean alive = true;
	int explosionTimer = 50; //explosion will show for explosionTimer milliseconds

	static final double homingForce = 3;

	public int bounces = 2;	//How many times it will bounce before going away
    public float size;

    public Bullet(Player p, float si, float sp)
	{				
		parent = p;
		radius = si;
		speed = sp;

		color = parent.color;

		xPos = (float) (parent.xPos + parent.radius *Math.cos(parent.direction));	//sets position to just in front of parent player
		yPos = (float) (parent.yPos + parent.radius *Math.sin(parent.direction));

		xVel = (float) (speed * Math.cos(parent.direction) + parent.xVel);	//sets velocities relative to player
		yVel = (float) (speed * Math.sin(parent.direction) + parent.yVel);

		speed = Math.sqrt(Math.pow(xVel,2)+Math.pow(yVel,2));	//changes speed variable for use in later calculations
	}

	long lastDownscale = System.currentTimeMillis();
	int downscaleDelay = 15;
	long nextDownscale = System.currentTimeMillis() + downscaleDelay;
	boolean initialGrowth = false;
	public void draw(Graphics2D g)
	{
		if(alive)
		{
			//draw bullet if alive
			g.setColor(color);
			Camera.fillOval(xPos, yPos, radius*2, radius*2, g);
			g.setColor(Color.black);
			Camera.drawOval(xPos, yPos, radius*2, radius*2, g);
		}
		else
		{
			if(!initialGrowth)
			{
				radius *= 2.7;
				initialGrowth = true;
			}
			
			boolean downscale = false;
			if(System.currentTimeMillis() >= nextDownscale)
			{
				downscale = true;
				lastDownscale = System.currentTimeMillis();
				nextDownscale = lastDownscale + downscaleDelay;
			}
			
			//draw explosion if dead
			if(downscale)
				radius *= .9;	//same as player explosion
			g.setColor(Color.red);
			Camera.fillCenteredOval(xPos, yPos, radius*2, radius*2, g);
			float radius2 = 2/3f*radius;	//scales next part of explosion down a bit
			g.setColor(Color.orange);
            Camera.fillCenteredOval(xPos, yPos, radius2*2, radius2*2, g);
			float radius3 = 1/2f*radius2;	//scales next part of explosion down a bit
			g.setColor(Color.yellow);
            Camera.fillCenteredOval(xPos, yPos, radius3*2, radius3*2, g);
		}
	}

	public void move(int del)
	{		
		double delta = (double)del/15;
		if(!alive)	//handles explosion
		{
			if(explosionTimer < 0)
				removeFromWorld();	//remove if done exploding
			else
				explosionTimer--;	//count down to removal
			return;
		}

		gravitateToPlayers(delta);
		gravitateToBullets(delta);

		xPos+=xVel*delta;	//apply movement
		yPos+=yVel*delta;
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

    public void onCollide(Entity other) {
        if(alive)
            if(other instanceof Bullet)
                onCollide((Bullet)other);
            else if(other instanceof Orbital)
                onCollide((Orbital)other);
            else if(other instanceof Player)
                onCollide((Player)other);
    }

    public void onCollide(Bullet other) {
        if(other.alive)
            alive = other.alive = false;
    }

    public void onCollide(Orbital other) {
        if(other.parent != this.parent)	//make sure to not hit its compadre
            if(other.colliding(this))	//If they're colliding kill itself
            {
                other.health--;
                alive = false;
            }
    }

    public void onCollide(Player other) {
        if(other.alive && other != parent) {
            hit(other);
            alive = false;
        }
    }

	public void gravitateToPlayers(double delta)
	{		
		for(int i = 0; i < BallGameStatic.players. size(); i++)
		{
			Player p = BallGameStatic.players.get(i);
			if(p != parent)
			{
				double angle = Math.atan2(p.yPos-yPos,p.xPos-xPos);
				double force = homingForce*Math.pow(Math.PI,2)*Math.pow(radius,2)*Math.pow(p.radius,2)/Math.pow(distance(xPos,yPos,p.xPos,p.yPos),2);
				double mass = Math.PI*Math.pow(radius,2);
				double pmass =  Math.PI*Math.pow(p.radius,2);

				xVel += (force*Math.cos(angle)/mass)*delta;
				yVel += (force*Math.sin(angle)/mass)*delta;
				speed = Math.sqrt(Math.pow(xVel,2)+Math.pow(yVel,2));

				p.xVel -= (force*Math.cos(angle)/pmass)*delta;
				p.yVel -= (force*Math.sin(angle)/pmass)*delta;
			}
		}
	}

	public void gravitateToBullets(double delta)
	{
		for(int i = 0; i < BallGameStatic.bullets.size(); i++)
		{
			Bullet b = BallGameStatic.bullets.get(i);
			if(b != this)
			{
				double angle = Math.atan2(b.yPos-yPos,b.xPos-xPos);
				double force = homingForce*Math.pow(Math.PI,2)*Math.pow(radius,2)*Math.pow(b.radius,2)/Math.pow(distance(xPos,yPos,b.xPos,b.yPos),2);
				double mass = Math.PI*radius*radius;

				xVel += (force*Math.cos(angle)/mass)*delta;
				yVel += (force*Math.sin(angle)/mass)*delta;
				speed = Math.sqrt(Math.pow(xVel,2)+Math.pow(yVel,2));
			}
		}
	}

	public void hit(Player targ)	//bullet hits player
	{
		if(!targ.shielded)
		{
			parent.hits++;	//add a hit to its parent
			targ.health--;	//lower victim's health
		}

		double direction = Math.atan2(targ.yPos-yPos, targ.xPos-xPos);	//get collision angle

		targ.xVel += speed*Math.cos(direction)*(Math.pow(radius,2)/Math.pow(targ.radius,2));	//apply force to player for knockback
		targ.yVel += speed*Math.sin(direction)*(Math.pow(radius,2)/Math.pow(targ.radius,2));
	}

	public void removeFromWorld()	//remove from main list of bullets
	{
		BallGameStatic.bullets.remove(BallGameStatic.bullets.indexOf(this));
	}

	//simple distance formula.  Shouldn't really be in here.
	double distance(double x1, double y1, double x2, double y2){return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));}
}