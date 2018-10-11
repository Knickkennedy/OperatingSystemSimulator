import java.util.ArrayList;

public class Process {
    private int ID;
    private ArrayList<Instruction> instructions;

    public Process(int ID){
        this.ID = ID;
        this.instructions = new ArrayList<Instruction>();
    }

    public int run(int currentInstruction){
        Instruction instruction = instructions.get(currentInstruction);

        if(instruction.getLength() != 0)
            instruction.updateLength(-1);

        if(instruction.isFinished())
            return ++currentInstruction;
        else
            return currentInstruction;
    }
}
