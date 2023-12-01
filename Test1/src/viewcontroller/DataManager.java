package viewcontroller;

import java.time.LocalTime;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataManager {
	
	private static ObservableList<String> projectNamesLocal = FXCollections.observableArrayList();
	private static ObservableList<String> lifeCycleStepsLocal = FXCollections.observableArrayList();
	private static ObservableList<String> effortCategoriesLocal = FXCollections.observableArrayList();
	private static ObservableList<String> plansLocal = FXCollections.observableArrayList();
	private static ObservableList<String> deliverablesLocal = FXCollections.observableArrayList();
	private static ObservableList<String> interruptionsLocal = FXCollections.observableArrayList();
	private static ObservableList<String> defectCategoriesLocal = FXCollections.observableArrayList();
 
	public static void setProjectNames(ObservableList<String> projectNames) {
		projectNamesLocal = projectNames;
	}
	
	public static ObservableList<String> getProjectNames() {
		return projectNamesLocal;
	}
	
	public static void setEffortCategories(ObservableList<String> effortCategories) {
		effortCategoriesLocal = effortCategories;
	}
	
	public static ObservableList<String> getEffortCategories() {
		return effortCategoriesLocal;
	}
	
	
	public static void setLifeCycleSteps(ObservableList<String> lifeCycleSteps) {
		lifeCycleStepsLocal = lifeCycleSteps;
	}
	
	public static ObservableList<String> getLifeCycleSteps() {
		return lifeCycleStepsLocal;
	}
	
	
	public static void setPlans(ObservableList<String> plans) {
		plansLocal = plans;
	}
	
	public static ObservableList<String> getPlans() {
		return plansLocal;
	}
	
	
	public static void setDeliverables(ObservableList<String> deliverables) {
		deliverablesLocal = deliverables;
	}
	
	public static ObservableList<String> getDeliverables() {
		return deliverablesLocal;
	}
	
	
	public static void setInterruptions(ObservableList<String> interruptions) {
		interruptionsLocal = interruptions;
	}
	
	public static ObservableList<String> getInterruptions() {
		return interruptionsLocal;
	}
	
	public static void setDefectCategories(ObservableList<String> defectCategories) {
		defectCategoriesLocal = defectCategories;
	}
	
	public static ObservableList<String> getDefectCategories() {
		return defectCategoriesLocal;
	}
	
	//Lines 79-116 serve to reporpulate the columns used in the tableview for DataViewController when moving between views.
	private static Vector<String> projectNamesDataLocal = new Vector<>();
	private static Vector<String> effortDefectDataLocal = new Vector<>();
	private static Vector<String> avgTimeDataLocal = new Vector<>();
	private static Vector<String> totalTimeDataLocal = new Vector<>();
	
	
	
	public static void setProjectNamesData(Vector<String> projectNamesData) {
		projectNamesDataLocal = projectNamesData;
	}
	
	public static Vector<String> getProjectNamesData(){
		return projectNamesDataLocal;
	}
	
	public static void setEffortDefectData(Vector<String> effortDefectData) {
		effortDefectDataLocal = effortDefectData;
	}
	
	public static Vector<String> getEffortDefectData(){
		return effortDefectDataLocal;
	}
	
	public static void setAvgTimeData(Vector<String> avgTimeData) {
		avgTimeDataLocal = avgTimeData;
	}
	
	public static Vector<String> getAvgTimeData(){
		return avgTimeDataLocal;
	}
	
	public static void setTotalTimeData(Vector<String> totalTimeData) {
		totalTimeDataLocal = totalTimeData;
	}
	
	public static Vector<String> getTotalTimeData(){
		return totalTimeDataLocal;
	}

	
	////////////////////////////////////////////////////////////////////Defect View Methods
	private static String statusLocal = "" ; 
	
	public static void setStatus(String status) {
		statusLocal = status; 
	}
	
	public static String getStatus() {
		return statusLocal; 
	}
	
	private static boolean ReopenLocal = false;
	
	public static void setReopen(boolean Reopen) {
		ReopenLocal = Reopen; 
	}
	public static boolean getReopen() {
		return ReopenLocal; 
	}
	
	private static boolean CloseLocal = true;
	
	public static void setClose(boolean Close) {
		CloseLocal = Close; 
	}
	public static boolean getClose() {
		return CloseLocal; 
	}
	
	private static int numTrackerLocal = 0;
	
	public static void setnumTracker(int numTracker) {
		numTrackerLocal = numTracker; 
	}
	
	public static int getnumTracker() {
		return numTrackerLocal; 
	}
	
	private static String selectDefLocal = ""; 
	
	public static void setselectDef(String selectDefData) {
		selectDefLocal = selectDefData; 
		
	}
	public static String getselectDef() {
		return selectDefLocal; 
	}
	 
	private static String selectProjectLocal = ""; 
	
	public static void setselectProject(String projectname) {
		selectProjectLocal = projectname;
		
	}
	public static String getselectProject() {
		return selectProjectLocal; 
	}
	
	private static LocalTime effortCreationStartTime = null;
	private static String effortCreationProject = null;
	private static String effortCreationStep = null;
	private static String effortCreationDefect = null;
	private static String effortCreationCategory = null;

	public static LocalTime getEffortCreationStartTime() {
		return effortCreationStartTime;
	}

	public static void setEffortCreationStartTime(LocalTime effortCreationStartTime) {
		DataManager.effortCreationStartTime = effortCreationStartTime;
	}

	public static String getEffortCreationProject() {
		return effortCreationProject;
	}

	public static void setEffortCreationProject(String effortCreationProject) {
		DataManager.effortCreationProject = effortCreationProject;
	}

	public static String getEffortCreationStep() {
		return effortCreationStep;
	}

	public static void setEffortCreationStep(String effortCreationStep) {
		DataManager.effortCreationStep = effortCreationStep;
	}

	public static String getEffortCreationDefect() {
		return effortCreationDefect;
	}

	public static void setEffortCreationDefect(String effortCreationDefect) {
		DataManager.effortCreationDefect = effortCreationDefect;
	}

	public static String getEffortCreationCategory() {
		return effortCreationCategory;
	}

	public static void setEffortCreationCategory(String effortCreationCategory) {
		DataManager.effortCreationCategory = effortCreationCategory;
	}
	 
}
