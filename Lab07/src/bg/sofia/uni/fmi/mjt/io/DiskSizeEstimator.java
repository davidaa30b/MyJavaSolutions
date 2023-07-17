package bg.sofia.uni.fmi.mjt.io;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;

public class DiskSizeEstimator {

    public static void main(String[] args) throws IOException {

        // обхождаме всички дялове на файловата система по подразбиране
        Iterable<FileStore> partitions = FileSystems.getDefault().getFileStores();
        for (FileStore fs : partitions) {
            System.out.println("Partition: " + fs.name());
            System.out.println("Total space: " + ((fs.getTotalSpace()/1024)/1024)/1000);
            System.out.println("Unallocated space: " + ((fs.getUnallocatedSpace()/1024)/1024)/1000);
            System.out.println("Usable space: " + ((fs.getUsableSpace()/1024)/1024)/1000);
        }

    }

}