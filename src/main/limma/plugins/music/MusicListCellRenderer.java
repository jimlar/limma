package limma.plugins.music;

import javax.swing.*;
import java.awt.*;

class MusicListCellRenderer implements ListCellRenderer {
    private MusicPlugin musicPlugin;

    public MusicListCellRenderer(MusicPlugin musicPlugin) {
        this.musicPlugin = musicPlugin;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return new ListItemComponent((MusicFile) value, cellHasFocus, isSelected);
    }

    private static class ListItemComponent extends JComponent {
        private MusicFile file;
        private boolean selected;
        private static final int FONT_SIZE = 30;

        public ListItemComponent(MusicFile file, boolean hasFocus, boolean isSelected) {
            this.file = file;
            selected = isSelected;
            if (hasFocus) {
                setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1), BorderFactory.createLineBorder(Color.yellow.brighter(), 1)));
            } else {
                setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            }
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

            String text = file.getArtist() + ": " + file.getTitle();
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
