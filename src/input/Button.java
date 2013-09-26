package input;

import entity.Entity;

import java.awt.*;

public class Button extends Entity {
    int width;
    int height;

    public String text;

    Color color;
    Color onColor;

    Rectangle bounds;

    public boolean chosen = false;

    boolean clicked = false;

    public Button(int xPos, int yPos, int width, int height, String text, Color color) {
        this.color = this.onColor = color;

        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.text = text;

        bounds = new Rectangle(xPos, yPos, width, height);

        init();
    }

    public void init() {

    }

    public void draw(Graphics2D g) {
        if (bounds.contains(Input.MOUSE_X, Input.MOUSE_Y))    //draw darker if mouse is touching it
        {
            g.setColor(color.darker());
            if (Input.MOUSE_LEFT) {
                if (!clicked) {
                    press();
                    clicked = true;
                }
            } else
                clicked = false;
        } else
            g.setColor(color);

        g.fillRoundRect((int) xPos, (int) yPos, width, height, 10, 10);
        g.setColor(Color.black);
        g.setFont(new Font("Monospaced", 0, (int) (height / 2)));
        g.drawString(text, (int) (xPos + (width - text.length() * height / 3.4) / 2), (int) (yPos + height / 1.55));    //draw text centered
    }

    public void press() {
        //press action
    }

    public void setColor(Color c) {
        color = c;
    }
}