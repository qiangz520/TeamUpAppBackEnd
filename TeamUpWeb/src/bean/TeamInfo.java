package bean;

import java.io.Serializable;

public class TeamInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int activityID;
    private String  issuerNickName;
    private String title;
    private String description;
    private String issueTime;
    private String startTime;
    private String place;
    private String demand;
    private String contactMethod;
    private int maxNumber;
    private int joinedNumber;
    private int weight;

    @Override
    public String toString() {
        return "{" +
                "\"activityID\":\"" + activityID + '\"' +
                ",\"issuerNickName\":\"" + issuerNickName + '\"' +
                ", \"title\":\"" + title + '\"' +
                ", \"description\":\"" + description + '\"' +
                ", \"issueTime\":\"" + issueTime + '\"' +
                ", \"startTime\":\"" + startTime + '\"' +
                ", \"place\":\"" + place + '\"' +
                ", \"demand\":\"" + demand + '\"' +
                ", \"contactMethod\":\"" + contactMethod+ '\"'+
                ", \"maxNumber\":\"" + maxNumber+ '\"'+
                ", \"joinedNumber\":\"" + joinedNumber+'\"' +
                '}';
    }

    public TeamInfo(int activity_id, String issuerNickName, String title, String description, String issueTime,
                    String startTime, String place, String demand, String contactMethod, int maxNumber, int joinedNumber) {
        this.activityID = activity_id;
        this.issuerNickName = issuerNickName;
        this.title = title;
        this.description = description;
        this.issueTime = issueTime;
        this.startTime = startTime;
        this.place = place;
        this.demand = demand;
        this.contactMethod = contactMethod;
        this.maxNumber = maxNumber;
        this.joinedNumber = joinedNumber;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setIssueTime(String issueTime) {
        this.issueTime = issueTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public void setJoinedNumber(int joinedNumber) {
        this.joinedNumber = joinedNumber;
    }

    public void setIssuerNickName(String issuerNickName) {
        this.issuerNickName = issuerNickName;
    }

    public String getIssuerNickName() {
        return issuerNickName;
    }

    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }

    public String getIssueTime() {
        return issueTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getPlace() {
        return place;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public int getJoinedNumber() {
        return joinedNumber;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public String getDemand() {
        return demand;
    }

    public String getContactMethod() {
        return contactMethod;
    }

    public void setContactMethod(String contactMethod) {
        this.contactMethod = contactMethod;
    }

    public int getActivityID() {
        return activityID;
    }

    public void setActivityID(int activityID) {
        this.activityID = activityID;
    }
}
