import java.util.Comparator;

public class PriorityComparator implements Comparator <Process> {

    @Override
    public int compare(Process p1, Process p2){
        return Integer.compare(p1.getProcessControlBlock().getPriority(), p2.getProcessControlBlock().getPriority());
    }
}
