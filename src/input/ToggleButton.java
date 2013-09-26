package input;

import java.awt.*;

public class ToggleButton extends Button {
    Color onColor;

    public ToggleButton(int xPos, int yPos, int width, int height, String text, Color color) {
        super(xPos, yPos, width, height, text, color);
        onColor = color;
        this.color = Color.white;
        init();
    }

    public void press() {
        setChosen(!chosen);
    }

    public void setChosen(boolean b) {
        chosen = b;
        if (chosen)
            color = onColor;
        else
            color = Color.white;
    }
}