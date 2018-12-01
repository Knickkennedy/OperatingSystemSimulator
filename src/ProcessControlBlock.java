import java.util.ArrayList;
import java.util.Random;

public class ProcessControlBlock {
    private ProcessState processState;
    private int programCounter;
    private int priority;
    private int numberOfFramesRequired;
    private ArrayList<Integer> pages;
    private int timeWaitingToRun;

    public ProcessControlBlock(){
        this.processState = ProcessState.NEW;
        this.programCounter = 0;
        this.priority = 1;
        Random random = new Random();
        this.numberOfFramesRequired = random.nextInt(5) + 1;
        this.pages = new ArrayList<>();
        this.timeWaitingToRun = 0;
    }

    public void updateTimeWaitingToRun(){
        timeWaitingToRun++;

        if(timeWaitingToRun > 9){

            if(priority > 1)
                priority--;

            timeWaitingToRun = 0;
        }
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

    public boolean updateState(Instruction instruction){
    	if(instruction == null)
    		return true;
    	else {
    	    processState = instruction.getState();
    	    return false;
        }
    }

    public void setToRunning(){
        this.processState = ProcessState.RUNNING;
    }
}
