package item;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import entity.Player;

public class TripleShotItem extends Item
{
	static final int durationSeconds = 3;
	
	static final double effectSize = .5;
	
	public TripleShotItem()
	{
		super();
		color = Color.green.darker();
	}
	
	public void supplyEffect(Player p)
	{
		p.giveTripleShot(durationSeconds);
	}
	
	public void drawEffect(Graphics g)
	{
		Polygon triangle = new Polygon();
		
		triangle.addPoint((int)xPos,(int)(yPos-size*effectSize));
		triangle.addPoint((int)(xPos+size*effectSize),(int)(yPos+size*effectSize));
		triangle.addPoint((int)(xPos-size*effectSize),(int)(yPos+size*effectSize));
		
		g.setColor(Color.yellow);
		g.fillPolygon(triangle);
	}
}
