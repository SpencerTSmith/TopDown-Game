import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game  extends Canvas implements Runnable
{

	private boolean isRunning = false;
	private Thread thread;
	private Handler handler;
	private Camera camera;
	private SpriteSheet ss;
	
	private BufferedImage level = null;
	private BufferedImage spriteSheet = null;
	private BufferedImage floor = null;
	
	public Game()
	{
		new Window(1000, 623, "TopDown", this);
		start();
		
		handler = new Handler();
		camera = new Camera(0, 0);
		this.addKeyListener(new KeyInput(handler));
		
		BufferedImageLoader loader = new BufferedImageLoader();
		level = loader.loadImage("/level.png");
		spriteSheet = loader.loadImage("/spritesheet.png");
		
		ss = new SpriteSheet(spriteSheet);
		
		floor = ss.grabImage(1, 2, 32, 32);
		
		this.addMouseListener(new MouseInput(handler, camera, ss));
		
		loadLevel(level);
	}
	
	private void start()
	{
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	private void stop()
	{
		isRunning = false;
		try {
			thread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public void run() 
	{
		//basically what is constantly running while playing game, rendering and updating the window,
		//this was created for minecraft
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(isRunning)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) 
			{
				tick();
				delta--;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
				frames = 0;
			}
		}
		stop();
	}
	
	public void tick()
	{
		//looks through objects to find player, attaches camera to that object
		for(int i = 0; i < handler.object.size(); i++)
		{
			if(handler.object.get(i).getId() == ID.Player)
			{
				camera.tick(handler.object.get(i));
			}
		}
		handler.tick();
	}
	
	public void render()
	{
		//preloading frames
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		//cast into 2d graphics
		Graphics2D g2d = (Graphics2D) g;
		//here this is what graphics will show
		
		g.setColor(Color.MAGENTA);
		g.fillRect(0, 0, 1000, 623);
		
		g2d.translate(-camera.getX(), -camera.getY());
		//translating from here
		
		for(int xx = 0; xx < 32 * 64; xx+=32)
		{
			for(int yy = 0; yy < 32 * 64; yy+=32)
			{
				g.drawImage(floor, xx, yy, null);
			}
		}
		
		
		
		handler.render(g);
		
		//to here
		g2d.translate(camera.getX(), camera.getY());
		//here 
		
		g.dispose();
		bs.show();
		
	}
	
	private void loadLevel(BufferedImage image)
	{
		int w = image.getWidth();
		int h = image.getHeight();
		
		for(int xx = 0; xx < w; ++xx)
		{
			for(int yy = 0; yy < h; ++yy)
			{
				int pixel = image.getRGB(xx, yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				if(red > 250)
					handler.addObject(new Block(xx*32, yy*32, ID.Block, handler, ss));
				if(blue > 250)
					handler.addObject(new Player(xx*32, yy*32, ID.Player, handler, ss));
				if(green > 250)
					handler.addObject(new Enemy(xx*32, yy*32, ID.Enemy, handler, ss));
			}
		}
	}
	
	public static void main(String args[])
	{
		new Game();
		
	}

	
}
