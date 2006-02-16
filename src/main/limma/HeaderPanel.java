package limma;

import limma.swing.AntialiasLabel;
import limma.swing.menu.*;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {
    private UIProperties uiProperties;

    public HeaderPanel(UIProperties uiProperties, LimmaMenu menu) {
        this.uiProperties = uiProperties;
        final AntialiasLabel title = new AntialiasLabel("Limma", uiProperties);
        title.setFont(uiProperties.getLargeFont());
        title.setForeground(Color.black);
        add(title);
        setOpaque(false);

        menu.addMenuListener(new MenuListener() {
            public void menuOpened(LimmaMenu menu) {
                updateTitle(menu.getMenuModel(), title);
            }

            public void menuClosed(LimmaMenu menu) {
                title.setText("Limma");
            }
        });

        menu.getMenuModel().addMenuListener(new MenuModelListener() {
            public void currentNodeChanged(MenuModel menuModel, MenuNode node) {
                updateTitle(menuModel, title);
            }
        });
    }

    private void updateTitle(MenuModel menuModel, AntialiasLabel title) {
        String text = "Limma";
        MenuNode node = menuModel.getCurrentNode();
        if (!menuModel.getRoot().equals(node)) {
            text = node.getTitle();
        }
        title.setText(text);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        Composite oldComposite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, uiProperties.getHeaderTransparency()));


        paintGradient(graphics);

        drawLine(graphics, 0, new Color(0xbabfc3));
        drawLine(graphics, getHeight() - 2, new Color(0x79949d));
        drawLine(graphics, getHeight() - 1, new Color(0x99aab1));

        super.paintComponent(g);
        graphics.setComposite(oldComposite);
    }

    private void drawLine(Graphics2D graphics, int y, Color color) {
        graphics.setColor(color);
        graphics.drawLine(0, y, getWidth(), y);
    }

    private void paintGradient(Graphics2D graphics) {
        Color topColor = new Color(0xf0f7fd);
        Color bottomColor = new Color(0xa4c0cc);
        int height = getHeight();
        int yPos = 0;
        graphics.setPaint(new GradientPaint(0, yPos, topColor, 0, yPos + height, bottomColor));
        graphics.fillRect(0, yPos, getWidth(), height);
    }
}
