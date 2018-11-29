public class CoreProcessingUnit {

    private RoundRobinScheduler roundRobinScheduler;
    private MemoryManagementUnit memoryManagementUnit;
    private int timeQuantum;
    private int currentTime;
    private Process runningProcess;
    private Process IoProcess;

    public CoreProcessingUnit(MemoryManagementUnit memoryManagementUnit, int timeQuantum){
    	this.memoryManagementUnit = memoryManagementUnit;
    	this.timeQuantum = timeQuantum;
    	this.roundRobinScheduler = new RoundRobinScheduler(memoryManagementUnit);
    	this.currentTime = 0;
    }

    public void addProcess(Process process){
    	this.roundRobinScheduler.addProcess(process);
    }

    public boolean run(){

	    // grab process from ready queue if it's not empty
	    if(runningProcess == null){
	    	runningProcess = roundRobinScheduler.pollReadyQueue();
	    }

	    // grab process from waiting queue if it's not empty
	    if(IoProcess == null){
	    	IoProcess = roundRobinScheduler.pollIoQueue();
	    }

	    if(currentTime < timeQuantum){

		    // process ready process
		    if (runningProcess != null) {
			    runningProcess.setRunningState();
			    runningProcess.getProcessControlBlock().setPages(memoryManagementUnit.demandPages(runningProcess));
			    runningProcess.run();
			    roundRobinScheduler.tickTimeWaitingInReadyQueue();
		    }

		    // process I/O process
		    if(IoProcess != null){
                IoProcess.getProcessControlBlock().setPages(memoryManagementUnit.demandPages(IoProcess));
			    IoProcess.run();
			    roundRobinScheduler.tickTimeWaitingInIoQueue();
		    }

		    // compare time quantum and determine if you move ready process
		    // move processes based on time quantum and new states

		    if(runningProcess != null && runningProcess.instructionIsFinished()){
			    runningProcess.updateProgramCounter();
			    roundRobinScheduler.addProcess(runningProcess);
			    runningProcess = null;
		    }

		    if(IoProcess != null && IoProcess.instructionIsFinished()){
			    IoProcess.updateProgramCounter();
			    roundRobinScheduler.addProcess(IoProcess);
			    IoProcess = null;
		    }

		    // increment time
            roundRobinScheduler.cleanUpMemory();
		    currentTime++;
	    }
	    else{
		    if (runningProcess != null) {
			    roundRobinScheduler.addProcess(runningProcess);
			    runningProcess = null;
		    }

		    if (IoProcess != null) {
			    roundRobinScheduler.addProcess(IoProcess);
			    IoProcess = null;
		    }

		    currentTime = 0;
	    }
	    return roundRobinScheduler.isEmpty() && runningProcess == null && IoProcess == null;

    }
}
