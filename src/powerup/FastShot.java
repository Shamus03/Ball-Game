package powerup;

import entity.Player;

public class FastShot extends Powerup
{
	public FastShot(Player parent, int durationTime)
	{
		super(parent,durationTime);
	}
	
	public void move(int delta)
	{
		parent.fastShot = true;
		if(System.currentTimeMillis() >= deathTime)
			removeFromWorld();
	}
	
	public void removeEffect()
	{
		parent.fastShot = false;
	}
}
