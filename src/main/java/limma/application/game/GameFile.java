package limma.application.game;

import java.io.File;

class GameFile {
    public static Type C64 = new Type(0);
    public static Type SNES = new Type(1);

    private String name;
    private File file;
    private Type type;

    public GameFile(String name, File file, Type type) {
        this.name = name;
        this.file = file;
        this.type = type;
    }

    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public Type getType() {
        return type;
    }

    public static class Type {
        private int id;

        private Type(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Type)) return false;

            final Type type = (Type) o;

            if (id != type.id) return false;

            return true;
        }

        public int hashCode() {
            return id;
        }
    }
}
