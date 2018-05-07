import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

public interface ThreadWorker {

    List<Callable<HashMap<String, Integer>>> createCallableList(List<String> filenames, FileWorker fileWorker);

    List<HashMap<String, Integer>> takeResults (List<Callable<HashMap<String, Integer>>> callables);
}
