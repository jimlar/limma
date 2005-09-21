package limma.plugins.menu;

import javax.swing.*;
import java.awt.*;

public class MenuCellRenderer implements ListCellRenderer {
    private ImageIcon activeIcon;
    private ImageIcon inactiveIcon;

    public MenuCellRenderer() {
        this.activeIcon = new ImageIcon("active.png");
        this.inactiveIcon = new ImageIcon("inactive.png");
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return new MenuButton((MenuNode) value, isSelected ? activeIcon : inactiveIcon);
    }

    private static class MenuButton extends JComponent {
        private ImageIcon icon;
        private MenuNode node;

        public MenuButton(MenuNode node, ImageIcon icon) {
            this.node = node;
            this.icon = icon;
            setOpaque(false);
        }

        public Dimension getPreferredSize() {
            return new Dimension(icon.getIconWidth(), icon.getIconHeight());
        }

        public void paint(Graphics g) {
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            icon.paintIcon(this, g, 0, 0);
            g.setFont(Font.decode("SansSerif").deriveFont(Font.BOLD).deriveFont((float) 30));
            g.setColor(Color.black);
            g.drawString(node.getTitle(), 30 + 1, icon.getIconHeight() / 2 + 11);
            g.setColor(Color.white);
            g.drawString(node.getTitle(), 30, icon.getIconHeight() / 2 + 9);
            super.paint(g);
        }
    }
}
