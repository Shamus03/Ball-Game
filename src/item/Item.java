package item;
import entity.Entity;
import entity.Player;
import game.BallGameStatic;

import java.awt.Color;
import java.awt.Graphics;


public class Item extends Entity
{
	float speed;
	float targxPos;
	float targyPos;

	int startDistance;

	Color color;

	int size;

	public Item()
	{
		speed = (float) (Math.random()+1);

		color = Color.white;

		size = 10;

		startDistance = (int)((Math.max(BallGameStatic.width+10, BallGameStatic.height+10)+size)/1.5);
		double angle = Math.toRadians(Math.random()*360);		//random starting location
		xPos = (float) ((BallGameStatic.width + 10)/2+startDistance*Math.cos(angle));
		yPos = (float) ((BallGameStatic.height + 10)/2+startDistance*Math.sin(angle));

		targxPos = (int)(Math.random()*((BallGameStatic.width- BallGameStatic.leftBounds)-size*2)+size+ BallGameStatic.leftBounds);	//random location
		targyPos = (int)(Math.random()*((BallGameStatic.height- BallGameStatic.topBounds)-size*2)+size+ BallGameStatic.topBounds);

		double travelAngle = Math.atan2(targyPos-yPos,targxPos-xPos);

		xVel = (float) (speed*Math.cos(travelAngle));
		yVel = (float) (speed*Math.sin(travelAngle));
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
		BallGameStatic.items.remove(BallGameStatic.items.indexOf(this));
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
		if(xPos - size > BallGameStatic.width + 10 && xVel > 0)
			return true;
		if(xPos + size < -10 && xVel < 0)
			return true;
		if(yPos - size > BallGameStatic.height + 10 && yVel > 0)
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
		if(distance(xPos,yPos,p.getxPos(),p.getyPos()) <= size+p.size )
			return true;
		return false;
	}

	//simple distance formula.  Shouldn't really be in here.
	double distance(double x1, double y1, double x2, double y2){return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));}
}
