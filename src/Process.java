import java.util.ArrayList;
import java.util.Random;

public class Process {
    private int ID;
    private ArrayList<Instruction> instructions;
    private ProcessControlBlock processControlBlock;
    private int size;
    private Process parent;
    private ArrayList<Process> children;

    public Process(int ID){
        this.ID = ID;
        this.instructions = new ArrayList<Instruction>();
        this.processControlBlock = new ProcessControlBlock();
        this.size = 0;
        this.children = new ArrayList<>();
    }

    public String toString(){
        return String.format("ID: %d Size: %d", ID, size);
    }

    public void setParent(Process parent){
        this.parent = parent;
    }

    public Process getParent(){
        return parent;
    }

    public ArrayList<Process> getChildren(){
        return children;
    }

    public void fork(ArrayList<Process> processes){
        Random random = new Random();

        Process process = new Process(OperatingSystem.nextPID++);

        int priority = random.nextInt(999) + 1;
        int exponent = random.nextInt(10) + 1;
        int size = (int)Math.pow(2, exponent);

        int numberOfInstructions = random.nextInt(15) + 5;
        int criticalSection = random.nextInt(numberOfInstructions);

        process.setPriority(priority);
        process.setSize(size);

        for(int j = 0; j < numberOfInstructions; j++) {

            int typeOfInstruction = random.nextInt(4);
            int lengthOfInstruction = random.nextInt(25) + 25;
            switch (typeOfInstruction){
                case 0:
                    if(j == criticalSection){
                        process.addInstruction(new Instruction(InstructionType.CALCULATE, lengthOfInstruction, true));
                    }
                    else{
                        process.addInstruction(new Instruction(InstructionType.CALCULATE, lengthOfInstruction));
                    }
                    break;
                case 1:
                    if(j == criticalSection){
                        process.addInstruction(new Instruction(InstructionType.IO, lengthOfInstruction, true));
                    }
                    else{
                        process.addInstruction(new Instruction(InstructionType.IO, lengthOfInstruction));
                    }
                    break;
                case 2:
                    if(j == criticalSection){
                        process.addInstruction(new Instruction(InstructionType.YIELD, lengthOfInstruction, true));
                    }
                    else{
                        process.addInstruction(new Instruction(InstructionType.CALCULATE, lengthOfInstruction));
                    }
                    break;
                case 3:
                    int roll = random.nextInt(100);
                    if(roll < 3) // our forks have a 3% chance of forking
                        process.fork(processes);
                    break;
            }
        }

        processes.add(process);
        process.setParent(this);
        children.add(process);
    }

    public void kill(){
        for(int i = 0; i < getChildren().size(); i++){
            abortChild(i);
        }

        processControlBlock.setProcessState(ProcessState.TERMINATED);
    }

    public void abortChild(int indexOfChildToAbort){
        children.get(indexOfChildToAbort).kill();
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
        if(getCurrentInstruction() != null)
	        return getCurrentInstruction().isFinished();
        else
            return true;
    }

    public void updateProgramCounter(){
    	getProcessControlBlock().moveProgramCounter();
    	boolean done = getProcessControlBlock().updateState(getCurrentInstruction());
    	if(done)
    	    kill();
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

        if(instruction != null && instruction.getLength() > 0)
            instruction.tick();

    }
}
