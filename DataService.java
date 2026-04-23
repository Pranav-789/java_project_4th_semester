import java.util.List;

public interface DataService<T extends FileSerializable> {
    void save(T entity);
    List<T> getAll();
}
