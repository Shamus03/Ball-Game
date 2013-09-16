package item;

import java.awt.Color;

import entity.Player;

public class OrbitalItem extends Item
{
	private int durationSeconds = 15;
	
	public OrbitalItem()
	{
		super();
		color = Color.gray;
	}
	
	public void supplyEffect(Player p)
	{
		p.giveOrbital(durationSeconds,this);
	}
}
