package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Random;

import item.*;
import powerup.*;
import game.*;
import input.*;


public class Player
{
	public double size;	//radius of player
	public double xPos;
	public double yPos;
	public double xVel;
	public double yVel;
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
		size = 30;
		speed = .3;		//Max speed will be (speed/(1-friction))


		xPos = (int)(Math.random()*((BallGame.width-BallGame.leftBounds)-size*2)+size+BallGame.leftBounds);	//random location
		yPos = (int)(Math.random()*((BallGame.height-BallGame.topBounds)-size*2)+size+BallGame.topBounds);

		boolean validLocation = false;
		while(!validLocation)
		{
			validLocation = true;
			for(int i = 0; i < BallGame.walls.size(); i++)
			{
				if(BallGame.walls.get(i).bounds.contains(xPos,yPos))
				{
					validLocation = false;
					xPos = (int)(Math.random()*((BallGame.width-BallGame.leftBounds)-size*2)+size+BallGame.leftBounds);	//random location
					yPos = (int)(Math.random()*((BallGame.height-BallGame.topBounds)-size*2)+size+BallGame.topBounds);
				}
				if(!validLocation)
					break;
			}
		}

		shielded = shieldDrawn = tripleShot = false;

		bulletSize = size/6;
		bulletSpeed = 10;
	}

	long lastDownscale = System.currentTimeMillis();
	int downscaleDelay = 15;
	long nextDownscale = System.currentTimeMillis() + downscaleDelay;
	boolean initialGrowth = false;
	public void draw(Graphics g)
	{
		shieldDrawn = false;
		
		if(alive)
		{
			//draw player if alive
			double jetSize = 1.3;	//sets jet's length
			int jetAngle = 15;	//sets jet's width

			if(up)	//if up is held, draw flame
			{
				Random r = new Random();
				double flameSize = size/3;
				if(r.nextBoolean()||r.nextBoolean()||r.nextBoolean())	// 7/8 chance to draw each flame part.  1/8 to skip. (creates random flicker)
				{
					g.setColor(Color.red);
					g.fillOval((int)(xPos + size*jetSize*Math.cos(direction + Math.PI)-flameSize),(int)(yPos + size*jetSize*Math.sin(direction + Math.PI)-flameSize),(int)(flameSize*2),(int)(flameSize*2));
				}
				flameSize *= (double)2/3;
				if(r.nextBoolean()||r.nextBoolean()||r.nextBoolean())
				{
					g.setColor(Color.orange);
					g.fillOval((int)(xPos + size*jetSize*Math.cos(direction + Math.PI)-flameSize),(int)(yPos + size*jetSize*Math.sin(direction + Math.PI)-flameSize),(int)(flameSize*2),(int)(flameSize*2));
				}
				flameSize *= (double)1/2;
				if(r.nextBoolean()||r.nextBoolean()||r.nextBoolean())
				{
					g.setColor(Color.yellow);
					g.fillOval((int)(xPos + size*jetSize*Math.cos(direction + Math.PI)-flameSize),(int)(yPos + size*jetSize*Math.sin(direction + Math.PI)-flameSize),(int)(flameSize*2),(int)(flameSize*2));
				}
			}

			g.setColor(Color.gray);	//create jet polygon
			Polygon jet = new Polygon();
			jet.addPoint((int)xPos,(int)yPos);
			jet.addPoint((int)(xPos - size*jetSize*Math.cos(direction - Math.toRadians(jetAngle))),(int)(yPos - size*jetSize*Math.sin(direction - Math.toRadians(jetAngle))));
			jet.addPoint((int)(xPos - size*jetSize*Math.cos(direction + Math.toRadians(jetAngle))),(int)(yPos - size*jetSize*Math.sin(direction + Math.toRadians(jetAngle))));
			g.fillPolygon(jet);			//draw jet
			g.setColor(Color.black);
			g.drawPolygon(jet);			//draw jet outline

			double healthSize = size * health/healthMax;	//size of health ring

			g.setColor(Color.black);	//draw initial circle
			g.fillOval((int)(xPos-size), (int)(yPos-size),(int)(size*2),(int)(size*2));

			g.setColor(color);			//draw colored center
			g.fillOval((int)(xPos-healthSize), (int)(yPos-healthSize),(int)(healthSize*2),(int)(healthSize*2));

			g.setColor(color);			//draw colored direction pointer
			g.fillArc((int)(xPos-size),(int)(yPos-size),(int)(size*2),(int)(size*2),(int)(-Math.toDegrees(direction)-15),30);

			g.setColor(Color.black);	//draw inside direction pointer
			g.fillArc((int)(xPos-healthSize),(int)(yPos-healthSize),(int)(healthSize*2),(int)(healthSize*2),(int)(-Math.toDegrees(direction)-15),30);

			g.setColor(Color.black);	//draw outer black ring
			g.drawOval((int)(xPos-size), (int)(yPos-size),(int)(size*2),(int)(size*2));
		}
		else
		{
			if(!initialGrowth)
			{
				size *= 2.7;
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
				size *= .9;
			g.setColor(Color.red);
			g.fillOval((int)(xPos-size), (int)(yPos-size),(int)(size*2),(int)(size*2));
			double size2 = (double)2/3*size;	//scales next part of explosion down a bit
			g.setColor(Color.orange);
			g.fillOval((int)(xPos-size2), (int)(yPos-size2),(int)(size2*2),(int)(size2*2));
			double size3 = (double)1/2*size2;	//scales next part of explosion down a bit
			g.setColor(Color.yellow);
			g.fillOval((int)(xPos-size3), (int)(yPos-size3),(int)(size3*2),(int)(size3*2));
		}
	}

	public void move(int del)
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
			direction -= .1*delta;
		if(right)
			direction += .1*delta;

		direction %= 2*Math.PI;	//prevent over-rotating.  Comment this line out and the player's direction arcs won't draw properly

		xVel *= Math.pow(friction, delta);	//apply deceleration force
		yVel *= Math.pow(friction, delta);

		xPos+=xVel*delta;	//move player
		yPos+=yVel*delta;

		checkPowerUps();

		wallClip();	//bounce player off walls
	}

	public void shoot()
	{
		lastShot = System.currentTimeMillis();
		if(fastShot)
			nextShot = lastShot + shotDelay/2;
		else
			nextShot = lastShot + shotDelay;
		
		Bullet b = new Bullet(this,bulletSize,bulletSpeed);	//add bullet to list of bullets
		BallGame.bullets.add(b);

		xVel -= b.speed*Math.cos(direction)*(Math.pow(b.size/size,2));	//apply knockback force
		yVel -= b.speed*Math.sin(direction)*(Math.pow(b.size/size,2));
		
		if(tripleShot)
			shootTriple(direction);
	}
	
	public void shootTriple(double direction)
	{
		double tripleShotAngle = .6;
		
		for(int i = 0; i < 2; i++)
		{
			Bullet b = new Bullet(this,bulletSize,bulletSpeed);
			b.xPos = xPos + size*Math.cos(direction+Math.pow(-1,i)*tripleShotAngle);
			b.yPos = yPos + size*Math.sin(direction+Math.pow(-1,i)*tripleShotAngle);
			b.xVel = xVel + bulletSpeed*Math.cos(direction+Math.pow(-1,i)*tripleShotAngle);
			b.yVel = yVel + bulletSpeed*Math.sin(direction+Math.pow(-1,i)*tripleShotAngle);
			b.speed = Math.sqrt(Math.pow(xVel,2)+Math.pow(yVel,2));
			BallGame.bullets.add(b);
		}
	}

	public void updateControls(double delta)
	{
		switch(controlScheme)	//update controls according to control scheme
		{
		case 1:
			up		= InputHandler.UP;
			down	= InputHandler.DOWN;
			left	= InputHandler.LEFT;
			right	= InputHandler.RIGHT;
			break;
		case 2:
			up		= InputHandler.W;
			down	= InputHandler.S;
			left	= InputHandler.A;
			right	= InputHandler.D;
			break;
		case 3:
			up		= InputHandler.I;
			down	= InputHandler.K;
			left	= InputHandler.J;
			right	= InputHandler.L;
			break;
		case 4:
			up		= InputHandler.MOUSE_LEFT;
			down	= InputHandler.MOUSE_RIGHT;
			left = right = false;			

			double delta_x = InputHandler.MOUSE_X - xPos;
			double delta_y = InputHandler.MOUSE_Y - yPos;
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

	public void wallClip()	//bounce off walls
	{
		//check if out of bounds; move back in; apply restitution and bounce.
		if(xPos >= BallGame.width - size)
		{
			xPos = BallGame.width - size;
			if(xVel > 0)
				xVel *= -restitution;
		}
		if(xPos <= size + BallGame.leftBounds)
		{
			xPos = size +BallGame.leftBounds;
			if(xVel < 0)
				xVel *= -restitution;
		}
		if(yPos >= BallGame.height - size)
		{
			yPos = BallGame.height - size;
			if(yVel > 0)
				yVel *= -restitution;
		}
		if(yPos <= size + BallGame.topBounds)
		{
			yPos = size + BallGame.topBounds;
			if(yVel < 0)
				yVel *= -restitution;
		}

		for(int i = 0; i < BallGame.walls.size(); i++)	//bounce off wall objects
			BallGame.walls.get(i).collide(this);
	}

	public void collideBounce(Player other)
	{		
		double thisAngle = Math.atan2(yPos-other.yPos,xPos-other.xPos);
		double targAngle = Math.atan2(other.yPos-yPos,other.xPos-xPos);

		double inside = Math.abs(distance(xPos,yPos,other.xPos,other.yPos)-size-other.size);

		xPos += (inside/2)*Math.cos(thisAngle);
		yPos += (inside/2)*Math.sin(thisAngle);
		other.xPos += (inside/2)*Math.cos(targAngle);
		other.yPos += (inside/2)*Math.sin(targAngle);

		double thisSpeed = Math.sqrt(Math.pow(xVel,2)+Math.pow(yVel,2));
		double targSpeed = Math.sqrt(Math.pow(other.xVel,2)+Math.pow(other.yVel,2));

		double massRatio = Math.pow(other.size/size,2);

		xVel += massRatio*targSpeed*Math.cos(thisAngle)*Math.pow(restitution,2);
		yVel += massRatio*targSpeed*Math.sin(thisAngle)*Math.pow(restitution,2);

		other.xVel += (1/massRatio)*thisSpeed*Math.cos(targAngle)*Math.pow(restitution,2);
		other.yVel += (1/massRatio)*thisSpeed*Math.sin(targAngle)*Math.pow(restitution,2);
	}

	public void checkPowerUps()
	{
		for(int i = 0; i < BallGame.items.size(); i++)
		{
			Item p = BallGame.items.get(i);
			if(p.colliding(this))
				p.givePowerUp(this);
		}
	}

	public boolean colliding(Player other)	//checks if players are touching
	{
		if(distance(xPos,yPos,other.xPos,other.yPos) < size + other.size)
			return true;
		return false;
	}

	public void removeFromWorld()	//removes self from main list of players
	{
		BallGame.players.remove(BallGame.players.indexOf(this));
	}

	public void addHealth(int delta)
	{
		health += delta;
		if(health > healthMax)
			health = healthMax;
	}

	public void giveshield(int durationSeconds)
	{
		BallGame.powerups.add(new Shield(this,durationSeconds));
	}
	
	public void giveOrbital(int durationSeconds, OrbitalItem item)
	{
		BallGame.powerups.add(new Orbital(this,durationSeconds,item));
	}
	
	public void giveTripleShot(int durationSeconds)
	{
		BallGame.powerups.add(new TripleShot(this,durationSeconds));
	}
	
	public void giveFastShot(int durationSeconds)
	{
		BallGame.powerups.add(new FastShot(this,durationSeconds));
	}

	//simple distance formula.  Shouldn't really be in here.
	double distance(double x1, double y1, double x2, double y2){return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));}
}