package limma.utils;

import java.io.File;
import java.util.*;

public class DirectoryScanner {
    private File baseDir;
    private boolean sortAlphabetically;
    private Set<File> skipped = new HashSet<File>();

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
                List<File> children = Arrays.asList(childrenArray != null ? childrenArray : new File[0]);
                if (sortAlphabetically) {
                    children = new ArrayList<File>(children);
                    Collections.sort(children, new FilenameComparator());
                }
                for (File child : children) {
                    if (!file.equals(child)) { //Wierd OSX bug? file is child to itself
                        accept(child, visitor);
                    }
                }
            }
        } else {
            visitor.visit(file);
        }
    }

    public static interface Visitor {
        boolean visit(File file);
    }

    private static class FilenameComparator implements Comparator<File> {
        public int compare(File o1, File o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }
}
