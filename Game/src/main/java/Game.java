import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game  extends Canvas implements Runnable{
    private static final long serialVersionUID = 1L;

    private boolean isRunning = false;
    private Thread thread;
    private Handler handler;
    private Camera camera;
    public int ammo = 100;

    private BufferedImage level = null;

    public Game(){
        new Window(1200,800,"WizardGame",this);
        start();
        handler = new Handler();
        camera = new Camera(0,0);
        this.addKeyListener(new KeyInput(handler));
        this.addMouseListener(new MouseInput(handler,camera,this));

        BufferedImageLoader loader = new BufferedImageLoader();
        level = loader.loadImage("/wizzard_level.png");

        loadLevel(level);
    }

    public static void main(String args[]){
        new Game();
    }

    private void start(){
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop(){
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(isRunning){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1){
                tick();
                //updates++;
                delta--;
            }
            render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                frames = 0;
                //updates = 0;
            }
        }
        stop();
    }

    private void tick() {


        for (int i = 0; i < handler.objects.size(); i++) {
            if(handler.objects.get(i).getId() == ID.Player){
                camera.tick(handler.objects.get(i));
            }
        }
        handler.tick();
    }

    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.red);
        g.fillRect(0,0,1200,800);

        g2d.translate(-camera.getX(),-camera.getY());

        handler.render(g);

        g2d.translate(camera.getX(),camera.getY());

        g.dispose();
        bs.show();
    }
    
    private void loadLevel(BufferedImage image){
        int w = image.getWidth();
        int h = image.getHeight();

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int pixel = image.getRGB(i,j);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if(red > 150)
                    handler.addObject(new Block(i*32,j*32,ID.Block));
                if(blue > 155)
                    handler.addObject(new Wizard(i*32,j*32,ID.Player,handler));
                if(green == 255)
                    handler.addObject(new Enemy(i*32,j*32,ID.Enemy,handler));
                if(red == 140){
                    handler.addObject(new Crate(i*32,j*32,ID.Crate));
                }
            }
        }
    }
}
