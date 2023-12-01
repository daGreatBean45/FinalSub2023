package viewcontroller;

 
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import boundary.Encryption;
import entity.Defect;
import entity.Effort;
import entity.EffortDefectIntf;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalTimeStringConverter;
//Note for Graders/Users, the methods/classes are as follows: undoFunc, redoFunc, saveFunc, and initialize, the code is large, so using CTRL+F would be beneficial for navigation
public class LogViewController implements Initializable{
	List<EffortDefectIntf> effortDefectIntfEffort;			//used to help initialize effort and defect lists
	List<EffortDefectIntf> effortDefectIntfDefect;
	
	LinkedList<Defect> udList = new LinkedList<Defect>();	//contains old objects that can be restored with undoFunc
	LinkedList<Effort> ueList = new LinkedList<Effort>();
	
	LinkedList<Defect> rdList = new LinkedList<Defect>(); 	//contains information stored to redo lists
	LinkedList<Effort> reList = new LinkedList<Effort>();
	
	LinkedList<String> actList1 = new LinkedList<String>();	//keeps track of actions that can be undone
	LinkedList<String> actList2 = new LinkedList<String>();	//keeps track of actions that can be redone
	//undo and redo related operations/methods contribute to the potential upside risk of reducing the likelihood of permanent loss of information, as does the save function
	//the inability to edit invalid input into cells without crashing the program contributes to the potential upside risk of being more tolerant of misinput as it just shoots out an error instead of just breaking. 
	//until you provide valid input or click off the cell (restoring the original value), you can't edit and set the value of cells on a table
	
	//table columns and tableview for logViewController.java, defined in the logs.fxml, @author Josh, edited by Aztlan Olmedo to be more suited for the overall design.
	@FXML
    private TableColumn<Defect, String> defectCategory_tblCol;

    @FXML
    private TableColumn<Defect, LocalDate> defectDate_tblCol;

    @FXML
    private TableColumn<Defect, String> defectDetail_tblCol;

    @FXML
    private TableColumn<Defect, String> defectFix_tblCol;

    @FXML
    private TableColumn<Defect, String> defectInject_tblCol;

    @FXML
    private TableColumn<Defect, Integer> defectNumber_tblCol;

    @FXML
    private TableColumn<Defect, String> defectRemoved_tblCol;

    @FXML
    private TableColumn<Defect, LocalTime> defectStart_tblCol;

    @FXML
    private TableColumn<Defect, String> defectStatus_tblCol;

    @FXML
    private TableColumn<Defect, LocalTime> defectStop_tblCol;

    @FXML
    private TableView<Defect> defectTable_tbl;

    @FXML
    private Label defectTbl_lbl;

    @FXML
    private TableColumn<Defect, Integer> defectTime_tblCol;

    @FXML
    private TableColumn<Effort, LocalDate> effortDate_tblCol;

    @FXML
    private TableColumn<Effort, String> effortDeliverable_tblCol;

    @FXML
    private TableColumn<Effort, String> effortLifeCycle_tblCol;

    @FXML
    private TableColumn<Effort, Integer> effortNumber_tblCol;

    @FXML
    private TableColumn<Effort, LocalTime> effortStart_tblCol;

    @FXML
    private TableColumn<Effort, LocalTime> effortStop_tblCol;

    @FXML
    private TableView<Effort> effortTable_tbl;

    @FXML
    private Label effortTbl_lbl;

    @FXML
    private TableColumn<Effort, String> effortTime_tblCol;

    @FXML
    private TableColumn<Effort, String> efforteffortCategory_tblCol;

    @FXML
    private Button redo_btn;

    @FXML
    private Button save_btn;

    @FXML
    private Label title_lbl;

    @FXML
    private Button undo_btn;
    @FXML
    void redoFunc(ActionEvent event) {//brings back changes that were undone and based on similarity comparisons if the associated list isn't empty, accounts for changes made in effort and defect table 
    	if(actList2.isEmpty()) {
    		System.out.println("Redo action list is empty, changes cannot be redone");
    	}
    	else {
    		String temp = actList2.pop();
    		if(temp.equals("dDate")) {
    			Defect dTemp = rdList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());//change the information to be safe
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					udList.push(newTemp);
    					defectTable_tbl.refresh(); 
    				}
    			}
    		}
    		else if(temp.equals("dCat")) {
    			Defect dTemp = rdList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					udList.push(newTemp);
    					defectTable_tbl.refresh(); 
    				}
    			}
    		}
    		else if(temp.equals("dFix")) {
    			Defect dTemp = rdList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					udList.push(newTemp);
    					defectTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("dDetail")) {
    			Defect dTemp = rdList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());//change the information to be safe
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					udList.push(newTemp);
    					defectTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("dNum")) {
    			Defect dTemp = rdList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					udList.push(newTemp);
    					defectTable_tbl.refresh(); 
    				}
    			}
    		}
    		else if(temp.equals("dInj")) {
    			Defect dTemp = rdList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					udList.push(newTemp);
    					defectTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("dStop")) {
    			Defect dTemp = rdList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					udList.push(newTemp);
    					defectTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("dStart")) {
    			Defect dTemp = rdList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());//change the information to be safe
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					udList.push(newTemp);
    					defectTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("dStat")) {
    			Defect dTemp = rdList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == dTemp.getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated()) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					udList.push(newTemp);
    					defectTable_tbl.refresh(); 
    				}
    			}
    		}
    		else if(temp.equals("dRem")) {
    			Defect dTemp = rdList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					udList.push(newTemp);
    					defectTable_tbl.refresh(); 
    				}
    			}
    		}
    		else if(temp.equals("eNum")) {
    			Effort eTemp = reList.pop();
    			for(int i = 0; i < effortList().size(); i++) {
    				if(effortList().get(i).getDateGenerated() == eTemp.getDateGenerated() && effortList().get(i).getEffortCategory().equals(eTemp.getEffortCategory()) && effortList().get(i).getLifeCycleStep().equals(eTemp.getLifeCycleStep()) && effortList().get(i).getStartTime() == eTemp.getStartTime() && effortList().get(i).getStopTime() == eTemp.getStopTime() && effortList().get(i).getDeliverableInterruption().equals(eTemp.getDeliverableInterruption())) {
    					Effort newTemp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
    					effortList().get(i).setNum(eTemp.getNum());
    					effortList().get(i).setDateGenerated(eTemp.getDateGenerated());
    					effortList().get(i).setDeliverableInterruption(eTemp.getDeliverableInterruption());
    					effortList().get(i).setProjectName(eTemp.getProjectName());
    					effortList().get(i).setLifeCycleStep(eTemp.getLifeCycleStep());
    					effortList().get(i).setStartTime(eTemp.getStartTime());
    					effortList().get(i).setStopTime(eTemp.getStopTime());
    					effortList().get(i).setTimeDifference(eTemp.getStartTime(), eTemp.getStopTime());
    					effortList().get(i).setEffortCategory(eTemp.getEffortCategory());
    					ueList.push(newTemp);
    					effortTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("eLife")) {
    			Effort eTemp = reList.pop();
    			for(int i = 0; i < effortList().size(); i++) {
    				if(effortList().get(i).getDateGenerated() == eTemp.getDateGenerated() && effortList().get(i).getEffortCategory().equals(eTemp.getEffortCategory()) && effortList().get(i).getNum() == eTemp.getNum() && effortList().get(i).getStartTime() == eTemp.getStartTime() && effortList().get(i).getStopTime() == eTemp.getStopTime() && effortList().get(i).getDeliverableInterruption().equals(eTemp.getDeliverableInterruption())) {
    					Effort newTemp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
    					effortList().get(i).setNum(eTemp.getNum());
    					effortList().get(i).setDateGenerated(eTemp.getDateGenerated());
    					effortList().get(i).setDeliverableInterruption(eTemp.getDeliverableInterruption());
    					effortList().get(i).setProjectName(eTemp.getProjectName());
    					effortList().get(i).setLifeCycleStep(eTemp.getLifeCycleStep());
    					effortList().get(i).setStartTime(eTemp.getStartTime());
    					effortList().get(i).setStopTime(eTemp.getStopTime());
    					effortList().get(i).setTimeDifference(eTemp.getStartTime(), eTemp.getStopTime());
    					effortList().get(i).setEffortCategory(eTemp.getEffortCategory());
    					ueList.push(newTemp);
    					effortTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("eCat")) {
    			Effort eTemp = reList.pop();
    			for(int i = 0; i < effortList().size(); i++) {
    				if(effortList().get(i).getDateGenerated() == eTemp.getDateGenerated() && effortList().get(i).getNum() == eTemp.getNum() && effortList().get(i).getLifeCycleStep().equals(eTemp.getLifeCycleStep()) && effortList().get(i).getStartTime() == eTemp.getStartTime() && effortList().get(i).getStopTime() == eTemp.getStopTime() && effortList().get(i).getDeliverableInterruption().equals(eTemp.getDeliverableInterruption())) {
    					Effort newTemp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
    					effortList().get(i).setNum(eTemp.getNum());
    					effortList().get(i).setDateGenerated(eTemp.getDateGenerated());
    					effortList().get(i).setDeliverableInterruption(eTemp.getDeliverableInterruption());
    					effortList().get(i).setProjectName(eTemp.getProjectName());
    					effortList().get(i).setLifeCycleStep(eTemp.getLifeCycleStep());
    					effortList().get(i).setStartTime(eTemp.getStartTime());
    					effortList().get(i).setStopTime(eTemp.getStopTime());
    					effortList().get(i).setTimeDifference(eTemp.getStartTime(), eTemp.getStopTime());
    					effortList().get(i).setEffortCategory(eTemp.getEffortCategory());
    					ueList.push(newTemp);
    					effortTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("eStart")) {
    			Effort eTemp = reList.pop();
    			for(int i = 0; i < effortList().size(); i++) {
    				if(effortList().get(i).getDateGenerated() == eTemp.getDateGenerated() && effortList().get(i).getEffortCategory().equals(eTemp.getEffortCategory()) && effortList().get(i).getNum() == eTemp.getNum() && effortList().get(i).getLifeCycleStep().equals(eTemp.getLifeCycleStep()) && effortList().get(i).getStopTime() == eTemp.getStopTime() && effortList().get(i).getDeliverableInterruption().equals(eTemp.getDeliverableInterruption())) {
    					Effort newTemp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
    					effortList().get(i).setNum(eTemp.getNum());
    					effortList().get(i).setDateGenerated(eTemp.getDateGenerated());
    					effortList().get(i).setDeliverableInterruption(eTemp.getDeliverableInterruption());
    					effortList().get(i).setProjectName(eTemp.getProjectName());
    					effortList().get(i).setLifeCycleStep(eTemp.getLifeCycleStep());
    					effortList().get(i).setStartTime(eTemp.getStartTime());
    					effortList().get(i).setStopTime(eTemp.getStopTime());
    					effortList().get(i).setTimeDifference(eTemp.getStartTime(), eTemp.getStopTime());
    					effortList().get(i).setEffortCategory(eTemp.getEffortCategory());
    					ueList.push(newTemp);
    					effortTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("eStop")) {
    			Effort eTemp = reList.pop();
    			for(int i = 0; i < effortList().size(); i++) {
    				if(effortList().get(i).getDateGenerated() == eTemp.getDateGenerated() && effortList().get(i).getEffortCategory().equals(eTemp.getEffortCategory()) && effortList().get(i).getNum() == eTemp.getNum() && effortList().get(i).getStartTime() == eTemp.getStartTime() && effortList().get(i).getLifeCycleStep().equals(eTemp.getLifeCycleStep()) && effortList().get(i).getDeliverableInterruption().equals(eTemp.getDeliverableInterruption())) {
    					Effort newTemp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
    					effortList().get(i).setNum(eTemp.getNum());
    					effortList().get(i).setDateGenerated(eTemp.getDateGenerated());
    					effortList().get(i).setDeliverableInterruption(eTemp.getDeliverableInterruption());
    					effortList().get(i).setProjectName(eTemp.getProjectName());
    					effortList().get(i).setLifeCycleStep(eTemp.getLifeCycleStep());
    					effortList().get(i).setStartTime(eTemp.getStartTime());
    					effortList().get(i).setStopTime(eTemp.getStopTime());
    					effortList().get(i).setTimeDifference(eTemp.getStartTime(), eTemp.getStopTime());
    					effortList().get(i).setEffortCategory(eTemp.getEffortCategory());
    					ueList.push(newTemp);
    					effortTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("eDate")) {
    			Effort eTemp = reList.pop();
    			for(int i = 0; i < effortList().size(); i++) {
    				if(effortList().get(i).getLifeCycleStep().equals(eTemp.getLifeCycleStep()) && effortList().get(i).getEffortCategory().equals(eTemp.getEffortCategory()) && effortList().get(i).getNum() == eTemp.getNum() && effortList().get(i).getStartTime() == eTemp.getStartTime() && effortList().get(i).getStopTime() == eTemp.getStopTime() && effortList().get(i).getDeliverableInterruption().equals(eTemp.getDeliverableInterruption())) {
    					Effort newTemp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
    					effortList().get(i).setNum(eTemp.getNum());
    					effortList().get(i).setDateGenerated(eTemp.getDateGenerated());
    					effortList().get(i).setDeliverableInterruption(eTemp.getDeliverableInterruption());
    					effortList().get(i).setProjectName(eTemp.getProjectName());
    					effortList().get(i).setLifeCycleStep(eTemp.getLifeCycleStep());
    					effortList().get(i).setStartTime(eTemp.getStartTime());
    					effortList().get(i).setStopTime(eTemp.getStopTime());
    					effortList().get(i).setTimeDifference(eTemp.getStartTime(), eTemp.getStopTime());
    					effortList().get(i).setEffortCategory(eTemp.getEffortCategory());
    					ueList.push(newTemp);
    					effortTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("eDel")) {
    			Effort eTemp = reList.pop();
    			for(int i = 0; i < effortList().size(); i++) {
    				if(effortList().get(i).getDateGenerated() == eTemp.getDateGenerated() && effortList().get(i).getEffortCategory().equals(eTemp.getEffortCategory()) && effortList().get(i).getNum() == eTemp.getNum() && effortList().get(i).getStartTime() == eTemp.getStartTime() && effortList().get(i).getStopTime() == eTemp.getStopTime() && effortList().get(i).getLifeCycleStep().equals(eTemp.getLifeCycleStep())) {
    					Effort newTemp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
    					effortList().get(i).setNum(eTemp.getNum());
    					effortList().get(i).setDateGenerated(eTemp.getDateGenerated());
    					effortList().get(i).setDeliverableInterruption(eTemp.getDeliverableInterruption());
    					effortList().get(i).setProjectName(eTemp.getProjectName());
    					effortList().get(i).setLifeCycleStep(eTemp.getLifeCycleStep());
    					effortList().get(i).setStartTime(eTemp.getStartTime());
    					effortList().get(i).setStopTime(eTemp.getStopTime());
    					effortList().get(i).setTimeDifference(eTemp.getStartTime(), eTemp.getStopTime());
    					effortList().get(i).setEffortCategory(eTemp.getEffortCategory());
    					ueList.push(newTemp);
    					effortTable_tbl.refresh();
    				}
    			}
    		}
    		//if we undo an action, we place it into the actList list for actions that can be undone
    		actList1.push(temp);
    	}   	
    }
    /* 
     * saveFunc @author Aztlan Olmedo, inspired by Encryption class written by Josh
     */
    @FXML
    void saveFunc(ActionEvent event) {//saves changes made to tables/objects with the Encryption class made by Josh, retain information and changes if applicable
    	while(!actList1.isEmpty()) {
    		actList1.removeAll(actList1);
    	}
    	while(!actList2.isEmpty()) {
    		actList2.removeAll(actList2);
    	}
    	for(int i = 0; i < defectList().size(); i++) {
    		Encryption.writeDefect(defectList().get(i));
    	}
    	for(int i = 0; i < effortList().size(); i++) {
    		Encryption.writeEffort(effortList().get(i));
    	}
    	
    }
    
    /*
     * undoFunc @author Aztlan Olmedo
     */
    @FXML
    void undoFunc(ActionEvent event) {//restores previous values made to table/objects and stores relevant information in redo related lists, enforces information retention and security unless program terminates, saves, or both
    	if(actList1.isEmpty()) {
    		System.out.println("Undo action list is empty, no changes can be undone");
    	}
    	else {
    		String temp = actList1.pop();
    		if(temp.equals("dDate")) {
    			Defect dTemp = udList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					rdList.push(newTemp);
    					defectTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("dCat")) {
    			Defect dTemp = udList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					rdList.push(newTemp);
    					defectTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("dFix")) {
    			Defect dTemp = udList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					rdList.push(newTemp);
    					defectTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("dDetail")) {
    			Defect dTemp = udList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					rdList.push(newTemp);
    					defectTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("dNum")) {
    			Defect dTemp = udList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					rdList.push(newTemp);
    					defectTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("dInj")) {
    			Defect dTemp = udList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					rdList.push(newTemp);
    					defectTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("dStop")) {
    			Defect dTemp = udList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					rdList.push(newTemp);
    					defectTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("dStart")) {
    			Defect dTemp = udList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					rdList.push(newTemp);
    					defectTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("dStat")) {
    			Defect dTemp = udList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == dTemp.getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getRemoved().equals(dTemp.getRemoved()) && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated()) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					rdList.push(newTemp);
    					defectTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("dRem")) {
    			Defect dTemp = udList.pop();
    			for(int i = 0; i < defectList().size(); i++) {
    				if(defectList().get(i).getDetail().equals(dTemp.getDetail()) && defectList().get(i).getNum() == dTemp.getNum() && defectList().get(i).getCategory().equals(dTemp.getCategory()) && defectList().get(i).getStartTime() == defectList().get(i).getStartTime() && defectList().get(i).getInjected().equals(dTemp.getInjected()) && defectList().get(i).getStopTime() == dTemp.getStopTime() && defectList().get(i).getProjectName().equals(dTemp.getProjectName()) && defectList().get(i).getDateGenerated() == dTemp.getDateGenerated() && defectList().get(i).getFix().equals(dTemp.getFix()) && defectList().get(i).getStatus().equals(dTemp.getStatus())) {
    					Defect newTemp = defectList().get(i).shallowCopy();
    					defectList().get(i).setNum(dTemp.getNum());
    					defectList().get(i).setCategory(dTemp.getCategory());
    					defectList().get(i).setDateGenerated(dTemp.getDateGenerated());
    					defectList().get(i).setName(dTemp.getName());
    					defectList().get(i).setDetail(dTemp.getDetail());
    					defectList().get(i).setFix(dTemp.getFix());
    					defectList().get(i).setInjected(dTemp.getInjected());
    					defectList().get(i).setProjectName(dTemp.getProjectName());
    					defectList().get(i).setRemoved(dTemp.getRemoved());
    					defectList().get(i).setStartTime(dTemp.getStartTime());
    					defectList().get(i).setStopTime(dTemp.getStopTime());
    					defectList().get(i).setStatus(dTemp.getStatus());
    					defectList().get(i).setTimeDifference(dTemp.getStartTime(), dTemp.getStopTime());
    					rdList.push(newTemp);
    					defectTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("eNum")) {
    			Effort eTemp = ueList.pop();
    			for(int i = 0; i < effortList().size(); i++) {
    				if(effortList().get(i).getDateGenerated() == eTemp.getDateGenerated() && effortList().get(i).getEffortCategory().equals(eTemp.getEffortCategory()) && effortList().get(i).getLifeCycleStep().equals(eTemp.getLifeCycleStep()) && effortList().get(i).getStartTime() == eTemp.getStartTime() && effortList().get(i).getStopTime() == eTemp.getStopTime() && effortList().get(i).getDeliverableInterruption().equals(eTemp.getDeliverableInterruption())) {
    					Effort newTemp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
    					effortList().get(i).setNum(eTemp.getNum());
    					effortList().get(i).setDateGenerated(eTemp.getDateGenerated());
    					effortList().get(i).setDeliverableInterruption(eTemp.getDeliverableInterruption());
    					effortList().get(i).setProjectName(eTemp.getProjectName());
    					effortList().get(i).setLifeCycleStep(eTemp.getLifeCycleStep());
    					effortList().get(i).setStartTime(eTemp.getStartTime());
    					effortList().get(i).setStopTime(eTemp.getStopTime());
    					effortList().get(i).setTimeDifference(eTemp.getStartTime(), eTemp.getStopTime());
    					effortList().get(i).setEffortCategory(eTemp.getEffortCategory());
    					reList.push(newTemp);
    					effortTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("eLife")) {
    			Effort eTemp = ueList.pop();
    			for(int i = 0; i < effortList().size(); i++) {
    				if(effortList().get(i).getDateGenerated() == eTemp.getDateGenerated() && effortList().get(i).getEffortCategory().equals(eTemp.getEffortCategory()) && effortList().get(i).getNum() == eTemp.getNum() && effortList().get(i).getStartTime() == eTemp.getStartTime() && effortList().get(i).getStopTime() == eTemp.getStopTime() && effortList().get(i).getDeliverableInterruption().equals(eTemp.getDeliverableInterruption())) {
    					Effort newTemp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
    					effortList().get(i).setNum(eTemp.getNum());
    					effortList().get(i).setDateGenerated(eTemp.getDateGenerated());
    					effortList().get(i).setDeliverableInterruption(eTemp.getDeliverableInterruption());
    					effortList().get(i).setProjectName(eTemp.getProjectName());
    					effortList().get(i).setLifeCycleStep(eTemp.getLifeCycleStep());
    					effortList().get(i).setStartTime(eTemp.getStartTime());
    					effortList().get(i).setStopTime(eTemp.getStopTime());
    					effortList().get(i).setTimeDifference(eTemp.getStartTime(), eTemp.getStopTime());
    					effortList().get(i).setEffortCategory(eTemp.getEffortCategory());
    					reList.push(newTemp);
    					effortTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("eCat")) {
    			Effort eTemp = ueList.pop();
    			for(int i = 0; i < effortList().size(); i++) {
    				if(effortList().get(i).getDateGenerated() == eTemp.getDateGenerated() && effortList().get(i).getNum() == eTemp.getNum() && effortList().get(i).getLifeCycleStep().equals(eTemp.getLifeCycleStep()) && effortList().get(i).getStartTime() == eTemp.getStartTime() && effortList().get(i).getStopTime() == eTemp.getStopTime() && effortList().get(i).getDeliverableInterruption().equals(eTemp.getDeliverableInterruption())) {
    					Effort newTemp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
    					effortList().get(i).setNum(eTemp.getNum());
    					effortList().get(i).setDateGenerated(eTemp.getDateGenerated());
    					effortList().get(i).setDeliverableInterruption(eTemp.getDeliverableInterruption());
    					effortList().get(i).setProjectName(eTemp.getProjectName());
    					effortList().get(i).setLifeCycleStep(eTemp.getLifeCycleStep());
    					effortList().get(i).setStartTime(eTemp.getStartTime());
    					effortList().get(i).setStopTime(eTemp.getStopTime());
    					effortList().get(i).setTimeDifference(eTemp.getStartTime(), eTemp.getStopTime());
    					effortList().get(i).setEffortCategory(eTemp.getEffortCategory());
    					reList.push(newTemp);
    					effortTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("eStart")) {
    			Effort eTemp = ueList.pop();
    			for(int i = 0; i < effortList().size(); i++) {
    				if(effortList().get(i).getDateGenerated() == eTemp.getDateGenerated() && effortList().get(i).getEffortCategory().equals(eTemp.getEffortCategory()) && effortList().get(i).getNum() == eTemp.getNum() && effortList().get(i).getLifeCycleStep().equals(eTemp.getLifeCycleStep()) && effortList().get(i).getStopTime() == eTemp.getStopTime() && effortList().get(i).getDeliverableInterruption().equals(eTemp.getDeliverableInterruption())) {
    					Effort newTemp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
    					effortList().get(i).setNum(eTemp.getNum());
    					effortList().get(i).setDateGenerated(eTemp.getDateGenerated());
    					effortList().get(i).setDeliverableInterruption(eTemp.getDeliverableInterruption());
    					effortList().get(i).setProjectName(eTemp.getProjectName());
    					effortList().get(i).setLifeCycleStep(eTemp.getLifeCycleStep());
    					effortList().get(i).setStartTime(eTemp.getStartTime());
    					effortList().get(i).setStopTime(eTemp.getStopTime());
    					effortList().get(i).setTimeDifference(eTemp.getStartTime(), eTemp.getStopTime());
    					effortList().get(i).setEffortCategory(eTemp.getEffortCategory());
    					reList.push(newTemp);
    					effortTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("eStop")) {
    			Effort eTemp = ueList.pop();
    			for(int i = 0; i < effortList().size(); i++) {
    				if(effortList().get(i).getDateGenerated() == eTemp.getDateGenerated() && effortList().get(i).getEffortCategory().equals(eTemp.getEffortCategory()) && effortList().get(i).getNum() == eTemp.getNum() && effortList().get(i).getStartTime() == eTemp.getStartTime() && effortList().get(i).getLifeCycleStep().equals(eTemp.getLifeCycleStep()) && effortList().get(i).getDeliverableInterruption().equals(eTemp.getDeliverableInterruption())) {
    					Effort newTemp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
    					effortList().get(i).setNum(eTemp.getNum());
    					effortList().get(i).setDateGenerated(eTemp.getDateGenerated());
    					effortList().get(i).setDeliverableInterruption(eTemp.getDeliverableInterruption());
    					effortList().get(i).setProjectName(eTemp.getProjectName());
    					effortList().get(i).setLifeCycleStep(eTemp.getLifeCycleStep());
    					effortList().get(i).setStartTime(eTemp.getStartTime());
    					effortList().get(i).setStopTime(eTemp.getStopTime());
    					effortList().get(i).setTimeDifference(eTemp.getStartTime(), eTemp.getStopTime());
    					effortList().get(i).setEffortCategory(eTemp.getEffortCategory());
    					reList.push(newTemp);
    					effortTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("eDate")) {
    			Effort eTemp = ueList.pop();
    			for(int i = 0; i < effortList().size(); i++) {
    				if(effortList().get(i).getLifeCycleStep().equals(eTemp.getLifeCycleStep()) && effortList().get(i).getEffortCategory().equals(eTemp.getEffortCategory()) && effortList().get(i).getNum() == eTemp.getNum() && effortList().get(i).getStartTime() == eTemp.getStartTime() && effortList().get(i).getStopTime() == eTemp.getStopTime() && effortList().get(i).getDeliverableInterruption().equals(eTemp.getDeliverableInterruption())) {
    					Effort newTemp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
    					effortList().get(i).setNum(eTemp.getNum());
    					effortList().get(i).setDateGenerated(eTemp.getDateGenerated());
    					effortList().get(i).setDeliverableInterruption(eTemp.getDeliverableInterruption());
    					effortList().get(i).setProjectName(eTemp.getProjectName());
    					effortList().get(i).setLifeCycleStep(eTemp.getLifeCycleStep());
    					effortList().get(i).setStartTime(eTemp.getStartTime());
    					effortList().get(i).setStopTime(eTemp.getStopTime());
    					effortList().get(i).setTimeDifference(eTemp.getStartTime(), eTemp.getStopTime());
    					effortList().get(i).setEffortCategory(eTemp.getEffortCategory());
    					reList.push(newTemp);
    					effortTable_tbl.refresh();
    				}
    			}
    		}
    		else if(temp.equals("eDel")) {
    			Effort eTemp = ueList.pop();
    			for(int i = 0; i < effortList().size(); i++) {
    				if(effortList().get(i).getDateGenerated() == eTemp.getDateGenerated() && effortList().get(i).getEffortCategory().equals(eTemp.getEffortCategory()) && effortList().get(i).getNum() == eTemp.getNum() && effortList().get(i).getStartTime() == eTemp.getStartTime() && effortList().get(i).getStopTime() == eTemp.getStopTime() && effortList().get(i).getLifeCycleStep().equals(eTemp.getLifeCycleStep())) {
    					Effort newTemp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
    					effortList().get(i).setNum(eTemp.getNum());
    					effortList().get(i).setDateGenerated(eTemp.getDateGenerated());
    					effortList().get(i).setDeliverableInterruption(eTemp.getDeliverableInterruption());
    					effortList().get(i).setProjectName(eTemp.getProjectName());
    					effortList().get(i).setLifeCycleStep(eTemp.getLifeCycleStep());
    					effortList().get(i).setStartTime(eTemp.getStartTime());
    					effortList().get(i).setStopTime(eTemp.getStopTime());
    					effortList().get(i).setTimeDifference(eTemp.getStartTime(), eTemp.getStopTime());
    					effortList().get(i).setEffortCategory(eTemp.getEffortCategory());
    					reList.push(newTemp);
    					effortTable_tbl.refresh();
    				}
    			}
    		}
    		//if we undo an action, we place it into relevant redo lists for actions that can be redone
    		actList2.push(temp);
    	}
    }
    /*
     * @author Aztlan Olmedo, inspired by the DataViewer constructed by Matt
     */
    public ObservableList<Effort> effortList(){
		List<Effort> effortDefectIntfEffort = new ArrayList<>();
		effortDefectIntfEffort.addAll(Encryption.getEffort());
		ObservableList<Effort> temp = FXCollections.observableArrayList();
		for(int i = 0; i < effortDefectIntfEffort.size(); i++) {
			temp.add(effortDefectIntfEffort.get(i));
		}
		return temp;
	}
		//make a list that generates an observable list of efforts and a separate one for defects
	public ObservableList<Defect> defectList(){
		List<Defect> effortDefectIntfDefect = new ArrayList<>();
		effortDefectIntfDefect.addAll(Encryption.getDefect());
		ObservableList<Defect> temp = FXCollections.observableArrayList();
		for(int i = 0; i < effortDefectIntfDefect.size(); i++) {
			temp.add(effortDefectIntfDefect.get(i));
		}
		return temp;
	}
	
	private ObservableList<Effort> effortList2 = FXCollections.observableArrayList(effortList());
	private ObservableList<Defect> defectList2 = FXCollections.observableArrayList(defectList());
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) { // the initialize method makes the tables editable, updates the table when changes are made, and prevents improper input (as in spits error messages out and prevents misinput until proper input is provided) 
		defectTable_tbl.setEditable(true);
		defectCategory_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Defect, String> event)->{
            for (int i = 0; i < defectList().size(); i++) {
            	if(defectList().get(i).getNum() == event.getRowValue().getNum() && defectList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && defectList().get(i).getDateGenerated() == event.getRowValue().getDateGenerated() && defectList().get(i).getStatus().equals(event.getRowValue().getStatus()) && defectList().get(i).getFix().equals(event.getRowValue().getFix()) && defectList().get(i).getDetail().equals(event.getRowValue().getDetail()) && defectList().get(i).getInjected().equals(event.getRowValue().getInjected()) && defectList().get(i).getRemoved().equals(event.getRowValue().getRemoved()) && defectList().get(i).getStartTime() == event.getRowValue().getStartTime() && defectList().get(i).getStopTime() ==  event.getRowValue().getStopTime()) {	
            		Defect temp = defectList().get(i).shallowCopy();
            		defectList().get(i).setCategory(event.getNewValue());
            		udList.push(temp);
            		actList1.push("dCat");
            	}
            }
        }); 
		defectCategory_tblCol.setCellFactory(TextFieldTableCell.forTableColumn());
		defectCategory_tblCol.setCellValueFactory(new PropertyValueFactory<>("category"));
		defectTable_tbl.refresh();
		
		
		defectDate_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Defect, LocalDate> event)->{
            for (int i = 0; i < defectList2.size(); i++) {
            	if(defectList().get(i).getNum() == event.getRowValue().getNum() && defectList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && defectList().get(i).getCategory().equals(event.getRowValue().getCategory()) && defectList().get(i).getStatus().equals(event.getRowValue().getStatus()) && defectList().get(i).getFix().equals(event.getRowValue().getFix()) && defectList().get(i).getDetail().equals(event.getRowValue().getDetail()) && defectList().get(i).getInjected().equals(event.getRowValue().getInjected()) && defectList().get(i).getRemoved().equals(event.getRowValue().getRemoved()) && defectList().get(i).getStartTime() == event.getRowValue().getStartTime() && defectList().get(i).getStopTime() ==  event.getRowValue().getStopTime()) {
            		Defect temp = defectList().get(i).shallowCopy();
            		udList.push(temp);
            		defectList().get(i).setDateGenerated(event.getNewValue());
            		actList1.push("dDate");
            	}
            }
        }); 
		defectDate_tblCol.setCellFactory(TextFieldTableCell.<Defect, LocalDate>forTableColumn(new LocalDateStringConverter()));
		defectDate_tblCol.setCellValueFactory(new PropertyValueFactory<>("dateGenerated"));
		
		
		defectDetail_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Defect, String> event)->{
            for (int i = 0; i < defectList().size(); i++) {
            	if(defectList().get(i).getNum() == event.getRowValue().getNum() && defectList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && defectList().get(i).getCategory().equals(event.getRowValue().getCategory()) && defectList().get(i).getDateGenerated() == event.getRowValue().getDateGenerated() && defectList().get(i).getStatus().equals(event.getRowValue().getStatus()) && defectList().get(i).getFix().equals(event.getRowValue().getFix()) && defectList().get(i).getInjected().equals(event.getRowValue().getInjected()) && defectList().get(i).getRemoved().equals(event.getRowValue().getRemoved()) && defectList().get(i).getStartTime() == event.getRowValue().getStartTime() && defectList().get(i).getStopTime() ==  event.getRowValue().getStopTime()) {
            		Defect temp = defectList().get(i).shallowCopy();
            		defectList().get(i).setDetail(event.getNewValue());
            		udList.push(temp);
            		actList1.push("dDetail");
            	}
            }
        }); 
		defectDetail_tblCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	defectDetail_tblCol.setCellValueFactory(new PropertyValueFactory<>("detail"));
    	
    	defectFix_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Defect, String> event)->{
            for (int i = 0; i < defectList().size(); i++) {
            	if(defectList().get(i).getNum() == event.getRowValue().getNum() && defectList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && defectList().get(i).getCategory().equals(event.getRowValue().getCategory()) && defectList().get(i).getDateGenerated() == event.getRowValue().getDateGenerated() && defectList().get(i).getStatus().equals(event.getRowValue().getStatus()) && defectList().get(i).getDetail().equals(event.getRowValue().getDetail()) && defectList().get(i).getInjected().equals(event.getRowValue().getInjected()) && defectList().get(i).getRemoved().equals(event.getRowValue().getRemoved()) && defectList().get(i).getStartTime() == event.getRowValue().getStartTime() && defectList().get(i).getStopTime() ==  event.getRowValue().getStopTime()) {
            		Defect temp = defectList().get(i).shallowCopy();
            		defectList().get(i).setFix(event.getNewValue());
            		udList.push(temp);
            		actList1.push("dFix");
            	}
            }
        });
    	defectFix_tblCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	defectFix_tblCol.setCellValueFactory(new PropertyValueFactory<>("fix"));
    	
    	defectInject_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Defect, String> event)->{
            for (int i = 0; i < defectList().size(); i++) {
            	if(defectList().get(i).getNum() == event.getRowValue().getNum() && defectList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && defectList().get(i).getCategory().equals(event.getRowValue().getCategory()) && defectList().get(i).getDateGenerated() == event.getRowValue().getDateGenerated() && defectList().get(i).getStatus().equals(event.getRowValue().getStatus()) && defectList().get(i).getFix().equals(event.getRowValue().getFix()) && defectList().get(i).getDetail().equals(event.getRowValue().getDetail()) && defectList().get(i).getRemoved().equals(event.getRowValue().getRemoved()) && defectList().get(i).getStartTime() == event.getRowValue().getStartTime() && defectList().get(i).getStopTime() ==  event.getRowValue().getStopTime()) {
            		Defect temp = defectList().get(i).shallowCopy();
            		defectList().get(i).setInjected(event.getNewValue());
            		udList.push(temp);
            		actList1.push("dInj");
            	}
            }
        });
    	defectInject_tblCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	defectInject_tblCol.setCellValueFactory(new PropertyValueFactory<>("injected"));
    	
    	defectNumber_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Defect, Integer> event)->{
            for (int i = 0; i < defectList().size(); i++) {
            	if(defectList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && defectList().get(i).getCategory().equals(event.getRowValue().getCategory()) && defectList().get(i).getDateGenerated() == event.getRowValue().getDateGenerated() && defectList().get(i).getStatus().equals(event.getRowValue().getStatus()) && defectList().get(i).getFix().equals(event.getRowValue().getFix()) && defectList().get(i).getDetail().equals(event.getRowValue().getDetail()) && defectList().get(i).getInjected().equals(event.getRowValue().getInjected()) && defectList().get(i).getRemoved().equals(event.getRowValue().getRemoved()) && defectList().get(i).getStartTime() == event.getRowValue().getStartTime() && defectList().get(i).getStopTime() ==  event.getRowValue().getStopTime()) {
            		Defect temp = defectList().get(i).shallowCopy();
            		defectList().get(i).setNum(event.getNewValue());
            		udList.push(temp);
            		actList1.push("dNum");
            	}
            }
        });
        defectNumber_tblCol.setCellFactory(TextFieldTableCell.<Defect, Integer>forTableColumn(new IntegerStringConverter()));
    	defectNumber_tblCol.setCellValueFactory(new PropertyValueFactory<>("num"));
    	
    	defectRemoved_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Defect, String> event)->{
            for (int i = 0; i < defectList().size(); i++) {
            	if(defectList().get(i).getNum() == event.getRowValue().getNum() && defectList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && defectList().get(i).getCategory().equals(event.getRowValue().getCategory()) && defectList().get(i).getDateGenerated() == event.getRowValue().getDateGenerated() && defectList().get(i).getStatus().equals(event.getRowValue().getStatus()) && defectList().get(i).getFix().equals(event.getRowValue().getFix()) && defectList().get(i).getDetail().equals(event.getRowValue().getDetail()) && defectList().get(i).getInjected().equals(event.getRowValue().getInjected()) && defectList().get(i).getStartTime() == event.getRowValue().getStartTime() && defectList().get(i).getStopTime() ==  event.getRowValue().getStopTime()) {
            		Defect temp = defectList().get(i).shallowCopy();
            		defectList().get(i).setRemoved(event.getNewValue());
            		udList.push(temp);
            		actList1.push("dRem");
            	}
            }
        });
    	defectRemoved_tblCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	defectRemoved_tblCol.setCellValueFactory(new PropertyValueFactory<>("removed"));
    	
    	defectStart_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Defect, LocalTime> event)->{
            for (int i = 0; i < defectList2.size(); i++) {
            	if(defectList().get(i).getNum() == event.getRowValue().getNum() && defectList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && defectList().get(i).getCategory().equals(event.getRowValue().getCategory()) && defectList().get(i).getDateGenerated() == event.getRowValue().getDateGenerated() && defectList().get(i).getStatus().equals(event.getRowValue().getStatus()) && defectList().get(i).getFix().equals(event.getRowValue().getFix()) && defectList().get(i).getDetail().equals(event.getRowValue().getDetail()) && defectList().get(i).getInjected().equals(event.getRowValue().getInjected()) && defectList().get(i).getRemoved().equals(event.getRowValue().getRemoved()) && defectList().get(i).getStopTime() ==  event.getRowValue().getStopTime()) {
            		Defect temp = defectList().get(i).shallowCopy();
            		defectList().get(i).setStartTime(event.getNewValue());
            		defectList().get(i).setTimeDifference(defectList().get(i).getStartTime(), defectList().get(i).getStopTime());
            		udList.push(temp);
            		defectTable_tbl.refresh();
            		actList1.push("dStart");
            	}
            }
        });
    	defectStart_tblCol.setCellFactory(TextFieldTableCell.<Defect, LocalTime>forTableColumn(new LocalTimeStringConverter()));
    	defectStart_tblCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
    	
    	
    	defectStatus_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Defect, String> event)->{
            for (int i = 0; i < defectList().size(); i++) {
            	if(defectList().get(i).getNum() == event.getRowValue().getNum() && defectList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && defectList().get(i).getCategory().equals(event.getRowValue().getCategory()) && defectList().get(i).getDateGenerated() == event.getRowValue().getDateGenerated()  && defectList().get(i).getFix().equals(event.getRowValue().getFix()) && defectList().get(i).getDetail().equals(event.getRowValue().getDetail()) && defectList().get(i).getInjected().equals(event.getRowValue().getInjected()) && defectList().get(i).getRemoved().equals(event.getRowValue().getRemoved()) && defectList().get(i).getStartTime() == event.getRowValue().getStartTime() && defectList().get(i).getStopTime() ==  event.getRowValue().getStopTime()) {
            		Defect temp = defectList().get(i).shallowCopy();
            		defectList().get(i).setStatus(event.getNewValue());
            		udList.push(temp);
            		actList1.push("dStat");
            	}
            }
        });
    	defectStatus_tblCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	defectStatus_tblCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    	
    	defectStop_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Defect, LocalTime> event)->{
            for (int i = 0; i < defectList2.size(); i++) {
            	if(defectList().get(i).getNum() == event.getRowValue().getNum() && defectList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && defectList().get(i).getCategory().equals(event.getRowValue().getCategory()) && defectList().get(i).getDateGenerated() == event.getRowValue().getDateGenerated() && defectList().get(i).getStatus().equals(event.getRowValue().getStatus()) && defectList().get(i).getFix().equals(event.getRowValue().getFix()) && defectList().get(i).getDetail().equals(event.getRowValue().getDetail()) && defectList().get(i).getInjected().equals(event.getRowValue().getInjected()) && defectList().get(i).getRemoved().equals(event.getRowValue().getRemoved()) && defectList().get(i).getStartTime() == event.getRowValue().getStartTime()) {
            		Defect temp = defectList().get(i).shallowCopy();
            		defectList().get(i).setStopTime(event.getNewValue());
            		defectList().get(i).setTimeDifference(defectList().get(i).getStartTime(), defectList().get(i).getStopTime());
            		udList.push(temp);
            		defectTable_tbl.refresh();
            		actList1.push("dStop");
            	}
            }
        });
    	defectStop_tblCol.setCellFactory(TextFieldTableCell.<Defect, LocalTime>forTableColumn(new LocalTimeStringConverter()));
    	defectStop_tblCol.setCellValueFactory(new PropertyValueFactory<>("stopTime"));
    	
    	
    	
    	//users cannot edit defectTime_tblCol, there is no reason to and it would entail requiring me to fabricate Start and Stop times to match the input
    	defectTime_tblCol.setCellValueFactory(new PropertyValueFactory<>("timeMinutesDifference"));
    	defectTable_tbl.setItems(defectList2);//populate the table with observable list
    	
    	effortTable_tbl.setEditable(true);
    	
    	effortDate_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Effort, LocalDate> event)->{
            for (int i = 0; i < defectList().size(); i++) {
            	if(effortList().get(i).getNum() == event.getRowValue().getNum() && effortList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && effortList().get(i).getEffortCategory().equals(event.getRowValue().getEffortCategory()) && effortList().get(i).getDeliverableInterruption().equals(event.getRowValue().getDeliverableInterruption()) && effortList().get(i).getLifeCycleStep().equals(event.getRowValue().getLifeCycleStep()) && effortList().get(i).getStartTime() == event.getRowValue().getStartTime() && effortList().get(i).getStopTime() ==  event.getRowValue().getStopTime()){
            		Effort temp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), event.getOldValue(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
            		effortList().get(i).setDateGenerated(event.getNewValue());
            		ueList.push(temp);
            		actList1.push("eDate");
            	}
            }
        });
    	effortDate_tblCol.setCellFactory(TextFieldTableCell.<Effort, LocalDate>forTableColumn(new LocalDateStringConverter()));
    	effortDate_tblCol.setCellValueFactory(new PropertyValueFactory<>("dateGenerated"));
    	
    	effortDeliverable_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Effort, String> event)->{
            for (int i = 0; i < effortList().size(); i++) {
            	if(effortList().get(i).getNum() == event.getRowValue().getNum() && effortList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && effortList().get(i).getEffortCategory().equals(event.getRowValue().getEffortCategory()) && effortList().get(i).getDateGenerated() == event.getRowValue().getDateGenerated() && effortList().get(i).getLifeCycleStep().equals(event.getRowValue().getLifeCycleStep()) && effortList().get(i).getStartTime() == event.getRowValue().getStartTime() && effortList().get(i).getStopTime() ==  event.getRowValue().getStopTime()) {
            		Effort temp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), event.getOldValue());
            		effortList().get(i).setDeliverableInterruption(event.getNewValue());            	
            		ueList.push(temp);
            		actList1.push("eDel");
            	}
            }
        });
    	effortDeliverable_tblCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	effortDeliverable_tblCol.setCellValueFactory(new PropertyValueFactory<>("deliverableInterruption"));
    	
    	effortLifeCycle_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Effort, String> event)->{
            for (int i = 0; i < effortList().size(); i++) {
            	if(effortList().get(i).getNum() == event.getRowValue().getNum() && effortList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && effortList().get(i).getEffortCategory().equals(event.getRowValue().getEffortCategory()) && effortList().get(i).getDateGenerated() == event.getRowValue().getDateGenerated() && effortList().get(i).getDeliverableInterruption().equals(event.getRowValue().getDeliverableInterruption()) && effortList().get(i).getStartTime() == event.getRowValue().getStartTime() && effortList().get(i).getStopTime() ==  event.getRowValue().getStopTime()){
            		Effort temp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), event.getOldValue(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
            		effortList().get(i).setLifeCycleStep(event.getNewValue());
            		ueList.push(temp);
            		actList1.push("eLife");
            	}
            }
        });
    	effortLifeCycle_tblCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	effortLifeCycle_tblCol.setCellValueFactory(new PropertyValueFactory<>("lifeCycleStep"));
    	
    	effortNumber_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Effort, Integer> event)->{
            for (int i = 0; i < effortList().size(); i++) {
            	if(effortList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && effortList().get(i).getEffortCategory().equals(event.getRowValue().getEffortCategory()) && effortList().get(i).getDateGenerated() == event.getRowValue().getDateGenerated() && effortList().get(i).getDeliverableInterruption().equals(event.getRowValue().getDeliverableInterruption()) && effortList().get(i).getLifeCycleStep().equals(event.getRowValue().getLifeCycleStep()) && effortList().get(i).getStartTime() == event.getRowValue().getStartTime() && effortList().get(i).getStopTime() ==  event.getRowValue().getStopTime()){
            		Effort temp = new Effort(event.getOldValue(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
            		effortList().get(i).setNum(event.getNewValue());
            		ueList.push(temp);
            		actList1.push("eNum");
            	}
            }
        });
    	effortNumber_tblCol.setCellFactory(TextFieldTableCell.<Effort, Integer>forTableColumn(new IntegerStringConverter()));
    	effortNumber_tblCol.setCellValueFactory(new PropertyValueFactory<>("num"));
    	
    	effortStart_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Effort, LocalTime> event)->{
            for (int i = 0; i < effortList().size(); i++) {
            	if(effortList().get(i).getNum() == event.getRowValue().getNum() && effortList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && effortList().get(i).getEffortCategory().equals(event.getRowValue().getEffortCategory()) && effortList().get(i).getDateGenerated() == event.getRowValue().getDateGenerated() && effortList().get(i).getDeliverableInterruption().equals(event.getRowValue().getDeliverableInterruption()) && effortList().get(i).getLifeCycleStep().equals(event.getRowValue().getLifeCycleStep()) && effortList().get(i).getStopTime() ==  event.getRowValue().getStopTime()){
            		Effort temp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), event.getOldValue(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
            		effortList().get(i).setStartTime(event.getNewValue());
            		effortList().get(i).setTimeDifference(effortList().get(i).getStartTime(), effortList().get(i).getStopTime());
            		ueList.push(temp);
            		effortTable_tbl.refresh();
            		actList1.push("eStart");
            		
            	}
            }
        });
    	effortStart_tblCol.setCellFactory(TextFieldTableCell.<Effort, LocalTime>forTableColumn(new LocalTimeStringConverter()));
    	effortStart_tblCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
    	
    	effortStop_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Effort, LocalTime> event)->{
            for (int i = 0; i < effortList().size(); i++) {
            	if(effortList().get(i).getNum() == event.getRowValue().getNum() && effortList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && effortList().get(i).getEffortCategory().equals(event.getRowValue().getEffortCategory()) && effortList().get(i).getDateGenerated() == event.getRowValue().getDateGenerated() && effortList().get(i).getDeliverableInterruption().equals(event.getRowValue().getDeliverableInterruption()) && effortList().get(i).getLifeCycleStep().equals(event.getRowValue().getLifeCycleStep()) && effortList().get(i).getStartTime() == event.getRowValue().getStartTime()){
            		Effort temp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), event.getOldValue(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), effortList().get(i).getEffortCategory(), effortList().get(i).getDeliverableInterruption());
            		effortList().get(i).setStopTime(event.getNewValue());
            		effortList().get(i).setTimeDifference(effortList().get(i).getStartTime(), effortList().get(i).getStopTime());
            		ueList.push(temp);
            		effortTable_tbl.refresh();
            		actList1.push("eStop");
            	}
            }
        });
    	effortStop_tblCol.setCellFactory(TextFieldTableCell.<Effort, LocalTime>forTableColumn(new LocalTimeStringConverter()));
    	effortStop_tblCol.setCellValueFactory(new PropertyValueFactory<>("stopTime"));
    	
    	effortTime_tblCol.setCellValueFactory(new PropertyValueFactory<>("timeMinutesDifference"));//similar reasoning for why users cannot alter defectTime_tblCol
    	
    	efforteffortCategory_tblCol.setOnEditCommit((TableColumn.CellEditEvent<Effort, String> event)->{
            for (int i = 0; i < effortList().size(); i++) {
            	if(effortList().get(i).getNum() == event.getRowValue().getNum() && effortList().get(i).getProjectName().equals(event.getRowValue().getProjectName()) && effortList().get(i).getDateGenerated() == event.getRowValue().getDateGenerated() && effortList().get(i).getDeliverableInterruption().equals(event.getRowValue().getDeliverableInterruption()) && effortList().get(i).getLifeCycleStep().equals(event.getRowValue().getLifeCycleStep()) && effortList().get(i).getStartTime() == event.getRowValue().getStartTime() && effortList().get(i).getStopTime() ==  event.getRowValue().getStopTime()){
            		Effort temp = new Effort(effortList().get(i).getNum(), effortList().get(i).getProjectName(), effortList().get(i).getDateGenerated(), effortList().get(i).getStartTime(), effortList().get(i).getStopTime(), effortList().get(i).getTimeMinutesDifference(), effortList().get(i).getLifeCycleStep(), event.getOldValue(), effortList().get(i).getDeliverableInterruption());
            		effortList().get(i).setEffortCategory(event.getNewValue());
            		ueList.push(temp);
            		actList1.push("eCat");
            	}
            }
        });
    	efforteffortCategory_tblCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	efforteffortCategory_tblCol.setCellValueFactory(new PropertyValueFactory<>("effortCategory"));
    	effortTable_tbl.setItems(effortList2);
	}
}