package game;

import entity.Entity;
import entity.Player;
import entity.Wall;
import frame.GameFrame;
import input.Button;
import item.ItemSpawner;

import java.awt.*;
import java.util.ArrayList;

public class MainClass {
    public static GameFrame frame;
    static Game game;

    static ArrayList<Entity> menuList = new ArrayList<Entity>();
    static ArrayList<Entity> gameList = new ArrayList<Entity>();
    static ArrayList<Entity> pauseList = new ArrayList<Entity>();

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

        Entity background = new Entity() {
            public void draw(Graphics2D g) {
                g.setColor(Color.black);
                g.fillRect(0, 0, MainClass.frame.getWidth(), MainClass.frame.getHeight());
            }
        };
        Button startButton = new Button(400, 300, 200, 75, "Start", Color.white) {
            public void press() {
                Entity.switchEntityList(1);
            }
        };

        menuList.add(background);
        menuList.add(startButton);
    }

    public static void createGame() {
        Player player1 = new Player(1);
        player1.setxPos(100);
        gameList.add(player1);

        Player player2 = new Player(2);
        player2.setxPos(-100);
        gameList.add(player2);

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

        pauseList.add(menuButton);
        pauseList.add(resumeButton);
    }
}
