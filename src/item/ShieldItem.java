package item;
import entity.Player;
import powerup.Shield;

import java.awt.Color;

public class ShieldItem extends Item
{
	int durationSeconds;

	static final int secondsMin = 5;
	static final int secondsMax = 8;

	public ShieldItem() {
		super();
		durationSeconds = (int)(Math.random()*(secondsMax-secondsMin))+secondsMin;
		color = Color.cyan;
	}

	public void supplyEffect(Player p) {
        new Shield(p,durationSeconds).addToList();
	}
}
