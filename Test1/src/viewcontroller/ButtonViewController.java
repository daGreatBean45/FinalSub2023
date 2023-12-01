package viewcontroller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
public class ButtonViewController implements Initializable {

    @FXML
    private Button dataView_btn;

    @FXML
    private Button data_btn;

    @FXML
    private ImageView data_img;

    @FXML
    private Button defect_btn;

    @FXML
    private ImageView defect_img;

    @FXML
    private ImageView definitions_img;

    @FXML
    private Button effort_btn;

    @FXML
    private ImageView effort_img;

    @FXML
    private Button fileSelect_btn;

    @FXML
    private Button log_btn;

    @FXML
    private ImageView logs_img;

    @FXML
    private ImageView selectFile_img;

    @FXML
    private VBox toolbar_vbox;
    

    @FXML
    void displayData(ActionEvent event) throws IOException{
    	System.out.println("Display data");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("data_view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void displayDefect(ActionEvent event) throws IOException{ //throws IOException
        FXMLLoader loader = new FXMLLoader(getClass().getResource("defect.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void displayDefinitions(ActionEvent event) throws IOException{
    	System.out.println("Display definitons");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("definitions_view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void displayEffort(ActionEvent event) throws IOException{
    	System.out.println("Display effort");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("effort.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void displayHome(ActionEvent event) throws IOException{
    	System.out.println("Display home");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void displayLogs(ActionEvent event) throws IOException {
    	System.out.println("Display logs");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("logs.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
