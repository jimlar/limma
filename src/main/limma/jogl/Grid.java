package limma.jogl;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import limma.jogl.utils.Time;

public class Grid implements Drawable {
    private Drawable[][] children;
    private int columns;
    private int rows;

    public Grid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        children = new Drawable[columns][rows];
    }

    public void set(int row, int column, Drawable drawable) {
        children[row][column] = drawable;
    }

    public void draw(GLAutoDrawable autoDrawable, Time time) {
        GL gl = autoDrawable.getGL();

        double cellWidth = 2d / columns;
        double cellHeight = 2d / rows;

        for (int y = 0; y < children.length; y++) {
            Drawable[] row = children[y];
            for (int x = 0; x < row.length; x++) {
                Drawable drawable = row[x];

                if (drawable != null) {
                    gl.glPushMatrix();

                    double translateX = cellWidth * x - (cellWidth / 2d);
                    double translateY = cellHeight * y - (cellHeight / 2d);
                    gl.glTranslated(translateX, translateY, 0);
                    double scaleX = 1.5d / columns;
                    double scaleY = 1.5d / rows;
                    gl.glScaled(scaleX, scaleY, 1);

                    drawable.draw(autoDrawable, time);

                    gl.glPopMatrix();
                }
            }
        }
    }
}
