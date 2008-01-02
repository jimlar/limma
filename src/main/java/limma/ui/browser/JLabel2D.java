package limma.ui.browser;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

/**
 * http://www.javafaq.nu/java-bookpage-34-3.html
 */
class JLabel2D extends JLabel {

    private Color m_outlineColor;
    private Stroke m_stroke;

    public JLabel2D(String text) {
        super(text);
    }

    public void setOutlineColor(Color outline) {
        m_outlineColor = outline;
        repaint();
    }

    public Color getOutlineColor() {
        return m_outlineColor;
    }

    public void setStroke(Stroke stroke) {
        m_stroke = stroke;
        repaint();
    }

    public Stroke getStroke() {
        return m_stroke;
    }


    protected void paintComponent(Graphics g) {

        Dimension d = getSize();
        Insets ins = getInsets();

        int x = ins.left;
        int y = ins.top;
        int w = d.width - ins.left - ins.right;
        int h = d.height - ins.top - ins.bottom;

        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, d.width, d.height);
        }

        paintBorder(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        FontRenderContext frc = g2.getFontRenderContext();

        TextLayout tl = new TextLayout(getText(), getFont(), frc);

        AffineTransform shear = AffineTransform.getShearInstance(0, 0.0);

        Shape src = tl.getOutline(shear);
        Rectangle rText = src.getBounds();

        float xText = x - rText.x;

        switch (getHorizontalAlignment()) {
            case CENTER:
                xText = x + (w - rText.width) / 2;
                break;

            case RIGHT:
                xText = x + (w - rText.width);
                break;
        }

        float yText = y + h / 2 + tl.getAscent() / 4;

        AffineTransform shift = AffineTransform.getTranslateInstance(xText, yText);

        Shape shp = shift.createTransformedShape(src);

        if (m_outlineColor != null) {

            g2.setColor(m_outlineColor);
            if (m_stroke != null) {
                g2.setStroke(m_stroke);
            }
            g2.draw(shp);

        }

        g2.setColor(getForeground());
        g2.fill(shp);

    }
}
