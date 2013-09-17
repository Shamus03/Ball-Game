package input;
import game.BallGameStatic.Location;

import java.awt.Color;

public class ToggleButton extends Button
{
	Color onColor;

	public ToggleButton(Location location, int xPos, int yPos, int width, int height, String text, Color color)
	{
		super(location,xPos,yPos,width,height,text,null,color);
		onColor = color;
		this.color = Color.white;
		init();
	}

	void press()
	{		
		setChosen(!chosen);
	}

	public void setChosen(boolean b)
	{
		chosen = b;
		if(chosen)
			color = onColor;
		else
			color = Color.white;	
	}
}