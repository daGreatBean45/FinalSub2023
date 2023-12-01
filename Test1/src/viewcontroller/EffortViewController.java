package viewcontroller;

 
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.security.auth.callback.Callback;

import boundary.Encryption;
import entity.Effort;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class EffortViewController {
  
	@FXML
	private  ComboBox<String> defect_cb = new ComboBox<>(FXCollections.observableArrayList());
 
    @FXML
    private Label effortInfo1_lbl;

    @FXML
    private Label effortInfo2_lbl;

    @FXML
    private Label effortInfo3_lbl;

    @FXML
    private ComboBox<String> effortcategory_cb = new ComboBox<>(FXCollections.observableArrayList());;

    @FXML
    private ComboBox<String> lifecycstep_cb = new ComboBox<>(FXCollections.observableArrayList());;

    @FXML
    private ComboBox<String> project_cb = new ComboBox<>(FXCollections.observableArrayList());;

    @FXML
    private Button startActivity_btn;

    @FXML
    private Button stopActivity_btn;

    @FXML
    private Label timeElapsed_lbl = new Label();

    @FXML
    private Label timeStart_lbl = new Label();

    @FXML
    private Label title_lbl;
    
    boolean shouldUpdateClock = false;
    LocalTime startTime = null;
    AnimationTimer clockUpdater;
    
    public void initialize() {
    	ObservableList<String> dataList = DataManager.getProjectNames();
    	displayData(dataList, "projectName");
    	dataList = DataManager.getLifeCycleSteps();
    	displayData(dataList, "lifeCycleStep");
    	dataList = DataManager.getEffortCategories();
    	displayData(dataList, "effortCategory");
    	dataList = DataManager.getDefectCategories();
    	displayData(dataList, "defect");
    	
    	clockUpdater = new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				updateClock();
			}
		};
		
		if (null != DataManager.getEffortCreationStartTime())
		{
			startTime = DataManager.getEffortCreationStartTime();
			shouldUpdateClock = true;
			clockUpdater.start();
		}
		project_cb.setValue(DataManager.getEffortCreationProject());
		lifecycstep_cb.setValue(DataManager.getEffortCreationStep());
		defect_cb.setValue(DataManager.getEffortCreationDefect());
		effortcategory_cb.setValue(DataManager.getEffortCreationCategory());
    }
 
    @FXML
    void startFunc(ActionEvent event) {
    	System.out.println("starting clock");
    	startTime = LocalTime.now();
    	DataManager.setEffortCreationStartTime(startTime);
    	shouldUpdateClock = true;
		clockUpdater.start();
    }

    @FXML
    void stopFunc(ActionEvent event) {
    	if (null == startTime) return;
    	System.out.println("ending clock");
    	Effort createdEffort = new Effort(
    			0, // TODO should this value change?
    			project_cb.getValue(),
    			LocalDate.now(),
    			startTime,
    			LocalTime.now(),
    			0, // set below using setTimeDifference()
    			lifecycstep_cb.getValue(),
    			effortcategory_cb.getValue(),
    			defect_cb.getValue()
    		);
    	startTime = null;
    	DataManager.setEffortCreationStartTime(null);
    	createdEffort.setTimeDifference(createdEffort.getStartTime(), createdEffort.getStopTime());
    	Encryption.writeEffort(createdEffort);

    	shouldUpdateClock = false;
		clockUpdater.stop();
		updateClock(); // run one last time to make sure the displayed times are reset
    }
    
    private void updateClock()
    {
    	if (shouldUpdateClock)
    	{
    		timeStart_lbl.setText("Time started: " + startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    		LocalTime difference = LocalTime.now().minusSeconds(startTime.toSecondOfDay());
    		timeElapsed_lbl.setText("Time elapsed: " + difference.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    		DataManager.setEffortCreationProject(project_cb.getSelectionModel().getSelectedItem());
    		DataManager.setEffortCreationStep(lifecycstep_cb.getSelectionModel().getSelectedItem());
			DataManager.setEffortCreationDefect(defect_cb.getSelectionModel().getSelectedItem());
			DataManager.setEffortCreationCategory(effortcategory_cb.getSelectionModel().getSelectedItem());
    	}
    	else
    	{
    		timeStart_lbl.setText("Time started: 00:00:00");
    		timeElapsed_lbl.setText("Time elapsed: 00:00:00");
    	}
    }

    public void displayData(ObservableList<String> data, String title) {
      	if(data != null && !data.isEmpty()) {
      		switch(title) {
		    	case "projectName":
		    		project_cb.setItems(data);
		    		break;
		    	case "lifeCycleStep":
		    		lifecycstep_cb.setItems(data);
		    		break;
		    	case "effortCategory":
		    		effortcategory_cb.setItems(data);
		    		break;
		    	case "defect":
		    		defect_cb.setItems(data);
		    		break;
		    	default:
		    		break;
      		}
     	} 
    }
}
