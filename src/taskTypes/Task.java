package taskTypes;


import static taskTypes.TaskStatus.statuses;

public class Task {
    final String name;
    final String description;
    final int id;
    String status;

    public Task(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = statuses[0];
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

    public void setStatus(int statusId) {
        this.status = TaskStatus.statuses[statusId];
    }

    @Override
    public String toString() {
        return "[" + "Название='" + name + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус='" + status + '\'' +
                "]";
    }
}