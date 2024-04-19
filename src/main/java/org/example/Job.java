package org.example;

public class Job {
    private int jobClassID;
    private String jobTitle;
    private double hourlyWage;

    public Job(int jobClassID, String jobTitle, double hourlyWage) {
        this.jobClassID = jobClassID;
        this.jobTitle = jobTitle;
        this.hourlyWage = hourlyWage;
    }

    public int getJobClassID() {
        return jobClassID;
    }

    public void setJobClassID(int jobClassID) {
        this.jobClassID = jobClassID;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public double getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    @Override
    public String toString() {
        return "Job{" +
                "jobClassID=" + jobClassID +
                ", jobTitle='" + jobTitle + '\'' +
                ", hourlyWage=" + hourlyWage +
                '}';
    }
}