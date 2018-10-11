public class ProcessControlBlock {
    private ProcessState processState;
    private int processNumber;
    private int programCounter;

    public ProcessControlBlock(int processNumber){
        this.processState = ProcessState.NEW;
        this.processNumber = processNumber;
        this.programCounter = 0;
    }
}
