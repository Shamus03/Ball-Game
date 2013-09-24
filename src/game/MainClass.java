package game;

import entity.Entity;
import entity.Player;
import entity.Wall;
import frame.GameFrame;
import input.Input;
import item.ItemSpawner;

import java.util.ArrayList;

public class MainClass {
    public static GameFrame frame;
    static Game game;

    public static void main(String args[]) {
        game = new Game();
        game.showFPS(true);
        frame = new GameFrame("Ball Game", game);
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);

        Player player1 = new Player(1);
        player1.setxPos(100);
        player1.addToList();

        Player player2 = new Player(2);
        player2.setxPos(-100);
        player2.addToList();

        new ItemSpawner(1, 10).addToList();

        new Wall(-520, 380, 520, 360).addToList();
        new Wall(-520, -380, 520, -360).addToList();
        new Wall(-520, 380, -500, -380).addToList();
        new Wall(520, 380, 500, -380).addToList();

        Entity.addEntityList(Entity.entities);
    }
}
