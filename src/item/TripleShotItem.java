package item;

import java.awt.*;

import camera.Camera;
import entity.Player;
import powerup.TripleShot;
import shape.Polygon2D;

public class TripleShotItem extends Item
{
	static final int DURATION_SECONDS = 3;
	
	static final float effectSize = .5f;
	
	public TripleShotItem() {
		super();
		color = Color.green.darker();
	}
	
	public void supplyEffect(Player p) {
        new TripleShot(p, DURATION_SECONDS).addToList();
	}
	
	public void drawEffect(Graphics2D g) {
		Polygon2D triangle = new Polygon2D();
		
		triangle.addPoint(xPos,yPos-radius*effectSize);
		triangle.addPoint(xPos+radius*effectSize,yPos+radius*effectSize);
		triangle.addPoint(xPos-radius*effectSize,yPos+radius*effectSize);
		
		g.setColor(Color.yellow);
		Camera.fillPolygon2D(triangle, g);
	}
}
