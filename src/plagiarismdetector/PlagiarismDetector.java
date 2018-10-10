/*
 * Program that detects the percentage of plagiarism
 * given two input files and a file full of synonyms using
 * a custom n-tupple comparison alogorithm
 * 
 */

package plagiarismdetector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Harsh Rana
 * @date October 10, 2018
 */
public class PlagiarismDetector {
    
    // Turn debug mode to true to print
    // important variables in different parts of the program
    public static Boolean debugMode = false;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException{
        // Validate input
        if (args.length < 3 || args.length > 4) {
            System.out.println("Error -- Please provide a minimum of 3 (max 4) arguments : filename #1, filename #2, synonyms filename. Note: Argument 4 is optional and can be used to provide the size of tuples to be checked for plagiarism (default- 3)");
            return;
        }
        // Store synonyms in a hashmap 
        // with each word as a key, and all its synonyms as values.
        // Includes repeats for easier querying
        HashMap<String, List<String>> synonymMap = getSynonyms(args[2]);
        
        
        Integer tupLength;
        if(args.length == 4){
            tupLength = Integer.parseInt(args[3]);
        } else {
            tupLength = 3;
        }
        
        // Get number of repeated and total tuples in the input files (as a hashmap)
        HashMap<String, Integer> tupleInfo = countSameTuples(args[0], args[1], synonymMap, tupLength);
        Integer sameTuples = tupleInfo.get("same");
        Integer totalTuples = tupleInfo.get("all");
        
        // Math to calculate % plagiarism
        DecimalFormat df = new DecimalFormat("##.##");
        Float percentPlagiarised = ((float)sameTuples/totalTuples) * 100;
        System.out.println("The plagiarism percentage between the two texts was: " + df.format(percentPlagiarised) + "%");
        
        if (debugMode){
            System.out.println(sameTuples);
            System.out.println(totalTuples); 
        }
    }
    
    /**
     *
     * @param fileName1 the name of the first input file (args[0])
     * @param fileName2 the name of the second input file (args[1])
     * @param synonyms the name of the file containing synonyms (args[2])
     * @param num length of tuples to compare at once.
     * @return HashMap with "same" and "all" as keys and integer count of 
     *          same tuples and all tuples in the input files
     * @throws IOException
     */
    public static HashMap<String, Integer> countSameTuples(String fileName1, String fileName2, HashMap<String, List<String>> synonyms, Integer num) throws IOException{
        
        // Streams and Readers for processing input files 
        InputStream is1 = PlagiarismDetector.class.getResourceAsStream(fileName1);
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(is1));
        InputStream is2 = PlagiarismDetector.class.getResourceAsStream(fileName2);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(is2));
        
        String line1;
        String line2;
        
        Integer numComparison = 1;
        Integer sameTuples = 0;
        Integer allTuples = 0;
        
        while (((line1 = reader1.readLine()) != null) && ((line2 = reader2.readLine()) != null)) {
            // Account for an empty line ie: change in paragraph etc.
            // Consecutive lines (even on different rows) with no empty lines
            // in the middle will be considered "continuous". 
            if(line1.length() == 0 || line2.length() == 0){
                numComparison = 1;
                continue;
            }
            
            List<String> words1 = Arrays.asList(line1.split(" "));
            List<String> words2 = Arrays.asList(line2.split(" "));
            
            Iterator<String> iterator1 = words1.iterator();
            Iterator<String> iterator2 = words2.iterator();
            while (iterator1.hasNext() && iterator2.hasNext()){
                allTuples++;
                
                if(isSame(iterator1.next(), iterator2.next(), synonyms)){
                    if (numComparison < num){
                        numComparison ++;
                    } else{
                        sameTuples++;
                    }
                } else{
                    numComparison = 1;
                }
            }
            allTuples = allTuples - num + 1;
            if (debugMode){
                System.out.println(allTuples + " ");
            }
            
        }
        reader1.close();
        reader2.close();
        
        HashMap<String, Integer> tupleInfo = new HashMap<>();
        tupleInfo.put("same", sameTuples);
        tupleInfo.put("all", allTuples);
        return tupleInfo;
    }
    
    /**
     *
     * @param fileName the name of the file containing synonyms (args[2])
     * @return Hashmap with every word that appears in the file as a key
     *          and all of its synonyms as values (a list)
     * @throws IOException
     */
    public static HashMap<String, List<String>> getSynonyms(String fileName) throws IOException{
        InputStream is = PlagiarismDetector.class.getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        
        HashMap<String, List<String>> input= new HashMap<>();
        String line;
        
        // Iterate through the entire file and store every word as a key
        // and all synonyms of the word as its values. 
        // Note: Synonyms are on the same line
        while ((line = reader.readLine()) != null) {
            List<String> words = Arrays.asList(line.split(" "));
            for (String word: words){
                input.put(word, words);
            }
        }
        if (debugMode){
            System.out.println(input.toString());
        }
        reader.close();
        
        return input;
    }
    
    /**
     *
     * @param input1 first string to compare
     * @param input2 second string to compare
     * @param synonyms Hashmap of synonyms 
     * @return Boolean as true if the words are equal or synonyms of each other
     *                  false if otherwise
     */
    public static Boolean isSame(String input1, String input2, HashMap<String, List<String>> synonyms){
        
        // Micro optimization- If the words are same, return true. 
        if(input1.equals(input2)){
            return true;
        }
        
        if(synonyms.containsKey(input1)){
            return(synonyms.get(input1).contains(input2));
            
        } else if (synonyms.containsKey(input2)){
            return (synonyms.get(input2).contains(input1));
            
        } else{
            return false;
        }
    }
}
        

