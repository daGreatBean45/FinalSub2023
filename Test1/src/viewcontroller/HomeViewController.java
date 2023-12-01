package viewcontroller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import boundary.Encryption;
import entity.Effort;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

public class HomeViewController {

    @FXML
    private Button decrypt_btn;

    @FXML
    private TableColumn<File, String> decrypted_col;

    @FXML
    private Button fileSearch_btn;

    @FXML
    private TableView<File> tobeDecrypted_tbl;

    @FXML
    private TableView<File> decrypted_tbl;

    @FXML
    private PasswordField password_tf;

    @FXML
    private Label title_lbl;

    @FXML
    private TableColumn<File, String> tobeDecrypted_col;

    private ObservableList<File> toDecrypt = FXCollections.observableArrayList();
    private ObservableList<File> decrypted = FXCollections.observableArrayList();

    @FXML
    void decryptFunc(ActionEvent event) {
        System.out.println("Decrypt file");

        Encryption.usePassword(password_tf.getText());

        // decrypt all files in the list
        for (int i = toDecrypt.size() - 1; i >= 0; i--)
        // loop needs to go backwards since we are removing elements as we are iterating
        {
            if (Encryption.decrypt(toDecrypt.get(i).getAbsolutePath())) {
                decrypted.add(toDecrypt.remove(i));
            }
        }
    }

    @FXML
    void searchFileFunc(ActionEvent event) {
        System.out.println("search file func");
        FileChooser fileChooser = new FileChooser();
        List<File> response = fileChooser.showOpenMultipleDialog(null);
        if (response != null) {
            for (File f : response) {
                if (!toDecrypt.contains(f) && !decrypted.contains(f)) {
                    toDecrypt.add(f);
                }
            }
        } else {
            System.out.println("user gave no files");
        }
    }
    @FXML
    void initialize() {
    	// Catch up decrypted to be up to date with Encryption's knowledge
    	for (String s : Encryption.getEffortFiles())
    	{
    		decrypted.add(new File(s));
    	}
    	for (String s : Encryption.getDefectFiles())
    	{
    		decrypted.add(new File(s));
    	}

        decrypted_col.setCellValueFactory(file -> new SimpleStringProperty(file.getValue().getName()));
        tobeDecrypted_col.setCellValueFactory(file -> new SimpleStringProperty(file.getValue().getName()));

        tobeDecrypted_tbl.setItems(toDecrypt);
        decrypted_tbl.setItems(decrypted);
    }
}
