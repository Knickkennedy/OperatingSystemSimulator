import java.util.ArrayList;

@SuppressWarnings("Duplicates")
public class MemoryManagementUnit {
    private ArrayList<Frame> mainMemory;
    private ArrayList<Frame> secondaryMemory;
    private ArrayList<Frame> cache;
    private int[] registers;

    public MemoryManagementUnit(){
        this.mainMemory = new ArrayList<>();
        this.secondaryMemory = new ArrayList<>();
        this.cache = new ArrayList<>();

        registers = new int[8];

        // OperatingSystem Memory consists of 512 4MB frames
        for(int i = 0; i < 512; i++){
            mainMemory.add(new Frame());
        }

        // Secondary Memory is four times as large as OperatingSystem Memory
        for(int i = 0; i < 2048; i++){
            secondaryMemory.add(new Frame());
        }

        // Cache is 16MB
        for(int i = 0; i < 4; i++){
            cache.add(new Frame());
        }
    }

    public void removeFromCacheAndMainMemory(Process process){
        for(Integer i : process.getProcessControlBlock().getPages()){
            freeCachePage(i);
        }

        freeMainMemory(process.getProcessControlBlock().getPages());
    }

    public ArrayList<Integer> demandPages(Process process){
    	int numberOfFramesRequired = process.getNumberOfFramesRequired();
		ArrayList<Integer> indexesOfPossibleRemovals = new ArrayList<>();

		while(numberOfFramesRequired > 0){
		    if(cacheSpaceAvailable()){
		        numberOfFramesRequired = numberOfFramesRequired - addPageToCache();
            }
            else{
		        for(int i = 0; i < mainMemory.size(); i++){
		            if(mainMemory.get(i).isFree()){
		                indexesOfPossibleRemovals.add(i);
		                mainMemory.get(i).setReferenceBit(true);
		                numberOfFramesRequired--;
                    }
                    else if(!mainMemory.get(i).isReferenceBit()){
		                indexesOfPossibleRemovals.add(i);
		                mainMemory.get(i).setReferenceBit(true);
                    }
                    else{
		                mainMemory.get(i).setReferenceBit(false);
                    }

                }
            }
        }

        freeSecondaryMemory(indexesOfPossibleRemovals);
		return indexesOfPossibleRemovals;
    }

    public int addPageToCache(){
        int numberAdded = 0;
        for(int i = 0; i < cache.size(); i++){
            if(cache.get(i).isFree()){
                cache.get(i).setFree(false);
                numberAdded++;
            }
        }

        return numberAdded;
    }

    public boolean cacheSpaceAvailable(){
        boolean available = false;
        for(Frame frame : cache){
            if(frame.isFree()){
                available = true;
            }
        }

        return available;
    }

    public void freeMainMemory(ArrayList<Integer> indexesToFree){
        for(Integer i : indexesToFree){
            if(!mainMemory.get(i).isReferenceBit()){
                mainMemory.get(i).setReferenceBit(true);
            }
            else{
                mainMemory.get(i).setFree(true);
                mainMemory.get(i).setReferenceBit(false);
            }
        }
    }

    public void freeSecondaryMemory(ArrayList<Integer> indexesToFree){
        for(Integer i : indexesToFree){
            if(!secondaryMemory.get(i).isReferenceBit()){
                secondaryMemory.get(i).setReferenceBit(true);
            }
            else{
                secondaryMemory.get(i).setFree(true);
                secondaryMemory.get(i).setReferenceBit(false);
            }
        }
    }

    public void freeCachePage(int indexToFree){
        if(indexToFree < cache.size()){
            if(!cache.get(indexToFree).isReferenceBit()){
                cache.get(indexToFree).setReferenceBit(true);
            }
            else{
                cache.get(indexToFree).setFree(true);
                cache.get(indexToFree).setReferenceBit(false);
            }
        }
    }

    public boolean attemptToAddToMemory(Process process){
        int numberOfFramesRequired = process.getNumberOfFramesRequired();

        ArrayList<Integer> frameIndexes = new ArrayList<>();

        for(int i = 0; i < secondaryMemory.size(); i++){
            if(secondaryMemory.get(i).isFree()){
                frameIndexes.add(i);
                numberOfFramesRequired--;
            }

            if(numberOfFramesRequired == 0)
                break;
        }

        if(process.getNumberOfFramesRequired() == frameIndexes.size()){
            ArrayList<Integer> framesToUse = new ArrayList<>();

            for(Integer i : frameIndexes){
                framesToUse.add(i);
                secondaryMemory.get(i).setFree(false);
            }

            process.getProcessControlBlock().setPages(framesToUse);
            return true;
        }
        else{
            return false;
        }

    }
}
