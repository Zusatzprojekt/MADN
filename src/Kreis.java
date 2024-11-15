import javax.swing.*;
import java.awt.*;

public class Kreis extends JComponent {
    private int x = 0;
    private int y = 0;
    private int r = 0;
    private Color fillColor = Color.BLACK;
    private Color borderColor = Color.BLACK;
    private int borderWidth = 1;

//    public Kreis() {
//        r = 10;
//    }
//
//    public Kreis(int r) {
//        this.r = r;
//    }
//
//    public Kreis(int r, Color color) {
//        this.r = r;
//        this.fillColor = color;
//        this.borderColor = color;
//    }
//
//    public Kreis(int x, int y, int r) {
//        this.x = x;
//        this.y = y;
//        this.r = r;
//    }

    public Kreis(int x, int y, int r, Color fillColor, Color borderColor, int borderWidth) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public int getRadius() {
        return r;
    }
//
//    public void setPosition(Point point) {
//        x = point.x;
//        y = point.y;
//    }
//
//    public void setPosition(int x, int y) {
//        this.x = x;
//        this.y = y;
//    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color oc = g2.getColor();
        Stroke os = g2.getStroke();

        g2.setColor(fillColor);
        g2.fillOval(x - r, y - r, r * 2, r * 2);

        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke((float) borderWidth));
        g2.drawOval(x - r, y - r, r * 2, r * 2);

        g2.setColor(oc);
        g2.setStroke(os);
    }


}
