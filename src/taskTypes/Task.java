package taskTypes;



public class Task {
    final String name;
    final String description;
    final int id;
    TaskStatus status;

    public Task(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = TaskStatus.IN_PROGRESS;
    }

    @Override
    public String toString() {
        return "[" + "Название='" + name + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус='" + status + '\'' +
                "]";
    }
}