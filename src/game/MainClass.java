package game;

import entity.Entity;
import entity.Player;
import entity.Wall;
import frame.GameFrame;
import input.Button;
import input.ToggleButton;
import item.ItemSpawner;

import java.awt.*;
import java.util.ArrayList;

public class MainClass {
    public static GameFrame frame;
    static Game game;

    static ArrayList<Entity> menuList = new ArrayList<Entity>();
    static ArrayList<Entity> gameList = new ArrayList<Entity>();
    static ArrayList<Entity> pauseList = new ArrayList<Entity>();

    static ToggleButton player1Button;
    static ToggleButton player2Button;
    static ToggleButton player3Button;
    static ToggleButton player4Button;

    public static void main(String args[]) {
        game = new Game();
        game.showFPS(true);
        frame = new GameFrame("Ball Game", game);
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);

        createMenu();
        createGame();
        createPauseScreen();

        Entity.addEntityList(menuList);
        Entity.addEntityList(gameList);
        Entity.addEntityList(pauseList);
        Entity.switchEntityList(0);
    }

    public static void createMenu() {
        menuList.clear();

        menuList.add(new Entity() {
            public void draw(Graphics2D g) {
                g.setColor(Color.black);
                g.fillRect(0, 0, MainClass.frame.getWidth(), MainClass.frame.getHeight());

                g.setFont(new Font("Monospaced", 0, 80));
                g.setColor(Color.white);
                g.drawString("Ball Game", 285, 200);    //draw game name
            }
        });
        menuList.add(new Button(400, 300, 200, 75, "Start", Color.white) {
            public void press() {
                int playerCount = 0;
                if (MainClass.player1Button.chosen)
                    playerCount++;
                if (MainClass.player2Button.chosen)
                    playerCount++;
                if (MainClass.player3Button.chosen)
                    playerCount++;
                if (MainClass.player4Button.chosen)
                    playerCount++;
                if (playerCount > 1)
                    Entity.switchEntityList(1);
            }
        });
        player1Button = new ToggleButton(125, 500, 150, 75, "Blue", Color.blue) {
            public void init() {
                setChosen(true);
            }
        };
        player2Button = new ToggleButton(325, 500, 150, 75, "Red", Color.red) {
            public void init() {
                setChosen(true);
            }
        };
        player3Button = new ToggleButton(525, 500, 150, 75, "Green", Color.green);
        player4Button = new ToggleButton(725, 500, 150, 75, "Pink", Color.magenta);

        menuList.add(player1Button);
        menuList.add(player2Button);
        menuList.add(player3Button);
        menuList.add(player4Button);
    }

    public static void createGame() {
        gameList.clear();

        if (player1Button.chosen) {
            Player player1 = new Player(1);
            player1.setxPos(400);
            gameList.add(player1);
        }

        if (player2Button.chosen) {
            Player player2 = new Player(2);
            player2.setxPos(-400);
            gameList.add(player2);
        }

        if (player3Button.chosen) {
            Player player3 = new Player(3);
            player3.setyPos(300);
            gameList.add(player3);
        }

        if (player4Button.chosen) {
            Player player4 = new Player(4);
            player4.setyPos(-300);
            gameList.add(player4);
        }

        gameList.add(new ItemSpawner(1, 10));

        gameList.add(new Wall(-520, 380, 520, 360));
        gameList.add(new Wall(-520, -380, 520, -360));
        gameList.add(new Wall(-520, 380, -500, -380));
        gameList.add(new Wall(520, 380, 500, -380));
    }

    public static void createPauseScreen() {
        Button menuButton = new Button(20, 20, 200, 75, "Menu", Color.gray) {
            public void press() {
                Entity.switchEntityList(0);
            }
        };
        Button resumeButton = new Button(400, 300, 200, 75, "Resume", Color.gray) {
            public void press() {
                Entity.switchEntityList(1);
            }
        };

        Entity gameOverlayEntity = new Entity() {
            public void draw(Graphics2D g) {
                for (Entity e : MainClass.gameList)
                    e.draw(g);
            }
        };

        pauseList.add(gameOverlayEntity);
        pauseList.add(menuButton);
        pauseList.add(resumeButton);
    }
}
