import java.util.ArrayList;
import java.util.Random;

public class ProcessControlBlock {
    private ProcessState processState;
    private int programCounter;
    private int priority;
    private int numberOfFramesRequired;
    private ArrayList<Integer> pages;

    public ProcessControlBlock(){
        this.processState = ProcessState.NEW;
        this.programCounter = 0;
        this.priority = 1;
        Random random = new Random();
        this.numberOfFramesRequired = random.nextInt(5) + 1;
        this.pages = new ArrayList<>();
    }

    public int getNumberOfFramesRequired(){
        return this.numberOfFramesRequired;
    }

    public ArrayList<Integer> getPages(){
        return this.pages;
    }

    public void setPages(ArrayList<Integer> pages){
        this.pages = pages;
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

    public void setProcessState(ProcessState state){
        this.processState = state;
    }

    public ProcessState getProcessState(){
        return this.processState;
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
