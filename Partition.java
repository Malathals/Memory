public class Partition {
    public String partitionStatus;
    public int PartitionSize;
    public int StartingAddress;
    public int EndingAddress;
    public String processID;
    public int fragmentationSize;



    public Partition(int Size){

        partitionStatus="free";
        PartitionSize= Size;
       StartingAddress= 0;
         EndingAddress=0;
        processID="Null";
        fragmentationSize=-1;

    }


    public int getEndingAddress() {
        return EndingAddress;
    }

    public String getPartitionStatus() {
        return partitionStatus;
    }

    public int getPartitionSize() {
        return PartitionSize;
    }

    public String getProcessID() {
        return processID;
    }

    public int getStartingAddress() {
        return StartingAddress;
    }

    public int getFragmentationSize() {
        return fragmentationSize;
    }

    public void setPartitionStatus(String partitionStatus) {
        this.partitionStatus = partitionStatus;
    }

    public void setStartingAddress(int startingAddress) {
        StartingAddress = startingAddress;
    }

    public void setPartitionSize(int partitionSize) {
        PartitionSize = partitionSize;
    }

    public void setEndingAddress(int endingAddress) {
        EndingAddress = endingAddress;
    }

    public void setFragmentationSize(int fragmentationSize) {
        this.fragmentationSize = fragmentationSize;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }
}