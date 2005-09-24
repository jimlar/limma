package limma.utils;

import java.io.File;

public class DirectoryScanner {
    private File baseDir;

    public DirectoryScanner(File baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * Traverse directory tree and let the visitor visit all ordinary files (no directories)
     */
    public void accept(Visitor visitor) {
        accept(baseDir, visitor);
    }

    private void accept(File file, Visitor visitor) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            for (int i = 0; children != null && i < children.length; i++) {
                File child = children[i];
                accept(child, visitor);
            }
        } else {
            visitor.visit(file);
        }
    }

    public static interface Visitor {
        void visit(File file);
    }
}
