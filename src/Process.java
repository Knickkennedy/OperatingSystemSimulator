import java.util.ArrayList;

public class Process {
    private int ID;
    private ArrayList<Instruction> instructions;
    private ProcessControlBlock processControlBlock;

    public Process(int ID){
        this.ID = ID;
        this.instructions = new ArrayList<Instruction>();
        this.processControlBlock = new ProcessControlBlock();
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

    public void setRunningState(){
        this.processControlBlock.setToRunning();
    }

    public void run(){
        Instruction instruction = getCurrentInstruction();

        System.out.printf("Running instruction: %s with length %d left.\n", instruction.getInstructionType(), instruction.getLength());

        if(instruction.getLength() > 0)
            instruction.tick();

        if(instruction.isFinished()) {
            processControlBlock.moveProgramCounter();
            processControlBlock.updateState(getCurrentInstruction());
        }
    }
}
