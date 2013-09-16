package game;
import input.Button;
import input.InputHandler;
import input.ToggleButton;
import item.Item;
import item.ItemSpawner;

import java.awt.*;
import java.util.*;

import java.applet.*;

import powerup.Orbital;
import powerup.Powerup;

import entity.Bullet;
import entity.Player;
import entity.Wall;


@SuppressWarnings("serial")
public class BallGame extends Applet
{		
	public static ArrayList<Player> players = new ArrayList<Player>();
	public static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	public static ArrayList<Wall> walls = new ArrayList<Wall>();    
	public static ArrayList<Button> buttons = new ArrayList<Button>();
	public static ArrayList<Item> items = new ArrayList<Item>();
	public static ArrayList<Powerup> powerups = new ArrayList<Powerup>();
	
	ItemSpawner itemSpawner;

	public static int height;
	public static int width;
	public static int leftBounds;
	public static int topBounds;

	public static Location location = Location.MENU;

	final boolean RESIZEABLE = false;

	Image virtualMem;
	Graphics2D gBuffer;

	InputHandler keyboard;
	
	public enum Location
	{
		MENU,
		NEWGAME,
		GAME,
		PAUSESCREEN,
		WIN;
	}
	
	public void start()
	{
		players = new ArrayList<Player>();	//reset players and bullets and powerUps
		bullets = new ArrayList<Bullet>();
		items = new ArrayList<Item>();
		powerups = new ArrayList<Powerup>();
	}
	
	public void init()
	{	
		
		
		width = 1000 - 10;
		height = 600 - 10;

		leftBounds = 10;
		topBounds = 10;

		virtualMem = createImage(width+20,height+20);	//create buffer
		gBuffer = (Graphics2D)virtualMem.getGraphics();
		gBuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		setSize(width+10,height+10);	//set size of window

		//add buttons
		buttons.add(new Button(Location.MENU,400,300,200,75,"Start",Location.NEWGAME,Color.white));
		buttons.add(new Button(Location.PAUSESCREEN,20,20,200,75,"Menu",Location.MENU,Color.gray));
		buttons.add(new Button(Location.WIN,400,300,200,75,"Menu",Location.MENU,Color.gray));
		buttons.add(new Button(Location.PAUSESCREEN,400,300,200,75,"Resume",Location.GAME,Color.gray));

		buttons.add(new ToggleButton(Location.MENU,125,500,150,75,"Blue",Color.blue) {
			public void init() {setChosen(true);}
		});
		buttons.add(new ToggleButton(Location.MENU,325,500,150,75,"Red",Color.red));
		buttons.add(new ToggleButton(Location.MENU,525,500,150,75,"Green",Color.green));

		buttons.add(new ToggleButton(Location.MENU,725,500,150,75,"Pink",Color.magenta) {
			public void init() {setChosen(true);}
		});
	}

	public void newGame(int delta)
	{
		players = new ArrayList<Player>();	//reset players and bullets and powerUps
		bullets = new ArrayList<Bullet>();
		items = new ArrayList<Item>();
		powerups = new ArrayList<Powerup>();

		itemSpawner = new ItemSpawner(5,10);

		//add players
		for(int i = 0; i < buttons.size(); i++)
			if(buttons.get(i).text.equals("Blue"))
				if(buttons.get(i).chosen)
					players.add(new Player(1));
		for(int i = 0; i < buttons.size(); i++)
			if(buttons.get(i).text.equals("Red"))
				if(buttons.get(i).chosen)
					players.add(new Player(2));
		for(int i = 0; i < buttons.size(); i++)
			if(buttons.get(i).text.equals("Green"))
				if(buttons.get(i).chosen)
					players.add(new Player(3));
		for(int i = 0; i < buttons.size(); i++)
			if(buttons.get(i).text.equals("Pink"))
				if(buttons.get(i).chosen)
					players.add(new Player(4));

		if(players.size() >= 2)
			location = Location.GAME;
		else
			location = Location.MENU;
	}

	boolean pauseButtonStillPressed = false;
	public void game(int delta)
	{				
		if(!(InputHandler.P||InputHandler.ESC||!isFocusOwner()))	//handles single presses of P
			pauseButtonStillPressed = false;	//if the window loses focus the game will pause
		else
			if(!pauseButtonStillPressed)
			{
				pauseButtonStillPressed = true;
				location = Location.PAUSESCREEN;
			}
		
		if(players.size() == 1) {
			location = Location.WIN;
			return;
		}

		itemSpawner.attemptSpawn();	//spawn items

		moveBullets(delta);
		movePlayers(delta);
		movePowerups(delta);
		moveItems(delta);

		collidePlayers(delta);
		collideOrbitals();

		gBuffer.clearRect(0,0,getWidth(),getHeight());	//clear buffer

		drawWalls();

		drawPlayers();
		drawPowerups();
		drawBullets();
		drawItems();
	}
	
	public void win(int delta)
	{
		int winnerColor = players.get(0).controlScheme;
		String winner;
		
		switch(winnerColor)
		{
		case 1:
			winner = "Blue";
			break;
		case 2:
			winner = "Red";
			break;
		case 3:
			winner = "Green";
			break;
		case 4:
			winner = "Pink";
			break;
		default:
			winner = "ERROR";
			break;
		}
		
		gBuffer.setColor(Color.black);
		gBuffer.fillRect(0,0,getWidth(),getHeight());	//draw background

		gBuffer.setFont(new Font("Monospaced",0,80));
		gBuffer.setColor(Color.white);
		gBuffer.drawString(winner + " wins!",285,200);	//draw winner
	}

	public void pauseScreen(int delta)
	{		
		if(!(InputHandler.P||InputHandler.ESC))	//handles single presses of pause button
			pauseButtonStillPressed = false;
		else
			if(!pauseButtonStillPressed)
			{
				pauseButtonStillPressed = true;
				location = Location.GAME;
			}

		gBuffer.setColor(Color.gray);
		gBuffer.fillRoundRect(325,100,350,150,10,10);

		gBuffer.setFont(new Font("Monospaced",0,80));
		gBuffer.setColor(Color.black);
		gBuffer.drawString("PAUSED",355,200);
	}

	public void menu(int delta)
	{
		gBuffer.setColor(Color.black);
		gBuffer.fillRect(0,0,getWidth(),getHeight());	//draw background

		gBuffer.setFont(new Font("Monospaced",0,80));
		gBuffer.setColor(Color.white);
		gBuffer.drawString("Ball Game",285,200);	//draw game name
	}

	
	int fps = 0;
	int lastFPS = 0;
	long lastFPSFrame = System.currentTimeMillis();
	public void drawFPS()
	{				
		gBuffer.setFont(new Font("Monospaced",0,16));
		gBuffer.setColor(Color.black);
		gBuffer.drawString("FPS: " + lastFPS,15,26);
		
		if(System.currentTimeMillis() >= lastFPSFrame + 1000)
		{
			lastFPS = fps;
			fps = 0;
			lastFPSFrame = System.currentTimeMillis();
		}else
			fps++;
	}
	
	long lastFrame = System.currentTimeMillis();
	public void paint(Graphics g)
	{		
		long thisFrame = System.currentTimeMillis();
		int delta = (int)(thisFrame - lastFrame);
		
		if(true)	//game code run each frame goes here
		{	
			resizeWindow();

			switch(location)
			{
			case NEWGAME:
				newGame(delta);
				break;
			case GAME:
				game(delta);
				break;
			case PAUSESCREEN:
				pauseScreen(delta);
				break;
			case MENU:
				menu(delta);
				break;
			case WIN:
				win(delta);
				break;
			default:
				break;
			}

			for(int i = 0; i < buttons.size(); i++)	//draw buttons
				buttons.get(i).draw(gBuffer);
			drawFPS();
		}

		g.drawImage(virtualMem,0,0, this);	//draw buffer to applet

		lastFrame = thisFrame;
		
		repaint();
	}

	public void drawWalls()
	{
		gBuffer.setColor(Color.black);	//draw arena walls in buffer
		for(int i = 0; i < 5; i++)
			gBuffer.drawRect(leftBounds - i, topBounds - i, width - 10 + 2*i, height - 10 + 2*i);
		for(int i = 0; i < walls.size(); i++)
			walls.get(i).draw(gBuffer);
	}
	
	public void movePowerups(int delta)
	{
		for(int i = 0; i < powerups.size(); i++)
			powerups.get(i).move(delta);
	}

	public void drawPowerups()
	{
		for(int i = 0; i < powerups.size(); i++)
			powerups.get(i).draw(gBuffer);
	}
	
	public void moveItems(int delta)
	{
		for(int i = 0; i < items.size(); i++)
			items.get(i).move(delta);
	}

	public void drawItems()
	{
		for(int i = 0; i < items.size(); i++)
			items.get(i).draw(gBuffer);
	}

	public void moveBullets(int delta)
	{
		for(int i = 0; i < bullets.size(); i++)	//move all bullets
			bullets.get(i).move(delta);
	}

	public void drawBullets()
	{
		for(int i = 0; i < bullets.size(); i++)	//draw bullets in buffer
			bullets.get(i).draw(gBuffer);
	}

	public void movePlayers(int delta)
	{
		for(int i = 0; i < players.size(); i++)	//move all players
			players.get(i).move(delta);
	}

	public void drawPlayers()
	{
		for(int i = 0; i < players.size(); i++)	//draw players in buffer
			players.get(i).draw(gBuffer);
	}

	public void collidePlayers(int delta)
	{
		for(int i = 0; i < players.size() - 1; i++)		//collide all players
		{
			Player p1 = players.get(i);
			for(int ii = i + 1; ii < players.size(); ii++)
			{
				Player p2 = players.get(ii);
				if(p1.colliding(p2))
					p1.collideBounce(p2);
			}
		}
	}
	
	public void collideOrbitals()
	{
		for(int i = 0; i < powerups.size() - 1; i++)		//collide all orbitals
		{
			if(powerups.get(i) instanceof Orbital)
			{
				Orbital o1 = (Orbital)powerups.get(i);
				for(int ii = i + 1; ii < powerups.size(); ii++)
				{
					if(powerups.get(ii) instanceof Orbital)
					{
						Orbital o2 = (Orbital)powerups.get(ii);
						if(o1.colliding(o2))
							o1.collideBounce(o2);
					}
				}
			}
		}
	}

	public void resizeWindow()
	{
		if(RESIZEABLE)
		{
			if(getHeight() != height + 10 || getWidth() != width + 10 )	//check if window has been resized
			{
				height = getHeight() - 10;		//set height/width variables
				width = getWidth() - 10;
				virtualMem = createImage(width+20,height+20);	//change buffer size
				gBuffer = (Graphics2D)virtualMem.getGraphics();
				gBuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}
		}else
			setSize(width+10,height+10);
	}

	public void update(Graphics g)
	{
		paint(g);
	}

	public boolean mouseMove(Event e, int x, int y){return InputHandler.mouseUpdate(x,y);}
	public boolean mouseDrag(Event e, int x, int y){return InputHandler.mouseUpdate(x,y);}
	
	public boolean mouseDown(Event e, int x, int y){return InputHandler.mouseChange(e,true );}
	public boolean mouseUp  (Event e, int x, int y){return InputHandler.mouseChange(e,false);}

	public boolean keyDown(Event e, int key){return InputHandler.keyChange(e,key,true);}
	public boolean keyUp  (Event e, int key){return InputHandler.keyChange(e,key,false);}
}
