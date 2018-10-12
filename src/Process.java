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

    public ProcessControlBlock getProcessControlBlock(){
        return this.processControlBlock;
    }

    public void setRunningState(){
        this.processControlBlock.setToRunning();
    }

    public void run(){
        Instruction instruction = instructions.get(processControlBlock.getProgramCounter());

        if(instruction.getLength() > 0)
            instruction.tick();

        if(instruction.isFinished()) {
            processControlBlock.moveProgramCounter();
            processControlBlock.updateState(instructions.get(processControlBlock.getProgramCounter()));
        }
    }
}
