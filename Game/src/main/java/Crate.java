import java.awt.*;

public class Crate extends GameObject {
    public Crate(int x, int y, ID id) {
        super(x, y, id);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.cyan);
        g.fillRect(x,y,32,32);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,32,32);
    }
}
