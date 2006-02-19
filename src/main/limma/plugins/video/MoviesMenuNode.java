package limma.plugins.video;

import limma.swing.menu.MenuNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MoviesMenuNode extends MenuNode {
    private MovieStorage movieStorage;
    private MovieNodeFactory movieNodeFactory;
    private List<MenuNode> trailingNodes = new ArrayList<MenuNode>();

    public MoviesMenuNode(MovieStorage movieStorage, MovieNodeFactory movieNodeFactory) {
        this.movieStorage = movieStorage;
        this.movieNodeFactory = movieNodeFactory;
    }

    public String getTitle() {
        return "Movies";
    }

    public void addTrailingNode(MenuNode menuNode) {
        trailingNodes.add(menuNode);
        menuNode.setParent(this);
    }

    public void performAction() {
    }

    public List<MenuNode> getChildren() {
        ArrayList<MenuNode> children = new ArrayList<MenuNode>();
        MenuNode allMoviesNode = movieNodeFactory.createAllMoviesNode();
        allMoviesNode.setParent(this);
        children.add(allMoviesNode);

        for (Iterator<String> i = movieStorage.getTags().iterator(); i.hasNext();) {
            String tag = i.next();
            MovieTagNode movieTagNode = movieNodeFactory.createTagNode(tag);
            movieTagNode.setParent(this);
            children.add(movieTagNode);
        }

        children.addAll(trailingNodes);
        return children;
    }
}
