package limma.ui.browser.model;

import com.agical.rmock.extension.junit.RMockTestCase;

import javax.swing.*;

public class BrowserModelTest extends RMockTestCase {
    private BrowserModel browserModel;
    private SimpleBrowserNode child1;
    private SimpleBrowserNode child2;
    private SimpleBrowserNode child11;
    private SimpleBrowserNode child12;
    private SimpleBrowserNode child21;
    private SimpleBrowserNode child22;

    protected void setUp() throws Exception {
        super.setUp();
        browserModel = new BrowserModel();
        child1 = new SimpleBrowserNode("child1");
        child2 = new SimpleBrowserNode("child2");
        child11 = new SimpleBrowserNode("child11");
        child12 = new SimpleBrowserNode("child12");
        child21 = new SimpleBrowserNode("child21");
        child22 = new SimpleBrowserNode("child22");

        child1.add(child11);
        child1.add(child12);

        child2.add(child21);
        child2.add(child22);
    }

    public void testLeftModelHoldsChildrenOfTheBaseNode() throws Exception {
        ListModel leftListModel = browserModel.getLeftListModel();

        assertEquals(0, leftListModel.getSize());

        browserModel.add(child1);
        browserModel.add(child2);

        assertEquals(2, leftListModel.getSize());
        assertSame(child1,  leftListModel.getElementAt(0));
        assertSame(child2,  leftListModel.getElementAt(1));
    }

    public void testRightModelHoldsChildrenOfTheSelectedChildOfTheBaseNode() throws Exception {
        ListModel rightListModel = browserModel.getRightListModel();
        assertEquals(0, rightListModel.getSize());

        browserModel.add(child1);
        browserModel.add(child2);

        assertEquals(2, rightListModel.getSize());
        assertSame(child11,  rightListModel.getElementAt(0));
        assertSame(child12,  rightListModel.getElementAt(1));

        browserModel.getBaseNode().setSelectedChildIndex(1);
        assertEquals(2, rightListModel.getSize());
        assertSame(child21,  rightListModel.getElementAt(0));
        assertSame(child22,  rightListModel.getElementAt(1));
    }


    public void testLeftSelectionModelUsesTheSelectedChildIndexOfTheBaseNode() throws Exception {
        browserModel.add(child1);
        browserModel.add(child2);

        assertEquals(0, browserModel.getLeftListSelectionModel().getAnchorSelectionIndex());
        assertEquals(0, browserModel.getLeftListSelectionModel().getLeadSelectionIndex());
        assertEquals(0, browserModel.getLeftListSelectionModel().getMaxSelectionIndex());
        assertEquals(0, browserModel.getLeftListSelectionModel().getMinSelectionIndex());

        browserModel.getLeftListSelectionModel().setSelectionInterval(1, 1);

        assertEquals(1,browserModel.getBaseNode().getSelectedChildIndex());

        assertEquals(1, browserModel.getLeftListSelectionModel().getAnchorSelectionIndex());
        assertEquals(1, browserModel.getLeftListSelectionModel().getLeadSelectionIndex());
        assertEquals(1, browserModel.getLeftListSelectionModel().getMaxSelectionIndex());
        assertEquals(1, browserModel.getLeftListSelectionModel().getMinSelectionIndex());
    }

    public void testRightSelectionModelUsesTheSelectedChildIndexOfTheBaseNodesSelectedChild() throws Exception {
        browserModel.add(child1);
        browserModel.add(child2);
        ListSelectionModel rightListSelectionModel = browserModel.getRightListSelectionModel();
        
        assertEquals(0, rightListSelectionModel.getAnchorSelectionIndex());
        assertEquals(0, rightListSelectionModel.getLeadSelectionIndex());
        assertEquals(0, rightListSelectionModel.getMaxSelectionIndex());
        assertEquals(0, rightListSelectionModel.getMinSelectionIndex());

        assertSame(child1, browserModel.getBaseNode().getSelectedChild());
        assertEquals(0,child1.getSelectedChildIndex());
        assertSame(child11, child1.getSelectedChild());

        rightListSelectionModel.setSelectionInterval(1, 1);

        assertEquals(1,child1.getSelectedChildIndex());

        assertEquals(1, rightListSelectionModel.getAnchorSelectionIndex());
        assertEquals(1, rightListSelectionModel.getLeadSelectionIndex());
        assertEquals(1, rightListSelectionModel.getMaxSelectionIndex());
        assertEquals(1, rightListSelectionModel.getMinSelectionIndex());
    }

    public void testSelectingInLeftModelUpdatesTheRightModel() throws Exception {
        browserModel.add(child1);
        browserModel.add(child2);
        child2.setSelectedChildIndex(1);

        assertSame(child11, browserModel.getRightListModel().getElementAt(0));
        assertSame(0, browserModel.getRightListSelectionModel().getMaxSelectionIndex());

        browserModel.getLeftListSelectionModel().setSelectionInterval(1, 1);

        assertSame(child21, browserModel.getRightListModel().getElementAt(0));
        assertSame(1, browserModel.getRightListSelectionModel().getMaxSelectionIndex());
    }
}
