import java.io.*;
import java.util.Scanner;

public class Memory {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        int M; // Memory size
        int Eaddr=0; // Ending Address;

        System.out.println("");
        System.out.println("Welcome to our memory manager! to start");
        System.out.println("*Enter the number of memory partitions : ");
        M = input.nextInt(); // N: we might need to check those inputs too

        Partition[] memory = new Partition[M]; // N: Here is our Memory

        System.out.println("");

        System.out.println("*Enter memory allocation approach: First-fit (F), Best-fit (B), Worst-fit (W) ");
        char allocationStrategy = input.next().charAt(0);



        for (int i = 0; i < M; i++) {
            System.out.println("*Enter the size of partition " + (i+1) + " in KB:");
            int partitionSize = input.nextInt();

            memory[i] = new Partition(partitionSize);

            memory[i].setStartingAddress(Eaddr);
            Eaddr += partitionSize - 1; // -1
            memory[i].setEndingAddress(Eaddr);
            Eaddr++; // N: for the next partition to start from here.

        }

        boolean exit = false;
        while (!exit) {
            System.out.println("*Enter your choice :)\n" + "(1).Allocate memory\n" + "(2).Deallocate memory\n"
                    + "(3).Display memory information and write it to textFile (Report.txt)\n" + "(4).Exit");
            int choice = input.nextInt();

            switch (choice) {

                case 1:

                    System.out.println("Enter the process ID");
                    String PA_ID = input.next(); // Process allocate id

                    System.out.println("Enter the process size in KB");
                    int P_Size = input.nextInt();

                    Allocate(PA_ID, P_Size, allocationStrategy, memory); 

                    break;

                case 2:
                    System.out.println("Enter the process ID");
                    String PDA_ID = input.next(); // Process deallocate id

                    Deallocate(PDA_ID, memory);
                    break;

                case 3:
                    DisplayMemoryStatus(memory);
                    Memoryinfo(memory);
                    WriteTxtFile(memory);
                    break;

                case 4:
                    exit = true;

                    break;

                default:

                    System.out.println("Invalid choice! try again :(");

            }

        }
    }

    public static void Allocate(String ProcessID, int ProcessSize, char allocationStrategy, Partition[] memory) {

        int partitionAvailable = -1;
        int Internalfragmentation;
        int count =0;
        switch (allocationStrategy) {
            case 'F': case 'f':
                partitionAvailable = FirstFit(memory, ProcessSize);
                break;
            case 'B': case 'b':
                partitionAvailable = BestFit(memory, ProcessSize);
                break;
            case 'W': case 'w':
                partitionAvailable = WorstFit(memory, ProcessSize);
                break;
        }

        if (partitionAvailable < 0) { // N: no available partitions
            for(int i = 0; i < memory.length; i++){
                if(memory[i].getPartitionStatus().equals("allocated")){
                        count++;
                }
            }
                    if(count == memory.length){
                        System.out.println("Error: process could not be allocated due to not having enough memory!") ;
            return;}
            else{
                System.out.println("Error: Process size is largar than the available partitions! ") ;
                return;
            }

        }

        Partition selectedPartition = memory[partitionAvailable];
        selectedPartition.setProcessID(ProcessID);
        selectedPartition.setPartitionStatus("allocated");
        Internalfragmentation = selectedPartition.getPartitionSize() - ProcessSize;
        selectedPartition.setFragmentationSize(Internalfragmentation);
        DisplayMemoryStatus(memory);

    }

    public static int FirstFit(Partition[] partitions, int processSize) {

        // Find the first partition that can fit the process
        for (int i = 0; i < partitions.length; i++) {
            Partition MF = partitions[i];
            if (MF.getPartitionStatus().equals("free") && MF.getPartitionSize() >= processSize) {
                return i;
            }
        }

        // No partition can fit the process
        return -1;

    }

    public static int BestFit(Partition[] memory, int processSize) {

        // Find the best partition that can fit the process

        int bestFitIndex = -1;

        for (int i = 0; i < memory.length; i++) {

            if (memory[i].getPartitionStatus().equals("free") && memory[i].getPartitionSize() >= processSize) {

                if (bestFitIndex == -1) // The partition that fits it should be assigned for the first pass
                    bestFitIndex = i;

                    // If the current partition is less than the previously allocated one, check if
                    // it is smaller than the previous one
                else if (memory[i].getPartitionSize() < memory[bestFitIndex].getPartitionSize())
                    bestFitIndex = i;
            }
        }

        if (bestFitIndex != -1) {
            return bestFitIndex;
        } else {
            // No partition can fit the process
            return -1;
        }
    }

    public static int WorstFit(Partition[] memory, int processSize) {

        // Find the best partition that can fit the process

        int worstFitIndex = -1;

        for (int i = 0; i < memory.length; i++) {

            if (memory[i].getPartitionStatus().equals("free") && memory[i].getPartitionSize() >= processSize) {

                if (worstFitIndex == -1) // The partition that fits it should be assigned for the first pass
                    worstFitIndex = i;

                    // If the current partition is larger than the previously allocated one, check
                    // if it is larger than the previous one
                else if (memory[i].getPartitionSize() > memory[worstFitIndex].getPartitionSize())
                    worstFitIndex = i;
            }
        }
        // Allocate the memory to the process
        if (worstFitIndex != -1) {
            return worstFitIndex;
        } else {
            // No partition can fit the process
            return -1;
        }
    }

    public static void Deallocate(String ID, Partition[] M) {
        for (int i = 0; i < M.length; i++) {
            if (ID.equals(M[i].getProcessID())) {
                M[i].setProcessID("Null");
                M[i].setPartitionStatus("free");
                M[i].setFragmentationSize(-1);
                DisplayMemoryStatus(M);
                return;
            }

        }
        System.out.println("Unable to find process with this ID");
    }

    public static void DisplayMemoryStatus(Partition[] MS) {
        System.out.print("[ ");

        for (int i = 0; i < MS.length; i++) {

            if (MS[i].getPartitionStatus().equals("allocated")) {

                System.out.print(MS[i].getProcessID() + "| ");

            } else {
                System.out.print("H|  ");
            }

        }

        System.out.println("] ");

    }

    public static void Memoryinfo(Partition[] M) {


        for (int i = 0; i < M.length; i++) {

            System.out.println ("Partition Status: "+ M[i].getPartitionStatus() + "\n " + "Partition Size: " + M[i].getPartitionSize() + "KB" + "\n "+
                    "Starting Address: " + M[i].getStartingAddress() + "KB" + "\n " + "Ending Address: " + M[i].getEndingAddress() + "KB" + "\n " + "Process ID:  "+
                    M[i].getProcessID() + "\n" + "Internal Fragmentation Size: " + M[i].getFragmentationSize() +"KB"+ "\n "+"================================================================" );

        }
    }

    public static void WriteTxtFile(Partition[] WM) {
        try {
            File outFile = new File("Report.txt");
            FileOutputStream sf = new FileOutputStream(outFile);
            PrintWriter pf = new PrintWriter(sf);

            
            for (int i = 0; i < WM.length; i++) {
                pf.println ("Partition Status: "+ WM[i].getPartitionStatus() + "\n " + "Partition Size: " + WM[i].getPartitionSize() + "KB" + "\n "+
                        "Starting Address: " + WM[i].getStartingAddress() + "KB" + "\n " + "Ending Address: " + WM[i].getEndingAddress() + "KB" + "\n " + "Process ID:  "+
                                 WM[i].getProcessID() + "\n" + "Internal Fragmentation Size: " + WM[i].getFragmentationSize()+"KB" + "\n "+"================================================================" );
            }
            System.out.println("The file was written successfully!\n");

            pf.close();
        }

        catch (IOException e) {
            System.out.println("IO Excption ");
        }
    }

}