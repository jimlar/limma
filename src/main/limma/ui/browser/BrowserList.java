package limma.ui.browser;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;

import limma.ui.UIProperties;

public class BrowserList extends JList {
    private List<NavigationNodeRenderer> renderers = new ArrayList<NavigationNodeRenderer>();

    public BrowserList(UIProperties uiProperties, ListModel listModel) {
        super(listModel);

        setCellRenderer(new ListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                NavigationNode node = (NavigationNode) value;
                for (NavigationNodeRenderer renderer : renderers) {
                    if (renderer.supportsRendering(node)) {
                        return renderer.getNodeRendererComponent(BrowserList.this, node, index, isSelected & isEnabled(), isSelected);
                    }
                }
                throw new IllegalArgumentException("Rendering of " + value + " is not supported (is it a subclass of NavigationNode?)");
            }
        });
        setOpaque(false);
        addCellRenderer(new DefaultNavigationNodeRenderer(uiProperties));

        addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                NavigationNode selectedNode = (NavigationNode) getSelectedValue();
                if (selectedNode != null) {
                    selectedNode.getParent().setSelectedChildIndex(getSelectedIndex());
                }
            }
        });

    }

    public int getNextMatch(String prefix, int startIndex, Position.Bias bias) {
        return -1;
    }

    public void addCellRenderer(NavigationNodeRenderer renderer) {
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
