import java.util.PriorityQueue;

public class ShortestJobFirstScheduler {
    private PriorityQueue<Process> readyQueue;
    private PriorityQueue<Process> waitQueue;
    private PriorityQueue<Process> terminateQueue;

    public ShortestJobFirstScheduler(){
        this.readyQueue = new PriorityQueue<Process>(25, new SizeComparator());
        this.waitQueue = new PriorityQueue<Process>(25, new SizeComparator());
        this.terminateQueue = new PriorityQueue<Process>(25, new SizeComparator());
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

    public void addProcess(Process process){
        InstructionType instructionType = process.getInstructionType();

        if(instructionType != null) {
            switch (instructionType) {
                case CALCULATE:
                    readyQueue.add(process);
                    break;
                case IO:
                    waitQueue.add(process);
                    break;
                case YIELD:
                    readyQueue.add(process);
                    break;
            }
        }
        else
            terminateQueue.add(process);
    }

    public boolean isEmpty(){
        return this.readyQueue.isEmpty() && this.waitQueue.isEmpty();
    }
}
