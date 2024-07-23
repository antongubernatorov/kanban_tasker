package manager;

import java.util.List;

public interface HistoryManager {
    void add(int id);
    List<Integer> getHistory();

}
