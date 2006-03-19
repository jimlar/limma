package limma.plugins.video;

import limma.swing.menu.NavigationNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MoviesNavigationNode extends NavigationNode {
    private MovieStorage movieStorage;
    private MovieNodeFactory movieNodeFactory;
    private List<NavigationNode> trailingNodes = new ArrayList<NavigationNode>();

    public MoviesNavigationNode(MovieStorage movieStorage, MovieNodeFactory movieNodeFactory) {
        this.movieStorage = movieStorage;
        this.movieNodeFactory = movieNodeFactory;
    }

    public String getTitle() {
        return "Movies";
    }

    public void addTrailingNode(NavigationNode navigationNode) {
        trailingNodes.add(navigationNode);
        navigationNode.setParent(this);
    }

    public void performAction() {
    }

    public List<NavigationNode> getChildren() {
        ArrayList<NavigationNode> children = new ArrayList<NavigationNode>();
        NavigationNode allMoviesNode = movieNodeFactory.createAllMoviesNode();
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
