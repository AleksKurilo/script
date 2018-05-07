import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadWorkerImpl implements ThreadWorker {
    private final ExecutorService executorService;
    private final CompletionService completionService;

    ThreadWorkerImpl(CompletionService completionService) {
        executorService = Executors.newFixedThreadPool(5);
        this.completionService = completionService;
    }

    @Override
    public List<Callable<HashMap<String, Integer>>> createCallableList(List<String> filenames, FileWorker fileWorker) {
        List<Callable<HashMap<String, Integer>>> callables = new LinkedList<>();
        for (String fileNema : filenames) {
            callables.add(new ThreaTask(fileNema, fileWorker));
        }
        return callables;
    }

    @Override
    public List<HashMap<String, Integer>> takeResults(List<Callable<HashMap<String, Integer>>> callables) {
        List<HashMap<String, Integer>> results = new LinkedList<>();
        try {
            for (int i = 0; i < callables.size(); i++) {
                Future future = completionService.take();
                HashMap<String, Integer> resultFromTrhead = (HashMap<String, Integer>) future.get();
                results.add(resultFromTrhead);
            }
        } catch (InterruptedException | ExecutionException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            executorService.shutdown();
        }
        return results;
    }
}
