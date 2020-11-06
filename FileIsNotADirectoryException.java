package javabits.FolderListTree;

import java.io.File;
import java.io.IOException;

/**
 * Signals that a en existing file, that is not a directory, was found when a directory was expected.
 */
public class FileIsNotADirectoryException extends IOException {
    public FileIsNotADirectoryException(File file) {
        super("File is not a directory: " + file);
    }
}