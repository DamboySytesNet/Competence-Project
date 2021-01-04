package repository;

import java.util.List;

public interface RepositorySaver<T> {

    boolean saveAllGeneric(List<T> objects);
}
