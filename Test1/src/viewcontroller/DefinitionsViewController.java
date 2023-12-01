package viewcontroller;

 

import java.io.File;
import java.io.FileNotFoundException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;


public class DefinitionsViewController {
	public class Pairing{
	    public String heading;
	    Vector<String> content = new Vector<String>();
	}
 
	//Global headers
	private Set<String> headers = new HashSet<String>();
	
	/**
	 * reference of set of strings used to identify columns existing in Definitions_Backup.csv
	 * @param headers a set of string
	 */
	private void setHeader(Set<String> headers) {
		this.headers = headers;
	}
	
	private Set<String> getHeaders() {
		return headers;
	}

    @FXML
    private TableColumn<String, String> defectCategories_col;  

    @FXML
    private TableView<String> defectCategories_tbl;
 
    @FXML
    private TableColumn<String, String> deliverables_col;

    @FXML
    private TableView<String> deliverables_tbl;

    @FXML
    private TableColumn<String, String> effortCategories_Col;

    @FXML
    private TableView<String> effortCategories_tbl;

    @FXML
    private TableColumn<String, String> interruptions_col;

    @FXML
    private TableView<String> interruptions_tbl;

    @FXML
    private TableColumn<String, String> lifeCycle_col;

    @FXML
    private TableView<String> lifeCycle_tbl;

    @FXML
    private TableColumn<String, String> plans_col;

    @FXML
    private TableView<String> plans_tbl;

    @FXML
    private TableColumn<String, String> projectName_col;

    @FXML
    private TableView<String> projectName_tbl;

    @FXML
    private Button selectFile_btn;

    @FXML
    private Label title_lbl;
    
    private File file;
    
    private void setFile(File file) {
    	this.file = file;
    }
    
    private File getFile() {
    	return file;
    }
    
 

    @FXML 
    void selectFileFunc(ActionEvent event) { 
    	FileChooser fc = new FileChooser();
    	File response = fc.showOpenDialog(null);
    	//The beginning of each new line in the Definitions.csv or another similar file must have corresponding headings as the first string in the line, otherwise there will be empty combobox selections
    	Set<String> headers = new HashSet<String>();
    	headers.add("ProjectNamesHeader");
    	headers.add("LifeCycleStepsHeader");
    	headers.add("EffortCategoriesHeader");
    	headers.add("PlansHeader");
    	headers.add("DeliverablesHeader");
    	headers.add("InterruptionsHeader");
    	headers.add("DefectsHeader");
    	setHeader(headers);
    	if(response != null) {
    		setFile(response);
    		Vector<Pairing> pairings = readFile(response, headers);
    		populateLocalColumns(pairings, headers);
    	
    	}
    }
    
  
    
    /**
     * Handles the loading of values in all columns, provided the data has been loaded before
     */
    public void initialize() {
    	ObservableList<String> dataList = DataManager.getProjectNames();
 
    	Set<String> headers = getHeaders();
    	populateLocalColumns(loadPairing(dataList, "ProjectNamesHeader"), headers);
    	dataList = DataManager.getDefectCategories();
    	populateLocalColumns(loadPairing(dataList, "DefectsHeader"), headers);
    	dataList = DataManager.getDeliverables();
    	populateLocalColumns(loadPairing(dataList, "DeliverablesHeader"), headers);
    	dataList = DataManager.getEffortCategories();
    	populateLocalColumns(loadPairing(dataList, "EffortCategoriesHeader"), headers);
    	dataList = DataManager.getInterruptions();
    	populateLocalColumns(loadPairing(dataList, "InterruptionsHeader"), headers);
    	dataList = DataManager.getLifeCycleSteps();
    	populateLocalColumns(loadPairing(dataList, "LifeCycleStepsHeader"), headers);
    	dataList = DataManager.getPlans();
    	populateLocalColumns(loadPairing(dataList, "PlansHeader"), headers);
 
    }
    
    private Vector<Pairing> loadPairing(ObservableList<String> list, String headingName) {
    	Vector<String> modifiableVector = new Vector<String>();
    	modifiableVector.addAll(list);

    	Vector<Pairing> pair = new Vector<Pairing>();
    	Pairing p = new Pairing();
    	
    	p.heading = headingName;
    	p.content.addAll(modifiableVector);
    	pair.add(p);
    	
    	return pair;
    }
    
    private Alert createAlert(String title, String contentText) {
      	Alert alert = new Alert(Alert.AlertType.ERROR);
    	alert.setTitle(title);
    	alert.setContentText(contentText);
    	Optional<ButtonType> result = alert.showAndWait();
    	return alert;
    }
    private void populateLocalColumns(Vector<Pairing> pairings, Set<String> headers) {

    	for(Pairing pair: pairings){
    		ObservableList<String> modifiableList = FXCollections.observableArrayList(pair.content);

    		if(modifiableList != null & !modifiableList.isEmpty()) {
    			switch(pair.heading) {
		    		case "ProjectNamesHeader":
		    			populateTable(projectName_col, projectName_tbl, modifiableList);
		    			DataManager.setProjectNames(modifiableList);
		    			break;
		    		case "LifeCycleStepsHeader":
		    			populateTable(lifeCycle_col, lifeCycle_tbl, modifiableList);
		    			DataManager.setLifeCycleSteps(modifiableList);
	
		    			break;
		    		case "EffortCategoriesHeader":
		    			populateTable(effortCategories_Col, effortCategories_tbl, modifiableList);
		    			DataManager.setEffortCategories(modifiableList);
		    			break;
		    		case "PlansHeader":
		    			populateTable(plans_col, plans_tbl, modifiableList);
		    			DataManager.setPlans(modifiableList);
		    			break;
		    		case "DeliverablesHeader":
		    			populateTable(deliverables_col, deliverables_tbl, modifiableList);
		    			DataManager.setDeliverables(modifiableList);
		    			break;
		    		case "InterruptionsHeader":
		    			populateTable(interruptions_col, interruptions_tbl, modifiableList);
		    			DataManager.setInterruptions(modifiableList);
		    			break;
		    		case "DefectsHeader":
		    			populateTable(defectCategories_col, defectCategories_tbl, modifiableList);
		    			DataManager.setDefectCategories(modifiableList);
		    			break;
		    		default:
		    			createAlert("Heading Error", "Heading: " + pair.heading + " does not exist please ensure your heading(s) match [ProjectNames, LifeCycleSteps, EffortCategories, Plans, Deliberables, Interruptions, Defects]");
		    			break;
    			}	
    		} 
        }
 
	}

	private void populateTable(TableColumn<String, String> tblCol, TableView<String> tblView, ObservableList<String> modifiableList) {
		tblCol.setCellValueFactory(new PropertyValueFactory<>(""));
		tblCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
		tblView.setItems(modifiableList);
		tblView.setEditable(true);
		tblCol.setCellFactory(TextFieldTableCell.forTableColumn());

		tblCol.setOnEditCommit((TableColumn.CellEditEvent<String, String> event)->{
			int colIndex = tblCol.getTableView().getColumns().indexOf(tblCol);
			updateFile(colIndex, event.getRowValue(), event.getNewValue());
		});
	}

	

	private void updateFile(int colIndex, String oldValue, String newValue) {
		File file = getFile();

        Path path = file.toPath();
        Charset charset = StandardCharsets.UTF_8;

        // Read all lines from the CSV file into a list
        List<String> lines = null;
		try {
			lines = Files.readAllLines(path, charset);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
        // Replace all occurrences of the old value with the new value
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String newLine = line.replaceAll(oldValue, newValue);
            lines.set(i, newLine);
        }

        // Write the modified lines back to the CSV file
        try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
        
        Set<String> headers = getHeaders();
        
        Vector<Pairing> pair = readFile(file, headers);
        populateLocalColumns(pair, headers);
	}

	private void addPairing(Vector<Pairing> pairings, String data, Set<String> headers){
        String[] tokens = data.split(",");
        Pairing p1 = new DefinitionsViewController().new Pairing();
        p1.heading = tokens[0];
        if(headers.contains(p1.heading)){
            for(int i = 1; i < tokens.length; i++){
            	p1.content.add(tokens[i]);
            }
            pairings.add(p1);
        }else{
			createAlert("Heading Error", "Heading: " + p1.heading + " does not exist please ensure your heading(s) match [ProjectNames, LifeCycleSteps, EffortCategories, Plans, Deliberables, Interruptions, Defects]");
        }
    }
    
	private Vector<Pairing> readFile(File response, Set<String> headers) {
 		Vector<Pairing> pairings = new Vector<Pairing>();
 		try{
            BufferedReader read = new BufferedReader(new FileReader(response.getAbsolutePath()));
            String data = read.readLine();
            addPairing(pairings, data, headers);
            while((data = read.readLine()) != null){
            	addPairing(pairings, data, headers);
            }
            read.close();
        }catch(Exception e){
			createAlert("Exception errorr", "Context of error: " + e);
        }
        return pairings;
	}
}
