package powerup;

import entity.Bullet;
import entity.Player;
import game.BallGame;

import item.OrbitalItem;

import java.awt.Color;
import java.awt.Graphics;

public class Orbital extends Powerup
{	
	public static final double restitution = .5;
	static final double attractForce = .7;
	
	static final double friction = .99;
	
	public double xPos;
	public double yPos;
	public double xVel;
	public double yVel;
	
	public double size;
	
	public boolean alive = true;
	
	public int health = 10;
	
	Color color;
	
	public Orbital(Player parent, int durationSeconds, OrbitalItem item)
	{
		super(parent, durationSeconds);
		
		xPos = item.xPos;
		yPos = item.yPos;
		
		xVel = item.xVel;
		yVel = item.yVel;
		
		size = 10;
		
		color = Color.gray;
	}
	
	public void draw(Graphics g)
	{
		if(!colliding(parent))
		{
			double angle = Math.atan2(yPos-parent.yPos,xPos-parent.xPos);
			int endX = (int)(parent.xPos + parent.size*Math.cos(angle));
			int endY = (int)(parent.yPos + parent.size*Math.sin(angle));

			g.setColor(Color.black);
			g.drawLine((int)xPos,(int)yPos,endX,endY);
		}
		
		g.setColor(color);
		g.fillOval((int)(xPos-size),(int)(yPos-size),(int)(size*2),(int)(size*2));
		g.setColor(Color.black);
		g.drawOval((int)(xPos-size),(int)(yPos-size),(int)(size*2),(int)(size*2));
	}
	
	public void move(int delta)
	{
		super.move(delta);
		
		springToPlayer(delta);
		collideWithPlayers();
		
		xVel *= friction;
		yVel *= friction;
		
		xPos+=xVel;
		yPos+=yVel;
		
		wallClip();
	}
	
	public void springToPlayer(int delta)
	{
		double deltaFrame = (double)delta/15;
		
		double angle = Math.atan2(parent.yPos-yPos,parent.xPos-xPos);
		
		double distance = distance(xPos,yPos,parent.xPos,parent.yPos) - (parent.size*1.5);
		if(distance < 0)
			distance = 0;
		
		double force = attractForce*Math.pow(distance,1.1);
		double accel = force/(Math.PI*Math.pow(size,2));
		
		xVel += accel*Math.cos(angle)*deltaFrame;
		yVel += accel*Math.sin(angle)*deltaFrame;
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
	
	public void collideWithPlayers()
	{
		for(int i = 0; i < BallGame.players.size(); i++)
		{
			Player p = BallGame.players.get(i);
			if(p != parent)
			{
				if(colliding(p))
				{
					hit(p);
					collideBounce(p);
				}
			}
		}
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
			double angle = Math.atan2(yPos-p.yPos,xPos-p.xPos);
			b.xVel = b.yVel = 0;
			b.bounces = 0;
			b.xPos = p.xPos + p.size*Math.cos(angle);
			b.yPos = p.yPos + p.size*Math.sin(angle);
			BallGame.bullets.add(b);

			health--;
			if(health <= 0)
				removeFromWorld();
		}
	}
	
	public boolean colliding(Player other)
	{

		if(distance(xPos,yPos,other.xPos,other.yPos) < size + other.size)
			return true;
		return false;
	}
	
	public boolean colliding(Bullet other)
	{

		if(distance(xPos,yPos,other.xPos,other.yPos) < size + other.size)
			return true;
		return false;
	}
	
	public boolean colliding(Orbital other)
	{

		if(distance(xPos,yPos,other.xPos,other.yPos) < size + other.size)
			return true;
		return false;
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

		double massRatio = Math.pow(other.size/(size*2),2);

		xVel += massRatio*targSpeed*Math.cos(thisAngle)*Math.pow(restitution,2);
		yVel += massRatio*targSpeed*Math.sin(thisAngle)*Math.pow(restitution,2);

		other.xVel += (1/massRatio)*thisSpeed*Math.cos(targAngle)*Math.pow(restitution,2);
		other.yVel += (1/massRatio)*thisSpeed*Math.sin(targAngle)*Math.pow(restitution,2);
	}
	
	public void collideBounce(Orbital other)
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

		double massRatio = Math.pow(other.size/(size*2),2);

		xVel += massRatio*targSpeed*Math.cos(thisAngle)*Math.pow(restitution,2);
		yVel += massRatio*targSpeed*Math.sin(thisAngle)*Math.pow(restitution,2);

		other.xVel += (1/massRatio)*thisSpeed*Math.cos(targAngle)*Math.pow(restitution,2);
		other.yVel += (1/massRatio)*thisSpeed*Math.sin(targAngle)*Math.pow(restitution,2);
	}
	
	//simple distance formula.  Shouldn't really be in here.
	public double distance(double x1, double y1, double x2, double y2){return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));}
}