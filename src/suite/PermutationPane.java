package suite;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Allows the user to permutate a list.
 * @author Jeff
 */
public class PermutationPane extends BorderPane {

    FileChooser fileChooser = new FileChooser();
    Stage parentStage;

    Label lbPermutationStart;
    Label lbNumberOfPermutations;
    Label lbGeneratedPermutations;
    TextField tfStartingPermutation;
    TextField tfNumberOfPermutations;
    TextArea taGeneratedPermutations;
    Button btGeneratePermutations;
    Button btSaveData;

    /**
     * Sets up the Permutation panel
     * @param parentStage the stage that is creating this Pane. necessary for the FileChooser
     *
     */
    public PermutationPane(Stage parentStage) {
        setupFileChooser();
        this.parentStage = parentStage;

        //instantiation of components
        lbPermutationStart = new Label("Beginning Permutation\nPlace a , between values");
        lbNumberOfPermutations = new Label("Number of permutations");
        lbGeneratedPermutations = new Label("Following permutations");
        tfStartingPermutation = new TextField();
        tfStartingPermutation.setPrefColumnCount(20);
        tfNumberOfPermutations = new TextField();
        tfNumberOfPermutations.setPrefColumnCount(8);
        taGeneratedPermutations = new TextArea();
        btGeneratePermutations = new Button("Generate Permutations");
        btGeneratePermutations.setOnAction(event -> generatePermutations());
        btSaveData = new Button("Save Permutations");
        btSaveData.setOnAction(event -> savePermutations());

        //putting components on panels
        FlowPane controlPane = new FlowPane();
        controlPane.getChildren().addAll(lbPermutationStart, tfStartingPermutation,
                lbNumberOfPermutations, tfNumberOfPermutations, btGeneratePermutations);
        controlPane.setPadding(new Insets(10, 10, 10, 10));
        controlPane.setHgap(5);
        
        BorderPane generatedPane = new BorderPane();
        generatedPane.setPadding(new Insets(10, 10, 10, 10));
        generatedPane.setTop(lbGeneratedPermutations);
        generatedPane.setCenter(taGeneratedPermutations);
        generatedPane.setBottom(btSaveData);
        
        this.setTop(controlPane);
        this.setCenter(generatedPane);

    }

    /**
     * Runs the lexicographic permutation for the desired amount of times,
     * outputting each permutation to a TextArea
     */
    private void generatePermutations() {
        int[] startingPermutation = getStartingPermutation();
        int numOfIterations = 1;
        try {
            numOfIterations = Integer.parseInt(tfNumberOfPermutations.getText());
        } catch (NumberFormatException nfe) {
            System.out.println("pls, that wasn't an integer in the number of iterations field.");
        }
        taGeneratedPermutations.clear();
        for (int i = 0; i < numOfIterations; i++) {
            startingPermutation = lexicographicPermutation(startingPermutation);
            taGeneratedPermutations.appendText(permutationToString(startingPermutation));
            taGeneratedPermutations.appendText("\n");
        }
    }

    /**
     * Formats an integer array into a String representation.
     * @param array an array of integers that correspond to a permutation
     * @return a String representation of the array.
     */
    private String permutationToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i : array) {
            sb.append(i).append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    /**
     * Parses the starting permutation TextField, breaking it on ,'s and 
     * converting the strings to integers. This method will toss out strings
     * that can't be converted
     * @return an integer array corresponding to the given permutation
     */
    private int[] getStartingPermutation() {
        String[] permStrings = tfStartingPermutation.getText().split(",");
        ArrayList<Integer> intArray = new ArrayList<Integer>();
        for (String s : permStrings) {
            try {
                intArray.add(Integer.parseInt(s));
            } catch (Exception e) {
                System.out.println("That probably wasn't an integer in the starting permutation, carry on");
            }
        }
        return intArray.stream().mapToInt(d -> d).toArray();
    }

    /**
     * Runs the lexicographic permutation algorithm on the input array
     * @param array an array of integers
     * @return the next lexicographic permutation of the input array
     */
    public static int[] lexicographicPermutation(int[] array) {
        int i = array.length - 1;
        int j = array.length - 1;

        for (int x = array.length - 2; x >= 0; x--) {
            if (array[x] < array[i]) {
                i = x;
                break;
            }
            i = x;
        }//got biggest index i where ai < ai+1

        for (int y = array.length - 1; y > i; y--) {
            j = y;
            if (array[i] < array[y]) {
                break;
            }
        }//got biggest index j where ai < aj

        //return original array if max permutation
        if (array[j] < array[i]) {
            return array;
        }

        //swap ai and aj
        int holder = array[i];
        array[i] = array[j];
        array[j] = holder;

        //reverse ai+1 to an
        //swap ai+1 and an;
        //swap ai+2 and an-1
        for (int a = i + 1, n = array.length - 1; a < n; a++, n--) {
            holder = array[a];
            array[a] = array[n];
            array[n] = holder;
        }
        return array;
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
                new FileChooser.ExtensionFilter("Data", "*.dat"),
                new FileChooser.ExtensionFilter("Text", "*.txt"),
                new FileChooser.ExtensionFilter("All", "*.*")
        );
    }

    /**
     * Saves the contents of the generated permutations text area to a selected
     * file.
     */
    private void savePermutations() {
        System.out.println("saving file");
        File f = fileChooser.showSaveDialog(parentStage);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            bw.append(taGeneratedPermutations.getText());
        } catch (Exception e) {
            System.out.println("Writer failed to initialize");
        }

    }

}
