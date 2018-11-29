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

    public void tickTimeWaitingInReadyQueue(){
        for(Process process : readyQueue){
            process.getProcessControlBlock().updateTimeWaitingToRun();
        }
    }

    public void tickTimeWaitingInIoQueue(){
        for(Process process : IoQueue){
            process.getProcessControlBlock().updateTimeWaitingToRun();
        }
    }

    public Process pollReadyQueue(){
        return readyQueue.poll();
    }

    public Process pollIoQueue(){
        return IoQueue.poll();
    }

    public void cleanUpMemory(){
        for(Process process : terminateQueue){
            memoryManagementUnit.removeFromCacheAndMainMemory(process);
        }

        Process processToAdd = waitingQueue.poll();
        if(processToAdd != null)
            addProcess(processToAdd);
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
            process.getProcessControlBlock().updateState(process.getCurrentInstruction());
            performAdd(process);
        }

    }

    public void performAdd(Process process){
        if(process.getInstructionType() != null) {
            switch (process.getProcessControlBlock().getProcessState()) {
                case READY:
                    readyQueue.add(process);
                    break;
                case WAITING:
                    IoQueue.add(process);
                    break;
                case TERMINATED:
                    terminateQueue.add(process);
            }

            process.getProcessControlBlock().updateState(process.getCurrentInstruction());
        }
    }

    public boolean isEmpty(){
    	return this.readyQueue.isEmpty() && this.IoQueue.isEmpty() && terminateQueue.isEmpty() && waitingQueue.isEmpty();
    }
}
