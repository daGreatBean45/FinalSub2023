package entity;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 
 * @author Edited by the entire team throughout duration of project
 *
 */
//Simulates the effort class for the purposes of this prototype with possible tweaks later on. This is to validate the idea of bheing able to encrypt Effort objects and store comma delimited data that isnt necessarily classified as a csv.
public class Effort implements Serializable, EffortDefectIntf {
	// TODO
			 	private int num;
			 	private String projectName;
			    private LocalDate dateGenerated;
			    private LocalTime startTime;
			    private LocalTime stopTime;
			    private int timeMinutesDifference;
			    private String lifeCycleStep;
			    private String effortCategory;
			    private String deliverableInterruption;
 

			    public Effort(int num, String projectName, LocalDate dateGenerated, LocalTime startTime, LocalTime stopTime, int timeMinutesDifference, String lifeCycleStep, String effortCategory, String deliverableInterruption){
			        this.num = num;
			        this.projectName = projectName;
			        this.dateGenerated = dateGenerated;
			        this.startTime = startTime;
			        this.stopTime = stopTime;
			        this.timeMinutesDifference = timeMinutesDifference;
			        this.lifeCycleStep = lifeCycleStep;
			        this.effortCategory = effortCategory;
 			        this.deliverableInterruption = deliverableInterruption;
 		
		 
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

			    public String getLifeCycleStep(){
			        return lifeCycleStep;
			    }

			    public void setLifeCycleStep(String lifeCycleStep){
			        this.lifeCycleStep = lifeCycleStep;
			    }

			    public String getEffortCategory(){
			        return effortCategory;
			    }

			    public void setEffortCategory(String effortCategory){
			        this.effortCategory = effortCategory;
			    }
			    public String getDeliverableInterruption(){
			        return deliverableInterruption;
			    }

			    public void setDeliverableInterruption(String deliverableInterruption){
			        this.deliverableInterruption = deliverableInterruption;
			    }

			   
			    @Override
			    public void print(){
			        System.out.println("Num " + num + " ProjectName: " + projectName + " dateGenerated: " + dateGenerated + " startTime: " + startTime + " stopTime: " + stopTime + " timeMinutesDifference: " + timeMinutesDifference + " lifecyclestep: " + lifeCycleStep + " effortCategory: " + effortCategory + " deliverableInterruption: " + deliverableInterruption);
 			    }

			    //Customized toString method in order to form something what would be similar to a .csv without using the extension but still follows the pattern.
			    @Override
			    public String toString(){
			        return "Effort" + "," + num + "," + projectName + "," + dateGenerated + "," + startTime + "," + stopTime + "," + timeMinutesDifference + "," + lifeCycleStep + "," + effortCategory + "," + deliverableInterruption;
 
  			    }

    
}