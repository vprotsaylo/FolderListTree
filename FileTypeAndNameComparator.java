package javabits.FolderListTree;

import java.io.File;
import java.util.Comparator;

/**
 * Comparator for sorting files first on type (directory / file) and then on file name.
 */
public class FileTypeAndNameComparator implements Comparator<File> {

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(File file1, File file2) {
        if (file1.isDirectory() && !file2.isDirectory()) {
            return -1;
        } else if (file2.isDirectory() && !file1.isDirectory()) {
            return 1;
        } else {
            return file1.getName().compareTo(file2.getName());
        }
    }
}