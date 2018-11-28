import java.util.PriorityQueue;

public class RoundRobinScheduler {
    private PriorityQueue<Process> readyQueue;
    private PriorityQueue<Process> IoQueue;
    private PriorityQueue<Process> waitingQueue;
    private PriorityQueue<Process> terminateQueue;

    private MemoryManagementUnit memoryManagementUnit;

    public RoundRobinScheduler(MemoryManagementUnit memoryManagementUnit){
        this.readyQueue = new PriorityQueue<>(25, new PriorityComparator());
        this.IoQueue = new PriorityQueue<>(25, new PriorityComparator());
        this.waitingQueue = new PriorityQueue<>(25, new PriorityComparator());
        this.terminateQueue = new PriorityQueue<>(25, new PriorityComparator());
        this.memoryManagementUnit = memoryManagementUnit;
    }

    public PriorityQueue<Process> getReadyQueue() {
        return readyQueue;
    }

    public PriorityQueue<Process> getTerminateQueue() {
        return terminateQueue;
    }

    public PriorityQueue<Process> getIoQueue() {
        return IoQueue;
    }

    public void addProcess(Process process){

        if(process.getProcessControlBlock().getProcessState() == ProcessState.NEW){
            boolean success = memoryManagementUnit.attemptToAddToMemory(process);
            if(success){
                process.getProcessControlBlock().setProcessState(ProcessState.READY);
                performAdd(process);
            }
            else{
                this.waitingQueue.add(process);
            }
        }
        else{
            performAdd(process);
        }

    	/*InstructionType instructionType = process.getInstructionType();

    	if(instructionType != null) {
            switch (instructionType) {
                case CALCULATE:
                    readyQueue.add(process);
                    break;
                case IO:
                    IoQueue.add(process);
                    break;
                case YIELD:
                    readyQueue.add(process);
                    break;
            }
        }
        else
            terminateQueue.add(process);*/
    }

    public void performAdd(Process process){
        if(process.getInstructionType() != null) {
            switch (process.getInstructionType()) {
                case CALCULATE:
                    readyQueue.add(process);
                    break;
                case IO:
                    IoQueue.add(process);
                    break;
                case YIELD:
                    readyQueue.add(process);
            }

            process.getProcessControlBlock().updateState(process.getCurrentInstruction());
        }
    }

    public boolean isEmpty(){
    	return this.readyQueue.isEmpty() && this.IoQueue.isEmpty();
    }
}
