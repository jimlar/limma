package limma.jogl;

import java.awt.Font;
import java.awt.geom.Rectangle2D;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import com.sun.opengl.util.j2d.TextRenderer;
import limma.jogl.utils.Time;

public class Cube implements Drawable {
    private TextRenderer renderer;
    private float textScaleFactor;
    private float xAng;
    private float yAng;


    public Cube() {
        renderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 72));
        // Compute the scale factor of the largest string which will make
        // them all fit on the faces of the cube
        Rectangle2D bounds = renderer.getBounds("Bottom");
        float w = (float) bounds.getWidth();
        textScaleFactor = 1.0f / (w * 1.1f);
    }


    public void draw(GLAutoDrawable drawable, Time time) {
        xAng += 200 * (float) time.deltaT();
        yAng += 150 * (float) time.deltaT();

        GL gl = drawable.getGL();
        // Base rotation of cube
        gl.glRotatef(xAng, 1, 0, 0);
        gl.glRotatef(yAng, 0, 1, 0);

        // Six faces of cube
        // Top face
        gl.glPushMatrix();
        gl.glRotatef(-90, 1, 0, 0);
        drawFace(gl, 1.0f, 0.2f, 0.2f, 0.8f, "Top");
        gl.glPopMatrix();
        // Front face
        drawFace(gl, 1.0f, 0.8f, 0.2f, 0.2f, "Front");
        // Right face
        gl.glPushMatrix();
        gl.glRotatef(90, 0, 1, 0);
        drawFace(gl, 1.0f, 0.2f, 0.8f, 0.2f, "Right");
        // Back face
        gl.glRotatef(90, 0, 1, 0);
        drawFace(gl, 1.0f, 0.8f, 0.8f, 0.2f, "Back");
        // Left face
        gl.glRotatef(90, 0, 1, 0);
        drawFace(gl, 1.0f, 0.2f, 0.8f, 0.8f, "Left");
        gl.glPopMatrix();
        // Bottom face
        gl.glPushMatrix();
        gl.glRotatef(90, 1, 0, 0);
        drawFace(gl, 1.0f, 0.8f, 0.2f, 0.8f, "Bottom");
        gl.glPopMatrix();


    }

    private void drawFace(GL gl, float faceSize, float r, float g, float b, String text) {
        float halfFaceSize = faceSize / 2;
        // Face is centered around the local coordinate system's z axis,
        // at a z depth of faceSize / 2
        gl.glColor3f(r, g, b);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex3f(-halfFaceSize, -halfFaceSize, halfFaceSize);
        gl.glVertex3f(halfFaceSize, -halfFaceSize, halfFaceSize);
        gl.glVertex3f(halfFaceSize, halfFaceSize, halfFaceSize);
        gl.glVertex3f(-halfFaceSize, halfFaceSize, halfFaceSize);
        gl.glEnd();

        // Now draw the overlaid text. In this setting, we don't want the
        // text on the backward-facing faces to be visible, so we enable
        // back-face culling; and since we're drawing the text over other
        // geometry, to avoid z-fighting we disable the depth test. We
        // could plausibly also use glPolygonOffset but this is simpler.
        // Note that because the TextRenderer pushes the enable state
        // internally we don't have to reset the depth test or cull face
        // bits after we're done.
        renderer.begin3DRendering();
        gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_CULL_FACE);
        // Note that the defaults for glCullFace and glFrontFace are
        // GL_BACK and GL_CCW, which match the TextRenderer's definition
        // of front-facing text.
        Rectangle2D bounds = renderer.getBounds(text);
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        renderer.draw3D(text,
                        w / -2.0f * textScaleFactor,
                        h / -2.0f * textScaleFactor,
                        halfFaceSize,
                        textScaleFactor);
        renderer.end3DRendering();
    }
}
