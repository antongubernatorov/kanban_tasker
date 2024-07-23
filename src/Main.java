import manager.InMemoryTaskManager;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createNewTask("Купить торт", "Должен быть сегодняшней даты");
        manager.createNewTask("Купить шляпу", "В секонд хенде скидка");
        manager.updateTask(2,2);
        manager.getTask(2);
        manager.createEpic("Подготовка к покупке машины", "Взять кредит в 2025 году");
        manager.showEpics();
        manager.addSubtasks(2,"Собрать документы", "до 22.07");
        manager.showSubtasks(2);
        manager.getEpic(2);
        System.out.println();
        manager.history();
    }
}