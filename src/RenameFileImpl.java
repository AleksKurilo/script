import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class RenameFileImpl implements RenameFile {

    private final String oldDir;
    private final String newDir;

    RenameFileImpl(String oldDir, String newDir){
        this.oldDir = oldDir;
        this.newDir = newDir;
    };

    @Override
    public void rename(String oldName, String newName) {
        Path source = FileSystems.getDefault().getPath(oldDir + oldName);
        Path target = FileSystems.getDefault().getPath(newDir+ newName);

        try {
            Files.copy(source, target.resolveSibling(newName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e);
        }
    }

    @Override
    public String assignNewName(String fileName) {
        String[] parts = fileName.split("-ke");
        return parts[0] + ".csv";
    }
}