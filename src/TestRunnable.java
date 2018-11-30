public class TestRunnable implements Runnable{

    private RoundRobinScheduler roundRobinScheduler;
    private Process runningProcess;
    private Process IoProcess;
    private int timeQuantum;
    private int currentTime;
    private MemoryManagementUnit memoryManagementUnit;
    private boolean done;

    public TestRunnable(MemoryManagementUnit memoryManagementUnit, RoundRobinScheduler roundRobinScheduler, int timeQuantum){
        this.memoryManagementUnit = memoryManagementUnit;
        this.roundRobinScheduler = roundRobinScheduler;
        this.timeQuantum = timeQuantum;
        this.currentTime = 0;
        this.done = false;
    }

    @Override
    public void run() {
        try {

            if(runningProcess == null && IoProcess == null && roundRobinScheduler.isEmpty()){
                done = true;
                return;
            }

            // grab process from ready queue if it's not empty
            if (runningProcess == null) {
                runningProcess = roundRobinScheduler.pollReadyQueue();
            }

            // grab process from waiting queue if it's not empty
            if (IoProcess == null) {
                IoProcess = roundRobinScheduler.pollIoQueue();
            }

            if (currentTime < timeQuantum) {

                // process ready process
                if (runningProcess != null) {
                    runningProcess.setRunningState();
                    runningProcess.getProcessControlBlock().setPages(memoryManagementUnit.demandPages(runningProcess));
                    runningProcess.run();
                    roundRobinScheduler.tickTimeWaitingInReadyQueue();
                }

                // process I/O process
                if (IoProcess != null) {
                    IoProcess.getProcessControlBlock().setPages(memoryManagementUnit.demandPages(IoProcess));
                    IoProcess.run();
                    roundRobinScheduler.tickTimeWaitingInIoQueue();
                }

                // compare time quantum and determine if you move ready process
                // move processes based on time quantum and new states

                if (runningProcess != null && runningProcess.instructionIsFinished()) {
                    runningProcess.updateProgramCounter();
                    roundRobinScheduler.addProcess(runningProcess);
                    runningProcess = null;
                }

                if (IoProcess != null && IoProcess.instructionIsFinished()) {
                    IoProcess.updateProgramCounter();
                    roundRobinScheduler.addProcess(IoProcess);
                    IoProcess = null;
                }

                // increment time
                roundRobinScheduler.cleanUpMemory();
                currentTime++;
            } else {
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

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isDone(){
        return done;
    }
}
