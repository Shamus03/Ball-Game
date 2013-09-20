package powerup;

import entity.Entity;
import entity.Player;

public class Powerup extends Entity {
    public long birthTime;
    public long deathTime;

    public Player parent;

    public Powerup(Player parent, int durationSeconds) {
        this.parent = parent;
        birthTime = System.currentTimeMillis();
        deathTime = birthTime + durationSeconds * 1000;
    }

    public void tick(int delta) {
        if (System.currentTimeMillis() >= deathTime)
            removeFromList();
    }

    public void removeEffect() {
        //does nothing; extend class
    }

    public void removeFromList() { //removes self from main list of players
        removeEffect();
        super.removeFromList();
    }
}
