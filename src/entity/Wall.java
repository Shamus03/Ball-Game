package entity;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import powerup.Orbital;

public class Wall extends Entity
{
	Rectangle bounds;
	Color color;

	float xMin,yMin,xMax,yMax;

	public Wall(int x1, int y1, int x2, int y2)
	{
		bounds = new Rectangle(x1,y1,x2-x1,y2-y1);
		color = Color.gray;

		xMin = (float) bounds.getMinX();
		yMin = (float) bounds.getMinY();
		xMax = (float) bounds.getMaxX();
		yMax = (float) bounds.getMaxY();
	}

    public void onCollide(Entity e) {
        if ( e instanceof Player )
            collide((Player)e);
        else if ( e instanceof Orbital )
            collide((Orbital)e);

    }

	public void collide(Player p)
	{
		if(p.yPos >= yMin && p.yPos <= yMax)	//left and right side collision
		{
			if(p.xPos + p.radius >= xMin && p.xPos - p.xVel <= xMin)
			{
				p.xPos = xMin - p.radius;
				p.xVel *= -p.RESTITUTION;
			}
			if(p.xPos - p.radius <= xMax && p.xPos - p.xVel >= xMax)
			{
				p.xPos = xMax + p.radius;
				p.xVel *= -p.RESTITUTION;
			}
		}
		if(p.xPos >= xMin && p.xPos <= xMax)	//top and bottom side collision
		{
			if(p.yPos + p.radius >= yMin && p.yPos - p.yVel <= yMin)
			{
				p.yPos = yMin - p.radius;
				p.yVel *= -p.RESTITUTION;
			}
			if(p.yPos - p.radius <= yMax && p.yPos - p.yVel >= yMax)
			{
				p.yPos = yMax + p.radius;
				p.yVel *= -p.RESTITUTION;
			}
		}

		if(p.distance(p.xPos,p.yPos,xMin,yMin) <= p.radius)	//upper left corner collision
		{
			Double angle = Math.atan2(yMin-p.yPos,xMin-p.xPos);

			if(p.xVel > 0)
				p.xVel *= -p.RESTITUTION *Math.cos(angle);
			if(p.yVel > 0)
				p.yVel *= -p.RESTITUTION *Math.sin(angle);

			p.xPos = (float) (xMin - p.radius *Math.cos(angle));
			p.yPos = (float) (yMin - p.radius *Math.sin(angle));
		}

		if(p.distance(p.xPos,p.yPos,xMin,yMax) <= p.radius)	//lower left corner collision
		{
			Double angle = Math.atan2(yMax-p.yPos,xMin-p.xPos);

			if(p.xVel > 0)
				p.xVel *= -p.RESTITUTION *Math.cos(angle);
			if(p.yVel < 0)
				p.yVel *= p.RESTITUTION *Math.sin(angle);

			p.xPos = (float) (xMin - p.radius *Math.cos(angle));
			p.yPos = (float) (yMax - p.radius *Math.sin(angle));
		}

		if(p.distance(p.xPos,p.yPos,xMax,yMin) <= p.radius)	//upper right corner collision
		{
			Double angle = Math.atan2(yMin-p.yPos,xMax-p.xPos);

			if(p.xVel < 0)
				p.xVel *= p.RESTITUTION *Math.cos(angle);
			if(p.yVel > 0)
				p.yVel *= -p.RESTITUTION *Math.sin(angle);

			p.xPos = (float) (xMax - p.radius *Math.cos(angle));
			p.yPos = (float) (yMin - p.radius *Math.sin(angle));
		}

		if(p.distance(p.xPos,p.yPos,xMax,yMax) <= p.radius)	//lower right corner collision
		{
			Double angle = Math.atan2(yMax-p.yPos,xMax-p.xPos);

			if(p.xVel < 0)
				p.xVel *= p.RESTITUTION *Math.cos(angle);
			if(p.yVel < 0)
				p.yVel *= p.RESTITUTION *Math.sin(angle);

			p.xPos = (float) (xMax - p.radius *Math.cos(angle));
			p.yPos = (float) (yMax - p.radius *Math.sin(angle));
		}

	}
	
	@SuppressWarnings("static-access")
	public void collide(Orbital o)
	{
		if(o.yPos >= yMin && o.yPos <= yMax)	//left and right side collision
		{
			if(o.xPos + o.size >= xMin && o.xPos - o.xVel <= xMin)
			{
				o.xPos = xMin - o.size;
				o.xVel *= -o.restitution;
			}
			if(o.xPos - o.size <= xMax && o.xPos - o.xVel >= xMax)
			{
				o.xPos = xMax + o.size;
				o.xVel *= -o.restitution;
			}
		}
		if(o.xPos >= xMin && o.xPos <= xMax)	//top and bottom side collision
		{
			if(o.yPos + o.size >= yMin && o.yPos - o.yVel <= yMin)
			{
				o.yPos = yMin - o.size;
				o.yVel *= -o.restitution;
			}
			if(o.yPos - o.size <= yMax && o.yPos - o.yVel >= yMax)
			{
				o.yPos = yMax + o.size;
				o.yVel *= -o.restitution;
			}
		}

		if(o.distance(o.xPos,o.yPos,xMin,yMin) <= o.size)	//upper left corner collision
		{
			Double angle = Math.atan2(yMin-o.yPos,xMin-o.xPos);

			if(o.xVel > 0)
				o.xVel *= -o.restitution*Math.cos(angle);
			if(o.yVel > 0)
				o.yVel *= -o.restitution*Math.sin(angle);

			o.xPos = xMin - o.size*Math.cos(angle);
			o.yPos = yMin - o.size*Math.sin(angle);
		}

		if(o.distance(o.xPos,o.yPos,xMin,yMax) <= o.size)	//lower left corner collision
		{
			Double angle = Math.atan2(yMax-o.yPos,xMin-o.xPos);

			if(o.xVel > 0)
				o.xVel *= -o.restitution*Math.cos(angle);
			if(o.yVel < 0)
				o.yVel *= o.restitution*Math.sin(angle);

			o.xPos = xMin - o.size*Math.cos(angle);
			o.yPos = yMax - o.size*Math.sin(angle);
		}

		if(o.distance(o.xPos,o.yPos,xMax,yMin) <= o.size)	//upper right corner collision
		{
			Double angle = Math.atan2(yMin-o.yPos,xMax-o.xPos);

			if(o.xVel < 0)
				o.xVel *= o.restitution*Math.cos(angle);
			if(o.yVel > 0)
				o.yVel *= -o.restitution*Math.sin(angle);

			o.xPos = xMax - o.size*Math.cos(angle);
			o.yPos = yMin - o.size*Math.sin(angle);
		}

		if(o.distance(o.xPos,o.yPos,xMax,yMax) <= o.size)	//lower right corner collision
		{
			Double angle = Math.atan2(yMax-o.yPos,xMax-o.xPos);

			if(o.xVel < 0)
				o.xVel *= o.restitution*Math.cos(angle);
			if(o.yVel < 0)
				o.yVel *= o.restitution*Math.sin(angle);

			o.xPos = xMax - o.size*Math.cos(angle);
			o.yPos = yMax - o.size*Math.sin(angle);
		}

	}

	public void onCollide(Bullet b)
	{
		boolean bounced = false;

		if(b.yPos >= yMin && b.yPos <= yMax)	//left and right side collision
		{
			if(b.xPos + b.radius >= xMin && b.xPos - b.xVel <= xMin)
			{
				b.xPos = xMin - b.radius;
				b.xVel *= -1;
				bounced = true;
			}
			if(b.xPos - b.radius <= xMax && b.xPos - b.xVel >= xMax)
			{
				b.xPos = xMax + b.radius;
				b.xVel *= -1;
				bounced = true;
			}
		}
		if(b.xPos >= xMin && b.xPos <= xMax)	//top and bottom side collision
		{
			if(b.yPos + b.radius >= yMin && b.yPos - b.yVel <= yMin)
			{
				b.yPos = yMin - b.radius;
				b.yVel *= -1;
				bounced = true;
			}
			if(b.yPos - b.radius <= yMax && b.yPos - b.yVel >= yMax)
			{
				b.yPos = yMax + b.radius;
				b.yVel *= -1;
				bounced = true;
			}
		}

		if(b.distance(b.xPos,b.yPos,xMin,yMin) <= b.radius)	//upper left corner collision
		{
			Double angle = Math.atan2(yMin-b.yPos,xMin-b.xPos);

			if(b.xVel > 0)
				b.xVel *= -1*Math.cos(angle);
			if(b.yVel > 0)
				b.yVel *= -1*Math.sin(angle);

			b.xPos = (float) (xMin - b.radius*Math.cos(angle));
			b.yPos = (float) (yMin - b.radius*Math.sin(angle));
			bounced = true;
		}

		if(b.distance(b.xPos,b.yPos,xMin,yMax) <= b.radius)	//lower left corner collision
		{
			Double angle = Math.atan2(yMax-b.yPos,xMin-b.xPos);

			if(b.xVel > 0)
				b.xVel *= -1*Math.cos(angle);
			if(b.yVel < 0)
				b.yVel *= Math.sin(angle);

			b.xPos = (float) (xMin - b.radius*Math.cos(angle));
			b.yPos = (float) (yMax - b.radius*Math.sin(angle));
			bounced = true;
		}

		if(b.distance(b.xPos,b.yPos,xMax,yMin) <= b.radius)	//upper right corner collision
		{
			Double angle = Math.atan2(yMin-b.yPos,xMax-b.xPos);

			if(b.xVel < 0)
				b.xVel *= Math.cos(angle);
			if(b.yVel > 0)
				b.yVel *= -1*Math.sin(angle);

			b.xPos = (float) (xMax - b.radius*Math.cos(angle));
			b.yPos = (float) (yMin - b.radius*Math.sin(angle));
			bounced = true;
		}

		if(b.distance(b.xPos,b.yPos,xMax,yMax) <= b.radius)	//lower right corner collision
		{
			Double angle = Math.atan2(yMax-b.yPos,xMax-b.xPos);

			if(b.xVel < 0)
				b.xVel *= Math.cos(angle);
			if(b.yVel < 0)
				b.yVel *= Math.sin(angle);

			b.xPos = (float) (xMax - b.radius*Math.cos(angle));
			b.yPos = (float) (yMax - b.radius*Math.sin(angle));
			bounced = true;
		}

		if(bounced)
			b.bounces--;
	}

	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillRect(bounds.x,bounds.y,bounds.width,bounds.height);
		g.setColor(Color.black);
		for(int i = 0; i < 5; i++)
			g.drawRect(bounds.x + i,bounds.y + i,bounds.width - i*2,bounds.height - i*2);
	}
}