import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ProcessScheduler processScheduler = new ProcessScheduler();

        // Generate Processes

        // Set start time to 0
        int currentCPUtime = 0;
        int timeQuantum = 25;

        // Start CPU Loop
        while(true){
            // grab process from ready queue if it's not empty
            Process readyProcess = processScheduler.getReadyQueue().poll();

            // grab process from waiting queue if it's not empty
            Process waitingProcess = processScheduler.getWaitQueue().poll();

            int timer = 0;

            while(timer < timeQuantum){

                // process ready process
                if (readyProcess != null) {
                    readyProcess.setRunningState();
                    readyProcess.run();
                }

                // process waiting process
                if(waitingProcess != null){
                    waitingProcess.run();
                }

                // update ready process state
                // update waiting process state

                // compare time quantum and determine if you move ready process

                // move processes based on time quantum and new states

                // increment time
                timer++;
                currentCPUtime++;
            }
        }
    }
}
