package taskTypes;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task> {
    private int id;
    private String name;
    private String description;
    private TaskStatus status;
    private int duration;
    private LocalDateTime startTime;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH.mm");

    public Task(int id, String name, String description, LocalDateTime startTime, int duration){
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, String name, String description, TaskStatus status, LocalDateTime startTime, int duration){
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, LocalDateTime startTime, int duration){
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, TaskStatus status, LocalDateTime startTime, int duration){
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id + "," +
                "Task" + "," +
                name + "," +
                status + "," +
                description + "," +
                formatter.format(startTime) + "," +
                duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime(){
        if(startTime == null){
           return null;
        }
        return this.startTime.plusMinutes(duration);
    }

    @Override
    public int compareTo(Task o) {
        if(this.startTime.isBefore(o.startTime)){
            return -1;
        } else if (this.startTime.isEqual(o.startTime)){
            return 0;
        } else {
            return 1;
        }
    }
}