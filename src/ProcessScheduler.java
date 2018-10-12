import java.util.PriorityQueue;

public class ProcessScheduler {
    private PriorityQueue<Process> readyQueue;
    private PriorityQueue<Process> waitQueue;
    private PriorityQueue<Process> terminateQueue;

    public ProcessScheduler(){
        this.readyQueue = new PriorityQueue<Process>(25, new PriorityComparator());
        this.waitQueue = new PriorityQueue<Process>(25, new PriorityComparator());
        this.terminateQueue = new PriorityQueue<Process>(25, new PriorityComparator());
    }

    public PriorityQueue<Process> getReadyQueue() {
        return readyQueue;
    }

    public PriorityQueue<Process> getTerminateQueue() {
        return terminateQueue;
    }

    public PriorityQueue<Process> getWaitQueue() {
        return waitQueue;
    }
}
