
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;


public class ThreaTask implements Callable<HashMap<String, Integer>> {

    private final String fileName;
    private final FileWorker fileWorker;
    private final HashMap<String, Integer> map;

    ThreaTask(String fileName, FileWorker fileWorker) {
        this.fileName = fileName;
        this.fileWorker = fileWorker;
        this.map = new HashMap<String, Integer>();
    }

    @Override
    public HashMap<String, Integer> call() throws Exception {
        System.out.println("Created " + Thread.currentThread().getName());
        Path source = FileSystems.getDefault().getPath(Binding.PATH_NEW_DIR + fileName);
        List<String> lines = fileWorker.getListStringFromFile(source);
        setData(lines, map, fileName);
        return this.map;
    }

    private void setData(List<String> lines, HashMap<String, Integer> map, String fileSoursName) {
        synchronized (this) {
            System.out.println("Set data to file" + fileSoursName);
            String regrex = fileSoursName.replace("-", " ").replace(".csv", "");
            for (int j = 1; j < lines.size(); j++) {
                String line = lines.get(j);
                String[] array = line.split(",");
                String key = array[1].replaceAll(regrex, "");
                if(key.isEmpty() || key.contains("\"\"")){
                    key = array[1];
                }
                String s = array[4].replace("\" ", "").replace("\"", "");
                Integer volume = fileWorker.getVolume(s);
                if (map.containsKey(key)) {
                    map.put(key, map.get(key) + volume);
                } else
                    map.put(key, volume);
            }
        }
    }
}