package robotgame;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application{

	
	@Override
	public void start(Stage primaryStage) throws Exception {
	
		Group root = new MapInterpreter();
		
		Scene scene = new Scene(root,1200,600);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
				
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
