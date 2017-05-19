package suite;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Allows the user to solve a Traveling Sales Person problem.
 * @author Jeff
 */
public class TSPPane extends HBox {

    FileChooser fileChooser = new FileChooser();
    Stage parentStage;

    Label lbMatrix;
    Label lbCalculations;
    
    TextArea taMatrix;
    TextArea taCalculations;
    
    Button btLoadMatrix;
    Button btCalculateTSP;

    /**
     * Sets up the Traveling Sales Person Problem panel
     * @param parentStage the stage that is creating this Pane. necessary for the FileChooser
     */
    public TSPPane(Stage parentStage) {
        setupFileChooser();
        this.parentStage = parentStage;

        //instantiation of components
        lbMatrix = new Label("Matrix");
        lbCalculations = new Label("Calculations");
        taMatrix = new TextArea();
        taCalculations = new TextArea();
        btLoadMatrix = new Button("Load Matrix");
        btLoadMatrix.setOnAction(event -> loadMatrix());
        btCalculateTSP = new Button("Calculate TSP");
        btCalculateTSP.setOnAction(event -> solveTSP());

        //putting components on panels
        VBox matrixBox = new VBox(5);
        matrixBox.getChildren().addAll(lbMatrix, taMatrix, btLoadMatrix);
        matrixBox.setPadding(new Insets(10,10,10,10));
        VBox tspBox = new VBox(5);
        tspBox.getChildren().addAll(lbCalculations, taCalculations, btCalculateTSP);
        tspBox.setPadding(new Insets(10,10,10,10));
        
        this.setPadding(new Insets(10,10,10,10));
        this.getChildren().addAll(matrixBox, tspBox);
    }
    
    /**
     * Runs the lexicographic permutation algorithm on the input array
     * Works in place, so output is unnecessary
     * @param array an array of integers
     * @return the next lexicographic permutation of the input array
     */
    public static int[] lexicographicPermutation(int[] array) {
        int i;
        int j;

        for (i = array.length - 2; i >= 0; i--) {
            if (array[i] < array[i+1]) {
                break;
            }
        }//got biggest index i where ai < ai+1

        for (j = array.length - 1; j > i; j--) {
            if (array[i] < array[j]) {
                break;
            }
        }//got biggest index j where ai < aj

        //return original array if max permutation
        if (array[j] < array[i]) {
            return array;
        }

        //swap ai and aj
        swap(array,i,j);

        //reverse ai+1 to an
        //swap ai+1 and an;
        //swap ai+2 and an-1, etc, etc
        for (int a = i + 1, n = array.length - 1; a < n; a++, n--) {
            swap(array,a,n);
        }
        return array;
    }
    
    /**
     * Finds the vertex that breaks the minimum cost and cuts all 
     * sub permutations off by calling branchAndBound with that vertex
     * @param matrix cost matrix of the TSP problem
     * @param lexico tour through the vertices
     * @param minCost value of the current best tour.
     */
    public void branch(int[][] matrix, int[] lexico, int minCost){
        //calculate index where minCost is surpassed
        int minIndex = 0;
        int cost = 0;
        for(int i = 0; i < lexico.length-1; i++){
             minIndex = i+1;
             cost += matrix[lexico[i]][lexico[i+1]];
             if(cost > minCost){
                 break;
             }
        }
        branchAndBound(lexico, minIndex);
        
    }
    /**
     * Cuts the sub permutations off of a lexicograph at the given index
     * @param lexico current circuit
     * @param index the vertex that causes the minimum cost to be surpassed.
     */
    public void branchAndBound(int[] lexico, int index){
        //sort the rest of the array into descending to begin the next branch
        //insertion sort for faster sorting than selection/bubble
        int mindex;
        for(int sortI = index+2; sortI < lexico.length; sortI++){
            mindex = sortI;
            while( mindex > index+1){
                if(lexico[mindex] > lexico[mindex -1]){
                    swap(lexico,mindex,mindex-1);
                    mindex--;
                }
                else break;
            }
        }
        lexicographicPermutation(lexico);
    }
    /**
     * Swaps to values inside of an array
     * @param array array to swap values
     * @param index1 first index to swap
     * @param index2 second index to swap
     */
    public static void swap(int[] array, int index1, int index2){
        int holder = array[index1];
        array[index1] = array[index2];
        array[index2] = holder;
    }
    
 
    /**
     * Solves the TSP problem and outputs to the taCalculateTSP text area.
     * First loads the matrix from the taMatrix text area
     * Then initializes the lexicograph to the start.
     * Then permutates the circuit given by the lexicograph until the first
     * node is changed. At this point every possible circuit has been gone
     * through and there is no need to keep going.
     */
    private void solveTSP() {
        long timeStart = System.currentTimeMillis();
        taCalculations.clear();
        int[][] matrix = formatMatrix();
        
        //initialize starting lexicograph to feed generator
        //along with approximation of best circuit using a greedy algorithm.
        
        int[] lexico = new int[matrix.length];
        
        int minCost = Integer.MAX_VALUE;
        int[] minCircuit = new int[lexico.length];
        
        for(int i = 0; i < lexico.length; i++){
            lexico[i] = i;
            minCircuit[i] = i;
        }
        
        //get greedy circuit;
        minCost = greedyCircuit(matrix, minCircuit);
        taCalculations.appendText("Greedy Circuit\n");
        outputCircuit(minCircuit, minCost);
        
        do{
            if(calculateCircuit(matrix,lexico, minCost)){
                minCost = getCost(matrix, lexico);
                copyArray(minCircuit, lexico);
            }
            
            //prune lexico if tour gets bigger than minCost
            branch(matrix,lexico,minCost);
            
            //run through each permutation
            //lexicographicPermutation(lexico);
        }
        while(lexico[0] == 0); // only go through the first n-1 iterations
        
        long secondsElapsed = (System.currentTimeMillis() - timeStart) / 1000;
        taCalculations.appendText("Optimal Circuit\n");
        outputCircuit(minCircuit, minCost);
        taCalculations.appendText("Time elapsed: " + secondsElapsed);
        
    }
    
    /**
     * Test's whether a circuit is smaller than the given minimum cost.
     * Breaks early if the cost goes over for increased speed.
     * 
     * @param matrix cost matrix for moving from one node to the other
     * @param lexico current circuit being checked for its cost
     * @param minCost currently lowest cost for a circuit found
     * @return true if the circuit is better, false is worse.
     */
    private boolean calculateCircuit(int[][] matrix, int[] lexico, int minCost) {
        int cost = 0;
        for(int i = 0; i < lexico.length-1; i++){
            cost += matrix[lexico[i]][lexico[i+1]];
            if(cost > minCost){
                return false;
            }
        }
        cost += matrix[lexico[lexico.length-1]][lexico[0]];
        return cost < minCost;
    }
    
    /**
     * Returns the cost of the lexicograph
     * @param matrix cost matrix
     * @param lexico current lexicographic permutation
     * @return the cost of the given lexicograph
     */
    private int getCost(int[][] matrix, int[] lexico) {
        int cost = 0;
        for(int i = 0; i < lexico.length-1; i++){
            cost += matrix[lexico[i]][lexico[i+1]];
        }
        cost += matrix[lexico[lexico.length-1]][lexico[0]];
        return cost;
    }
    
    /**
     * Calculates a greedy tour using the best available vertex.
     * Searches through a tour beginning at each vertex.
     * Works on lexico in place.
     * @param matrix The cost matrix of the problem.
     * @param lexico the array to save the greedy tour to.
     * @return the cost of the greedy tour
     */
    private int greedyCircuit(int[][] matrix, int[] lexico){
        int[] greedyLexico = new int[lexico.length];
        copyArray(greedyLexico,lexico);
        int greedyCost = Integer.MAX_VALUE;
        
        //Start from each vertex
        for(int start = 0; start < greedyLexico.length; start++){
            greedyLexico[0] = start;
            
            //calculate greedy tour
            for(int i = 1; i < greedyLexico.length; i++){
                int previousIndex = greedyLexico[i-1];
                int minIndex = -1;
                int minValue = Integer.MAX_VALUE;
                for(int j = 0; j < matrix.length; j++){
                    System.out.println("I: " + i + " J: " + j);
                    System.out.println("Value: " + matrix[j][i]);
                    if(matrix[j][previousIndex] < minValue && previousIndex != j){
                        boolean isUsed = false;
                        for(int k = 0; k < i; k++){
                            if(greedyLexico[k] == j){
                                isUsed = true;
                            }
                        }
                        if(!isUsed){
                            minIndex = j;
                            minValue = matrix[j][previousIndex];
                        }
                        else{
                            System.out.println("j used though");
                        }
                    }
                }
                if(minIndex == -1){
                    //find available node
                    boolean isUsed = false;
                    for(int j = 0; j < matrix.length; j++){
                        for(int k = 0; k < i; k++){
                            if(greedyLexico[k] == j){
                                isUsed = true;
                            }
                        }
                        if(!isUsed){
                            minIndex = j;
                        }
                    }
                }
                greedyLexico[i] = minIndex;
            }//done calculating greedy tour
            
            //check if this greedy tour is the best
            //if so, save to lexico.
            if(getCost(matrix,greedyLexico) < greedyCost){
                copyArray(lexico, greedyLexico);
                greedyCost = getCost(matrix, greedyLexico);
            }
        }
        
        return greedyCost;
    }
   
    /**
     * Displays the best circuit on taCalculations after the problem is solved
     * @param lexico best circuit to travel through
     * @param minCost lexico's cost
     */
    private void outputCircuit(int[] lexico, int minCost){
        taCalculations.appendText("min cost: "+ minCost + "  Circuit: ");
        for(int i = 0; i < lexico.length; i++){
            String label = Character.toString((char)('A' + lexico[i]));
            taCalculations.appendText(label + ", ");
        }
        taCalculations.appendText(Character.toString((char)('A' + lexico[0])));
        taCalculations.appendText("\n");
    }
    
    /**
     * Copy the values from one array into another. Assumed that both arrays are
     * the same size
     * @param destination array to copy into
     * @param source array to copy from
     */
    private void copyArray(int[] destination, int[] source){
        for(int i = 0; i < destination.length; i++){
            destination[i] = source[i];
        }
    }
    
    /**
     * Initializes the FileChoosers starting directory and allowable file
     * formats.
     */
    public void setupFileChooser() {
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.dir"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text", "*.txt"),
                new FileChooser.ExtensionFilter("Data", "*.dat"),
                new FileChooser.ExtensionFilter("All", "*.*")
        );
    } 
    
    /**
     * Opens a file chooser dialog to choose the cost matrix for the TSPSolver
     * This outputs the files contents to taMatrix
     */
    private void loadMatrix() {
        System.out.println("opening file");
        File f = fileChooser.showOpenDialog(parentStage);
        try (Scanner sc = new Scanner(f)) {
            taMatrix.clear();
            while(sc.hasNextLine()){
                taMatrix.appendText(sc.nextLine());
                if(sc.hasNextLine()){
                    taMatrix.appendText("\n");
                }
            }
        } catch (Exception e) {
            System.out.println("Scanner failed to initialize when loading a matrix");
        }
    }
    
    /**
     * Parses the data in taMatrix to create a 2 dimensional cost matrix for
     * the TSP solver to use.
     * @return cost matrix corresponding to taMatrix's text area
     */
    private int[][] formatMatrix(){
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        Scanner sc = new Scanner(taMatrix.getText());
        while(sc.hasNextLine()){
            ArrayList<Integer> values = new ArrayList<Integer>();
            String line = sc.nextLine();
            String[] variables = line.split(",");
            for(int i = 0; i < variables.length; i++){
                variables[i] = variables[i].trim();
                int val;
                try{
                    val = Integer.parseInt(variables[i]);
                    values.add(val);
                }catch(NumberFormatException nfe){
                    taCalculations.setText("Could not parse int from matrix");
                }
            }
            matrix.add(values);
        }
        int[][] array2d = new int[matrix.size()][matrix.get(0).size()];
        for(int i = 0; i < array2d.length; i++){
            for(int j = 0; j < array2d[0].length; j++){
                array2d[i][j] = matrix.get(i).get(j);
            }
        }
        return array2d;
    }
}
