import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BagIntersection {
    public static void main(String[] args)
    {
    	
    	// Initializing the Relation S as a block Structure containing blocks s1,s2,s3 
    	ArrayList<List<List<Integer>>> s = new ArrayList<List<List<Integer>>>(3);
    	
    	// Creating each sub block S1, S2 and S3 respectively which will have values in tuple form 
        ArrayList<List<Integer>> s1 = new ArrayList<List<Integer>>(3);
        ArrayList<List<Integer>> s2 = new ArrayList<List<Integer>>(3);
        ArrayList<List<Integer>> s3 = new ArrayList<List<Integer>>(3);
        
        // Adding 3 tuples in sub block S1
        s1.add(Arrays.asList(1, 1));
        s1.add(Arrays.asList(1, 1));
        s1.add(Arrays.asList(1, 1));

        // Adding 3 tuples in sub block S2
        s2.add(Arrays.asList(2, 2));
        s2.add(Arrays.asList(2, 2));
        s2.add(Arrays.asList(3, 3));

      
        // Adding 3 tuples in Sub block s3
        s3.add(Arrays.asList(4, 4));
        s3.add(Arrays.asList(5, 5));
        s3.add(Arrays.asList(6, 6));

        // Adding all the 3 sub blocks to Main block S
        s.add(s1);
        s.add(s2);
        s.add(s3);

        // Printing the block structure of S with its size
        System.out.println("Number of Blocks in  relation S = "+ s.size());
        System.out.println("Relation S  = "+ s);
	    System.out.println();
	    
	// Initializing the Memory for S
        Map<List<Integer>, Long> MemoryOfS = new HashMap<>();
        
        // Initializing the Relation R as a block Structure containing blocks r1,r2,r3,r4
        ArrayList<List<List<Integer>>> r = new ArrayList<List<List<Integer>>>(4);
        
        // Creating each sub block R1, R2, R3 and R4 respectively
        ArrayList<List<Integer>> r1 = new ArrayList<List<Integer>>(3);
        ArrayList<List<Integer>> r2 = new ArrayList<List<Integer>>(3);
        ArrayList<List<Integer>> r3 = new ArrayList<List<Integer>>(3);
        ArrayList<List<Integer>> r4 = new ArrayList<List<Integer>>(3);
        
       // Adding 3 tuples in sub block R1
        r1.add(Arrays.asList(1, 1));
        r1.add(Arrays.asList(1, 1));
        r1.add(Arrays.asList(1, 1));

       // Adding 3 tuples in sub block R2
        r2.add(Arrays.asList(2, 2));
        r2.add(Arrays.asList(11, 11));
        r2.add(Arrays.asList(12, 12));
        

       // Adding 3 tuples in sub block R3
        r3.add(Arrays.asList(3, 3));
        r3.add(Arrays.asList(4, 4));
        r3.add(Arrays.asList(5,5));

      // Adding 3 tuples in sub block R4
        r4.add(Arrays.asList(3, 3));
        r4.add(Arrays.asList(8, 8));
        r4.add(Arrays.asList(9, 9));

        
      // Adding all the 3 sub blocks to Main block R 
        r.add(r1);
        r.add(r2);
        r.add(r3);
        r.add(r4);

        System.out.println("Number of Blocks in Relation R = "+ r.size());
        System.out.println("Relation R  = "+ r);
        System.out.println();
	    
  
	    // Initializing Output Buffer 
        // Here assuming that output buffer has one block of size 3   
        // when output buffer is filled with 3 tuples, transfer that block to disk and empty the output buffer 
        ArrayList<List<Integer>> output = new ArrayList<List<Integer>>(3);
        
        // Initializing Disk 
        ArrayList<List<Integer>> disk = new ArrayList<List<Integer>>(3);
        
        // Initializing the memory for the R 
    	ArrayList<List<Integer>> MemoryOfR = new ArrayList<>(3);

        System.out.println("Now transferring the whole S in the Memory ");
        System.out.println("The tuples with its occurrences in S : ");
        
        // Loop will create a Memory Map for each tuple and store its occurrences
        for (int i = 0; i < s.size(); i++) 
        {
           
            Map<List<Integer>, Long> temp = s.get(i).stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            temp.forEach((k, v) -> MemoryOfS.merge(k, v, Long::sum));

        }
        
        // Loop for printing the distinct tuples with its occurrences  
        MemoryOfS.forEach((tuple, c) -> {
            System.out.println("count of tuple "+ tuple + " = " + c);
        });
        
        System.out.println();
        System.out.println("Now matching the tuples which is in Memory of R with the Memory of S and sending it to output buffer :");
        
        // Taking each block of R {R1,R2,R3 and R4 respectively}
        for (int i = 0; i < r.size(); i++) 
         {
        	
        	// Adding each block of R to the memory 
        	MemoryOfR.addAll(r.get(i));
  
	       // Getting each tuple from the block of Memory 
            MemoryOfR.forEach(t1 -> 
            {
            	// Condition if the tuple is present in memory or not
                if (MemoryOfS.containsKey(t1)) 
                {
                	// taking a count variable associated with each tuple 
                    Long c = MemoryOfS.get(t1);
                   
                    // loop for checking the count of the variable to be positive number or not
                    if (c > 0) 
                    {     
                    	   // Check for the condition of size of the output buffer 
                    	   if (output.size() >= 3)
                    	   { 
                    	  // If output is full then transfer the content to disk
                     	   disk.addAll(output);
                     	  System.out.println();
                    	   System.out.println("Now transferring the content of the output buffer to disk as  output buffer is full");
                           System.out.println("Disk Content  = "+ disk);
                           // Now as the output is transfered to the disk, reset the output buffer
                           output.clear();
                           System.out.println("At this point Output Buffer is empty, that is " + output);
                    	   }
                    	   
			// Add that tuple to the Output Buffer because count of that tuple is still a positive number 
                        // and output buffer is yet not full
                       output.add(t1); 
                       System.out.println("Output Buffer size = "+output.size());
                       System.out.println("Output Buffer Content = "+ output);
                       // Replacing the count of that tuple with count-1
                       MemoryOfS.replace(t1, c - 1);
                    }
                }
            });
            
            // Resetting the memory of R to take next block from R to be stored in it, as we are processing block by block 
            MemoryOfR.clear();
        }
  
        
     // At the end add everything from output to disk
        disk.addAll(output);
        System.out.println("Now transferring the content of the output buffer to disk as there are no tuples in R to be scanned " );
        System.out.println("Final Disk Content = "+ disk);
     // Now as the output is transfered to the disk, reset the output buffer
        output.clear();
    }
}

