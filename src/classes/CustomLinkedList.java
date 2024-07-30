package classes;

import taskTypes.Task;

import java.util.*;

public class CustomLinkedList {
    private final Map<Integer, Node<Task>> watchedTasks = new HashMap<>();
    Node<Task> head;
    Node<Task> tail;
    int size = 0;

    public static class Node<T> {
        private Node<T> prev;
        private Task data;
        private Node<T> next;

        public Node(Node<T> prev, Task data, Node<T> next) {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(prev, node.prev) && Objects.equals(data, node.data) && Objects.equals(next, node.next);
        }

        public Task getTask(){
            return this.data;
        }

        public Node<T> getPrev() {
            return prev;
        }

        public void setPrev(Node<T> prev) {
            this.prev = prev;
        }

        public Task getData() {
            return data;
        }

        public void setData(Task data) {
            this.data = data;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        @Override
        public int hashCode() {
            return Objects.hash(prev, data, next);
        }
    }

    public void linkLast(Task task){
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        if(watchedTasks.containsKey(task.getId())){
            removeNode(watchedTasks.get(task.getId()));
        }

        tail = newNode;
        if(oldTail == null){
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        watchedTasks.put(task.getId(), newNode);
        size++;
    }

    public void removeNode(Node<Task> node){
        if(node != null){
            watchedTasks.remove(node.getTask().getId());
            Node<Task> prev = node.getPrev();
            Node<Task> next = node.getNext();

            if(head == node){
                head = next;
            }

            if(tail == node){
                tail = prev;
            }

            if(prev != null){
                prev.setNext(next);
            }

            if(next != null){
                next.setPrev(prev);
            }
        }
    }

    public List<Task> getWatchedTasks(){
        List<Task> res = new ArrayList<>();
        Node<Task> el = head;
        while(el != null){
            res.add(el.getTask());
            el = el.getNext();
        }
        return res;
    }

    public Node<Task> getNode(int id){
        return watchedTasks.get(id);
    }
}
