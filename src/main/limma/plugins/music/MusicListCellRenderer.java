package limma.plugins.music;

import javax.swing.*;
import java.awt.*;

class MusicListCellRenderer implements ListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return new ListItemComponent((MusicFile) value, isSelected);
    }

    private static class ListItemComponent extends JComponent {
        private MusicFile file;

        public ListItemComponent(MusicFile file, boolean isSelected) {
            this.file = file;
            setOpaque(false);
            if (isSelected) {
                setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1), BorderFactory.createLineBorder(Color.white, 1)));
            } else {
                setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            }
        }


        public Dimension getPreferredSize() {
            return new Dimension(1, 30);
        }

        public void paint(Graphics g) {
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            String text = file.getArtist() + ": " + file.getTitle();
            Font font = Font.decode("SansSerif");
            font = font.deriveFont(Font.BOLD);
            font = font.deriveFont((float) 30);
            g.setFont(font);
            g.setColor(Color.black);
            g.drawString(text, 1, font.getSize() / 2 + 11);
            g.setColor(Color.white);
            g.drawString(text, 0, font.getSize() / 2 + 9);
            super.paint(g);
        }
    }
}
