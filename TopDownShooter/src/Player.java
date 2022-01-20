import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player extends GameObject {
	
	private int hp = 1;
	
	private BufferedImage playerTexture;
	
	public Player(int x, int y, ID id, Handler handler, SpriteSheet ss) {
		super(x, y, id, handler, ss);

		playerTexture = ss.grabImage(1, 1, 32, 32);
	}

	public void tick() {

		x+= velX;
		y+= velY;
		
		movement();
		
		//ticks collision after determining object's velocity, doesn't work when check before
		collision();
		
		//makes sure melee attacks do not go outside player's melee range
		meleeAttackRangeCheck();
		
		if(hp <= 0)
		{
			handler.removeObject(this);
			System.out.println("Game Over");
		}
		
	}

	private void collision() {
		for(int i = 0; i < handler.object.size(); ++i)
		{
			GameObject tempObject = handler.object.get(i);
			if(tempObject.getId() == ID.Block)
			{
				
				if(getBounds().intersects(tempObject.getBounds()))
				{
					if(velX > 0)
					{
						velX = 0;
						x = tempObject.getX() - 32;
					}
					else if(velX < 0)
					{
						velX = 0;
						x = tempObject.getX() + 32;
					}
				}
				
				if(getBounds2().intersects(tempObject.getBounds()))
				{
					if(velY > 0)
					{
						velY = 0;
						y = tempObject.getY() - 32;
					}
					else if(velY < 0)
					{
						velY = 0;
						y = tempObject.getY() + 32;
					}
				}
			}
			
			if(tempObject.getId() == ID.Enemy)
			{
				if(getBounds4().intersects(tempObject.getBounds()))
				{
					hp -= 1;
				}
			}
		}
	}
	
	private void meleeAttackRangeCheck()
	{
		
		for(int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
			//removes melee attack object as soon as it is out of melee range
			if(tempObject.getId() == ID.Melee)
			{
				if(!(getBounds3().intersects(tempObject.getBounds())))
				{
					handler.removeObject(tempObject);
				}
			}
		}
	}
	
	private void movement()
	{
		//how the player moves
		if(handler.isUp()) velY = -10;
		else if(!handler.isDown()) velY = 0;
				
		if(handler.isDown()) velY = 10;
		else if(!handler.isUp()) velY = 0;
				
		if(handler.isRight()) velX = 10;
		else if(!handler.isLeft()) velX = 0;
				
		if(handler.isLeft()) velX = -10;
		else if(!handler.isRight()) velX = 0;
				
		//diagonal movement too fast, so adjusted here
				
		if(handler.isLeft() && handler.isUp())
		{
			velY = -7;
			velX = -7;
		}
		if(handler.isRight() && handler.isUp())
		{
			velY = -7;
			velX = 7;
		}
		if(handler.isLeft() && handler.isDown())
		{
			velY = 7;
			velX = -7;
		}
		if(handler.isRight() && handler.isDown())
		{
			velY = 7;
			velX = 7;
		}
				
	}
	

	public void render(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		
		//g2d.setColor(Color.red);
		//g2d.fill(getBounds3());
		//g2d.setColor(Color.blue);
		//g2d.fill(getBounds());
		//g2d.setColor(Color.green);
		//g2d.fill(getBounds2());
		
		g.drawImage(playerTexture, x, y, null);
	}

	//horizontal collision
	public Rectangle getBounds() {
		
		int bx = (int) (x + velX);
		int by = y;
		int bw = 32;
		int bh = 32;
		
		return new Rectangle(bx, by, bw, bh);
	}
	
	//vertical collision
	public Rectangle getBounds2() {
		int bx = x;
		int by = (int) (y + velY);
		int bw = 32;
		int bh = 32;
		
		return new Rectangle(bx, by, bw, bh);
	}
	
	//melee player attack max range collision box
	public Rectangle getBounds3()
	{
		int bx = x - 16;
		int by = y -16;
		int bw = 32 * 2;
		int bh = 32 * 2;
		
		return new Rectangle(bx, by, bw, bh);
	}
	
	//player hitbox
	public Rectangle getBounds4()
	{
		int bx = x;
		int by = y;
		int bw = 32;
		int bh = 32;
		
		return new Rectangle(bx, by, bw, bh);
	}

}
