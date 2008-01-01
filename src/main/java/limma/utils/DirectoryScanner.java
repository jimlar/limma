package limma.utils;

import java.io.File;
import java.util.*;

public class DirectoryScanner {
    private File baseDir;
    private boolean sortAlphabetically;
    private Set skipped = new HashSet();

    public DirectoryScanner(File baseDir) {
        this(baseDir, false);
    }

    public DirectoryScanner(File baseDir, boolean sortAlphabetically) {
        this.baseDir = baseDir;
        this.sortAlphabetically = sortAlphabetically;
    }

    /**
     * Traverse directory tree and let the visitor visit all ordinary files (no directories)
     */
    public void accept(Visitor visitor) {
        accept(baseDir, visitor);
    }

    public void skip(File file) {
        skipped.add(file);
    }

    private void accept(File file, Visitor visitor) {
        if (skipped.contains(file)) {
            return;
        }
        if (file.isDirectory()) {
            boolean descend = visitor.visit(file);
            if (descend) {
                File[] childrenArray = file.listFiles();
                List children = Arrays.asList(childrenArray != null ? childrenArray : new File[0]);
                if (sortAlphabetically) {
                    children = new ArrayList(children);
                    Collections.sort(children, new Comparator() {
                        public int compare(Object o1, Object o2) {
                            return ((File) o1).getName().compareToIgnoreCase(((File) o2).getName());
                        }
                    });
                }
                for (Iterator i = children.iterator(); i.hasNext();) {
                    File child = (File) i.next();
                    accept(child, visitor);
                }
            }
        } else {
            visitor.visit(file);
        }
    }

    public static interface Visitor {
        boolean visit(File file);
    }
}
