package javabits.FolderListTree;

import java.io.File;
import java.io.IOException;

/**
 * Signals that a directory does not exist when it was expected to exist.
 */
public class DirectoryDoesNotExistException extends IOException {
    public DirectoryDoesNotExistException(File file) {
        super("Directory does not exist: " + file.getAbsolutePath());
    }
}