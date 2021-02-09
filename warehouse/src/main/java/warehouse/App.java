package warehouse;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			HlavnaObrazovkaController controller = new HlavnaObrazovkaController();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("HlavnaObrazovka.fxml"));
			loader.setController(controller);
			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			primaryStage.setTitle("Vyh¾adávanie tovaru");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	static class MyLauncher {
		public static void main(String[] args) {
			App.main(args);
		}
	}

}
