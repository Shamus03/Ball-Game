package powerup;

import entity.Player;

public class TripleShot extends Powerup {
    public TripleShot(Player parent, int durationTime) {
        super(parent, durationTime);
    }

    public void tick(int delta) {
        parent.tripleShot = true;
        if (System.currentTimeMillis() >= deathTime)
            removeFromList();
    }

    public void removeEffect() {
        parent.tripleShot = false;
    }
}
