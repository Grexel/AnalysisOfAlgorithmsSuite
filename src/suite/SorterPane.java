package suite;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Allows the user to sort single dimensional arrays of doubles and save to file
 * @author Jeff
 */
public class SorterPane extends BorderPane {
    Stage parentStage;
    FileChooser fileChooser = new FileChooser();

    Label lbData;
    Label lbSorter;
    Label lbSorted;
    TextArea taData;
    TextArea taSorter;
    TextArea taSorted;

    ToggleGroup tgSelectorGroup;
    RadioButton rbSaveData;
    RadioButton rbSaveSorter;
    RadioButton rbSaveSorted;
    RadioButton rbShowSort;

    Button btSelectionSort;
    Button btInsertionSort;
    Button btMergeSort;
    Button btQuickSort;
    Button btSaveData;
    Button btLoadData;

    /**
     * Sets up the Sorter Panel
     * @param parentStage the stage that is creating this Pane. necessary for the FileChooser
     */
    public SorterPane(Stage parentStage) {
        setupFileChooser();
        this.parentStage = parentStage;
        
        //Instantiate all components
        lbData = new Label("Data");
        lbSorter = new Label("Sorter Alg.");
        lbSorted = new Label("Sorted");
        lbData.setAlignment(Pos.CENTER);
        lbSorter.setAlignment(Pos.CENTER);
        lbData.setAlignment(Pos.CENTER);
        taData = new TextArea();
        taSorter = new TextArea();
        taSorted = new TextArea();
        taSorter.setPrefColumnCount(20);
        taSorted.setPrefColumnCount(20);
        taData.setPrefColumnCount(20);
        taSorter.setEditable(false);
        taSorted.setEditable(false);

        tgSelectorGroup = new ToggleGroup();
        rbSaveData = new RadioButton("Save Data");
        rbSaveSorter = new RadioButton("Save Sorter");
        rbSaveSorted = new RadioButton("Save Sorted");
        rbShowSort = new RadioButton("Show Sorting");
        rbShowSort.setSelected(false);
        
        rbSaveData.setToggleGroup(tgSelectorGroup);
        rbSaveSorter.setToggleGroup(tgSelectorGroup);
        rbSaveSorted.setToggleGroup(tgSelectorGroup);
        rbSaveSorted.setSelected(true);

        btSelectionSort = new Button("Selection sort");
        btInsertionSort = new Button("Insertion sort");
        btMergeSort = new Button("Merge Sort");
        btQuickSort = new Button("Quick Sort");
        btSaveData = new Button("Save to File");
        btLoadData = new Button("Load Data from File");
        btSelectionSort.setOnAction(event -> selectionSort());
        btInsertionSort.setOnAction(event -> insertionSort());
        btMergeSort.setOnAction(event -> mergeSort());
        btQuickSort.setOnAction(event -> quickSort());
        btSaveData.setOnAction(event -> saveToFile());
        btLoadData.setOnAction(event -> loadFromFile());
        
        //Add all components to Panes
        HBox btPane = new HBox(5);
        btPane.getChildren().addAll(btSelectionSort, btInsertionSort, btMergeSort, btQuickSort);
        btPane.setPadding(new Insets(10,10,10,10));
        HBox filePane = new HBox(5);
        filePane.getChildren().addAll(btSaveData,btLoadData);
        filePane.setPadding(new Insets(10,10,10,10));
        btPane.setAlignment(Pos.CENTER);
        filePane.setAlignment(Pos.CENTER);
        HBox sorterPane = new HBox(10);
        sorterPane.getChildren().addAll(lbSorter,rbShowSort);
        
        BorderPane bpData = new BorderPane();
        bpData.setTop(lbData);
        bpData.setCenter(taData);
        bpData.setBottom(rbSaveData);
        BorderPane bpSorter = new BorderPane();
        bpSorter.setTop(sorterPane);
        bpSorter.setCenter(taSorter);
        bpSorter.setBottom(rbSaveSorter);
        BorderPane bpSorted = new BorderPane();
        bpSorted.setTop(lbSorted);
        bpSorted.setCenter(taSorted);
        bpSorted.setBottom(rbSaveSorted);
        
        BorderPane bpInformation = new BorderPane();
        BorderPane.setMargin(bpData, new Insets(10,10,10,10));
        BorderPane.setMargin(bpSorter, new Insets(10,10,10,10));
        BorderPane.setMargin(bpSorted, new Insets(10,10,10,10));
        bpInformation.setLeft(bpData);
        bpInformation.setCenter(bpSorter);
        bpInformation.setRight(bpSorted);
        this.setCenter(bpInformation);
        this.setTop(btPane);
        this.setBottom(filePane);
    }

    /**
     * Runs selection sort on the values in the Data TextArea. Writes each 
     * iteration to the Sorter TextArea if desired.
     */
    private void selectionSort() {
        //Convert text from the data text area to an array
        double[] dataArray = getData();
        boolean showSort = rbShowSort.isSelected();
        taSorter.clear();
        if(showSort){
            taSorter.appendText(arrayToString(dataArray));
        }
        
        //Sort array, outputting each step to the sorter text area
        int minimumIndex;
        double holder;
        for(int i = 0; i < dataArray.length -1; i++){
            minimumIndex = i;
            //search for next minimum value
            for(int j = i+1; j < dataArray.length; j++){
                if(dataArray[j] < dataArray[minimumIndex]){
                    minimumIndex = j;
                }
            }
            //swap minimum with current i
            holder = dataArray[i];
            dataArray[i] = dataArray[minimumIndex];
            dataArray[minimumIndex] = holder;
            if(showSort) {taSorter.appendText(arrayToString(dataArray));}
        }
        
        //output dataArray to sorted textarea
        outputSortedData(dataArray);
    }
    
    /**
     * Runs insertion sort on the values in the Data TextArea. Writes each 
     * iteration to the Sorter TextArea if desired.
     */
    private void insertionSort(){
        //Convert text from the data text area to an array
        double[] dataArray = getData();
        boolean showSort = rbShowSort.isSelected();
        taSorter.clear();
        if(showSort){
            taSorter.appendText(arrayToString(dataArray));
        }
        
        double holder;
        int index;
        for(int i = 0; i < dataArray.length; i++){
            index = i;
            while(index > 0){
                //if the right side is smaller than the left side
                //swap and move index down by 1
                if(dataArray[index] < dataArray[index-1]){
                    holder = dataArray[index -1];
                    dataArray[index - 1] = dataArray[index];
                    dataArray[index] = holder;
                }
                //otherwise array[0...i] is sorted
                else{
                    break;
                }
                index--;
            }
            if(showSort) {taSorter.appendText(arrayToString(dataArray));}
        }
        
        //output dataArray to sorted textarea
        outputSortedData(dataArray);
    }
    public void mergeSort(){
        taSorter.clear();
        double[] array = getData();
        // need a temportary array to sort into 
        // and a holder for swapping array with temp;
        double[] tempArray = new double[array.length];
        double[] tempHolder;
        
        // Interval = how big the left and right side are. 
        int interval = 1;           //start with single elements on a side
        int nextPlacementIndex = 0; // maintains index for merging placement
        
        // merge until a side is greater than the array at which point the
        // entire array is sorted
        while(interval < array.length){
            if(rbShowSort.isSelected()){
                taSorter.appendText(arrayToString(array));
            }
            
            //merge every 2 intervals together in array
            //for example, interval of 1 = merge 0 and 1, then 2 and 3.
            for(int i = 0; i < array.length; i+= interval*2){
                
                //set index for beginning of left and right side
                //calculate the middle and end, only really needed for the end
                int indexI = i;
                int indexJ = i + interval;
                
                //if we are at the end of the array, place rest of elements in
                //temp and break to go to next interval
                if(indexI >= array.length){
                    break;
                }
                if(indexJ >= array.length){
                    while(indexI < array.length){
                        tempArray[nextPlacementIndex] = array[indexI];
                        nextPlacementIndex++;
                        indexI++;
                    }
                    break;
                }
                
                //checks if middle or end is outside of the array
                int middle = (indexI + interval > array.length) 
                        ? array.length : indexI + interval;
                int end = (indexJ + interval > array.length) 
                        ? array.length : indexJ + interval;
                
                
                //merge left and right side into temp;
                boolean keepMerging = true;
                while(keepMerging){
                    //i is smaller or equal to, always take same values 
                    //from the left to ensure stability
                    if(array[indexI] <= array[indexJ]){
                        tempArray[nextPlacementIndex] = array[indexI];
                        indexI++;
                    }
                    
                    //j is smaller
                    else{
                        tempArray[nextPlacementIndex] = array[indexJ];
                        indexJ++;
                    }
                    nextPlacementIndex++;
                    
                    //check if i is empty, place remaining j into temp
                    if(indexI == middle){
                        while(indexJ < end){
                            tempArray[nextPlacementIndex] = array[indexJ];
                            nextPlacementIndex++;
                            indexJ++;
                        }
                        keepMerging = false;
                    }
                    //check if j is empty, place remaining i into temp
                    if(indexJ == end){
                        while(indexI < middle){
                            tempArray[nextPlacementIndex] = array[indexI];
                            nextPlacementIndex++;
                            indexI++;
                        }
                        keepMerging = false;
                    }
                }//done merging two intervals together
            }//done merging the array at an interval size
            
            //flip temp array into array
            tempHolder = tempArray;
            tempArray = array;
            array = tempHolder;
            
            //reset for next interval size
            nextPlacementIndex = 0; 
            //increase interval size by a factor of 2 for merging
            interval *= 2;
        }
        outputSortedData(array);
    } 
    
    public void quickSort(){
        taSorter.clear();
        double[] array = getData();
        
        quickSortHelper(array,0,array.length-1);
        
        outputSortedData(array);
    }
    public void quickSortHelper(double[] array, int L, int R){
        if(rbShowSort.isSelected()){
            taSorter.appendText(arrayToString(array));
        }
        
        if(L < R){
            int split = partition(array,L,R);
            quickSortHelper(array,L,split-1);
            quickSortHelper(array,split+1,R);
        }
    }
    public int partition(double[] array, int L, int R){
        //midpoint split
        int C = (R + L) / 2;
        if(array[L] < array[R]){ 
            //R Middle
            if(array[R] < array[C]){swap(array,L,R);}
            //C Middle
            else if(array[C] > array[L]){swap(array,L,C);}
        }
        // RVal < LVal
        else{
            // R Middle
            if(array[C] < array[R]){swap(array,L,R);}
            // C Middle
            else if(array[C] < array[L]){swap(array,L,C);}
        }
        //done optimizing pivot
        
        //begin partitioning
        int pivot = L;
        int i = L;
        int j = R+1;
        
        do{
            //look for first value that is greater than the pivot 
            // starting from the left
            do{
                i++;
            }while(array[i] < array[pivot] && i < R);
            
            // look for first value that is less than the pivot 
            // starting from the right
            do{
                j--;
            }while(array[j] > array[pivot] && j > L);
            //swap misplaced values
            swap(array,i,j);
        }while(i < j);
        //undo last swap that broke the loop
        swap(array,i,j);
        swap(array,pivot,j);// move pivot into the middle
        return j; //return position pivot is at
    }
    public static void swap(double[] array, int index1, int index2){
        double holder = array[index1];
        array[index1] = array[index2];
        array[index2] = holder;
    }
    
    /**
     * Scans the Data TextArea to create an array of doubles used for sorting.
     * Passes over any strings that are unable to converted to a double.
     * @return an array of doubles 
     */
    private double[] getData(){
        //Convert text from the data text area to an array
        ArrayList<Double> doubleList = new ArrayList<>();
        Scanner sc = new Scanner(taData.getText());
        while(sc.hasNextLine()){
            try{
                doubleList.add(Double.parseDouble(sc.nextLine()));
            }
            catch(Exception e){
                System.out.println("Wasn't a double, carry on");
            }
        }
        return doubleList.stream().mapToDouble(d->d).toArray();
    }
    
    /**
     * Writes the array to the Sorted TextArea
     * @param dataArray an array that has been sorted.
     */
    private void outputSortedData(double[] dataArray){
        taSorted.clear();
        for(double d : dataArray){
            taSorted.appendText(Double.toString(d) + "\n");
        }
        
        taSorted.deletePreviousChar();
    }
    
    /**
     * Help function for the sorter TextArea. Concatenates each value in the
     * array to a string, separated by a space, then adds a newline before
     * returning the generated string.
     * @param array an array of doubles.
     * @return a string representing the array
     */
    private String arrayToString(double[] array){
        StringBuilder sb = new StringBuilder();
        for(double d : array){
            sb.append(d).append(" ");
        }
        sb.delete(sb.length()-1, sb.length());
        sb.append("\n");
        return sb.toString();
    }
    
    /**
     * Saves the selected TextArea to a file chosen by the user
     */
    private void saveToFile() {
        System.out.println("saving file");
        File f = fileChooser.showSaveDialog(parentStage);
        if(rbSaveData.isSelected()){
            writeTextAreaToFile(taData, f);
        }
        else if(rbSaveSorter.isSelected()){
            writeTextAreaToFile(taSorter, f);
        }
        else if(rbSaveSorted.isSelected()){
            writeTextAreaToFile(taSorted, f);
        }
    }
    
    /**
     * Loads values into the Data TextArea from a file selected by the user
     */
    private void loadFromFile() {
        System.out.println("loading file");
        File f = fileChooser.showOpenDialog(parentStage);
        try{
            Scanner sc = new Scanner(f);
            taData.clear();
            while(sc.hasNextLine()){
                taData.appendText(sc.nextLine() + "\n");
            }
            taData.deletePreviousChar();//delete extra newline character
        }catch(Exception e){
            System.out.println("Could not find the file");
        }
    }
    
    /**
     * Outputs the TextArea to a file
     * @param ta the TextArea to save
     * @param f the file to save the TextArea to
     */
    public void writeTextAreaToFile(TextArea ta, File f){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(f))){
            Scanner sc = new Scanner(ta.getText());
            while(sc.hasNextLine()){
                bw.append(sc.nextLine() + "\n");
            }
        }catch(Exception e){
            System.out.println("Writer failed to initialize");
        }
    }
    
    /**
     * Initializes the FileChoosers starting directory and allowable file
     * formats.
     */
    public void setupFileChooser(){
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.dir"))
        ); 
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Data", "*.dat"),
            new FileChooser.ExtensionFilter("Text", "*.txt"),
            new FileChooser.ExtensionFilter("All", "*.*")
        );        
    }
}
