import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class AbstractFileService<T extends FileSerializable> implements DataService<T> {
    protected String filePath;

    public AbstractFileService(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(T entity) {
        try (FileWriter fw = new FileWriter(filePath, true)) {
            fw.write(entity.toFileString() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<T> getAll() {
        List<T> entities = new ArrayList<>();
        File file = new File(filePath);
        
        if (!file.exists()) {
            return entities;
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split(",");
                T entity = parseLine(data);
                if (entity != null) {
                    entities.add(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entities;
    }

    protected abstract T parseLine(String[] data);
}
