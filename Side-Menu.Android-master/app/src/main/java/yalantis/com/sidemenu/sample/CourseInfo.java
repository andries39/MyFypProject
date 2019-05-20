package yalantis.com.sidemenu.sample;

public class CourseInfo {
    String average_Result,mode,duration,room,desctiption,programmeCode,name,link,id1,id2,id3;
    int count;

    public CourseInfo() {
    }

    public CourseInfo(String average_Result, String mode, String duration, String room, String desctiption, String programmeCode, String name, String link, String id1, String id2, String id3, int count) {
        this.average_Result = average_Result;
        this.mode = mode;
        this.duration = duration;
        this.room = room;
        this.desctiption = desctiption;
        this.programmeCode = programmeCode;
        this.name = name;
        this.link = link;
        this.id1 = id1;
        this.id2 = id2;
        this.id3 = id3;
        this.count = count;
    }

    public String getAverage_Result() {
        return average_Result;
    }

    public void setAverage_Result(String average_Result) {
        this.average_Result = average_Result;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }

    public String getProgrammeCode() {
        return programmeCode;
    }

    public void setProgrammeCode(String programmeCode) {
        this.programmeCode = programmeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getId3() {
        return id3;
    }

    public void setId3(String id3) {
        this.id3 = id3;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
