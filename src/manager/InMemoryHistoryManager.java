package manager;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    List<Integer> historyManager = new ArrayList<>();

    @Override
    public void add(int id) {
        //нужно добавить логику, что максимум элементов 10
        historyManager.add(id);

    }

    @Override
    public List<Integer> getHistory() {
        return historyManager;
    }
}
