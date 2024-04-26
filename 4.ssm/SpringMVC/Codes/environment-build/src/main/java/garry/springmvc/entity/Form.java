package garry.springmvc.entity;

import java.util.List;

public class Form {
    private String name;
    private String course;
    private List<Integer> purpose;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public List<Integer> getPurpose() {
        return purpose;
    }

    public void setPurpose(List<Integer> purpose) {
        this.purpose = purpose;
    }

    @Override
    public String toString() {
        return "Form{" +
                "name='" + name + '\'' +
                ", course='" + course + '\'' +
                ", purpose=" + purpose +
                '}';
    }
}
