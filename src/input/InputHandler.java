package input;

import java.awt.Event;

public class InputHandler	//class to handle input
{
	//ids for keys
	public static final int id_UP		= 1004;
	public static final int id_DOWN		= 1005;
	public static final int id_LEFT		= 1006;
	public static final int id_RIGHT	= 1007;

	public static final int id_W		= 119;
	public static final int id_A		= 97;
	public static final int id_S		= 115;
	public static final int id_D		= 100;

	public static final int id_I		= 105;
	public static final int id_K		= 107;
	public static final int id_J		= 106;
	public static final int id_L		= 108;

	public static final int id_P		= 112;
	public static final int id_ESC		= 27;

	//boolean values determine if key is pressed
	public static boolean UP	= false;
	public static boolean DOWN	= false;
	public static boolean LEFT	= false;
	public static boolean RIGHT	= false;

	public static boolean W		= false;
	public static boolean A		= false;
	public static boolean S		= false;
	public static boolean D		= false;

	public static boolean I		= false;
	public static boolean J		= false;
	public static boolean K		= false;
	public static boolean L		= false;

	public static boolean P		= false;
	public static boolean ESC		= false;

	//mouse info
	public static boolean MOUSE_LEFT	= false;
	public static boolean MOUSE_RIGHT	= false;

	public static int MOUSE_X	= 0;
	public static int MOUSE_Y	= 0;
	
	public static boolean keyChange(Event e, int key, boolean newKeyState)
	{		//changes key state variables to correct values
		switch(key)
		{
		case id_UP:	//Up
			InputHandler.UP = newKeyState;
			break;
		case InputHandler.id_DOWN:	//Down
			InputHandler.DOWN = newKeyState;
			break;
		case InputHandler.id_LEFT:	//Left
			InputHandler.LEFT = newKeyState;
			break;
		case InputHandler.id_RIGHT:	//Right
			InputHandler.RIGHT = newKeyState;
			break;

		case InputHandler.id_W:	//Up
			InputHandler.W = newKeyState;
			break;
		case InputHandler.id_S:	//Down
			InputHandler.S = newKeyState;
			break;
		case InputHandler.id_A:	//Left
			InputHandler.A = newKeyState;
			break;
		case InputHandler.id_D:	//Right
			InputHandler.D = newKeyState;
			break;

		case InputHandler.id_I:	//Up
			InputHandler.I = newKeyState;
			break;
		case InputHandler.id_J:	//Down
			InputHandler.J = newKeyState;
			break;
		case InputHandler.id_K:	//Left
			InputHandler.K = newKeyState;
			break;
		case InputHandler.id_L:	//Right
			InputHandler.L = newKeyState;
			break;

		case InputHandler.id_P:
			InputHandler.P = newKeyState;
			break;
		case InputHandler.id_ESC:
			InputHandler.ESC = newKeyState;
			break;

		default:
			System.out.print(key);	//print any unbound key presses- may spam console
			if(newKeyState)
				System.out.println(" down");
			else
				System.out.println(" up");
			break;
		}
		return true;
	}
	
	public static boolean mouseUpdate(int x, int y)	//handles mouse movement
	{
		InputHandler.MOUSE_X = x;
		InputHandler.MOUSE_Y = y;
		return true;
	}
	
	public static boolean mouseChange(Event e, boolean newKeyState)	//handles mouse clicks
	{
		if(e.metaDown())
			InputHandler.MOUSE_RIGHT	= newKeyState;
		else
			InputHandler.MOUSE_LEFT		= newKeyState;
		return true;
	}
}