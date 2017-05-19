package suite;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Loads the SorterPane and LexicographicPane into tabs
 * @author Jeff
 */
public class SorterFXApp extends Application {
    TabPane panels;
    
    @Override
    public void start(Stage primaryStage) {
        panels = new TabPane();
        
        Tab sortTab = new Tab("Sorting", new SorterPane(primaryStage));
        Tab permutateTab = new Tab("Permutation", new PermutationPane(primaryStage));
        Tab tspTab = new Tab("TSP Solver", new TSPPane(primaryStage));
        sortTab.setClosable(false);
        permutateTab.setClosable(false);
        tspTab.setClosable(false);
        
        panels.getTabs().addAll(sortTab,permutateTab,tspTab);
        
        
        Scene scene = new Scene(panels, 800, 400);
        
        primaryStage.setTitle("Analysis of Algorithms Suite");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
