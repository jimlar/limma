package limma;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.Animator;
import limma.jogl.Cube;
import limma.jogl.utils.FPSCounter;
import limma.jogl.utils.SystemTime;
import limma.jogl.utils.Time;


public class JOGLMain implements GLEventListener {
    private GLU glu = new GLU();
    private Time time;
    private FPSCounter fps;

    private Cube cube;

    public static void main(String[] args) {
        Frame frame = new Frame("Text Cube");
        frame.setLayout(new BorderLayout());

        GLCanvas canvas = new GLCanvas();
        final JOGLMain demo = new JOGLMain();

        canvas.addGLEventListener(demo);
        frame.add(canvas, BorderLayout.CENTER);

        frame.setSize(512, 512);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glEnable(GL.GL_DEPTH_TEST);

        fps = new FPSCounter(drawable, 36);

        time = new SystemTime();
        ((SystemTime) time).rebase();
        gl.setSwapInterval(0);

        cube = new Cube();
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(0, 0, 10,
                      0, 0, 0,
                      0, 1, 0);

        cube.draw(drawable, time);

        fps.draw();
        time.update();
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(15, (float) width / (float) height, 5, 15);
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}