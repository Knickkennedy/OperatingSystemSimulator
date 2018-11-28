import java.util.ArrayList;

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

        // Main Memory consists of 512 4MB frames
        for(int i = 0; i < 512; i++){
            mainMemory.add(new Frame());
        }

        // Secondary Memory is four times as large as Main Memory
        for(int i = 0; i < 2048; i++){
            secondaryMemory.add(new Frame());
        }

        // Cache is 16MB
        for(int i = 0; i < 4; i++){
            cache.add(new Frame());
        }
    }

    public void demandPages(Process process){
    	int numberOfFramesRequired = process.getNumberOfFramesRequired();
		ArrayList<Integer> newFrameIndexes = new ArrayList<>();

		for(int i = 0; i < mainMemory.size(); i++){
			if(mainMemory.get(i).isFree()){
				newFrameIndexes.add(i);
				numberOfFramesRequired--;
			}

			if(numberOfFramesRequired == 0)
				break;
		}

		if(process.getNumberOfFramesRequired() == newFrameIndexes.size()){
			ArrayList<Integer> newFramesToUse = new ArrayList<>();

			for(Integer i : newFrameIndexes){
				newFramesToUse.add(i);
				mainMemory.get(i).setFree(false);
			}

			for(Integer i : process.getPages()){
				secondaryMemory.get(i).setFree(true);
			}

			process.getProcessControlBlock().setPages(newFramesToUse);
		}
		else{ // Free random frames for demand paging
			freeMainMemory(numberOfFramesRequired);
		}

    }

    public void freeMainMemory(int numberOfFramesRequired){

    }

    public void freeSecondaryMemory(int numberOfFramesRequired){

    }

    public void freeCachePage(int indexToFree){

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
