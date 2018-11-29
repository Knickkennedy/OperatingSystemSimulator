import java.util.ArrayList;
import java.util.Random;

public class Process {
    private int ID;
    private ArrayList<Instruction> instructions;
    private ProcessControlBlock processControlBlock;
    private int size;


    public Process(int ID){
        this.ID = ID;
        this.instructions = new ArrayList<Instruction>();
        this.processControlBlock = new ProcessControlBlock();
        this.size = 0;


    }

    public int getNumberOfFramesRequired(){
        return this.processControlBlock.getNumberOfFramesRequired();
    }

    public ArrayList<Integer> getPages(){
        return this.processControlBlock.getPages();
    }

    public void addInstruction(Instruction instruction){
    	this.instructions.add(instruction);
    }

    public Instruction getCurrentInstruction(){
    	if(processControlBlock.getProgramCounter() >= instructions.size())
    		return null;
    	else
    	    return instructions.get(processControlBlock.getProgramCounter());
    }

    public ProcessControlBlock getProcessControlBlock(){
        return this.processControlBlock;
    }

    public void setPriority(int priority){
        processControlBlock.setPriority(priority);
    }

    public void setRunningState(){
        this.processControlBlock.setToRunning();
    }

    public boolean instructionIsFinished(){
	    return getCurrentInstruction().isFinished();
    }

    public void updateProgramCounter(){
    	getProcessControlBlock().moveProgramCounter();
    	getProcessControlBlock().updateState(getCurrentInstruction());
    }

    public InstructionType getInstructionType(){
        Instruction instruction = getCurrentInstruction();
        if(instruction != null)
            return instruction.getInstructionType();
        else
            return null;
    }

    public int getPriority(){
        return processControlBlock.getPriority();
    }

    public int getSize(){
        return this.size;
    }

    public void setSize(int size){
        this.size = size;
    }

    public void run(){
        Instruction instruction = getCurrentInstruction();

        System.out.printf("Running Process: %s with priority: %d with instruction: %s with length: %d left. Process State: %s\n", ID, getPriority(), instruction.getInstructionType(), instruction.getLength(), getProcessControlBlock().getProcessState());

        if(instruction.getLength() > 0)
            instruction.tick();

    }
}
