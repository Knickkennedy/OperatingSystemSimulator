import java.util.Comparator;

public class PriorityComparator implements Comparator <Process> {

    @Override
    public int compare(Process p1, Process p2){
        if(p1.getProcessControlBlock().getPriority() < p2.getProcessControlBlock().getPriority())
            return -1;
        else if(p1.getProcessControlBlock().getPriority() > p2.getProcessControlBlock().getPriority())
            return 1;
        else
            return 0;
    }
}
