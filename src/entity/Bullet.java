package entity;
import game.BallGame;

import java.awt.Color;
import java.awt.Graphics;

import powerup.Orbital;
import powerup.Powerup;

public class Bullet
{
	private Color color;
	public double size;	//radius of bullet
	public double xPos;
	public double yPos;
	public double xVel;
	public double yVel;
	public double speed;	//Constant speed.  No friction for bullets.
	Player parent;	//Sets parent player.  Will not interact with parent.
	boolean alive = true;
	int explosionTimer = 20; //explosion will show for explosionTimer frames

	static final double homingForce = 3;

	public int bounces = 2;	//How many times it will bounce before going away

	public Bullet(Player p, double si, double sp)
	{				
		parent = p;
		size = si;
		speed = sp;

		color = parent.color;

		xPos = parent.xPos + parent.size*Math.cos(parent.direction);	//sets position to just in front of parent player
		yPos = parent.yPos + parent.size*Math.sin(parent.direction);

		xVel = speed * Math.cos(parent.direction) + parent.xVel;	//sets velocities relative to player
		yVel = speed * Math.sin(parent.direction) + parent.yVel;

		speed = Math.sqrt(Math.pow(xVel,2)+Math.pow(yVel,2));	//changes speed variable for use in later calculations
	}

	long lastDownscale = System.currentTimeMillis();
	int downscaleDelay = 15;
	long nextDownscale = System.currentTimeMillis() + downscaleDelay;
	boolean initialGrowth = false;
	public void draw(Graphics g)
	{
		if(alive)
		{
			//draw bullet if alive
			g.setColor(color);
			g.fillOval((int)(xPos-size), (int)(yPos-size),(int)(size*2),(int)(size*2));
			g.setColor(Color.black);
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
			
			//draw explosion if dead
			if(downscale)
				size *= .9;	//same as player explosion
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

		for(int i = 0; i < BallGame.players.size(); i++)	//checks for collisions with players
		{
			Player targ = BallGame.players.get(i);
			if(targ != parent)	//make sure to not hit its parent
				if(colliding(targ) && targ.alive)	//If they're colliding and the player is alive, hit it and die.
				{
					hit(targ);
					alive = false;
				}
		}

		for(int i = 0; i < BallGame.bullets.size(); i++)	//checks for collisions with other bullets
		{
			Bullet targ = BallGame.bullets.get(i);
			if(targ != this)	//make sure to not hit itself
				if(colliding(targ) && targ.alive)	//If they're colliding and the other bullet is alive kill both of them.
					alive = targ.alive = false;
		}
		
		for(int i = 0; i < BallGame.powerups.size(); i++)	//checks for collisions with orbitals
		{
			Powerup other = BallGame.powerups.get(i);
			if(other instanceof Orbital)
			{
				Orbital targ = (Orbital)BallGame.powerups.get(i);
				if(targ.parent != this.parent)	//make sure to not hit its compadre
					if(targ.colliding(this))	//If they're colliding kill itself
					{
						targ.health--;
						alive = false;
					}
			}
		}

		wallClip();	//bounce off walls
	}

	public void gravitateToPlayers(double delta)
	{		
		for(int i = 0; i < BallGame.players.size(); i++)
		{
			Player p = BallGame.players.get(i);
			if(p != parent)
			{
				double angle = Math.atan2(p.yPos-yPos,p.xPos-xPos);
				double force = homingForce*Math.pow(Math.PI,2)*Math.pow(size,2)*Math.pow(p.size,2)/Math.pow(distance(xPos,yPos,p.xPos,p.yPos),2);
				double mass = Math.PI*Math.pow(size,2);
				double pmass =  Math.PI*Math.pow(p.size,2);

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
		for(int i = 0; i < BallGame.bullets.size(); i++)
		{
			Bullet b = BallGame.bullets.get(i);
			if(b != this)
			{
				double angle = Math.atan2(b.yPos-yPos,b.xPos-xPos);
				double force = homingForce*Math.pow(Math.PI,2)*Math.pow(size,2)*Math.pow(b.size,2)/Math.pow(distance(xPos,yPos,b.xPos,b.yPos),2);
				double mass = Math.PI*size*size;

				xVel += (force*Math.cos(angle)/mass)*delta;
				yVel += (force*Math.sin(angle)/mass)*delta;
				speed = Math.sqrt(Math.pow(xVel,2)+Math.pow(yVel,2));
			}
		}
	}

	public boolean colliding(Player other)	//check if colliding with a specific player
	{
		if(parent.distance(xPos,yPos,other.xPos,other.yPos) <= size + other.size)
			return true;
		return false;
	}

	public boolean colliding(Bullet other)	//check if colliding with a specific bullet
	{
		if(parent.distance(xPos,yPos,other.xPos,other.yPos) <= size + other.size)
			return true;
		return false;
	}

	public void hit(Player targ)	//bullet hits player
	{
		if(!targ.shielded)
		{
			parent.hits++;	//add a hit to its parent
			targ.health--;	//lower victim's health
		}

		double direction = Math.atan2(targ.yPos-yPos, targ.xPos-xPos);	//get collision angle

		targ.xVel += speed*Math.cos(direction)*(Math.pow(size,2)/Math.pow(targ.size,2));	//apply force to player for knockback
		targ.yVel += speed*Math.sin(direction)*(Math.pow(size,2)/Math.pow(targ.size,2));
	}

	public void removeFromWorld()	//remove from main list of bullets
	{
		BallGame.bullets.remove(BallGame.bullets.indexOf(this));
	}

	public void wallClip()
	{
		//Bounce off walls.  Subtract from remaining bounces.
		if(xPos >= BallGame.width - size)
		{
			bounces--;
			xPos = BallGame.width - size;
			if(xVel > 0)
				xVel *= -1;
		}
		if(xPos <= size + BallGame.leftBounds)
		{
			bounces--;
			xPos = size +BallGame.leftBounds;
			if(xVel < 0)
				xVel *= -1;
		}
		if(yPos >= BallGame.height - size)
		{
			bounces--;
			yPos = BallGame.height - size;
			if(yVel > 0)
				yVel *= -1;
		}
		if(yPos <= size + BallGame.topBounds)
		{
			bounces--;
			yPos = size + BallGame.topBounds;
			if(yVel < 0)
				yVel *= -1;
		}

		for(int i = 0; i < BallGame.walls.size(); i++)
			BallGame.walls.get(i).collide(this);

		if(bounces < 0)		//checks bounces to remove
			alive = false;
	}

	//simple distance formula.  Shouldn't really be in here.
	double distance(double x1, double y1, double x2, double y2){return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));}
}