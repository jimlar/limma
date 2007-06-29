package examples.cubecb;
// CubeGLListener.java
// Andrew Davison, November 2006, ad@fivedots.coe.psu.ac.th

/* A rotating coloured cube.
   Illustrates the use of the GLEventListener callback methods:
       init(), reshape(), and display()

   Also includes statistics gathering code to report the 
   actual FPS. See chapter 2 of "Killer Game Programming
   in Java" for details (p.40-45). There's a version online
   at http://fivedots.coe.psu.ac.th/~ad/jg/ch1/.
*/

import java.text.DecimalFormat;
import java.util.Random;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;


public class CubeGLListener implements GLEventListener {
    private static final float INCR_MAX = 10.0f;   // for rotation increments
    private static final double Z_DIST = 7.0;      // for the camera position

    // statistics constants
    private static long MAX_STATS_INTERVAL = 1000L;
    // record stats every 1 second (roughly)

    private static int NUM_FPS = 10;
    // number of FPS values stored to get an average

    // used for gathering statistics
    private long statsInterval = 0L;    // in ms
    private long prevStatsTime;
    private long totalElapsedTime = 0L;

    private long frameCount = 0;
    private double fpsStore[];
    private long statsCount = 0;
    private double averageFPS = 0.0;


    private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp
    private DecimalFormat timedf = new DecimalFormat("0.####");  // 4 dp

    private int period;       // period between drawing in _ms_
    // i.e. the time requested for one frame iteration

    private GLU glu;


    // vertices for a cube of sides 2 units, centered on (0,0,0)
    private float[][] verts = {
            {-1.0f, -1.0f, 1.0f},  // vertex 0
            {-1.0f, 1.0f, 1.0f},  // 1
            {1.0f, 1.0f, 1.0f},  // 2
            {1.0f, -1.0f, 1.0f},  // 3
            {-1.0f, -1.0f, -1.0f},  // 4
            {-1.0f, 1.0f, -1.0f},  // 5
            {1.0f, 1.0f, -1.0f},  // 6
            {1.0f, -1.0f, -1.0f},  // 7
    };

    int cubeDList;   // display list for displaying the cube

    private CubeGL top;   // reference back to top-level JFrame

    // rotation variables
    private float rotX, rotY, rotZ;     // total rotations in x,y,z axes
    private float incrX, incrY, incrZ;  // increments for x,y,z rotations


    public CubeGLListener(CubeGL cg, int fps) {
        top = cg;

        period = (int) 1000.0 / fps;     // in ms
        System.out.println("period: " + period + " ms");

        // statistics initialization
        fpsStore = new double[NUM_FPS];
        for (int i = 0; i < NUM_FPS; i++)
            fpsStore[i] = 0.0;

        prevStatsTime = System.nanoTime();
    } // end of CubeGLListener

    // ----------------- listener callbacks -----------------------------

    public void init(GLAutoDrawable drawable)
        /* Called by the drawable immediately after the OpenGL context is
initialized. Can be used to perform start-up tasks. */ {
        // System.out.println("init called");
        GL gl = drawable.getGL();    /* don't make this gl a global! */
        glu = new GLU();    // this is okay as a global, but only use in callbacks

        // gl.setSwapInterval(0);
        /* switches off vertical synchronization, for extra speed (maybe) */

        // initialize the rotation variables
        rotX = 0;
        rotY = 0;
        rotZ = 0;
        Random random = new Random();
        incrX = random.nextFloat() * INCR_MAX;   // 0 - INCR_MAX degrees
        incrY = random.nextFloat() * INCR_MAX;
        incrZ = random.nextFloat() * INCR_MAX;

        // gl.glClearColor(0.17f, 0.65f, 0.92f, 0.3f);  // translucent sky for GLJPanel
        // gl.glClearColor(0.0f, 0.0f, 0.0f,  0.0f);
        // transparent background for GLJPanel
        gl.glClearColor(0.17f, 0.65f, 0.92f, 1.0f);
        // sky colour background for GLCanvas

        // z- (depth) buffer initialization for hidden surface removal
        gl.glEnable(GL.GL_DEPTH_TEST);
        // gl.glClearDepth(1.0f);
        // gl.glDepthFunc(GL.GL_LEQUAL);   // type of depth testing
        // gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

        // create a display list for drawing the cube
        cubeDList = gl.glGenLists(1);
        gl.glNewList(cubeDList, GL.GL_COMPILE);
        drawColourCube(gl);
        gl.glEndList();
    } // end of init()


    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
    // called when the drawable component is moved or resized
    {
        // System.out.println("reshape called");

        GL gl = drawable.getGL();
        // GLU glu = new GLU();

        if (height == 0)
            height = 1;    // to avoid division by 0 in aspect ratio below

        gl.glViewport(x, y, width, height);  // size of drawing area

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, (float) width / (float) height, 1, 100); // 5, 100);
        // fov, aspect ratio, near & far clipping planes

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    } // end of reshape()


    public void display(GLAutoDrawable drawable)
        /* Called when the drawable component needs to be rendered. It is
   called when FPSAnimator calls display(). */ {
        // System.out.println("display called");

        // update the rotations
        rotX = (rotX + incrX) % 360.0f;
        rotY = (rotY + incrY) % 360.0f;
        rotZ = (rotZ + incrZ) % 360.0f;
        top.setRots(rotX, rotY, rotZ);

        GL gl = drawable.getGL();
        // GLU glu = new GLU();

        // clear colour and depth buffers
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        glu.gluLookAt(0, 0, Z_DIST, 0, 0, 0, 0, 1, 0);   // position camera

        // apply rotations to the x,y,z axes
        gl.glRotatef(rotX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotY, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(rotZ, 0.0f, 0.0f, 1.0f);
        gl.glCallList(cubeDList);   // execute display list for drawing cube
        // drawColourCube(gl);

        reportStats();
    } // end of display


    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
                               boolean deviceChanged)
        /* Called when the display mode or device has changed.
    Currently unimplemented in JOGL */ {
    }

    // ------------------------- rendering methods ---------------------

    private void drawColourCube(GL gl)
    // six-sided cube, with a different colour on each face
    {
        gl.glColor3f(1.0f, 0.0f, 0.0f);   // red
        drawPolygon(gl, 0, 3, 2, 1);      // front face

        gl.glColor3f(0.0f, 1.0f, 0.0f);   // green
        drawPolygon(gl, 2, 3, 7, 6);      // right

        gl.glColor3f(0.0f, 0.0f, 1.0f);   // blue
        drawPolygon(gl, 3, 0, 4, 7);      // bottom

        gl.glColor3f(1.0f, 1.0f, 0.0f);   // yellow
        drawPolygon(gl, 1, 2, 6, 5);      // top

        gl.glColor3f(0.0f, 1.0f, 1.0f);   // light blue
        drawPolygon(gl, 4, 5, 6, 7);      // back

        gl.glColor3f(1.0f, 0.0f, 1.0f);   // purple
        drawPolygon(gl, 5, 4, 0, 1);      // left
    } // end of drawColourCube()


    private void drawPolygon(GL gl, int vIdx0, int vIdx1, int vIdx2, int vIdx3)
    // the polygon verticies come from the verts[] array
    {
        gl.glBegin(GL.GL_POLYGON);
        gl.glVertex3f(verts[vIdx0][0], verts[vIdx0][1], verts[vIdx0][2]);
        gl.glVertex3f(verts[vIdx1][0], verts[vIdx1][1], verts[vIdx1][2]);
        gl.glVertex3f(verts[vIdx2][0], verts[vIdx2][1], verts[vIdx2][2]);
        gl.glVertex3f(verts[vIdx3][0], verts[vIdx3][1], verts[vIdx3][2]);
        gl.glEnd();
    }  // end of drawPolygon()

    // -------------------- statistics --------------------------

    private void reportStats()
        /* The statistics:
             - the summed periods for all the iterations in this interval
               (period is the amount of time a single frame iteration should take),
               the actual elapsed time in this interval,
               the error between these two numbers;

             - the total number of calls to paintComponent();

             - the FPS (frames/sec) for this interval, and the average
               FPS (AFPS) over the last NUM_FPSs intervals.

           The data is collected every MAX_STATS_INTERVAL  (1 sec).
           ----- Changes ---
           * Changed J3DTimer.getValue() to System.nanoTime()
           * Only report the AFPS value.
        */ {
        frameCount++;
        statsInterval += period;

        if (statsInterval >= MAX_STATS_INTERVAL) {     // record stats every MAX_STATS_INTERVAL
            long timeNow = System.nanoTime();

            long realElapsedTime = timeNow - prevStatsTime;   // time since last stats collection
            totalElapsedTime += realElapsedTime;

            long sInterval = (long) statsInterval * 1000000L;  // ms --> ns
            double timingError =
                    ((double) (realElapsedTime - sInterval)) / sInterval * 100.0;

            double actualFPS = 0;     // calculate the latest FPS
            if (totalElapsedTime > 0)
                actualFPS = (((double) frameCount / totalElapsedTime) * 1000000000L);

            // store the latest FPS
            fpsStore[(int) statsCount % NUM_FPS] = actualFPS;
            statsCount = statsCount + 1;

            double totalFPS = 0.0;     // total the stored FPSs
            for (int i = 0; i < NUM_FPS; i++)
                totalFPS += fpsStore[i];

            if (statsCount < NUM_FPS)  // obtain the average FPS
                averageFPS = totalFPS / statsCount;
            else
                averageFPS = totalFPS / NUM_FPS;
/*
      System.out.println(timedf.format( (double) statsInterval/1000) + " " + 
                        timedf.format((double) realElapsedTime/1000000000L) + "s " + 
						df.format(timingError) + "% " + 
                        frameCount + "c " +
                        df.format(actualFPS) + " " +
                        df.format(averageFPS) + " afps" );
*/
            System.out.println(df.format(averageFPS));

            prevStatsTime = timeNow;
            statsInterval = 0L;   // reset
        }
    }  // end of reportStats()

} // end of CubeGLListener class

