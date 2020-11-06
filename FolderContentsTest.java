package javabits.FolderListTree;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the DirectoryContents class.
 */
public class FolderContentsTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File root;

    @Before
    public void initialize() throws IOException {
        root = temporaryFolder.newFolder("root");
    }

    @Test
    public void treeWithOneFile() throws IOException {
        create("file.txt");
        List<String> tree = new FolderContents().tree(root);
        List<String> expected = Arrays.asList(
                "root/",
                "+- file.txt");
        verifyTree(expected, tree);
    }

    @Test(expected = IOException.class)
    public void treeWithDirSpace() throws IOException {
        create(" ");
//        List<String> tree = new DirectoryContents().tree(root);
//        List<String> expected = Arrays.asList(
//                "root/",
//                "+-  ");
//        verifyTree(expected, tree);
    }


    @Test
    public void treeWithOneDirectory() throws IOException {
        create("directory/");
        List<String> tree = new FolderContents().tree(root);
        List<String> expected = Arrays.asList(
                "root/",
                "+- directory/");
        verifyTree(expected, tree);
    }

    @Test
    public void treeWithOneDirectoryAndFile() throws IOException {
        create("directory/");
        create("file.txt");
        List<String> tree = new FolderContents().tree(root);
        List<String> expected = Arrays.asList(
                "root/",
                "+- directory/",
                "+- file.txt");
        verifyTree(expected, tree);
    }

    @Test
    public void treeWithTwoDirectories() throws IOException {
        create("directory/");
        create("directory/subdir");
        List<String> tree = new FolderContents().tree(root);
        List<String> expected = Arrays.asList(
                "root/",
                "+- directory/",
                "   +- subdir");
        verifyTree(expected, tree);
    }

    @Test
    public void treeWithTwoDirectoriesAndFile() throws IOException {
        create("directory/");
        create("directory/subdir");
        create("file.txt");
        List<String> tree = new FolderContents().tree(root);
        List<String> expected = Arrays.asList(
                "root/",
                "+- directory/",
                "|  +- subdir",
                "+- file.txt");
        verifyTree(expected, tree);
    }

    // negative tests

    @Test(expected = NullPointerException.class)
    public void treeWithNullDir() throws IOException {
        new FolderContents().tree(null);
    }

    @Test(expected = DirectoryDoesNotExistException.class)
    public void treeWithNonExistDir() throws IOException {
        new FolderContents().tree(new File("/non_existing_dir/"));
    }

    @Test(expected = FileIsNotADirectoryException.class)
    public void treeWithFileIsNotDir() throws IOException {
        File file = temporaryFolder.newFile("file.txt");
        new FolderContents().tree(file);
    }

    @Test
    public void treeWithEmptyDir() throws IOException {
        List<String> tree = new FolderContents().tree(root);
        List<String> expected = Collections.singletonList("root/");
        verifyTree(expected, tree);
    }

    /**
     * Creates a new file under the {@link #root} directory with the specified name.
     * If the name contains sub-directories, they will be created if they don't already exist.
     *
     * @param name the name of the new file.
     * @return the created file.
     * @throws IOException on IO errors.
     */
    private File create(String name) throws IOException {
        assertNotNull("Root is null, make sure createRoot(String) is executed before creating files.", root);

        File file = new File(root, name);
        createDir(file.getParentFile());
        if (name.endsWith("/")) {
            createDir(file);
        } else {
            createFile(file);
        }
        return file;
    }

    /**
     * Verifies that the expected String list with the pretty-print tree output is the same as the actual one.
     *
     * @param expected a list with the expected pretty-print tree output.
     * @param actual a list with the actual pretty-print tree output.
     */
    private void verifyTree(List<String> expected, List<String> actual) {
        String expectedTree = join(expected, "\n");
        String actualTree = join(actual, "\n");
        assertEquals(expectedTree, actualTree);
    }

    private void createFile(File file) throws IOException {
        if (file.exists() && !file.isFile()) {
            throw new IOException("Unable to create file: " + file + ". A directory already exists.");
        }
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Unable to create file: " + file);
        }
    }

    private void createDir(File dir) throws IOException {
        if (dir.exists() && !dir.isDirectory()) {
            throw new IOException("Unable to create directory: " + dir + ". A file that is not a directory already exists.");
        }
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Unable to create directory: " + dir);
        }
    }

    private String join(List<String> entries, String separator) {
        StringBuilder result = new StringBuilder();
        for (String entry : entries) {
            result.append(entry).append(separator);
        }
        return result.toString();
    }
}