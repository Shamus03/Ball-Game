package powerup;

import entity.Player;

public class TripleShot extends Powerup
{
	public TripleShot(Player parent, int durationTime)
	{
		super(parent,durationTime);
	}
	
	public void move(int delta)
	{
		parent.tripleShot = true;
		if(System.currentTimeMillis() >= deathTime)
			removeFromWorld();
	}
	
	public void removeEffect()
	{
		parent.tripleShot = false;
	}
}
