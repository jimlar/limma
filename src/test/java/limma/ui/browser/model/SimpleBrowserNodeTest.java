package limma.ui.browser.model;

import junit.framework.TestCase;

public class SimpleBrowserNodeTest extends TestCase {
    public void testTitleIsSet() throws Exception {
        SimpleBrowserNode simpleNavigationNode = new SimpleBrowserNode("nice node");
        assertEquals("nice node", simpleNavigationNode.getTitle());
    }

    public void testAddSetsParentOfTheAddedNode() throws Exception {
        SimpleBrowserNode parent = new SimpleBrowserNode("Parent");
        SimpleBrowserNode child = new SimpleBrowserNode("child");

        assertNull(child.getParent());
        parent.add(child);
        assertSame(parent, child.getParent());
    }

    public void testCantAddAlreadyAddedNode() throws Exception {
        SimpleBrowserNode parent1 = new SimpleBrowserNode("Parent1");
        SimpleBrowserNode parent2 = new SimpleBrowserNode("Parent2");
        SimpleBrowserNode child = new SimpleBrowserNode("child");

        parent1.add(child);
        try {
            parent2.add(child);
            fail();
        } catch (Exception e) {
        }
    }


}
