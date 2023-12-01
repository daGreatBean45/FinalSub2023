package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * 
 * @author Edited by the entire team throughout duration of project
 *
 */

public class Defect implements Serializable, EffortDefectIntf {
	// TODO
	 	private int num;
	 	private String projectName;
	    private LocalDate dateGenerated;
	    private LocalTime startTime;
	    private LocalTime stopTime;
	    private int timeMinutesDifference;
	    private String name;
	    private String detail;
	    private String injected;
	    private String removed;
	    private String category;
	    private String status;
	    private String fix;

	    public Defect(int num, String projectName, LocalDate dateGenerated, LocalTime startTime, LocalTime stopTime, int timeMinutesDifference, String name, String detail, String injected, String removed, String category, String status, String fix){
	        this.num = num;
	        this.projectName = projectName;
	        this.dateGenerated = dateGenerated;
	        this.startTime = startTime;
	        this.stopTime = stopTime;
	        this.timeMinutesDifference = timeMinutesDifference;
	        this.name = name;
	        this.detail = detail;
	        this.injected = injected;
	        this.removed = removed;
	        this.category = category;
	        this.status = status;
	        this.fix = fix;
	    }
	    public Defect shallowCopy()
	    {
	    	return new Defect(
	    			num,
	    			projectName,
	    			dateGenerated,
	    			startTime,
	    			stopTime,
	    			timeMinutesDifference,
	    			name,
	    			detail,
	    			injected,
	    			removed,
	    			category,
	    			status,
	    			fix
	    			);
	    }

	    @Override
	    public int getNum(){
	        return num;
	    }
	    @Override
	    public void setNum(int num){
	        this.num = num;
	    }
	    @Override
	    public String getProjectName() {
	    	return projectName;
	    }
	    @Override
	    public void setProjectName(String projectName) {
	    	 this.projectName = projectName;
	    }
	    @Override
	    public LocalDate getDateGenerated(){
	        return dateGenerated;
	    }
	    @Override
	    public void setDateGenerated(LocalDate dateGenerated){
	        this.dateGenerated = dateGenerated;
	    }
	    @Override
	    public LocalTime getStartTime(){
	        return startTime;
	    }
	    @Override
	    public void setStartTime(LocalTime startTime){
	        this.startTime = startTime;
	    }
	    @Override
	    public LocalTime getStopTime(){
	        return stopTime;
	    }
	    @Override
	    public void setStopTime(LocalTime stopTime){
	        this.stopTime = stopTime;
	    }
	    @Override
	    public int getTimeMinutesDifference(){
	        return timeMinutesDifference;
	    }
	    @Override
	    public void setTimeDifference(LocalTime startTime, LocalTime stopTime){
	        long differenceOfTime = ChronoUnit.MINUTES.between(startTime, stopTime); 
	        this.timeMinutesDifference = (int)differenceOfTime;
	    }
	    

	    public String getName(){
	        return name;
	    }

	    public void setName(String name){
	        this.name = name;
	    }

	    public String getDetail(){
	        return detail;
	    }

	    public void setDetail(String detail){
	        this.detail = detail;
	    }
	    public String getInjected() {
	    	return injected;
	    }
	    public void setInjected(String injected) {
	    	this.injected = injected;
	    }
	    public String getRemoved(){
	        return removed;
	    }

	    public void setRemoved(String removed){
	        this.removed = removed;
	    }
	    public String getCategory(){
	        return category;
	    }

	    public void setCategory(String category){
	        this.category = category;
	    }

	    public String getStatus(){
	        return status;
	    }

	    public void setStatus(String status){
	        this.status = status;
	    }

	    public String getFix(){
	        return fix;
	    }

	    public void setFix(String fix){
	        this.fix = fix;
	    }
	    @Override
	    public void print(){
	        System.out.println("Num " + num + " ProjectName: " + projectName + " dateGenerated: " + dateGenerated + " startTime: " + startTime + " stopTime: " + stopTime + " timeMinutesDifference: " + timeMinutesDifference + " name: " + name +" detail: " + detail + " injected: " + injected + " removed: " + removed + " category: " + category + " status: " + status + " fix: " + fix);
	    }

	    //Customized toString method in order to form something what would be similar to a .csv without using the extension but still follows the pattern.
	    @Override
	    public String toString(){
	        return "Defect" + "," + num + "," + projectName + "," + dateGenerated + "," + startTime + "," + stopTime + "," + timeMinutesDifference + "," + name + ","+ detail + "," + injected + "," + removed + "," + category + "," + status + "," + fix;
	    }
}
