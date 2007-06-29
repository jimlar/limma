package examples.cubecb;
// CubeGL.java
// Andrew Davison, November 2006, ad@fivedots.coe.psu.ac.th

/* A JFrame contains a JPanel which holds a GLCanvas. The GLCanvas
   displays a rotating coloured cube. 

   There's also commented out code to use an opaque GLPanel, with
   the background drawn using Java 2D.

   The listener for the canvas is CubeGLListener, and the updates
   to the canvas' display are triggered by FPSAnimator using 
   fixed-rate scheduling.

   The code uses the JSR-231 1.0.0 release build of JOGL, 
   14th September 2006.

   The GLCanvas could be added directly to the JFrame, but this 
   approach allows other components to be included in the JFrame. 
   In this example, there's a textfield which reports the cube 
   rotations in the x,y,z- axes.
*/

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;

import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sun.opengl.util.FPSAnimator;    // for FPSAnimator


public class CubeGL extends JFrame {
    private static int DEFAULT_FPS = 80;

    private static final int PWIDTH = 512;   // initial size of panel
    private static final int PHEIGHT = 512;

    private CubeGLListener listener;
    private FPSAnimator animator;

    private JTextField rotsTF;   // displays cube rotations
    private DecimalFormat df = new DecimalFormat("0.#");  // 1 dp

    private Font font;


    public CubeGL(int fps) {
        super("CubeGL (Callback)");

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(makeRenderPanel(fps), BorderLayout.CENTER);

        rotsTF = new JTextField("Rotations: ");
        rotsTF.setEditable(false);
        c.add(rotsTF, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
                /* The animator must be stopped in a different thread from
             the AWT event queue, to make sure that it completes before
             exit is called. */ {
                new Thread(new Runnable() {
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            } // end of windowClosing()
        });

        pack();
        setVisible(true);

        animator.start();
    } // end of CubeGL()


    private JPanel makeRenderPanel(int fps)
        /* Construct a GLCanvas in a JPanel, and add a
           listener and animator. This method also contains
           alternative code for creating a GLPanel with a
           fancy background.
        */ {
        JPanel renderPane = new JPanel();   // for GLCanvas
        // JPanel renderPane = createBackPanel();   // for the GLJPanel

        renderPane.setLayout(new BorderLayout());
        renderPane.setOpaque(false);
        renderPane.setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

        GLCanvas canvas = new GLCanvas();
/*
    // for GLJPanel
    GLCapabilities caps = new GLCapabilities();
    caps.setAlphaBits(8);
    GLJPanel canvas = new GLJPanel(caps);
    canvas.setOpaque(false);
*/
        listener = new CubeGLListener(this, fps);
        canvas.addGLEventListener(listener);

        animator = new FPSAnimator(canvas, fps, true);
        /* true means fixed-rate scheduling;
     false means fixed-period */

        renderPane.add(canvas, BorderLayout.CENTER);
        return renderPane;
    }  // end of makeRenderPanel()


    private JPanel createBackPanel()
        /* A panel acting as a background for the rotating cube.
           It contains a gradient and some text.
           This only works when the cube is rendered in the
           lightweight GLJPanel, which can be made opaque.
        */ {
        font = new Font("SansSerif", Font.BOLD, 48);

        JPanel p = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                g2d.setPaint(new GradientPaint(0, 0, Color.YELLOW,
                                               width, height, Color.BLUE));
                g2d.fillRect(0, 0, width, height);

                g2d.setPaint(Color.BLACK);
                g2d.setFont(font);
                g2d.drawString("Hello World", width / 4, height / 4);
            } // end of paintComponent()
        };
        return p;
    } // end of createBackPanel()


    public void setRots(float rotX, float rotY, float rotZ)
    // called from CubeGLListener to show cube rotations
    {
        rotsTF.setText("Rotations: (" + df.format(rotX) + ", " +
                       df.format(rotY) + ", " +
                       df.format(rotZ) + ")");
    }

// -----------------------------------------

    public static void main(String[] args) {
        int fps = DEFAULT_FPS;
        if (args.length != 0)
            fps = Integer.parseInt(args[0]);
        System.out.println("fps: " + fps);

        new CubeGL(fps);
    }  // end of main()

} // end of CubeGL class
