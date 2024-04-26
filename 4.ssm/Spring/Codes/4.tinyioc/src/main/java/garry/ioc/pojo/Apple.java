package garry.ioc.pojo;

/**
 * @author Garry
 * ---------2024/2/29 20:20
 **/
public class Apple {
    private String title;
    private String location;
    private String color;

    public Apple() {
    }

    public Apple(String title, String location, String color) {
        this.title = title;
        this.location = location;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    public void show() {
        System.out.println(this.getClass() + " -> " + this.toString());
    }
}
