package item;
import entity.Player;
import game.BallGame;

import java.awt.Color;
import java.awt.Graphics;


public class Item
{
	public double xPos;
	public double yPos;
	double speed;
	double targxPos;
	double targyPos;

	public double xVel;
	public double yVel;

	int startDistance;

	Color color;

	int size;

	public Item()
	{
		speed = Math.random()+1;

		color = Color.white;

		size = 10;

		startDistance = (int)((Math.max(BallGame.width+10,BallGame.height+10)+size)/1.5);
		double angle = Math.toRadians(Math.random()*360);		//random starting location
		xPos = (BallGame.width + 10)/2+startDistance*Math.cos(angle);
		yPos = (BallGame.height + 10)/2+startDistance*Math.sin(angle);

		targxPos = (int)(Math.random()*((BallGame.width-BallGame.leftBounds)-size*2)+size+BallGame.leftBounds);	//random location
		targyPos = (int)(Math.random()*((BallGame.height-BallGame.topBounds)-size*2)+size+BallGame.topBounds);

		double travelAngle = Math.atan2(targyPos-yPos,targxPos-xPos);

		xVel = speed*Math.cos(travelAngle);
		yVel = speed*Math.sin(travelAngle);
	}

	public void supplyEffect(Player p)
	{
		//Does nothing.  Extend this class to add effects.
	}

	public void givePowerUp(Player p)
	{
		supplyEffect(p);
		removeFromWorld();
	}

	public void removeFromWorld()	//remove from main list of bullets
	{				
		BallGame.items.remove(BallGame.items.indexOf(this));
	}

	public void move(int delta)
	{
		double deltaFrame = (double)delta/15;
		
		if(isOutofView())
			removeFromWorld();

		xPos += xVel * deltaFrame;
		yPos += yVel * deltaFrame;
	}

	boolean isOutofView()
	{
		if(xPos - size > BallGame.width + 10 && xVel > 0)
			return true;
		if(xPos + size < -10 && xVel < 0)
			return true;
		if(yPos - size > BallGame.height + 10 && yVel > 0)
			return true;
		if(yPos + size < -10 && yVel < 0)
			return true;
		return false;
	}

	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillOval((int)(xPos-size),(int)(yPos-size),size*2,size*2);
		g.setColor(Color.black);
		g.drawOval((int)(xPos-size),(int)(yPos-size),size*2,size*2);
		drawEffect(g);
	}

	public void drawEffect(Graphics g)
	{
		//Add effect to extended classes
	}

	public boolean colliding(Player p)
	{
		if(distance(xPos,yPos,p.xPos,p.yPos) <= size+p.size )
			return true;
		return false;
	}

	//simple distance formula.  Shouldn't really be in here.
	double distance(double x1, double y1, double x2, double y2){return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));}
}
