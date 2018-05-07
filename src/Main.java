import java.io.File;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {

        final RenameFile renameFile = new RenameFileImpl(Binding.PATH_OLD_DIR, Binding.PATH_NEW_DIR);
        final FileWorker fileWorker = new FileWorkerImpl();
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CompletionService completionService = new ExecutorCompletionService(executorService);

        File folder = new File(Binding.PATH_OLD_DIR);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            String newName = renameFile.assignNewName(file.getName());
            renameFile.rename(file.getName(), newName);
            completionService.submit(new FileThreadWorker(newName, fileWorker, map));
        }

        try {
            completionService.take();
        } catch (InterruptedException e) {
            System.out.print("ERROR :");
            e.printStackTrace();
        } finally{
            executorService.shutdown();
        }

        fileWorker.createFile(Binding.FILE_NAME);
        fileWorker.writeToFile(map, Binding.FILE_NAME);
    }
}