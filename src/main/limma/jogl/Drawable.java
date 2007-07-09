package limma.jogl;

import javax.media.opengl.GLAutoDrawable;

import limma.jogl.utils.Time;

public interface Drawable {
    void draw(GLAutoDrawable drawable, Time time);
}
