public class ProcessControlBlock {
    private ProcessState processState;
    private int programCounter;
    private int priority;

    public ProcessControlBlock(){
        this.processState = ProcessState.NEW;
        this.programCounter = 0;
        this.priority = 1;
    }

    public ProcessControlBlock(int priority){
        this.processState = ProcessState.NEW;
        this.programCounter = 0;
        this.priority = priority;
    }

    public int getPriority(){
        return this.priority;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public int getProgramCounter(){
        return this.programCounter;
    }

    public void moveProgramCounter(){
        this.programCounter++;
    }

    public void updateState(Instruction instruction){
    	if(instruction == null)
    		processState = ProcessState.TERMINATED;
    	else
            processState = instruction.getState();
    }

    public void setToRunning(){
        this.processState = ProcessState.RUNNING;
    }
}
