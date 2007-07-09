package examples.bounce;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * Representation of a ball.  Uses a AWT Rectangle to
 * represent both the location and size of the ball, and
 * a vector (x and y offsets) for motion.  Calculates an int
 * angle from the vector for caller's convenience. Represents
 * color as floats of R, G, and B values for JOGL's convenience.
 */
public class BouncingBall extends Object {

    protected Rectangle rect;
    protected double xv;
    protected double yv;
    protected double angle;

    protected float red;
    protected float green;
    protected float blue;

    /**
     * Creates a ball with the given size and motion
     */
    public BouncingBall(Rectangle rect, double xv, double yv, Color color) {
        this.rect = rect;
        this.xv = xv;
        this.yv = yv;
        initRGB(color);
        calculateAngle();
    }

    /**
     * Creates a stationary ball of the given size
     */
    public BouncingBall(Rectangle rect, Color color) {
        this(rect, 0d, 0d, color);
    }

    /**
     * converts an AWT Color to the RGB floats that JOGL likes
     */
    protected void initRGB(Color color) {
        red = color.getRed() / 255f;
        green = color.getGreen() / 255f;
        blue = color.getBlue() / 255f;
    }


    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public double getXV() {
        return xv;
    }

    public double getYV() {
        return yv;
    }

    public void setXV(double xv) {
        this.xv = xv;
        calculateAngle();
    }

    public void setYV(double yv) {
        this.yv = yv;
        calculateAngle();
    }

    public double getAngle() {
        return angle;
    }

    public void calculateAngle() {
        // special case for divide-by-zero
        if (xv == 0d) {
            if (yv > 0)
                angle = 90d;
            else
                angle = 270d;
            return;
        }
        // get arctangent in radians
        double angleRad = Math.atan(yv / xv);
        // that returns -pi/2 <= angleRad <= pi/2
        // adjust degrees based on what way we're really going
        angle = angleRad * 57.2957795d;
        if ((xv < 0) && (yv >= 0)) {
            angle += 180;
        } else if ((xv < 0) && (yv < 0)) {
            angle += 180;
        } else if ((xv >= 0) && (yv < 0)) {
            angle += 360;
        }
        // System.out.println ("angle is " + angle + " degrees");
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }
}
