package input;
import game.BallGame;
import game.BallGame.Location;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Button
{
	int xPos;
	int yPos;
	int width;
	int height;

	public String text;
	Location location;
	Location newLocation;

	Color color;
	Color onColor;

	Rectangle bounds;

	public boolean chosen = false;

	boolean clicked = false;

	public Button(Location location, int xPos, int yPos, int width, int height, String text, Location newLocation, Color color)
	{	
		this.color = this.onColor = color;

		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.text = text;
		this.location = location;
		this.newLocation = newLocation;

		bounds = new Rectangle(xPos,yPos,width,height);
		
		init();
	}
	
	public void init()
	{
		
	}

	public void draw(Graphics g)
	{
		if(BallGame.location == location)
		{			
			if(bounds.contains(InputHandler.MOUSE_X,InputHandler.MOUSE_Y))	//draw darker if mouse is touching it
			{
				g.setColor(color.darker());
				if(InputHandler.MOUSE_LEFT)
				{
					if(!clicked)
					{
						press();
						clicked = true;
					}
				}else
					clicked = false;
			}
			else
				g.setColor(color);

			g.fillRoundRect(xPos,yPos,width,height,10,10);
			g.setColor(Color.black);
			g.setFont(new Font("Monospaced",0,(int)(height/2)));
			g.drawString(text,(int)(xPos+(width-text.length()*height/3.4)/2),(int)(yPos+height/1.55));	//draw text centered
		}
	}

	void press()
	{
		BallGame.location = newLocation;
	}

	public void setColor(Color c){ color = c; }
}