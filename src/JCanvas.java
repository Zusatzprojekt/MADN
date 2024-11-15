import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class JCanvas extends JComponent implements MouseListener, MouseMotionListener {

    int x_old, y_old = 0;

    public JCanvas() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.setBackground(Color.white);

        g.setColor(Color.GREEN);
        g.fillRect(50, 50, 50, 50);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        Graphics g = this.getGraphics();
        g.fillOval(x,y,3,3);
        g.dispose();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x_old = e.getX();
        y_old = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        Graphics g = this.getGraphics();
        g.drawLine(x_old, y_old, x, y);
        g.dispose();

        x_old = x;
        y_old = y;

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        Graphics g = this.getGraphics();

        g.setColor(Color.white);

        g.fillRect(75, 120, 80, 100);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("X: " + String.valueOf(x), 50, 150);
        g.drawString("Y: " + String.valueOf(y), 50, 200);

    }
}