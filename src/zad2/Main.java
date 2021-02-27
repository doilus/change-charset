
package zad2;


import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class Main extends Application{
	
	 static Service s = new Service("Italy");
	 String country = s.getCountry();
	 String url = "https://en.wikipedia.org/wiki/" + country;
	
  public static void main(String[] args) {
   
    String weatherJson = s.getWeather("Rome");
    Double rate1 = s.getRateFor("THB");
    Double rate2 = s.getNBPRate();
    // ...
    //spr wyniki
    System.out.println(weatherJson);
    System.out.println("przelicznik " + rate1);
    System.out.println("rata do pln " + rate2);
    // część uruchamiająca GUI
    
    launch(args);
    
    
  }

public void start(Stage primaryStage) throws Exception {
		
	final WebView webView = new WebView();
	
	final WebEngine webEngine = webView.getEngine();
	

	webEngine.load(url);
	
	VBox root = new VBox();
	
	root.getChildren().add(webView);
	
	Scene scene = new Scene(root);
	
	
	primaryStage.setScene(scene);
	primaryStage.setTitle("Klienci usług sieciowych");
	primaryStage.show();
	
}
}
