package item;

import java.awt.Color;

import entity.Player;
import powerup.Orbital;

public class OrbitalItem extends Item
{
	private int durationSeconds = 15;
	
	public OrbitalItem() {
		super();
		color = Color.gray;
	}
	
	public void supplyEffect(Player p) {
        new Orbital(p,durationSeconds,this).addToList();
    }
}
