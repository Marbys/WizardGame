import java.awt.*;

public class Bullet extends GameObject {

    private Handler handler;

    public Bullet(int x, int y, ID id, Handler handler, int mx, int my) {
        super(x, y, id);
        this.handler = handler;

//        velX = (mx - x) /10;
//        velY = (my - y) /10;
        calculateVelocity(x,y,mx,my);
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;

        for (int i = 0; i < handler.objects.size(); i++) {
            GameObject tempObject = handler.objects.get(i);

            if(tempObject.getId() == ID.Block){
                if(getBounds().intersects(tempObject.getBounds())){
                    handler.removeObject(this);
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.green);
        g.fillOval(x,y,8,8);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,8,8);
    }

    public void calculateVelocity(int fromX, int fromY, int toX, int toY)
    {
        double distance = Math.sqrt(Math.pow((toX - fromX), 2) + Math.pow((toY - fromY), 2));
        double speed = 10; //set the speed in [2,n)  n should be < 20 for normal speed
        //find Y
        velY = (float)((toY - fromY) * speed / distance);
        //find X
        velX = (float)((toX - fromX) * speed / distance);
    }
}
