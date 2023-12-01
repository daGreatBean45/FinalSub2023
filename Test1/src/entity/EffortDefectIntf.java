package entity;

import java.time.LocalDate;
import java.time.LocalTime;

public interface EffortDefectIntf {
	int getNum();
	void setNum(int num);
	String getProjectName();
	void setProjectName(String projectName);
	LocalDate getDateGenerated();
	void setDateGenerated(LocalDate dateGenerated);
	LocalTime getStartTime();
	void setStartTime(LocalTime startTime);
	LocalTime getStopTime();
	void setStopTime(LocalTime stopTime);
	int getTimeMinutesDifference();
	void setTimeDifference(LocalTime startTime, LocalTime stopTime);
	void print();
}
