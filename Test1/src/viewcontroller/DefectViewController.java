/**
 * This class is the scene loaded by JavaFX to handle creating Defects.
 * 
 * @author Anthony Touma
 * @author Joshua Budd
 */
package viewcontroller;

import java.time.LocalDate;
import java.time.LocalTime;

import boundary.Encryption;
import entity.Defect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DefectViewController {

    @FXML
    private Label actualNum_lbl;

    @FXML
    private Label attrSaved_lbl;

    @FXML
    private Button clearDefect_btn;

    @FXML
    private Label clearDefect_lbl;

    @FXML
    private Button close_btn;

    @FXML
    private Button createDefect_btn;

    @FXML
    private Label defectName_lbl;

    @FXML
    private TextField defectName_tf;

    @FXML
    private Label defectSelect_lbl;

    @FXML
    private Label defectSympt_lbl;

    @FXML
    private TextArea defectSymptom_ta;

    @FXML
    private ComboBox<String> defect_cb  = new ComboBox<>(FXCollections.observableArrayList()); 

    @FXML
    private Label defect_lbl;

    @FXML
    private Label defineAttr_lbl;

    @FXML
    private Button deleteCurrentDefect_btn;

    @FXML
    private ComboBox<String> fix_cb; 

    @FXML
    private Label fix_lbl;

    @FXML
    private ComboBox<String> injected_cb; 

    @FXML
    private Label injected_lbl;

    @FXML
    private Label insertInst_lbl;

    @FXML
    private Label num_lbl;

    @FXML
    private Label projectSelect_lbl;

    @FXML
    private Label remove_lbl;

    @FXML
    private ComboBox<String> removed_cb;

    @FXML
    private Button reopen_btn;

    @FXML
    private ComboBox<String> selectDefect_cb;

    @FXML
    private ComboBox<String> selectProject_cb = new ComboBox<>(FXCollections.observableArrayList());

    @FXML
    private Label status_lbl;

    @FXML
    private Label title_lbl;

    @FXML
    private Button updateCurrentDefect_btn;

    @FXML
    private Label updateDefect_lbl;
    
    /**
     * An initializer function to populate some comboboxes in Defect view with data loaded elsewhere in the application
     * @author Anthony Touma
     * @author Joshua Budd
     */
    public void initialize() {
    	ObservableList<String> dataList = DataManager.getProjectNames();
    	displayData(dataList, "projectName");
  
    	dataList = DataManager.getDefectCategories();
    	displayData(dataList, "defect");
    	
    	selectDefect_cb.setValue(DataManager.getselectDef());
    	
    	ObservableList<String> items = FXCollections.observableArrayList( //Populates injection and removed drop downs
                "Planning",
                "Information Gathering",
                "Information Understanding",
                "Verifying",
                "Outlining",
                "Drafting",
                "Finalizing",
                "Team Meeting",
                "Coach Meeting",
                "Stakeholder Meeting"
            );
        injected_cb.setItems(items);
        removed_cb.setItems(items);

    	selectDefect_cb.setOnAction(e -> { 
    		String value = selectDefect_cb.getSelectionModel().getSelectedItem(); 
    		DataManager.setselectDef(value); 
            updateFieldsFromDefect(findSelected());
    	});
    	
    	selectProject_cb.setOnAction(e -> { 
    		String value = selectProject_cb.getSelectionModel().getSelectedItem(); 
    		DataManager.setselectProject(value);
    		changeDefectComboBoxToMatchProject();
    		// if there is a possible replacement, replace the selection
    		selectDefect(findMatchingProject());
    		DataManager.setselectProject(value); // re-set the value in case selectDefect cleared it
    	});

    	selectProject_cb.getSelectionModel().select(0); // make sure to pick something
    	String value = selectProject_cb.getSelectionModel().getSelectedItem(); 
    	DataManager.setselectProject(value);
    	changeDefectComboBoxToMatchProject();
    	// if there is a possible replacement, replace the selection
    	selectDefect(findMatchingProject());
    	DataManager.setselectProject(value); // re-set the value in case selectDefect cleared it
    }
    
   /**
    * Populates combobox with pre-loaded data from another view/function
    * @param dataList an ObservableList<String> 
    * @param a name associated with the List of data.
    * @author Matthew Snyder
    */
    private void displayData(ObservableList<String> dataList, String name) {
		if(dataList != null && !dataList.isEmpty()) {
			switch(name) {
				case "projectName":
					selectProject_cb.setItems(dataList);
					break;
				case "defect":
					defect_cb.setItems(dataList);
					break;
				default:
					break;
			}
		}
	}

    
    
    /**
     * Delete all defects in the currently selected project.
     * @author Joshua Budd
     */
	@FXML
    void clearDefectFunc(ActionEvent event) {
		Defect d;
		while (true)
		{
			d = findMatchingProject();
			if (d == null) break;
			Encryption.deleteDefect(d);
		}
		// deselect deleted item
    	DataManager.setselectDef("");
    	// refresh list
    	changeDefectComboBoxToMatchProject();
    }
	
    /**
     * @author Anthony Touma
     */
	@FXML
    void reopenFunc(ActionEvent event) {
		DataManager.setStatus("Open");
		status_lbl.setText("Stats: " + DataManager.getStatus());
    }
	
    /**
     * @author Anthony Touma
     */
    @FXML
    void closeFunc(ActionEvent event) {
    	DataManager.setStatus("Closed");
		status_lbl.setText("Stats: " + DataManager.getStatus());
    }

    /**
     * @author Anthony Touma
     */
    @FXML
    void createNewDefectFunc(ActionEvent event) { //Adding user input defect into 2.b
    	
		String defectName = defectName_tf.getText(); 
    	
	    if (!selectDefect_cb.getItems().contains(defectName)) { //Preventing duplicates 
	    	
	    	selectDefect_cb.getItems().add(defectName);
	    	// add to fix_cb as well
	          	
	    	DataManager.setnumTracker(DataManager.getnumTracker() + 1); //Incrementing #Num
	    	
	    	// create a new defect object
	    	Defect d = new Defect(
	    			DataManager.getnumTracker(),
	    			selectProject_cb.getSelectionModel().getSelectedItem(), 
	    			LocalDate.now(),
	    			LocalTime.now(),
	    			LocalTime.now(),
	    			0,
	    			defectName,
	    			defectSymptom_ta.getText(),
	    			injected_cb.getSelectionModel().getSelectedItem(),
	    			removed_cb.getSelectionModel().getSelectedItem(),
	    			defect_cb.getSelectionModel().getSelectedItem(),
	    			"Open",
	    			fix_cb.getSelectionModel().getSelectedItem()
	    	);
	    	Encryption.writeDefect(d);
	    	 
	    	selectDefect(d);
		}
   }

    /**
     * @author Anthony Touma
     * @author Joshua Budd
     */
    @FXML
    void deleteCurrentDefectFunc(ActionEvent event) {
    	Defect d = findSelected();
    	if (d == null) return;

    	Encryption.deleteDefect(d);
    	selectDefect_cb.getItems().remove(d.getName());

    	// deselect deleted item
    	DataManager.setselectDef("");
    	// make sure the fields are blanked to make it clear something was deleted
    	updateFieldsFromDefect(null);
    }

    /**
     * @author Anthony Touma
     */
    @FXML
    void updateCurrentDefect(ActionEvent event) {
    	Defect d = findSelected();
    	if (d == null) return;

    	updateDefectFromFields(d);
    	
        Encryption.writeDefect(d); //Writing the defect using encryption 
    }

    /**
     * Change the available names of defects based on the project selected in DataManager.
     * @see DataManager#setselectDef(String)
     * @author Joshua Budd
     */
    void changeDefectComboBoxToMatchProject()
    {
    	selectDefect_cb.getItems().clear();
    	fix_cb.getItems().clear();

        // in a loop for all defects, write each defect's name to the list selectDefect_cb
        for (Defect d: Encryption.getDefect()) {
        	if (d.getNum() > DataManager.getnumTracker()) {
        		DataManager.setnumTracker(d.getNum());
        	}

        	// continue past defects not in the currently select project
        	// this is because they can't possible be the one that is selected
        	if (!DataManager.getselectProject().equals(d.getProjectName())) continue;

        	selectDefect_cb.getItems().add(d.getName());
        	fix_cb.getItems().add(d.getName());
        }
        updateFieldsFromDefect(null);
    }
    
    /**
     * Copy values from a defect into the fields.
     * @param d defect to copy out of
     * @author Anthony Touma
     * @author Joshua Budd
     */
    void updateDefectFromFields(Defect d)
    {
    	if (d == null) return;
    	/* num cannot be changed on this page so it is skipped */
        d.setName(defectName_tf.getText());
        DataManager.setselectDef(d.getName());
        d.setDetail(defectSymptom_ta.getText());
        /* no need to do anything with the actual status label itself */
        d.setStatus(DataManager.getStatus()); 
        d.setInjected(injected_cb.getSelectionModel().getSelectedItem());
        d.setRemoved(removed_cb.getSelectionModel().getSelectedItem());
        d.setCategory(defect_cb.getSelectionModel().getSelectedItem());
        d.setFix(fix_cb.getSelectionModel().getSelectedItem());
    }

    /**
     * Copy values from the fields into a defect.
     * @param d defect to copy into
     * @author Anthony Touma
     * @author Joshua Budd
     */
    void updateFieldsFromDefect(Defect d)
    {
    	if (d == null) d = new Defect(0, "", null, null, null, 0, "", "", "", "", "", "Closed", "");
    	actualNum_lbl.setText(Integer.toString(d.getNum()));
		defectName_tf.setText(d.getName());
		/* no need to change the do a change to the data manager for the name */
		status_lbl.setText("Stats: " + d.getStatus());
		DataManager.setStatus(d.getStatus());
        defectSymptom_ta.setText(d.getDetail());
		injected_cb.setValue(d.getInjected());
		removed_cb.setValue(d.getRemoved());
		defect_cb.setValue(d.getCategory());
		fix_cb.setValue(d.getFix());
    }

    /**
     * Changes the Project and Defect selections to match a defect and make all fields match change.
     * @param d defect to select
     * @author Joshua Budd
     */
    void selectDefect(Defect d)
    {
    	if (d != null)
    	{
    		selectProject_cb.setValue(d.getProjectName());
			DataManager.setselectProject(d.getProjectName());
			changeDefectComboBoxToMatchProject();
    		selectDefect_cb.setValue(d.getName());
			DataManager.setselectDef(d.getName());
    	}
    	else
    	{
			DataManager.setselectProject("");
			DataManager.setselectDef("");
    	}
    	updateFieldsFromDefect(d);
    }

    /**
     * Find the selected defect.
     * @return The defect which is referenced by the Project and Defect name.
     * @author Joshua Budd
     * @author Anthony Touma
     */
    Defect findSelected()
    {
    	for (Defect d: Encryption.getDefect()) {
        	if (
        			null != d.getProjectName() &&
        			null != DataManager.getselectProject() &&
        			null != d.getName() &&
        			null != DataManager.getselectDef() &&
        			d.getProjectName().equals(DataManager.getselectProject()) &&
        			d.getName().equals(DataManager.getselectDef())
        		){
        		return d;
        	}
    	}
    	return null;
    }

    /**
     * Find a defect which matches the selected project.
     * @return Returns a defect which matches the project.
     * @author Joshua Budd
     */
    Defect findMatchingProject()
    {
    	for (Defect d: Encryption.getDefect()) {
        	if (
        			null != d.getProjectName() &&
        			null != DataManager.getselectProject() &&
        			d.getProjectName().equals(DataManager.getselectProject())
        		){
        		return d;
        	}
    	}
    	return null;
    }
    
}
