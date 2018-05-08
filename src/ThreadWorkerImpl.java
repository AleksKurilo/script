import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class ThreadWorkerImpl implements ThreadWorker {
    private final CompletionService completionService;

    ThreadWorkerImpl(CompletionService completionService) {
        this.completionService = completionService;
    }

    @Override
    public List<Callable<Map<String, Integer>>> createCallableList(List<String> filenames, FileWorker fileWorker) {
        List<Callable<Map<String, Integer>>> callables = new LinkedList<>();
        for (String fileNema : filenames) {
            callables.add(new FileReader(fileNema, fileWorker));
        }
        return callables;
    }

    @Override
    public List<Map<String, Integer>> takeResults(List<Callable<Map<String, Integer>>> callables) {
        List<Map<String, Integer>> results = new LinkedList<>();
        try {
            for (int i = 0; i < callables.size(); i++) {
                Future <Map<String, Integer>> future = completionService.take();
                Map<String, Integer> resultFromTrhead = future.get(100, MILLISECONDS);
                results.add(resultFromTrhead);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException ex ) {
            System.out.println("ERROR: " + ex.getMessage());
        }
        return results;
    }

    @Override
    public void stop(ExecutorService executorService) {
        try {
            executorService.shutdown();
            executorService.awaitTermination(100, MILLISECONDS);
        } catch (InterruptedException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
