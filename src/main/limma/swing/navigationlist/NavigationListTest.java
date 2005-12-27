package limma.swing.navigationlist;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.*;

public class NavigationListTest extends TestCase {
    public void test() throws Exception {

        String[] availableFontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (int i = 0; i < availableFontFamilyNames.length; i++) {
            String availableFontFamilyName = availableFontFamilyNames[i];
            System.out.println("availableFontFamilyName = " + availableFontFamilyName);
        }

        JFrame jFrame = new JFrame("test");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        NavigationModel model = new NavigationModel();

        DefaultNavigationNode musicNode = new DefaultNavigationNode("Music");
        DefaultNavigationNode artists = new DefaultNavigationNode("Artists");
        artists.add(new DefaultNavigationNode("Alice Cooper"));
        artists.add(new DefaultNavigationNode("Coldplay"));
        artists.add(new DefaultNavigationNode("Rammstein"));
        musicNode.add(artists);
        musicNode.add(new DefaultNavigationNode("Albums"));
        musicNode.add(new DefaultNavigationNode("Songs"));
        model.add(musicNode);

        DefaultNavigationNode moviesNode = new DefaultNavigationNode("Movies");
        DefaultNavigationNode genre = new DefaultNavigationNode("Genre");
        genre.add(new DefaultNavigationNode("Action"));
        genre.add(new DefaultNavigationNode("Drama"));
        genre.add(new DefaultNavigationNode("Kids"));
        genre.add(new DefaultNavigationNode("War"));
        moviesNode.add(genre);
        DefaultNavigationNode categories = new DefaultNavigationNode("Categories");
        categories.add(new DefaultNavigationNode("Alfons"));
        categories.add(new DefaultNavigationNode("Bond"));
        categories.add(new DefaultNavigationNode("New Movies"));
        moviesNode.add(categories);
        moviesNode.add(new DefaultNavigationNode("Directors"));
        moviesNode.add(new DefaultNavigationNode("Actors"));
        model.add(moviesNode);

        jFrame.add(new NavigationList(model));
        jFrame.setSize(800, 600);
        jFrame.setVisible(true);
        while (true) {
            Thread.yield();
        }
    }

}
