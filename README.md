# Plagiarism Detection Program

#### Author: Harsh Rana
#### Date: October 10, 2018


*This program uses a dual input stream to calculate how similar two pieces of texts are, given a list of synonyms. It does so by comparing tuples in the same position between the two documents and cross-checking with the synonym list.*

### Assumptions and comments
This algorithm is design to compare identical pieces of text for similarities. This version of the solution decreases space complexity significantly by maintaining a running input stream and not storing either of the input files in memory to be able to process huge chucks of data. If the requirement was to consider all possible tuples (not just tuples in the same position), I would modify the program to store one of the two inputs in memory in a hashset, where the synonym file would be used to create different allowed versions of the same tuple and run all tuples in the other input through it, checking for copies. A Hashset would be used due to the fast retrieval (.contains()) method which has a time complexity of O(1).

### Time and Space Complexity
If there are 'n' words in the syns file, 'm' words in input1 and 'k' words in input2, this program has a time complexity of O(n + 3* (min(m, k)), thus linear time complexity. It has a space complexity of O(n) as well (discounting the temporary variables made, with the synonyms HashMap being the biggest store data).

### Inputs
This program accepts three required and one option parameters-
1. input 1 filename (required)
2. input 2 filename (required)
3. Synonyms list filename (required)
4. Size of tuples to compare (optional)
In order above.

### Running
1. Download and unzip the zip file
2. Open the command terminal and navigate to the directory where this file is located
3. Navigate to src/plagiarismdetector
4. Compile the .java file using the following command
    `javac PlagiarismDetector.java` (windows)
5. Use the command prompt to navigate back to the src folder using the following command-
    `cd ..` (windows)
6. Use the following command to run the program-
    `java plagiarismdetector.PlagiarismDetector input1.txt input2.txt syns.txt 3`
5. Edit the input1.txt, syns.txt and input2.txt as needed. Feel free to use the optional 4th parameter as required.

#### If you have any issues running the program, please contact me at hrana@wpi.edu
