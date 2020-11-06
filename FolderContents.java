package javabits.FolderListTree;

import com.sun.istack.internal.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple class for listing directory contents.
 */
public class FolderContents {

    /**
     * Lists the contents of specified root directory in a tree-like pretty-print format.<p/>
     *
     * Print each list element on a separate line to view the tree structure output. If an element
     * represents a directory, the name will have a slash "/" character appended: e.g.
     * "documents/". <p/>
     *
     * Contents of the root directory and each sub-directory are sorted first on type
     * (directory/file) and then on name. Note that the root directory itself is also included in
     * the output as the first element.<p/>
     *
     * Example:
     * <pre>
     * root/
     *  +- documents/
     *  |  +- letter.doc
     *  |  +- recipes.doc
     *  +- description.txt
     *  +- readme.txt
     * </pre>
     *
     * @param root the directory to create the tree from.
     * @return a String list with the contents of the specified root directory formatted
     * in a pretty tree-like structure.
     * @throws DirectoryDoesNotExistException if specified root directory does not exist.
     * @throws FileIsNotADirectoryException if specified root directory is not a directory.
     * @throws IOException on any other problems with the file system.
     * @throws NullPointerException if root is null.
     */
    public List<String> tree(File root) throws IOException {
        if (root == null) {
            throw new NullPointerException("Specified root directory is null");
        } else if (!root.exists()) {
            throw new DirectoryDoesNotExistException(root);
        } else if (!root.isDirectory()) {
            throw new FileIsNotADirectoryException(root);
        }
        return buildTree("", root, new ArrayList<>(), false);
    }

    private List<String> buildTree(String prefix, File currentFile, List<String> contents, boolean lastChild) throws IOException {
        String suffix = currentFile.isDirectory() ? "/" : "";
        contents.add(prefix + currentFile.getName() + suffix);
        if (currentFile.isDirectory()) {
            buildTreeForSubDirectory(prefix, currentFile, contents, lastChild);
        }
        return contents;
    }

    private void buildTreeForSubDirectory(String prefix, File currentFile, List<String> contents, boolean lastChild) throws IOException {
        File[] children = getDirectoryChildren(currentFile);
        String newPrefix = lastChild ?
                prefix.replace("+- ", "   ") + "+- " :
                prefix.replace("+- ", "|  ") + "+- ";

        for (int i = 0; i < children.length; i++) {
            File child = children[i];
            buildTree(newPrefix, child, contents, i == children.length - 1);
        }
    }

    private File[] getDirectoryChildren(File parent) throws IOException {
        File[] children = parent.listFiles();
        if (children == null) {
            throw new IOException("Unable to list contents of directory: " + parent);
        }
        Arrays.sort(children, new FileTypeAndNameComparator());
        return children;
    }

}