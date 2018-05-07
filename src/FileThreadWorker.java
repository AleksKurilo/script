
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;


public class FileThreadWorker implements Callable<Void> {

    private final String fileName;
    private final FileWorker fileWorker;
    private final ConcurrentHashMap<String, Integer> map;

    FileThreadWorker(String fileName, FileWorker fileWorker, ConcurrentHashMap<String, Integer> map) {
        this.fileName = fileName;
        this.fileWorker = fileWorker;
        this.map = map;
    }

    @Override
    public Void call() throws Exception {
        System.out.println("Created " + Thread.currentThread().getName());
        Path source = FileSystems.getDefault().getPath(Binding.PATH_NEW_DIR + fileName);
        List<String> lines = fileWorker.getListStringFromFile(source);
        setData(lines, map, fileName);
        return null;
    }

    private void setData(List<String> lines, ConcurrentHashMap<String, Integer> mapExist, String fileSoursName) {
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
                if (mapExist.containsKey(key)) {
                    mapExist.put(key, mapExist.get(key) + volume);
                } else
                    mapExist.put(key, volume);
            }
        }
    }
}