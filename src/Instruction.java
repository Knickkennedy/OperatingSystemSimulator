public class Instruction {
    private InstructionType instructionType;
    private int length;

    public Instruction(InstructionType instructionType){
        this.instructionType = instructionType;
        this.length = 0;
    }

    public Instruction(InstructionType instructionType, int length){
        this.instructionType = instructionType;
        this.length = length;
    }

    public void setInstructionType(InstructionType instructionType){
        this.instructionType = instructionType;
    }

    public InstructionType getInstructionType(){
        return this.instructionType;
    }

    public void setLength(int length){
        this.length = length;
    }

    public int getLength(){
        return this.length;
    }

    public boolean isFinished(){
        return this.length < 1;
    }

    public void tick(){
        this.length--;
    }

    public ProcessState getState(){
        switch (instructionType){
            case CALCULATE:
                return ProcessState.READY;
            case IO:
                return ProcessState.WAITING;
            case YIELD:
                return ProcessState.READY;
            default:
                return ProcessState.TERMINATED;
        }
    }
}
