package limma.ui.browser;

import limma.ui.UIProperties;
import limma.ui.browser.model.BrowserModelNode;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BrowserList extends JList {
    private List<BrowserNodeRenderer> renderers = new ArrayList<BrowserNodeRenderer>();

    public BrowserList(UIProperties uiProperties, ListModel listModel) {
        super(listModel);
        setCellRenderer(new ListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                BrowserModelNode node = (BrowserModelNode) value;
                for (BrowserNodeRenderer renderer : renderers) {
                    if (renderer.supportsRendering(node)) {
                        return renderer.getNodeRendererComponent(BrowserList.this, node, index, isSelected & isEnabled(), isSelected);
                    }
                }
                throw new IllegalArgumentException("Rendering of " + value + " is not supported (is it a subclass of BrowserModelNode?)");
            }
        });
        setOpaque(false);
        addCellRenderer(new DefaultBrowserNodeRenderer(uiProperties));

        addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                BrowserModelNode selectedNode = (BrowserModelNode) getSelectedValue();
                if (selectedNode != null) {
                    selectedNode.getParent().setSelectedChildIndex(getSelectedIndex());
                }
            }
        });
    }

    public int getNextMatch(String prefix, int startIndex, Position.Bias bias) {
        return -1;
    }

    public void addCellRenderer(BrowserNodeRenderer renderer) {
        renderers.add(0, renderer);
    }

    public void setSelectedIndex(int index) {
        super.setSelectedIndex(index);
        scrollToSelected();
    }

    private void scrollToSelected() {
        scrollRectToVisible(getCellBounds(getSelectedIndex(), getSelectedIndex()));
    }
}
