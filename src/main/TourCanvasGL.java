// TourCanvasGL.java
// Andrew Davison, February 2007, ad@fivedots.coe.psu.ac.th

/* A single thread is spawned which initialises the rendering
   and then loops, carrying out update, render, sleep with
   a fixed period.

   The active rendering framework comes from chapter 2
   of "Killing Game Programming in Java" (KGPJ). There's a 
   version online at http://fivedots.coe.psu.ac.th/~ad/jg/ch1/.

   The statistics code is lifted from chapter 3 of KGPJ (p.54-56), which
   is online at http://fivedots.coe.psu.ac.th/~ad/jg/ch02/.
   The time calculations in this version use System.nanoTime() rather 
   than J3DTimer.getValue(), so require J2SE 5.0.

   The canvas displays a 3D world consisting of:

     * a green and blue checkerboard floor with a red square at its center
       and numbers along its z- and z- axes (as in the Java 3D
       Checkers3D example in chapter 15 of KGPJ; 
       online at http://fivedots.coe.psu.ac.th/~ad/jg/ch8/).

     * a skybox of stars

     * a billboard showing a tree, which rotates around the y-axis
       to always face the camera

     * user navigation using keys to move forward, backwards, left,
       right, up, down, and turn left and right. The user cannot move
       off the checkboard or beyond the skybox

     * the user can quit the game by pressing 'q', ctrl-c, the 'esc' key,
       or by clicking the close box

     * several images are placed at random on the ground. The 'game'
       (such as it is) is to navigate over these shapes to make
       them disappear. Then the game ends. The shapes are managed
       by a GroundShapes object

     * a game-over image and message is placed as a 2D overlay in front
       of the game at the end

     * the application uses OpenGL bitmap fonts and Java fonts

   I borrowed the 2D overlay technique from ozak in his message at
   http://www.javagaming.org/forums/index.php?topic=8110.0

   A "Loading. Please wait..." message is displayed at start-up time.

   ------
   Changes (Feb 2007)
     - drawScreen() uses Texture.getImageTexCoords() values

     - drawSphere() rotation change to orientate sphere

     - drawStars() uses Texture.getImageTexCoords() values

     - recoding of drawAxisText() to use TextRenderer, and
       changes to labelAxes()

     - separated ground shapes code into a GroundShapes class
*/

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;
import com.sun.opengl.util.texture.TextureIO;


public class TourCanvasGL extends Canvas implements Runnable {
    // for the floor
    private final static int FLOOR_LEN = 20;  // should be even
    private final static int BLUE_TILE = 0;   // floor tile colour types
    private final static int GREEN_TILE = 1;
    private final static float SCALE_FACTOR = 0.01f;  // for the axis labels

    // camera related
    private final static double SPEED = 0.4;   // for camera movement
    private final static double LOOK_AT_DIST = 100.0;
    private final static double Z_POS = 9.0;
    private final static double ANGLE_INCR = 5.0;   // degrees
    private final static double HEIGHT_STEP = 1.0;

    // statistics constants
    private static long MAX_STATS_INTERVAL = 1000000000L;
    // private static long MAX_STATS_INTERVAL = 1000L;
    // record stats every 1 second (roughly)

    private static final int NO_DELAYS_PER_YIELD = 16;
    /* Number of renders with a sleep delay of 0 ms before the
animation thread yields to other running threads. */

    private static int MAX_RENDER_SKIPS = 5;   // was 2;
    // no. of renders that can be skipped in any one animation loop
    // i.e the games state is updated but not rendered

    private static int NUM_FPS = 10;
    // number of FPS values stored to get an average

    // used for gathering statistics
    private long statsInterval = 0L;    // in ns
    private long prevStatsTime;
    private long totalElapsedTime = 0L;
    private long gameStartTime;
    private int timeSpentInGame = 0;    // in seconds

    private long frameCount = 0;
    private double fpsStore[];
    private long statsCount = 0;
    private double averageFPS = 0.0;

    private long rendersSkipped = 0L;
    private long totalRendersSkipped = 0L;
    private double upsStore[];
    private double averageUPS = 0.0;

    private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp
    private DecimalFormat timedf = new DecimalFormat("0.####");  // 4 dp

    // used at game termination
    private volatile boolean gameOver = false;
    private int score = 0;

    // used by the loading message (and axis labels and game-over msg)
    private Font font;
    private FontMetrics metrics;
    private BufferedImage waitIm;

    private TourGL tourTop;     // reference back to top-level JFrame
    private long period;        // period between drawing in _nanosecs_

    private Thread animator;              // the thread that performs the animation
    private volatile boolean isRunning = false;   // used to stop the animation thread
    private volatile boolean isPaused = false;

    // OpenGL
    private GLDrawable drawable;  // the rendering 'surface'
    private GLContext context;    // the rendering context (holds rendering state info)
    private GL gl;
    private GLU glu;
    private GLUT glut;

    private int starsDList;        // display lists for stars
    private GLUquadric quadric;            // for the sphere
    private Texture earthTex, starsTex, treeTex, rTex, robotTex;

    private TextRenderer axisLabelRenderer;  // for floor axis labels
    private GroundShapes groundShapes;

    // camera movement
    private double xCamPos, yCamPos, zCamPos;
    private double xLookAt, yLookAt, zLookAt;
    private double xStep, zStep;
    private double viewAngle;

    // sphere movement
    private float orbitAngle = 0.0f;
    private float spinAngle = 0.0f;

    // window sizing
    private boolean isResized = false;
    private int panelWidth, panelHeight;


    public TourCanvasGL(TourGL top, long period, int width, int height,
                        GraphicsConfiguration config, GLCapabilities caps) {
        super(config);

        tourTop = top;
        this.period = period;
        panelWidth = width;
        panelHeight = height;

        setBackground(Color.white);

        // get a rendering surface and a context for this canvas
        drawable = GLDrawableFactory.getFactory().getGLDrawable(this, caps, null);
        context = drawable.createContext(null);

        initViewerPosn();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                processKey(e);
            }
        });

        // create 'loading' message font (and axis labels and game-over msg)
        font = new Font("SansSerif", Font.BOLD, 24);
        metrics = this.getFontMetrics(font);
        waitIm = loadImage("hourglass.jpg");

        // axes labels renderer
        axisLabelRenderer = new TextRenderer(font);

        // statistics initialization
        fpsStore = new double[NUM_FPS];
        upsStore = new double[NUM_FPS];
        for (int i = 0; i < NUM_FPS; i++) {
            fpsStore[i] = 0.0;
            upsStore[i] = 0.0;
        }
    } // end of TourCanvasGL()


    private void initViewerPosn()
        /* Specify the camera (player) position, the x- and z- step
 distance, and the position being looked at. */ {
        xCamPos = 0;
        yCamPos = 1;
        zCamPos = Z_POS;    // camera posn

        viewAngle = -90.0;   // along -z axis
        xStep = Math.cos(Math.toRadians(viewAngle));  // step distances
        zStep = Math.sin(Math.toRadians(viewAngle));

        xLookAt = xCamPos + (LOOK_AT_DIST * xStep);   // look-at posn
        yLookAt = 0;
        zLookAt = zCamPos + (LOOK_AT_DIST * zStep);
    }  // end of initViewerPosn()


    private BufferedImage loadImage(String fnm)
    // load the image from images/fnm
    {
        BufferedImage im = null;
        try {
            im = ImageIO.read(getClass().getResource("images/" + fnm));
        }
        catch (IOException e) {
            System.out.println("Could not load image from images/" + fnm);
        }
        return im;
    }  // end of loadImage()


    public void addNotify()
    // wait for the canvas to be added to the JPanel before starting
    {
        super.addNotify();      // creates the peer
        drawable.setRealized(true);  // the canvas can now be rendering into

        // initialise and start the animation thread
        if (animator == null || !isRunning) {
            animator = new Thread(this);
            animator.start();
        }
    } // end of addNotify()

    // ------------- game life cycle methods ------------
    // called by the JFrame's window listener methods

    public void resumeGame()
    // called when the JFrame is activated / deiconified
    {
        isPaused = false;
    }

    public void pauseGame()
    // called when the JFrame is deactivated / iconified
    {
        isPaused = true;
    }

    public void stopGame()
    // called when the JFrame is closing
    {
        isRunning = false;
    }

    // ----------------------------------------------

    public void reshape(int w, int h)
        /* called by the JFrame's ComponentListener when the window is resized
 (similar to the reshape() callback in GLEventListener) */ {
        isResized = true;
        if (h == 0)
            h = 1;  // to avoid division by 0 in aspect ratio in resizeView()
        panelWidth = w;
        panelHeight = h;
    }  // end of reshape()


    public void update(Graphics g) {
    }

    public void paint(Graphics g)
    // display a loading message while the canvas is being initialized
    {
        if (!isRunning && !gameOver) {
            String msg = "Loading. Please wait...";
            int x = (panelWidth - metrics.stringWidth(msg)) / 2;
            int y = (panelHeight - metrics.getHeight()) / 3;
            g.setColor(Color.blue);
            g.setFont(font);
            g.drawString(msg, x, y);

            // draw image under text
            int xIm = (panelWidth - waitIm.getWidth()) / 2;
            int yIm = y + 20;
            g.drawImage(waitIm, xIm, yIm, this);
        }
    } // end of paint()


    private void processKey(KeyEvent e)
    // handles termination, and the game-play keys
    {
        int keyCode = e.getKeyCode();

        // termination keys
        // listen for esc, q, end, ctrl-c on the canvas
        if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) ||
            (keyCode == KeyEvent.VK_END) ||
            ((keyCode == KeyEvent.VK_C) && e.isControlDown()))
            isRunning = false;

        // game-play keys
        if (isRunning) {
            // move based on the arrow key pressed
            if (keyCode == KeyEvent.VK_LEFT) {    // left
                if (e.isControlDown()) {   // translate left
                    xCamPos += zStep * SPEED;
                    zCamPos -= xStep * SPEED;
                } else {  // turn left
                    viewAngle -= ANGLE_INCR;
                    xStep = Math.cos(Math.toRadians(viewAngle));
                    zStep = Math.sin(Math.toRadians(viewAngle));
                    // System.out.println("left (xStep,zStep): (" + df.format(xStep) +
                    //                                      ", " + df.format(zStep) + ")");
                }
            } else if (keyCode == KeyEvent.VK_RIGHT) {  // right
                if (e.isControlDown()) {   // translate right
                    xCamPos -= zStep * SPEED;
                    zCamPos += xStep * SPEED;
                } else {  // turn right
                    viewAngle += ANGLE_INCR;
                    xStep = Math.cos(Math.toRadians(viewAngle));
                    zStep = Math.sin(Math.toRadians(viewAngle));
                    // System.out.println("right (xStep,zStep): (" + df.format(xStep) +
                    //                                      ", " + df.format(zStep) + ")");
                }
            } else if (keyCode == KeyEvent.VK_UP) {   // move forward
                xCamPos += xStep * SPEED;
                zCamPos += zStep * SPEED;
            } else if (keyCode == KeyEvent.VK_DOWN) {  // move backwards
                xCamPos -= xStep * SPEED;
                zCamPos -= zStep * SPEED;
            } else if (keyCode == KeyEvent.VK_PAGE_UP) {  // move up
                if ((yCamPos + HEIGHT_STEP) < FLOOR_LEN / 2) {   // stay below stars ceiling
                    yCamPos += HEIGHT_STEP;
                    yLookAt += HEIGHT_STEP;
                }
            } else if (keyCode == KeyEvent.VK_PAGE_DOWN) {  // move down
                if ((yCamPos - HEIGHT_STEP) > 0) {   // stay above floor
                    yCamPos -= HEIGHT_STEP;
                    yLookAt -= HEIGHT_STEP;
                }
            }

            // don't allow player to walk off the edge of the world
            if (xCamPos < -FLOOR_LEN / 2)
                xCamPos = -FLOOR_LEN / 2;
            else if (xCamPos > FLOOR_LEN / 2)
                xCamPos = FLOOR_LEN / 2;

            if (zCamPos < -FLOOR_LEN / 2)
                zCamPos = -FLOOR_LEN / 2;
            else if (zCamPos > FLOOR_LEN / 2)
                zCamPos = FLOOR_LEN / 2;

            // System.out.println("Player (x,z): (" + df.format(xCamPos) +
            //                                ", " + df.format(zCamPos) + ")");

            // new look-at point
            xLookAt = xCamPos + (xStep * LOOK_AT_DIST);
            zLookAt = zCamPos + (zStep * LOOK_AT_DIST);
        }
    }  // end of processKey()


    public void run()
    // initialize rendering and start frame generation
    {
        // makeContentCurrent();

        initRender();
        renderLoop();

        // discard the rendering context and exit
        // context.release();
        context.destroy();
        System.exit(0);
    } // end of run()


    private void makeContentCurrent()
    // make the rendering context current for this thread
    {
        try {
            while (context.makeCurrent() == GLContext.CONTEXT_NOT_CURRENT) {
                System.out.println("Context not yet current...");
                Thread.sleep(100);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }  // end of makeContentCurrent()


    private void initRender()
        /* rendering initialization (similar to the init() callback
      in GLEventListener) */ {
        makeContentCurrent();

        gl = context.getGL();
        glu = new GLU();
        glut = new GLUT();

        resizeView();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);      // black background
        // gl.glClearColor(0.17f, 0.65f, 0.92f, 0.0f);  // sky blue colour background

        // z- (depth) buffer initialization for hidden surface removal
        gl.glEnable(GL.GL_DEPTH_TEST);

        gl.glShadeModel(GL.GL_SMOOTH);    // use smooth shading

        // create a textured quadric
        quadric = glu.gluNewQuadric();
        glu.gluQuadricTexture(quadric, true);    // creates texture coords

        loadTextures();
        addLight();
        groundShapes = new GroundShapes(FLOOR_LEN);

        // create a display list for drawing the stars
        starsDList = gl.glGenLists(1);
        gl.glNewList(starsDList, GL.GL_COMPILE);
        drawStars();
        gl.glEndList();

        /* release the context, otherwise the AWT lock on X11
    will not be released */
        context.release();
    }  // end of initRender()


    private void resizeView() {
        gl.glViewport(0, 0, panelWidth, panelHeight);    // size of drawing area

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, (float) panelWidth / (float) panelHeight, 1, 100); // 5, 100);
        // fov, aspect ratio, near & far clipping planes
    }  // end of resizeView()


    private void loadTextures() {
        earthTex = loadTexture("earth.jpg");
        starsTex = loadTexture("stars.jpg");   // for the sky box
        treeTex = loadTexture("tree.gif");
        // rTex = loadTexture("r.gif");

        robotTex = loadTexture("robot.gif");  // the game-over image

        // repeat the sky box texture in every direction
        starsTex.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        starsTex.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);

        // rTex.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        // rTex.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
    }  // end of loadTextures()


    private Texture loadTexture(String fnm) {
        String fileName = "src/main/images/" + fnm;
        Texture tex = null;
        try {
            tex = TextureIO.newTexture(new File(fileName), false);
            tex.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
            tex.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        }
        catch (Exception e) {
            System.out.println("Error loading texture " + fileName);
        }

        return tex;
    }  // end of loadTexture()


    private void addLight()
        /* set up a point source with ambient, diffuse, and specular
      colour components */ {
        // enable a single light source
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);

        float[] grayLight = {0.1f, 0.1f, 0.1f, 1.0f};  // weak gray ambient
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, grayLight, 0);

        float[] whiteLight = {1.0f, 1.0f, 1.0f, 1.0f};  // bright white diffuse & specular
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, whiteLight, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, whiteLight, 0);

        float lightPos[] = {1.0f, 1.0f, 1.0f, 0.0f};  // top right front _direction_
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos, 0);
    }  // end of addLight()

    // ---------------- frame-based rendering -----------------------


    private void renderLoop()
        /* Repeatedly update, render, and sleep, keeping to a fixed
           period as closely as possible. gather and report statistics.
        */ {
        // timing-related variables
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        long excess = 0L;

        gameStartTime = System.nanoTime();
        prevStatsTime = gameStartTime;
        beforeTime = gameStartTime;

        isRunning = true;

        while (isRunning) {
            makeContentCurrent();
            gameUpdate();

            renderScene();          // rendering
            drawable.swapBuffers(); // put the scene onto the canvas
            // swap front and back buffers, making the new rendering visible

            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            sleepTime = (period - timeDiff) - overSleepTime;

            if (sleepTime > 0) {   // some time left in this cycle
                try {
                    Thread.sleep(sleepTime / 1000000L);  // nano -> ms
                }
                catch (InterruptedException ex) {
                }
                overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
            } else {    // sleepTime <= 0; this cycle took longer than the period
                excess -= sleepTime;  // store excess time value
                overSleepTime = 0L;

                if (++noDelays >= NO_DELAYS_PER_YIELD) {
                    Thread.yield();   // give another thread a chance to run
                    noDelays = 0;
                }
            }

            beforeTime = System.nanoTime();    // J3DTimer.getValue();

            /* If the rendering is taking too long, update the game state
         without rendering it, to get the updates/sec nearer to
         the required FPS. */
            int skips = 0;
            while ((excess > period) && (skips < MAX_RENDER_SKIPS)) {
                excess -= period;
                gameUpdate();    // update state but don't render
                skips++;
            }
            rendersSkipped += skips;

            /* release the context, otherwise the AWT lock on X11
        will not be released */
            context.release();

            storeStats();
        }

        printStats();

        glu.gluDeleteQuadric(quadric);
    } // end of renderLoop()


    private void gameUpdate() {
        if (!isPaused && !gameOver) {
            // update the earth's orbit and the R's
            orbitAngle = (orbitAngle + 2.0f) % 360.0f;
            spinAngle = (spinAngle + 1.0f) % 360.0f;
            checkShapes();
        }
    }  // end of gameUpdate()


    private void checkShapes()
        /* Count the number of visible shapes at the current
camera position (xCamPos, zCamPos).
If there are no shapes left, then the game is over. */ {
        int numVis = groundShapes.countVisibles(xCamPos, zCamPos);
        tourTop.setShapesLeft(numVis);

        if (numVis == 0) {   // when all the shapes are gone, the game is over
            gameOver = true;
            score = 25 - timeSpentInGame;   // hack together a score
        }
    }  // end of checkShapes()

    // ------------------ rendering methods -----------------------------


    private void renderScene() {
        if (context.getCurrent() == null) {
            System.out.println("Current context is null");
            System.exit(0);
        }

        if (isResized) {
            resizeView();
            isResized = false;
        }

        // clear colour and depth buffers
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

        glu.gluLookAt(xCamPos, yCamPos, zCamPos,
                      xLookAt, yLookAt, zLookAt, 0, 1, 0);    // position camera

/*  System.out.println("Posn: (" + df.format(xCamPos) + ", " + 
                                   df.format(yCamPos) + ", " + 
                                   df.format(zCamPos) + ")");
*/
        drawTree();
        groundShapes.draw(gl);
        drawSphere();
        drawFloor();

        // execute display lists for drawing the stars
        gl.glCallList(starsDList);
        // drawStars();

        if (gameOver)
            gameOverMessage();
    } // end of renderScene()


    private void drawTree()
        /* the tree is a 'billboard': a screen that is always rotated around
           the y-axis to be facing the camera.
        */ {
        float[] verts = {0, 0, 0, 2, 0, 0, 2, 2, 0, 0, 2, 0};  // posn of tree
        gl.glPushMatrix();
        gl.glRotatef(-1 * ((float) viewAngle + 90.0f), 0, 1, 0);
        // rotate in the opposite direction to the camera
        drawScreen(verts, treeTex);
        gl.glPopMatrix();
    }  // end of drawTree()


    private void drawScreen(float[] verts, Texture tex)
        /* A screen is a transparent quadrilateral which only shows
           the non-transparent parts of the texture. Lighting is disabled.
           The screen is positioned according to the vertices in verts[].
        */ {
        boolean enableLightsAtEnd = false;
        if (gl.glIsEnabled(GL.GL_LIGHTING)) {   // switch lights off if currently on
            gl.glDisable(GL.GL_LIGHTING);
            enableLightsAtEnd = true;
        }

        // do not draw the transparent parts of the texture
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        // don't show source alpha parts in the destination

        // determine which areas of the polygon are to be rendered
        gl.glEnable(GL.GL_ALPHA_TEST);
        gl.glAlphaFunc(GL.GL_GREATER, 0);  // only render if alpha > 0

        // enable texturing
        gl.glEnable(GL.GL_TEXTURE_2D);
        tex.bind();

        // replace the quad colours with the texture
        gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);

        TextureCoords tc = tex.getImageTexCoords();

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(tc.left(), tc.bottom());
        gl.glVertex3f(verts[0], verts[1], verts[2]);

        gl.glTexCoord2f(tc.right(), tc.bottom());
        gl.glVertex3f(verts[3], verts[4], verts[5]);

        gl.glTexCoord2f(tc.right(), tc.top());
        gl.glVertex3f(verts[6], verts[7], verts[8]);

        gl.glTexCoord2f(tc.left(), tc.top());
        gl.glVertex3f(verts[9], verts[10], verts[11]);
        gl.glEnd();

        gl.glDisable(GL.GL_TEXTURE_2D);

        // switch back to modulation of quad colours and texture
        gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
        gl.glDisable(GL.GL_ALPHA);  // switch off transparency
        gl.glDisable(GL.GL_BLEND);

        if (enableLightsAtEnd)
            gl.glEnable(GL.GL_LIGHTING);
    }  // end of drawScreen()


    private void drawSphere()
    // draw the earth orbiting a point, and rotating around its y-axis
    {
        // enable texturing and choose the 'earth' texture
        gl.glEnable(GL.GL_TEXTURE_2D);
        earthTex.bind();

        // set how the sphere's surface responds to the light
        gl.glPushMatrix();
        float[] grayCol = {0.8f, 0.8f, 0.8f, 1.0f};
        // float[] blueCol = {0.0f, 0.0f, 0.8f, 1.0f};
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, grayCol, 0);

        float[] whiteCol = {1.0f, 1.0f, 1.0f, 1.0f};
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, whiteCol, 0);
        gl.glMateriali(GL.GL_FRONT, GL.GL_SHININESS, 100);

        gl.glTranslatef(0.0f, 2.0f, -5.0f);          // position the sphere
        gl.glRotatef(orbitAngle, 0.0f, 1.0f, 0.0f);  // make it orbit around the y-axis
        gl.glTranslatef(2.0f, 0.0f, 0.0f);      // make the orbit x-axis radius 2 units

        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        // rotate sphere upwards around x-axis so texture is correctly orientated
        gl.glRotatef(spinAngle, 0.0f, 0.0f, 1.0f);  // spin around z-axis (which looks like y-axis)

        glu.gluSphere(quadric, 1.0f, 32, 32);  // generate the textured sphere
        // radius, slices, stacks
        gl.glPopMatrix();

        gl.glDisable(GL.GL_TEXTURE_2D);
    } // end of drawSphere()


    private void drawStars()
        /* Draws a sky box using a stars image.

           Each 'wall' of the box extends the width of the floor (FLOOR_LEN)
           and is FLOOR_LEN/2 high. The stars image is textured over the each
           wall twice, each texture occupying FLOOR_LEN/2*FLOOR_LEN/2 area.

           The ceiling is the same size as the floor, FLOOR_LEN*FLOOR_LEN, and is
           covered by four copies of the stars image. Each texture
           occupies a FLOOR_LEN/2*FLOOR_LEN/2 area.
        */ {
        gl.glDisable(GL.GL_LIGHTING);

        // enable texturing and choose the 'stars' texture
        gl.glEnable(GL.GL_TEXTURE_2D);
        starsTex.bind();
        // rTex.bind();

        TextureCoords tc = starsTex.getImageTexCoords();
        float left = tc.left();
        float right = tc.right();
        float bottom = tc.bottom();
        float top = tc.top();

        // replace the quad colours with the texture
        gl.glTexEnvf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);

        gl.glBegin(GL.GL_QUADS);
        // back wall
        int edge = FLOOR_LEN / 2;
        gl.glTexCoord2f(left, bottom);
        gl.glVertex3i(-edge, 0, -edge);
        gl.glTexCoord2f(2 * right, bottom);
        gl.glVertex3i(edge, 0, -edge);
        gl.glTexCoord2f(2 * right, top);
        gl.glVertex3i(edge, edge, -edge);
        gl.glTexCoord2f(left, top);
        gl.glVertex3i(-edge, edge, -edge);

        // right wall
        gl.glTexCoord2f(left, bottom);
        gl.glVertex3i(edge, 0, -edge);
        gl.glTexCoord2f(2 * right, bottom);
        gl.glVertex3i(edge, 0, edge);
        gl.glTexCoord2f(2 * right, top);
        gl.glVertex3i(edge, edge, edge);
        gl.glTexCoord2f(left, top);
        gl.glVertex3i(edge, edge, -edge);

        // front wall
        gl.glTexCoord2f(left, bottom);
        gl.glVertex3i(edge, 0, edge);
        gl.glTexCoord2f(2 * right, bottom);
        gl.glVertex3i(-edge, 0, edge);
        gl.glTexCoord2f(2 * right, top);
        gl.glVertex3i(-edge, edge, edge);
        gl.glTexCoord2f(left, top);
        gl.glVertex3i(edge, edge, edge);

        // left wall
        gl.glTexCoord2f(left, bottom);
        gl.glVertex3i(-edge, 0, edge);
        gl.glTexCoord2f(2 * right, bottom);
        gl.glVertex3i(-edge, 0, -edge);
        gl.glTexCoord2f(2 * right, top);
        gl.glVertex3i(-edge, edge, -edge);
        gl.glTexCoord2f(left, top);
        gl.glVertex3i(-edge, edge, edge);

        // ceiling
        gl.glTexCoord2f(left, bottom);
        gl.glVertex3i(edge, edge, edge);
        gl.glTexCoord2f(2 * right, bottom);
        gl.glVertex3i(-edge, edge, edge);
        gl.glTexCoord2f(2 * right, 2 * top);
        gl.glVertex3i(-edge, edge, -edge);
        gl.glTexCoord2f(left, 2 * top);
        gl.glVertex3i(edge, edge, -edge);
        gl.glEnd();

        // switch back to modulation of quad colours and texture
        gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);

        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glEnable(GL.GL_LIGHTING);
    } // end drawStars()

    // ---------------- finishing the game ------------------------


    private void gameOverMessage()
        /* The game-over message is an overlay that always stays at the front,
          'stuck' to the screen. The message consists of three elements:
          a robot image with transparent elements, a red rectangle, and
          a text message in the rectangle which shows the player's final
          score.

          I borrowed this 2D overlay technique from ozak in his message at
          http://www.javagaming.org/forums/index.php?topic=8110.0
        */ {
        gl.glDisable(GL.GL_LIGHTING);

        String msg = "Game Over. Your Score: " + score;

        // System.out.println(msg);
        int msgWidth = glut.glutBitmapLength(GLUT.BITMAP_TIMES_ROMAN_24, msg);
        // use a bitmap font (since no scaling required)
        // get (x,y) for centering the text on screen
        int x = (panelWidth - msgWidth) / 2;
        int y = panelHeight / 2;

        begin2D();  // switch to 2D viewing

        drawRobotImage(x + msgWidth / 2, y - 12, msgWidth * 1.5f);

        // draw a medium red rectangle, centered on the screen
        gl.glColor3f(0.8f, 0.4f, 0.3f);   // medium red
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex3i(x - 10, y + 10, 0);
        gl.glVertex3i(x + msgWidth + 10, y + 10, 0);
        gl.glVertex3i(x + msgWidth + 10, y - 24, 0);
        gl.glVertex3i(x - 10, y - 24, 0);
        gl.glEnd();

        // write the message in the center of the screen
        gl.glColor3f(1.0f, 1.0f, 1.0f);   // white text
        gl.glRasterPos2i(x, y);
        glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, msg);

        end2D();  // switch back to 3D viewing

        gl.glEnable(GL.GL_LIGHTING);
    }  // end of gameOverMessage()


    private void begin2D()
    // switch to 2D viewing (an orthographic projection)
    {
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPushMatrix();   // save projection settings
        gl.glLoadIdentity();
        gl.glOrtho(0.0f, panelWidth, panelHeight, 0.0f, -1.0f, 1.0f);
        // left, right, bottom, top, near, far

        /* In an orthographic projection, the y-axis runs from
           the bottom-left, upwards. This is reversed back to the
           more familiar top-left, downwards, by switching the
           the top and bottom values in glOrtho().
        */
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();   // save model view settings
        gl.glLoadIdentity();
        gl.glDisable(GL.GL_DEPTH_TEST);
    } // end of begin2D()


    private void end2D()
    // switch back to 3D viewing
    {
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPopMatrix();   // restore previous projection settings
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPopMatrix();   // restore previous model view settings
    } // end of end2D()


    private void drawRobotImage(float xc, float yc, float size) {
        /* The screen is centered on (xc,yc), with sides of length size,
     and standing upright. */
        float[] verts = {xc - size / 2, yc + size / 2, 0,
                xc + size / 2, yc + size / 2, 0,
                xc + size / 2, yc - size / 2, 0,
                xc - size / 2, yc - size / 2, 0};
        drawScreen(verts, robotTex);
    }  // end of drawRobotImage()

    // ------------------ the checkboard floor --------------------
    /* This code is almost a direct translation of the CheckerFloor class
       in the Java 3D Checkers3D example in chapter 15 of KGPJ;
       online at http://fivedots.coe.psu.ac.th/~ad/jg/ch8/).
    */

    private void drawFloor()
        /* Create tiles, the origin marker, then the axes labels.
           The tiles are in a checkboard pattern, alternating between
           green and blue.
        */ {
        gl.glDisable(GL.GL_LIGHTING);

        drawTiles(BLUE_TILE);   // blue tiles
        drawTiles(GREEN_TILE);  // green
        addOriginMarker();
        labelAxes();

        gl.glEnable(GL.GL_LIGHTING);
    }  // end of CheckerFloor()


    private void drawTiles(int drawType)
        /* Create a series of quads, all with the same colour. They are
           spaced out over a FLOOR_LEN*FLOOR_LEN area, with the area centered
           at (0,0) on the XZ plane, and y==0.
        */ {
        if (drawType == BLUE_TILE)
            gl.glColor3f(0.0f, 0.1f, 0.4f);
        else  // green
            gl.glColor3f(0.0f, 0.5f, 0.1f);

        gl.glBegin(GL.GL_QUADS);
        boolean aBlueTile;
        for (int z = -FLOOR_LEN / 2; z <= (FLOOR_LEN / 2) - 1; z++) {
            aBlueTile = (z % 2 == 0) ? true : false;    // set colour type for new row
            for (int x = -FLOOR_LEN / 2; x <= (FLOOR_LEN / 2) - 1; x++) {
                if (aBlueTile && (drawType == BLUE_TILE))  // blue tile and drawing blue
                    drawTile(x, z);
                else if (!aBlueTile && (drawType == GREEN_TILE))   // green
                    drawTile(x, z);
                aBlueTile = !aBlueTile;
            }
        }
        gl.glEnd();
    }  // end of drawTiles()


    private void drawTile(int x, int z)
        /* Coords for a single blue or green square;
 its top left hand corner at (x,0,z). */ {
        // points created in counter-clockwise order
        gl.glVertex3f(x, 0.0f, z + 1.0f);   // bottom left point
        gl.glVertex3f(x + 1.0f, 0.0f, z + 1.0f);
        gl.glVertex3f(x + 1.0f, 0.0f, z);
        gl.glVertex3f(x, 0.0f, z);
    }  // end of drawTile()


    private void addOriginMarker()
        /* A red square centered at (0,0.01,0), of length 0.5, lieing
   flat on the XZ plane. */ {
        gl.glColor3f(0.8f, 0.4f, 0.3f);   // medium red
        gl.glBegin(GL.GL_QUADS);

        // points created counter-clockwise, a bit above the floor
        gl.glVertex3f(-0.25f, 0.01f, 0.25f);  // bottom left point
        gl.glVertex3f(0.25f, 0.01f, 0.25f);
        gl.glVertex3f(0.25f, 0.01f, -0.25f);
        gl.glVertex3f(-0.25f, 0.01f, -0.25f);

        gl.glEnd();
    } // end of addOriginMarker();


    private void labelAxes()
    // Place numbers along the x- and z-axes at the integer positions
    {
        for (int i = -FLOOR_LEN / 2; i <= FLOOR_LEN / 2; i++)
            drawAxisText("" + i, (float) i, 0.0f, 0.0f);  // along x-axis

        for (int i = -FLOOR_LEN / 2; i <= FLOOR_LEN / 2; i++)
            drawAxisText("" + i, 0.0f, 0.0f, (float) i);  // along z-axis
    }  // end of labelAxes()


    private void drawAxisText(String txt, float x, float y, float z)
        /* Draw txt at (x,y,z), with the text centered in the x-direction,
           facing along the +z axis.
        */ {
        Rectangle2D dim = axisLabelRenderer.getBounds(txt);
        float width = (float) dim.getWidth() * SCALE_FACTOR;

        axisLabelRenderer.begin3DRendering();
        axisLabelRenderer.draw3D(txt, x - width / 2, y, z, SCALE_FACTOR);
        axisLabelRenderer.end3DRendering();
    } // end of drawAxisText()

    // ----------------- statistics methods ------------------------

    private void storeStats()
        /* The statistics:
             - the summed periods for all the iterations in this interval
               (period is the amount of time a single frame iteration should take),
               the actual elapsed time in this interval,
               the error between these two numbers;

             - the total frame count, which is the total number of calls to run();

             - the frames skipped in this interval, the total number of frames
               skipped. A frame skip is a game update without a corresponding render;

             - the FPS (frames/sec) and UPS (updates/sec) for this interval,
               the average FPS & UPS over the last NUM_FPSs intervals.

           The data is collected every MAX_STATS_INTERVAL  (1 sec).
        */ {
        frameCount++;
        statsInterval += period;

        if (statsInterval >= MAX_STATS_INTERVAL) {     // record stats every MAX_STATS_INTERVAL
            long timeNow = System.nanoTime();    // J3DTimer.getValue();
            timeSpentInGame = (int) ((timeNow - gameStartTime) / 1000000000L);  // ns --> secs
            tourTop.setTimeSpent(timeSpentInGame);

            long realElapsedTime = timeNow - prevStatsTime;   // time since last stats collection
            totalElapsedTime += realElapsedTime;

            double timingError =
                    ((double) (realElapsedTime - statsInterval) / statsInterval) * 100.0;

            totalRendersSkipped += rendersSkipped;

            double actualFPS = 0;     // calculate the latest FPS and UPS
            double actualUPS = 0;
            if (totalElapsedTime > 0) {
                actualFPS = (((double) frameCount / totalElapsedTime) * 1000000000L);
                actualUPS = (((double) (frameCount + totalRendersSkipped) / totalElapsedTime)
                             * 1000000000L);
            }

            // store the latest FPS and UPS
            fpsStore[(int) statsCount % NUM_FPS] = actualFPS;
            upsStore[(int) statsCount % NUM_FPS] = actualUPS;
            statsCount = statsCount + 1;

            double totalFPS = 0.0;     // total the stored FPSs and UPSs
            double totalUPS = 0.0;
            for (int i = 0; i < NUM_FPS; i++) {
                totalFPS += fpsStore[i];
                totalUPS += upsStore[i];
            }

            if (statsCount < NUM_FPS) { // obtain the average FPS and UPS
                averageFPS = totalFPS / statsCount;
                averageUPS = totalUPS / statsCount;
            } else {
                averageFPS = totalFPS / NUM_FPS;
                averageUPS = totalUPS / NUM_FPS;
            }
/*
      System.out.println(timedf.format( (double) statsInterval/1000000000L) + " " + 
                    timedf.format((double) realElapsedTime/1000000000L) + "s " + 
			        df.format(timingError) + "% " + 
                    frameCount + "c " +
                    rendersSkipped + "/" + totalRendersSkipped + " skip; " +
                    df.format(actualFPS) + " " + df.format(averageFPS) + " afps; " + 
                    df.format(actualUPS) + " " + df.format(averageUPS) + " aups" );
*/
            rendersSkipped = 0;
            prevStatsTime = timeNow;
            statsInterval = 0L;   // reset
        }
    }  // end of storeStats()


    private void printStats() {
        // System.out.println("Frame Count/Loss: " + frameCount + " / " + totalRendersSkipped);
        System.out.println("Average FPS: " + df.format(averageFPS));
        System.out.println("Average UPS: " + df.format(averageUPS));
        System.out.println("Time Spent: " + timeSpentInGame + " secs");
    }  // end of printStats()

} // end of TourCanvasGL class

