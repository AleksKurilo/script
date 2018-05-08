import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public interface ThreadWorker {

    List<Callable<Map<String, Integer>>> createCallableList(List<String> filenames, FileWorker fileWorker);

    List<Map<String, Integer>> takeResults (List<Callable<Map<String, Integer>>> callables);

    void stop(ExecutorService executorService);
}
