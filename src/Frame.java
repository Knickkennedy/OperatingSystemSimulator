public class Frame {

    private boolean free;
    private boolean referenceBit;
    private Process currentProcess;

    public Frame(){
        this.free = true;
        this.referenceBit = false;
    }

    public void setCurrentProcess(Process process){
        this.currentProcess = process;
    }

    public Process getCurrentProcess(){
        return this.currentProcess;
    }

    public boolean isFree(){
        return this.free;
    }

    public void setFree(boolean free){
        this.free = free;
    }

    public boolean isReferenceBit(){
        return this.referenceBit;
    }

    public void setReferenceBit(boolean referenceBit){
        this.referenceBit = referenceBit;
    }
}
