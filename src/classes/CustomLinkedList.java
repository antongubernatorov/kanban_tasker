package classes;


import taskTypes.Task;

import java.util.*;


public class CustomLinkedList {
    private final Map<Integer, Node> watchedTasks = new HashMap<>();
    private Node first;
    private Node last;

    public void linkLast(Task task) {
        Node element = new Node();
        element.setTask(task);

        if (watchedTasks.containsKey(task.getId())) {
            removeNode(watchedTasks.get(task.getId()));
        }

        if (first == null) {
            last = element;
            first = element;
            element.setNext(null);
            element.setPrev(null);
        } else {
            element.setPrev(last);
            element.setNext(null);
            last.setNext(element);
            last = element;
        }
        watchedTasks.put(task.getId(), element);
    }

    public List<Task> getWatchedTasks() {
        List<Task> result = new ArrayList<>();
        Node element = first;
        while (element != null) {
            result.add(element.getTask());
            element = element.getNext();
        }
        return result;
    }

    public void removeNode(Node node) {
        if (node != null) {
            watchedTasks.remove(node.getTask().getId());
            Node prev = node.getPrev();
            Node next = node.getNext();

            if (first == node) {
                first = node.getNext();
            }
            if (last == node) {
                last = node.getPrev();
            }

            if (prev != null) {
                prev.setNext(next);
            }

            if (next != null) {
                next.setPrev(prev);
            }
        }
    }

    public Node getNode(int id) {
        return watchedTasks.get(id);
    }

    public class Node {
        private Task task;
        private Node prev;
        private Node next;

        public Node getNext() {
            return next;
        }

        public Node getPrev() {
            return prev;
        }

        public Task getTask() {
            return task;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public void setTask(Task task) {
            this.task = task;
        }
    }

}


