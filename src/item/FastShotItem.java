package item;

import java.awt.*;

import camera.Camera;
import entity.Player;
import powerup.FastShot;

public class FastShotItem extends Item
{
	static final int DURATION_SECONDS = 3;

	static final float EFFECT_SIZE = .5f;
	
	public FastShotItem() {
		super();
		color = Color.orange;
	}
	
	public void supplyEffect(Player p) {
        new FastShot(p,DURATION_SECONDS).addToList();
	}
	
	public void drawEffect(Graphics2D g) {
		g.setColor(Color.black);
		
		Camera.fillRect(xPos - radius* EFFECT_SIZE - 1, yPos - radius* EFFECT_SIZE /2, radius* EFFECT_SIZE, radius* EFFECT_SIZE, g);
		Camera.fillRect(xPos+2, yPos - radius*EFFECT_SIZE/2, radius*EFFECT_SIZE, radius*EFFECT_SIZE, g);
	}
}
