package limma.swing;

import junit.framework.TestCase;

import javax.swing.*;

public class NavigationListTest extends TestCase {
    public void test() throws Exception {
        JFrame jFrame = new JFrame("test");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        NavigationModel model = new NavigationModel();

        DefaultNavigationNode musicNode = new DefaultNavigationNode("Music");
        musicNode.add(new DefaultNavigationNode("Artists"));
        musicNode.add(new DefaultNavigationNode("Albums"));
        musicNode.add(new DefaultNavigationNode("Songs"));
        model.add(musicNode);

        DefaultNavigationNode moviesNode = new DefaultNavigationNode("Movies");
        moviesNode.add(new DefaultNavigationNode("Genre"));
        moviesNode.add(new DefaultNavigationNode("Categories"));
        moviesNode.add(new DefaultNavigationNode("Directors"));
        moviesNode.add(new DefaultNavigationNode("Actors"));
        model.add(moviesNode);

        jFrame.add(new NavigationList(model));
        jFrame.setSize(300, 300);
        jFrame.setVisible(true);
        while (true) {
            Thread.yield();
        }
    }

}
