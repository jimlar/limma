// GroundShapes.java
// Andrew Davison, February 2007, ad@fivedots.coe.psu.ac.th

/* NUM_SHAPES copies of an image are placed at random,
   flat on the ground. 
   The image is drawn as a texture onto a quad.

   The 'game' (such as it is) is to navigate over these shapes 
   to make them disappear. Then the game ends.
*/

import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;
import com.sun.opengl.util.texture.TextureIO;


public class GroundShapes {
    private final static String TEX_FNM = "r.gif";  // the shape image

    private final static int NUM_SHAPES = 5;
    private final static float SH_SIZE = 0.5f;   // size of shape quad
    private final static double CLOSE_DIST = 1.0;
    // how near the camera must get to 'touch' a shape

    private Texture shapeTex;
    private float[] xCoords, zCoords;    // (x,z) placement of shapes
    private boolean[] visibles;          // if the shapes are visible
    private int numVisShapes;

    private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp


    public GroundShapes(int floorLen) {
        generatePosns(floorLen);
        shapeTex = loadTexture(TEX_FNM);
    } // end of GroundShapes()


    private void generatePosns(int floorLen)
        /* Generate (x,z) positions for the shape images, and specify
   that they are all visible. Each (x,z) will be treated as a
   center point for its image. */ {
        Random rand = new Random();
        xCoords = new float[NUM_SHAPES];
        zCoords = new float[NUM_SHAPES];
        visibles = new boolean[NUM_SHAPES];
        numVisShapes = NUM_SHAPES;

        for (int i = 0; i < NUM_SHAPES; i++) {
            xCoords[i] = (rand.nextFloat() * (floorLen - 1)) - floorLen / 2;
            zCoords[i] = (rand.nextFloat() * (floorLen - 1)) - floorLen / 2;
            visibles[i] = true;
        }
    }  // end of generatePosns()


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


    public int countVisibles(double xCamPos, double zCamPos)
        /* Check whether the camera is near a shape, and make it invisible.
If there are no shapes left, then the game is over. */ {
        if (numVisShapes == 0)    // no shapes left
            return 0;

        int nearIdx = isOverShape(xCamPos, zCamPos);    // is the camera near a shape?
        if (nearIdx != -1) {
            visibles[nearIdx] = false;    // make that shape invisible
            numVisShapes--;
/*
      System.out.println("Hit shape at (" + 
                     df.format(xCoords[nearIdx]) + ", " +
                     df.format(zCoords[nearIdx]) + "); " +
                     numVisShapes + " left");
*/
        }
        return numVisShapes;
    }  // end of countVisibles()


    private int isOverShape(double xCamPos, double zCamPos)
    // returns index of shape that the camera is 'near', or -1
    {
        for (int i = 0; i < NUM_SHAPES; i++) {
            if (visibles[i]) {
                double xDiff = xCamPos - (double) xCoords[i];
                double zDiff = zCamPos - (double) zCoords[i];
                if (((xDiff * xDiff) + (zDiff * zDiff)) < CLOSE_DIST)
                    return i;
            }
        }
        return -1;
    }  // end of isOverShape()


    public void draw(GL gl)
        /* Draw the visible shapes lying flat, just above the floor.
Each image is layered over a quad as a texture. */ {
        if (numVisShapes == 0)    // nothing to draw
            return;

        float[] verts = new float[12];    // to hold 4 (x,y,z) vertices

        float xc, zc;
        for (int i = 0; i < NUM_SHAPES; i++) {
            if (visibles[i]) {
                xc = xCoords[i];
                zc = zCoords[i];
                /* make a quad with center (xc,zc) with sides SH_SIZE,
           lying just above the floor */
                verts[0] = xc - SH_SIZE / 2;
                verts[1] = 0.1f;
                verts[2] = zc + SH_SIZE / 2;
                verts[3] = xc + SH_SIZE / 2;
                verts[4] = 0.1f;
                verts[5] = zc + SH_SIZE / 2;
                verts[6] = xc + SH_SIZE / 2;
                verts[7] = 0.1f;
                verts[8] = zc - SH_SIZE / 2;
                verts[9] = xc - SH_SIZE / 2;
                verts[10] = 0.1f;
                verts[11] = zc - SH_SIZE / 2;
                drawScreen(gl, verts, shapeTex);
            }
        }
    }  // end of drawShapes()


    private void drawScreen(GL gl, float[] verts, Texture tex)
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


} // end of GroundShapes class

