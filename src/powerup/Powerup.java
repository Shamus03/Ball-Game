package powerup;

import entity.Player;
import game.BallGame;

import java.awt.Graphics;

public class Powerup
{
	public long birthTime;
	public long deathTime;
	
	public Player parent;
	
	public Powerup(Player parent, int durationSeconds)
	{
		this.parent = parent;
		birthTime = System.currentTimeMillis();
		deathTime = birthTime + durationSeconds*1000;
	}
	
	public void move(int delta)
	{
		if(System.currentTimeMillis() >= deathTime)
			removeFromWorld();
	}
	
	public void draw(Graphics g)
	{
		//does nothing; extend class
	}
	
	public void removeEffect()
	{
		//does nothing; extend class
	}
	
	protected void removeFromWorld()	//removes self from main list of players
	{
		removeEffect();
		BallGame.powerups.remove(BallGame.powerups.indexOf(this));
	}
}
