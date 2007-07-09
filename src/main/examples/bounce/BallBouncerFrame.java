package examples.bounce;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.BoxLayout;

import com.sun.opengl.util.Animator;

public class BallBouncerFrame extends Frame
        implements ActionListener, ItemListener {

    BallBouncer ballBouncer;
    Panel configPanel;
    TextField ballCountField;
    Choice viewChoice;
    Checkbox shapeCheckbox;


    public static final Rectangle INITIAL_BOUNCER_VIEW =
            new Rectangle(0, 0, 3000, 3000);

    protected static final Rectangle[] BOUNCER_VIEW_CHOICES = {
            new Rectangle(1000, 1000, 1000, 1000),
            new Rectangle(500, 500, 2000, 2000),
            INITIAL_BOUNCER_VIEW,
            new Rectangle(-500, -500, 4000, 4000),
            new Rectangle(-1000, -1000, 5000, 5000),
    };
    protected static final int BOUNCER_VIEW_DEFALT_INDEX = 2;

    Random rand;

    public static final int BALL_WIDTH = 150;
    public static final int BALL_HEIGHT = 150;

    public BallBouncerFrame() {
        super("JOGL Animation Demo");
        rand = new Random();

        // build GUI
        setLayout(new BorderLayout());
        ballBouncer = new BallBouncer(INITIAL_BOUNCER_VIEW);
        add(ballBouncer.getCanvas(), BorderLayout.CENTER);

        configPanel = new Panel();
        configPanel.setLayout(new BoxLayout(configPanel,
                                            BoxLayout.Y_AXIS));

        // ball count label and field
        Panel countPanel = new Panel(new FlowLayout());
        countPanel.add(new Label("Balls: "));
        ballCountField = new TextField(4);
        ballCountField.setText("0");
        ballCountField.addActionListener(this);
        countPanel.add(ballCountField);
        configPanel.add(countPanel);

        // view choice
        Panel viewPanel = new Panel(new FlowLayout());
        viewPanel.add(new Label("View: "));
        viewChoice = new Choice();
        for (int i = 0; i < BOUNCER_VIEW_CHOICES.length; i++) {
            viewChoice.add(BOUNCER_VIEW_CHOICES[i].width +
                           "x" +
                           BOUNCER_VIEW_CHOICES[i].height);
        }
        viewChoice.select(BOUNCER_VIEW_DEFALT_INDEX);
        viewChoice.addItemListener(this);
        viewPanel.add(viewChoice);
        configPanel.add(viewPanel);

        // arrox checkbox
        shapeCheckbox = new Checkbox("Draw as arrows", false);
        shapeCheckbox.addItemListener(this);
        configPanel.add(shapeCheckbox);

        add(configPanel, BorderLayout.SOUTH);

        // set up quit-on-close behavior
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    /**
     * called when user hits return on ballCountField; clears
     * balls and creates n random balls
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == ballCountField)
            resetBallCount();
    }

    public void resetBallCount() {
        // change number of bouncing balls
        try {
            int ballCount = Integer.parseInt(ballCountField.getText());
            ballBouncer.clearBalls();
            Rectangle wallInterior = ballBouncer.getWallInterior();
            for (int i = 0; i < ballCount; i++) {
                // pick random  point within the wall interior
                int x = rand.nextInt(wallInterior.width - BALL_WIDTH) + wallInterior.x;
                int y = rand.nextInt(wallInterior.height - BALL_HEIGHT) + wallInterior.y;
                Rectangle ballRect =
                        new Rectangle(x, y, BALL_WIDTH, BALL_HEIGHT);
                // pick random speed
                // (up to 0.5 * width or height, positive or negative)
                double xv = (rand.nextDouble() - 0.5d) * (wallInterior.width);
                double yv = (rand.nextDouble() - 0.5d) * (wallInterior.height);
                // pick random color
                Color c = new Color(rand.nextInt(255),
                                    rand.nextInt(255),
                                    rand.nextInt(255));
                // add ball to bouncer
                ballBouncer.addBall(ballRect, xv, yv, c);
            } // for
        } catch (NumberFormatException nfe) {
            // discard bogus input
            ballCountField.setText(Integer.toString(ballBouncer.getBallCount()));
            Toolkit.getDefaultToolkit().beep();
        }
    } // resetBallCount


    public void resetBallShapes() {
        if (shapeCheckbox.getState())
            ballBouncer.setBallShape(BallBouncer.ARROW_BALL_SHAPE);
        else
            ballBouncer.setBallShape(BallBouncer.CIRCLE_BALL_SHAPE);
    }

    /**
     * called when the viewChoice is changed or the shape
     * checkbox is hit
     */
    public void itemStateChanged(ItemEvent ie) {
        if (ie.getSource() == shapeCheckbox)
            resetBallShapes();
        else if (ie.getSource() == viewChoice) {
            // dispatch to resetView(), which figures out what's selected
            if (ie.getStateChange() != ItemEvent.SELECTED)
                return;
            resetView();
        }
    }

    /**
     * get the selected index from viewChoice and use the equivalent
     * rectangle from BOUNCER_VIEW_CHOICES to reset the bouncer's
     * world window
     */
    public void resetView() {
        ballBouncer.setWorldWindow(BOUNCER_VIEW_CHOICES[viewChoice.getSelectedIndex()]);
    }


    public static void main(String[] arrrImAPirate) {
        BallBouncerFrame bbf = new BallBouncerFrame();
        bbf.pack();
        // force a size for the jogl component
        bbf.ballBouncer.getCanvas().setSize(400, 400);
        bbf.pack();
        bbf.setVisible(true);

        // tiny little stall to give awt a chance to get set up
        // before animator needs to get the canvas.  yes, a hack
        // but prevents this:
        /*
          Error: empty view at "src/native/jogl/MacOSXWindowSystemInterface.m:createContext:29"
          net.java.games.jogl.GLException: Error creating nsContext
          at net.java.games.jogl.impl.macosx.MacOSXGLContext.create(MacOSXGLContext.java:127)
          at net.java.games.jogl.impl.macosx.MacOSXGLContext.makeCurrent(MacOSXGLContext.java:136)
          at net.java.games.jogl.impl.macosx.MacOSXOnscreenGLContext.makeCurrent(MacOSXOnscreenGLContext.java:131)
          at net.java.games.jogl.impl.GLContext.invokeGL(GLContext.java:199)
          at net.java.games.jogl.impl.macosx.MacOSXOnscreenGLContext.invokeGL(MacOSXOnscreenGLContext.java:79)
          at net.java.games.jogl.GLCanvas.displayImpl(GLCanvas.java:182)
          at net.java.games.jogl.GLCanvas.display(GLCanvas.java:82)
          at net.java.games.jogl.Animator$1.run(Animator.java:104)
          at java.lang.Thread.run(Thread.java:552)
        */
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
        }

        // start animator
        Animator animator = new Animator(bbf.ballBouncer.getCanvas());
        animator.start();


    }


}
