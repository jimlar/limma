package limma.plugins.game.c64;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Note:     first track has number 1 and first sector has number 0
 * Fat note: number of sectors per track vary with the track number (thanks commodore)
 */
public class D64Parser {
    private static final int SECTOR_SIZE = 256;
    private static final int SECTORS_PER_TRACK_1_TO_17 = 21;
    private static final int SECTORS_PER_TRACK_18_TO_24 = 19;
    private static final int SECTORS_PER_TRACK_25_TO_30 = 18;
    private static final int SECTORS_PER_TRACK_31_TO_35 = 17;
    private static final int TRACK_18_SECTOR_1_OFFSET = (17 * SECTORS_PER_TRACK_1_TO_17 + 1) * SECTOR_SIZE;
    private List names = new ArrayList();

    private DataInputStream dataIn;

    public D64Parser(InputStream in) throws IOException {
        dataIn = new DataInputStream(in);
        dataIn.skip(TRACK_18_SECTOR_1_OFFSET);

        System.out.println("Integer.toHexString(TRACK_18_SECTOR_1_OFFSET) = " + Integer.toHexString(TRACK_18_SECTOR_1_OFFSET));

        for (int i = 0; i < 8; i++) {
            DirectoryEntry entry = readNextDirectoryEntry();
            names.add(entry);
        }
    }

    private DirectoryEntry readNextDirectoryEntry() throws IOException {
        int nextDirectoryTrack = dataIn.readUnsignedByte();
        int nextDirectorySector = dataIn.readUnsignedByte();
        int fileType = dataIn.readUnsignedByte();
        int firstDataTrack = dataIn.readUnsignedByte();
        int firstDataSector = dataIn.readUnsignedByte();
        byte[] filename = new byte[16];
        dataIn.readFully(filename);
        int firstSideSectorBlock = dataIn.readUnsignedShort();
        int relRecordLength = dataIn.readUnsignedByte();
        dataIn.skip(6); /* Unused execpt GEOS disks */
        int numberOfSectors = dataIn.readUnsignedByte() + 256 * dataIn.readUnsignedByte();
        String name = petAsciiToString(filename);

        return new DirectoryEntry(name, firstDataTrack, firstDataSector, numberOfSectors);
    }

    private String petAsciiToString(byte[] petAscii) {
        return new String(petAscii);
    }

    public List getDirectoryEntries() {
        return names;
    }

    private static class DirectoryEntry {
        private String name;
        private int firstDataTrack;
        private int firstDataSector;
        private int numberOfSectors;

        public DirectoryEntry(String name, int firstDataTrack, int firstDataSector, int numberOfSectors) {
            this.name = name;
            this.firstDataTrack = firstDataTrack;
            this.firstDataSector = firstDataSector;
            this.numberOfSectors = numberOfSectors;
        }

        public String toString() {
            return new ToStringBuilder(this)
                    .append("name", name)
                    .append("firstDataTrack", firstDataTrack)
                    .append("firstDataSector", firstDataSector)
                    .append("numberOfSectors", numberOfSectors)
                    .toString();
        }
    }
}
