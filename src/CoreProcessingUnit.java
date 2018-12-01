public class CoreProcessingUnit{

    private RoundRobinScheduler roundRobinScheduler;
    private MemoryManagementUnit memoryManagementUnit;
    private int timeQuantum;
    private TestRunnable firstRunnable;
    private Thread one;
    private TestRunnable secondRunnable;
    private Thread two;
    private TestRunnable thirdRunnable;
    private Thread three;
    private TestRunnable fourthRunnable;
    private Thread four;

    public CoreProcessingUnit(MemoryManagementUnit memoryManagementUnit, int timeQuantum){
    	this.memoryManagementUnit = memoryManagementUnit;
    	this.timeQuantum = timeQuantum;
    	this.roundRobinScheduler = new RoundRobinScheduler(memoryManagementUnit);
    	firstRunnable = new TestRunnable(memoryManagementUnit, roundRobinScheduler, timeQuantum);
        one = new Thread(firstRunnable);
        secondRunnable = new TestRunnable(memoryManagementUnit, roundRobinScheduler, timeQuantum);
        two = new Thread(secondRunnable);
        thirdRunnable = new TestRunnable(memoryManagementUnit, roundRobinScheduler, timeQuantum);
        three = new Thread(thirdRunnable);
        fourthRunnable = new TestRunnable(memoryManagementUnit, roundRobinScheduler, timeQuantum);
        four = new Thread(fourthRunnable);

    }

    public void addProcess(Process process){
    	roundRobinScheduler.addProcess(process);
    	for(Process child : process.getChildren()){
    	    addProcess(child);
        }
    }

    public void run(){
        try {

            if(firstRunnable.isDone()){
                one.interrupt();
            }
            else{
                one.run();
            }

            if(secondRunnable.isDone()){
                two.interrupt();
            }
            else{
                two.run();
            }

            if(thirdRunnable.isDone()){
                three.interrupt();
            }
            else{
                three.run();
            }

            if(fourthRunnable.isDone()){
                four.interrupt();
            }
            else{
                four.run();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkIfDone(){

	    return roundRobinScheduler.isEmpty() && firstRunnable.isDone() && secondRunnable.isDone() && thirdRunnable.isDone() && fourthRunnable.isDone();

    }
}
