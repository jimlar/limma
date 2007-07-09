package examples.bounce;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class BallBouncer extends Object
        implements GLEventListener, ActionListener {

    GLCanvas canvas;

    public static final double TWO_PI = 2 * Math.PI;
    public static final double ARC_SEGMENT = TWO_PI / 36; // 10 degrees

    public long lastUpdateTime;

    /**
     * All the BouncingBalls we're dealing with
     */
    protected ArrayList balls;

    protected Rectangle worldWindowRect;
    boolean worldWindowChanged;

    protected Rectangle wallInterior;

    protected int ballShape;

    public static final int CIRCLE_BALL_SHAPE = 0;
    public static final int ARROW_BALL_SHAPE = 1;

    int viewportWidth;
    int viewportHeight;

    public BallBouncer(Rectangle worldWindowRect) {
        this.worldWindowRect = worldWindowRect;
        worldWindowChanged = false;

        initWall();

        ballShape = CIRCLE_BALL_SHAPE;

        // angleChanged = false;
        balls = new ArrayList();
        lastUpdateTime = System.currentTimeMillis();
        // get a GLCanvas
        canvas = new GLCanvas();

        // add a GLEventListener, which will get called when the
        // canvas is resized or needs a repaint
        canvas.addGLEventListener(this);

        // temp debug
        javax.swing.Timer timer = new javax.swing.Timer(25, this);
        timer.start();

    }

    public GLCanvas getCanvas() {
        return canvas;
    }

    //
    // GLEventListener methods
    // 

    /**
     * Called after OpenGL is init'ed
     */
    public void init(GLAutoDrawable drawable) {
        System.out.println("init()");
        GL gl = drawable.getGL();
        // set erase color
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); //white
        // set initial drawing color and point size
        gl.glColor3f(0.0f, 0.0f, 0.0f);
    }

    /**
     * Called to indicate the drawing surface has been moved and/or resized
     */
    public void reshape(GLAutoDrawable drawable,
                        int x,
                        int y,
                        int width,
                        int height) {
        GL gl = drawable.getGL();

        GLU glu = new GLU();
        ;

        // save size for viewport reset
        viewportWidth = width;
        viewportHeight = height;

        resetWorldWindow(gl, glu);

    }

    /**
     * Called by drawable to initiate drawing
     */
    public void display(GLAutoDrawable drawable) {
        long inTime = System.currentTimeMillis();
        // System.out.println ("display()");
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        // is there a pending world window change?
        if (worldWindowChanged)
            resetWorldWindow(gl, glu);

        // load identity matrix
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

        // clear screen
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        // draw the wallInterior
        // TODO: fatten this up?
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex2i(wallInterior.x,
                      wallInterior.y);
        gl.glVertex2i(wallInterior.x + wallInterior.width,
                      wallInterior.y);
        gl.glVertex2i(wallInterior.x + wallInterior.width,
                      wallInterior.y + wallInterior.height);
        gl.glVertex2i(wallInterior.x,
                      wallInterior.y + wallInterior.height);
        gl.glEnd();

        // loop through balls and draw them
        for (int i = 0; i < balls.size(); i++) {
            BouncingBall ball = (BouncingBall) balls.get(i);

            gl.glColor3f(ball.getRed(), ball.getGreen(), ball.getBlue());

            // draw the selected shape
            if (ballShape == ARROW_BALL_SHAPE)
                drawBallAsArrow(gl, ball);
            else
                drawBallAsCircle(gl, ball);
        }
    }

    /**
     * Called by drawable to indicate mode or device has changed
     */
    public void displayChanged(GLAutoDrawable drawable,
                               boolean modeChanged,
                               boolean deviceChanged) {
        System.out.println("displayChanged()");
    }

    //
    // end of GLEventListener methods
    //

    /**
     * creates the interior wall, which is 5 units in from
     * the original world window
     */
    protected void initWall() {
        wallInterior = new Rectangle(worldWindowRect.x + 5,
                                     worldWindowRect.y + 5,
                                     worldWindowRect.width - 10,
                                     worldWindowRect.height - 10);
    }

    protected void resetWorldWindow(GL gl, GLU glu) {
        System.out.println("reset world window: " + worldWindowRect);
        // set the world window
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(worldWindowRect.x,
                       worldWindowRect.x + worldWindowRect.width,
                       worldWindowRect.y,
                       worldWindowRect.y + worldWindowRect.height);
        // set viewport
        // args are x, y, width, height
        gl.glViewport(0, 0, viewportWidth, viewportHeight);

        worldWindowChanged = false;
    }

    public void setWorldWindow(Rectangle worldWindowRect) {
        this.worldWindowRect = worldWindowRect;
        worldWindowChanged = true;
    }

    public Rectangle getWorldWindow() {
        return worldWindowRect;
    }

    public Rectangle getWallInterior() {
        return wallInterior;
    }

    public void addBall(Rectangle rect, double xv, double yv, Color c) {
        BouncingBall ball = new BouncingBall(rect, xv, yv, c);
        balls.add(ball);
    }

    public int getBallCount() {
        return balls.size();
    }

    public void clearBalls() {
        balls.clear();
    }

    public void setBallShape(int ballShape) {
        this.ballShape = ballShape;
    }

    public int getBallShape() {
        return ballShape;
    }

    public void drawBallAsCircle(GL gl,
                                 BouncingBall ball) {
        // System.out.println ("drawCircle at " + cx + "," + cy);
        Rectangle rect = ball.getRect();
        int cx = rect.x + (rect.width / 2);
        int cy = rect.y + (rect.height / 2);
        int radius = rect.width / 2;
        gl.glBegin(GL.GL_POLYGON);
        // figure out points on a circle with width of (x2-x1)/2
        // TODO: non-arbitrary number of points
        for (double theta = 0; theta < TWO_PI; theta += ARC_SEGMENT) {
            int x = (int) (cx + (Math.sin(theta) * radius));
            int y = (int) (cy + (Math.cos(theta) * radius));
            gl.glVertex2i(x, y);
        }
        gl.glEnd();
    }


    public void drawBallAsArrow(GL gl,
                                BouncingBall ball) {
        // System.out.println ("drawBallAsArrow");
        Rectangle rect = ball.getRect();
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslated(rect.x + 50, rect.y + 50, 0);
        gl.glScaled(rect.width / 100d, rect.height / 100d, 0);
        gl.glRotated(ball.getAngle(), 0, 0, 1);
        gl.glBegin(GL.GL_POLYGON);
        gl.glVertex2i(50, 0);
        gl.glVertex2i(-50, 50);
        gl.glVertex2i(0, 0);
        gl.glVertex2i(-50, -50);
        gl.glEnd();
    }


    /**
     * updates balls' positions
     */
    public void actionPerformed(ActionEvent e) {
        // calculate elapsed time since last update
        long elapsed = System.currentTimeMillis() - lastUpdateTime;
        // System.out.println ("elapsed ms = " + elapsed);
        double elapsedSec = elapsed / 1000d;
        lastUpdateTime = System.currentTimeMillis();
        // move each ball
        for (int i = 0; i < balls.size(); i++) {
            BouncingBall ball = (BouncingBall) balls.get(i);
            Rectangle rect = ball.getRect();
            // is ball touching a wall, if so bounce
            // (checking location _and_ direction keeps balls from
            // getting "stuck" in the wall)
            // left
            if ((rect.x <= wallInterior.x) && (ball.getXV() < 0)) {
                ball.setXV(ball.getXV() * -1);
            }
            // right
            if (((rect.x + rect.width) >= (wallInterior.x + wallInterior.width)) && (ball.getXV() > 0)) {
                ball.setXV(ball.getXV() * -1);
            }
            // bottom
            if ((rect.y <= wallInterior.y) && (ball.getYV() < 0)) {
                ball.setYV(ball.getYV() * -1);
            }
            // top
            if (((rect.y + rect.height) >= (wallInterior.y + wallInterior.height)) && (ball.getYV() > 0)) {
                ball.setYV(ball.getYV() * -1);
            }
            // calculate movement
            double dx = ball.getXV() * elapsedSec;
            double dy = ball.getYV() * elapsedSec;
            int oldx = rect.x;
            rect.x = (int) (((double) rect.x) + dx);
            rect.y = (int) (((double) rect.y) + dy);
        } // for
    } // run

}
