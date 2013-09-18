package entity;

import java.awt.*;
import java.util.Random;

import camera.Camera;
import item.*;
import powerup.*;
import game.*;
import input.*;
import shape.Polygon2D;


public class Player extends Entity
{
	public float radius;	//radius of player
	double direction;
	private double speed;	//accel speed
	private boolean up,down,left,right = false;	//which keys are pressed
	Color color;
	public int controlScheme = 0;			//determines controls and color for player
//	private int shotLength = 30;			//how many frames to wait for the next shot
//	private int shootTimer = shotLength;	//shows how many frames ago the last shot was fired

	private long lastShot = System.currentTimeMillis();
	private long nextShot = System.currentTimeMillis();
	private int shotDelay = 450;
	
	public double bulletSize;
	double bulletSpeed;

	public final double friction = .99;	//deceleration of player each frame
	public final double restitution = .6;	//bounciness from walls

	int explosionTimer = 100; //explosion will show for explosionTimer frames

	int hits = 0;	//how many times this player has hit another
	public final int healthMax = 50;	//max (starting) health
	public int health = healthMax;

	public boolean shielded;
	public boolean shieldDrawn;
	public boolean tripleShot;
	public boolean fastShot;

	public boolean alive = true;	//shows whether player is alive (prevents bullets from hitting exploding players)
	
	public Player(int c)
	{
		controlScheme = c;

		switch(c)	//sets color according to controls
		{
		case 0:
			color = new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
			break;
		case 1:
			color = Color.blue;
			break;
		case 2:
			color = Color.red;
			break;
		case 3:
			color = Color.green;
			break;
		case 4:
			color = Color.magenta;
			break;
		default:	//defaults to random color
			color = new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
			break;
		}

		direction = Math.random()*2*Math.PI;	//random direction
		radius = 30;
		speed = .3;		//Max speed will be (speed/(1-friction))


		xPos = 0;
        yPos = 0;

		shielded = shieldDrawn = tripleShot = false;

		bulletSize = radius /6;
		bulletSpeed = 10;
	}

	long lastDownscale = System.currentTimeMillis();
	int downscaleDelay = 15;
	long nextDownscale = System.currentTimeMillis() + downscaleDelay;
	boolean initialGrowth = false;
	public void draw(Graphics2D g)
	{
		shieldDrawn = false;
		
		if(alive)
		{
			//draw player if alive
			float jetSize = 1.3f;	//sets jet's length
			int jetAngle = 15;	//sets jet's width

			if(up)	//if up is held, draw flame
			{
				Random r = new Random();
				float flameSize = radius *2/3;
                float jetX = (float) (xPos + radius * jetSize * Math.cos(direction + Math.PI));
                float jetY = (float) (yPos + radius * jetSize * Math.sin(direction + Math.PI));
				if(r.nextBoolean()||r.nextBoolean()||r.nextBoolean())	// 7/8 chance to draw each flame part.  1/8 to skip. (creates random flicker)
				{
					g.setColor(Color.red);
					Camera.fillCenteredOval(jetX, jetY, flameSize, flameSize, g);
				}
				flameSize *= (double)2/3;
				if(r.nextBoolean()||r.nextBoolean()||r.nextBoolean())
				{
					g.setColor(Color.orange);
					Camera.fillCenteredOval(jetX, jetY, flameSize, flameSize, g);
				}
				flameSize *= (double)1/2;
				if(r.nextBoolean()||r.nextBoolean()||r.nextBoolean())
				{
					g.setColor(Color.yellow);
					Camera.fillCenteredOval(jetX, jetY, flameSize, flameSize, g);
				}
			}

			g.setColor(Color.gray);	//create jet polygon
			Polygon2D jet = new Polygon2D();
			jet.addPoint(xPos, yPos);
			jet.addPoint((float) (xPos - radius *jetSize*Math.cos(direction - Math.toRadians(jetAngle))), (float) (yPos - radius *jetSize*Math.sin(direction - Math.toRadians(jetAngle))));
			jet.addPoint((float) (xPos - radius *jetSize*Math.cos(direction + Math.toRadians(jetAngle))), (float) (yPos - radius *jetSize*Math.sin(direction + Math.toRadians(jetAngle))));
			Camera.fillPolygon2D(jet, g);			//draw jet
			g.setColor(Color.black);
			Camera.drawPolygon2D(jet, g);			//draw jet outline

			float healthSize = radius*2 * health/healthMax;	//radius of health ring

			g.setColor(Color.black);	//draw initial circle
			Camera.fillCenteredOval(xPos, yPos, radius*2, radius*2, g);

			g.setColor(color);			//draw colored center
			Camera.fillCenteredOval(xPos, yPos, healthSize, healthSize, g);

			g.setColor(color);			//draw colored direction pointer
			Camera.fillArc(xPos, yPos, radius, radius,(int)(Math.toDegrees(direction)-15),30,g);

			g.setColor(Color.black);	//draw inside direction pointer
			Camera.fillArc(xPos, yPos, healthSize, healthSize,(int)(Math.toDegrees(direction)-15),30,g);

			g.setColor(Color.black);	//draw outer black ring
			Camera.drawCenteredOval(xPos, yPos, radius*2, radius*2, g);
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
			
			//draw explosion if not alive
			if(downscale)
				radius *= .9;
			g.setColor(Color.red);
			Camera.fillOval(xPos- radius, yPos- radius, radius *2, radius *2,g);
			double size2 = (double)2/3* radius;	//scales next part of explosion down a bit
			g.setColor(Color.orange);
			Camera.fillOval((float)(xPos-size2), (float)(yPos-size2),(float)(size2*2),(float)(size2*2),g);
			double size3 = (double)1/2*size2;	//scales next part of explosion down a bit
			g.setColor(Color.yellow);
			Camera.fillOval((float)(xPos-size3), (float)(yPos-size3),(float)(size3*2),(float)(size3*2),g);
		}
	}

	public void tick(int del)
	{
		double delta = (double)del/15;

		if(!alive)	//if dead, do nothing
		{
			if(explosionTimer < 0)
				removeFromWorld();	//remove when done exploding
			else
				explosionTimer--;	//count down to removal
			return;
		}
		if(health <= 0)	//kill if health is less than zero	
			alive = false;

		updateControls(delta);	//updates control variables

		if(up)
		{
			xVel += (speed * Math.cos(direction))*delta;	//applies force in player's direction
			yVel += (speed * Math.sin(direction))*delta;
		}
		if(down)
		{
			if(System.currentTimeMillis() >= nextShot)
				shoot();
		}
		if(left)		//rotate
			direction += .1*delta;
		if(right)
			direction -= .1*delta;

        if(Input.Q)
            Camera.scale *= 1.001;
        if(Input.Z)
            Camera.scale /= 1.001;

		direction %= 2*Math.PI;	//prevent over-rotating.  Comment this line out and the player's direction arcs won't draw properly

		xVel *= Math.pow(friction, delta);	//apply deceleration force
		yVel *= Math.pow(friction, delta);

		xPos+=xVel*delta;	//move player
		yPos+=yVel*delta;

		updateBoundingBox();
        checkPowerUps();
	}

	public void shoot()
	{
		lastShot = System.currentTimeMillis();
		if(fastShot)
			nextShot = lastShot + shotDelay/2;
		else
			nextShot = lastShot + shotDelay;
		
		Bullet b = new Bullet(this,bulletSize,bulletSpeed);	//add bullet to list of bullets
		BallGameStatic.bullets.add(b);

		xVel -= b.speed*Math.cos(direction)*(Math.pow(b.size/ radius,2));	//apply knockback force
		yVel -= b.speed*Math.sin(direction)*(Math.pow(b.size/ radius,2));
		
		if(tripleShot)
			shootTriple(direction);
	}
	
	public void shootTriple(double direction)
	{
		double tripleShotAngle = .6;
		
		for(int i = 0; i < 2; i++)
		{
			Bullet b = new Bullet(this,bulletSize,bulletSpeed);
			b.xPos = xPos + radius *Math.cos(direction+Math.pow(-1,i)*tripleShotAngle);
			b.yPos = yPos + radius *Math.sin(direction+Math.pow(-1,i)*tripleShotAngle);
			b.xVel = xVel + bulletSpeed*Math.cos(direction+Math.pow(-1,i)*tripleShotAngle);
			b.yVel = yVel + bulletSpeed*Math.sin(direction+Math.pow(-1,i)*tripleShotAngle);
			b.speed = Math.sqrt(Math.pow(xVel,2)+Math.pow(yVel,2));
			BallGameStatic.bullets.add(b);
		}
	}

	public void updateControls(double delta)
	{
		switch(controlScheme)	//update controls according to control scheme
		{
		case 1:
			up		= Input.UP;
			down	= Input.DOWN;
			left	= Input.LEFT;
			right	= Input.RIGHT;
			break;
		case 2:
			up		= Input.W;
			down	= Input.S;
			left	= Input.A;
			right	= Input.D;
			break;
		case 3:
			up		= Input.I;
			down	= Input.K;
			left	= Input.J;
			right	= Input.L;
			break;
		case 4:
			up		= Input.MOUSE_LEFT;
			down	= Input.MOUSE_RIGHT;
			left = right = false;			

			double delta_x = Input.MOUSE_X - xPos;
			double delta_y = Input.MOUSE_Y - yPos;
			double angle = Math.toDegrees(Math.atan2(delta_y, delta_x));

			double difference = angle - Math.toDegrees(direction);
			while (difference < -180) difference += 360;
			while (difference > 180) difference -= 360;   		//all this is to determine direction to turn

			if(Math.abs(difference) > Math.toDegrees(.1) * delta)
				if(difference > 0)
					right = true;
				else
					left = true;
			else
				direction = Math.toRadians(angle);

			break;
		default:	//default to nothing
			break;
		}
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
        if(other instanceof Player)
            onCollide((Player)other);
	}

    public void onCollide(Player other) {
        double thisAngle = Math.atan2(yPos-other.yPos,xPos-other.xPos);
        double targAngle = Math.atan2(other.yPos-yPos,other.xPos-xPos);

        double inside = Math.abs(distance(xPos,yPos,other.xPos,other.yPos)- radius -other.radius);

        xPos += (inside/2)*Math.cos(thisAngle);
        yPos += (inside/2)*Math.sin(thisAngle);
        other.xPos += (inside/2)*Math.cos(targAngle);
        other.yPos += (inside/2)*Math.sin(targAngle);

        double thisSpeed = Math.sqrt(Math.pow(xVel,2)+Math.pow(yVel,2));
        double targSpeed = Math.sqrt(Math.pow(other.xVel,2)+Math.pow(other.yVel,2));

        double massRatio = Math.pow(other.radius / radius,2);

        xVel += massRatio*targSpeed*Math.cos(thisAngle)*Math.pow(restitution,2);
        yVel += massRatio*targSpeed*Math.sin(thisAngle)*Math.pow(restitution,2);

        other.xVel += (1/massRatio)*thisSpeed*Math.cos(targAngle)*Math.pow(restitution,2);
        other.yVel += (1/massRatio)*thisSpeed*Math.sin(targAngle)*Math.pow(restitution,2);
    }

	public void checkPowerUps()
	{
		for(int i = 0; i < BallGameStatic.items.size(); i++)
		{
			Item p = BallGameStatic.items.get(i);
			if(p.colliding(this))
				p.givePowerUp(this);
		}
	}

	public void removeFromWorld()	//removes self from main list of players
	{
		BallGameStatic.players.remove(BallGameStatic.players.indexOf(this));
	}

	public void addHealth(int delta)
	{
		health += delta;
		if(health > healthMax)
			health = healthMax;
	}

	public void giveShield(int durationSeconds)
	{
		BallGameStatic.powerups.add(new Shield(this,durationSeconds));
	}
	
	public void giveOrbital(int durationSeconds, OrbitalItem item)
	{
		BallGameStatic.powerups.add(new Orbital(this,durationSeconds,item));
	}
	
	public void giveTripleShot(int durationSeconds)
	{
		BallGameStatic.powerups.add(new TripleShot(this,durationSeconds));
	}
	
	public void giveFastShot(int durationSeconds)
	{
		BallGameStatic.powerups.add(new FastShot(this,durationSeconds));
	}

	//simple distance formula.  Shouldn't really be in here.
	double distance(double x1, double y1, double x2, double y2){return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));}
}