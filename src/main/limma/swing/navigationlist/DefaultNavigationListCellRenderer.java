package limma.swing.navigationlist;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

class DefaultNavigationListCellRenderer extends JLabel implements NavigationListCellRenderer {
    private boolean selected;
    private NavigationNode node;

    public DefaultNavigationListCellRenderer() {
        setOpaque(false);
        setFont(Font.decode("Verdana").deriveFont((float) 40));
        setBackground(Color.white);
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
    }

    public boolean supportsRendering(Object value) {
        return true;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        this.selected = isSelected;
        node = (NavigationNode) value;
        setComponentOrientation(list.getComponentOrientation());
        setForeground(isSelected ? Color.white : Color.black);
        setText(node.getTitle());
        return this;
    }

    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (selected) {
            paintGradient(graphics);
            graphics.setColor(new Color(0x127ec7));
            graphics.drawLine(0, 0, graphics.getClipBounds().width, 0);
            graphics.setColor(new Color(0x428ac5));
            graphics.drawLine(0, graphics.getClipBounds().height - 1, graphics.getClipBounds().width, graphics.getClipBounds().height - 1);
        }
        if (node.getChildCount() > 0) {
            graphics.setPaint(getForeground());
            drawRigthString(graphics, "+");
        }
        super.paintComponent(g);
    }

    private void drawRigthString(Graphics2D graphics, String str) {
        Rectangle2D stringBounds = getFont().getStringBounds(str, graphics.getFontRenderContext());
        FontMetrics fontMetrics = getFontMetrics(getFont());
        graphics.drawString(str, (int) (graphics.getClipBounds().width - stringBounds.getWidth()) - 10, (int) fontMetrics.getHeight() - fontMetrics.getDescent());
    }

    private void paintGradient(Graphics2D graphics) {
        Color topColor = new Color(0x209bd6);
        Color bottomColor = new Color(0x0177bf);
        int height = graphics.getClipBounds().height;
        int yPos = 0;
        graphics.setPaint(new GradientPaint(0, yPos, topColor, 0, yPos + height, bottomColor));
        graphics.fillRect(0, yPos, graphics.getClipBounds().width, height);
    }


    /**
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     *
     * @return <code>true</code> if the background is completely opaque
     *         and differs from the JList's background;
     *         <code>false</code> otherwise
     * @since 1.5
     */
    public boolean isOpaque() {
        Color back = getBackground();
        Component p = getParent();
        if (p != null) {
            p = p.getParent();
        }
        // p should now be the JList.
        boolean colorMatch = (back != null) && (p != null) &&
                             back.equals(p.getBackground()) &&
                             p.isOpaque();
        return !colorMatch && super.isOpaque();
    }

    public void validate() {
    }

    public void invalidate() {
    }

    public void repaint() {
    }

    public void revalidate() {
    }

    public void repaint(long tm, int x, int y, int width, int height) {
    }

    public void repaint(Rectangle r) {
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (propertyName == "text") {
            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
    }

    public void firePropertyChange(String propertyName, char oldValue, char newValue) {
    }

    public void firePropertyChange(String propertyName, short oldValue, short newValue) {
    }

    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
    }

    public void firePropertyChange(String propertyName, long oldValue, long newValue) {
    }

    public void firePropertyChange(String propertyName, float oldValue, float newValue) {
    }

    public void firePropertyChange(String propertyName, double oldValue, double newValue) {
    }

    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }
}