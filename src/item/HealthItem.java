package item;
import entity.Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;


public class HealthItem extends Item
{
	static double refillMax = .2;
	static double refillMin = .1;

	static final double crossOutside	= .5;
	static final double crossInside		= .2;

	double percentRefill;

	public HealthItem()
	{
		super();
		percentRefill = Math.random()*(refillMax-refillMin)+refillMin;
		color = Color.red;
	}

	public void supplyEffect(Player p)
	{
		p.addHealth((int)(p.HEALTHMAX *percentRefill));
	}

	public void drawEffect(Graphics g)
	{
		Polygon cross = new Polygon();		//will be the health cross

		cross.addPoint((int)(xPos-size*crossInside),(int)(yPos-size*crossInside));
		cross.addPoint((int)(xPos-size*crossInside),(int)(yPos-size*crossOutside));
		cross.addPoint((int)(xPos+size*crossInside),(int)(yPos-size*crossOutside));

		cross.addPoint((int)(xPos+size*crossInside),(int)(yPos-size*crossInside));
		cross.addPoint((int)(xPos+size*crossOutside),(int)(yPos-size*crossInside));
		cross.addPoint((int)(xPos+size*crossOutside),(int)(yPos+size*crossInside));

		cross.addPoint((int)(xPos+size*crossInside),(int)(yPos+size*crossInside));
		cross.addPoint((int)(xPos+size*crossInside),(int)(yPos+size*crossOutside));
		cross.addPoint((int)(xPos-size*crossInside),(int)(yPos+size*crossOutside));

		cross.addPoint((int)(xPos-size*crossInside),(int)(yPos+size*crossInside));
		cross.addPoint((int)(xPos-size*crossOutside),(int)(yPos+size*crossInside));
		cross.addPoint((int)(xPos-size*crossOutside),(int)(yPos-size*crossInside));

		g.setColor(Color.white);
		g.fillPolygon(cross);
	}
}
