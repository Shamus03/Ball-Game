package item;

import java.awt.Color;
import java.awt.Graphics;

import entity.Player;

public class FastShotItem extends Item
{
	static final int durationSeconds = 3;
	
	static final double effectSize = .5;
	
	public FastShotItem()
	{
		super();
		color = Color.orange;
	}
	
	public void supplyEffect(Player p)
	{
		p.giveFastShot(durationSeconds);
	}
	
	public void drawEffect(Graphics g)
	{
		g.setColor(Color.black);
		
		g.fillRect((int)(xPos-size*effectSize-1),(int)(yPos-size*effectSize/2),(int)(size*effectSize),(int)(size*effectSize));
		g.fillRect((int)(xPos+2),(int)(yPos-size*effectSize/2),(int)(size*effectSize),(int)(size*effectSize));
	}
}
