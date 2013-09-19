package powerup;

import entity.Player;

public class FastShot extends Powerup
{
	public FastShot(Player parent, int durationTime) {
		super(parent,durationTime);
	}
	
	public void tick(int delta) {
		parent.fastShot = true;
		if(System.currentTimeMillis() >= deathTime)
			removeFromList();
	}
	
	public void removeEffect() {
		parent.fastShot = false;
	}
}
