import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {

        final HashMap<String, Integer> map = new HashMap<>();
        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        final CompletionService completionService = new ExecutorCompletionService(executorService);

        final RenameFile renameFile = new RenameFileImpl(Binding.PATH_OLD_DIR, Binding.PATH_NEW_DIR);
        final FileWorker fileWorker = new FileWorkerImpl();
        final ThreadWorker threadWorker = new ThreadWorkerImpl(completionService);

        List<String> filenames = new LinkedList<>();
        File folder = new File(Binding.PATH_OLD_DIR);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            String newName = renameFile.assignNewName(file.getName());
            renameFile.rename(file.getName(), newName);
            filenames.add(newName);
        }

        List<Callable<HashMap<String, Integer>>> callables = threadWorker.createCallableList(filenames, fileWorker);
        for (Callable<HashMap<String, Integer>> callable : callables) completionService.submit(callable);
        List<HashMap<String, Integer>> results = threadWorker.takeResults(callables);


        for(HashMap<String, Integer> resultFromTrhead : results){
            resultFromTrhead.forEach((k, v) -> map.merge(k, v, Integer::sum));
        }

        fileWorker.createFile(Binding.FILE_NAME);
        fileWorker.writeToFile(map, Binding.FILE_NAME);

    }

}