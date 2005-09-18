package limma.swing;

import javax.swing.*;
import java.awt.*;

class AntialiasCellRenderer implements ListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return new ListItemComponent(value.toString(), isSelected);
    }

    private static class ListItemComponent extends JComponent {
        private boolean selected;
        private static final int FONT_SIZE = 30;
        private String text;

        public ListItemComponent(String text, boolean isSelected) {
            this.text = text;
            selected = isSelected;
        }


        public Dimension getPreferredSize() {
            return new Dimension(1, FONT_SIZE + 2);
        }

        public void paint(Graphics g) {
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (selected) {
                Rectangle clipBounds = g.getClipBounds();
                g.setColor(SystemColor.textHighlight);
                g.fillRect(0, 0, (int) clipBounds.getWidth(), (int) clipBounds.getHeight());
            }

            Font font = Font.decode("SansSerif");
            font = font.deriveFont(Font.BOLD);
            font = font.deriveFont((float) FONT_SIZE);
            g.setFont(font);
            g.setColor(Color.black);
            g.drawString(text, 1, font.getSize() / 2 + 11);
            g.setColor(Color.white);
            g.drawString(text, 0, font.getSize() / 2 + 9);
            paintBorder(g);
        }
    }
}
