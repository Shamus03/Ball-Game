package item;

import entity.Player;
import powerup.Orbital;

import java.awt.*;

public class OrbitalItem extends Item {
    private int durationSeconds = 15;

    public OrbitalItem() {
        super();
        color = Color.gray;
    }

    public void supplyEffect(Player p) {
        new Orbital(p, durationSeconds, this).addToList();
    }
}
