import java.util.Comparator;

public class SizeComparator implements Comparator<Process> {

    @Override
    public int compare(Process p1, Process p2){
        return Integer.compare(p1.getSize(), p2.getSize());
    }
}
