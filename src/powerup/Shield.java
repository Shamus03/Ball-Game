package powerup;
import entity.Player;

import java.awt.Color;
import java.awt.Graphics;


public class Shield extends Powerup
{
	int depth;
	int radius;

	public Shield(Player p, int durationSeconds)
	{
		super(p,durationSeconds);
		
		parent = p;

		birthTime = System.currentTimeMillis();
		deathTime = birthTime + durationSeconds*1000;

		radius = 12;
		depth = 5;

		parent.shielded = true;
	}

	public void draw(Graphics g)
	{	
		parent.shielded = true;
		if(parent.shieldDrawn)		//don't draw more than one shield
			return;
		
		g.setColor(new Color(0,255,255,100));
		fillCenteredCircle(parent.xPos,parent.yPos,parent.size+radius,g);
		parent.shieldDrawn = true;
	}
	
	public void removeEffect()
	{
		parent.shielded = false;
	}

	void fillCenteredCircle(double cx, double cy, double radius, Graphics g)
	{
		g.fillOval((int)(cx-radius), (int)(cy-radius), (int)(radius*2), (int)(radius*2));
	}
}
