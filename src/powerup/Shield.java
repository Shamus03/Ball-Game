package powerup;

import camera.Camera;
import entity.Player;

import java.awt.*;


public class Shield extends Powerup {
    int depth;
    int radius;

    public Shield(Player p, int durationSeconds) {
        super(p, durationSeconds);

        parent = p;

        birthTime = System.currentTimeMillis();
        deathTime = birthTime + durationSeconds * 1000;

        radius = 12;
        depth = 5;

        parent.shielded = true;
    }

    public void draw(Graphics2D g) {
        parent.shielded = true;
        if (parent.shieldDrawn)        //don't draw more than one shield
            return;

        g.setColor(new Color(0, 255, 255, 100));
        Camera.fillCenteredOval(parent.getxPos(), parent.getyPos(), parent.radius * 2 + radius * 2, parent.radius * 2 + radius * 2, g);
        parent.shieldDrawn = true;
    }

    public void removeEffect() {
        parent.shielded = false;
    }
}
