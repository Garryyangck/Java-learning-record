package garry.pojo;

/**
 * @author Garry
 * ---------2024/2/29 20:21
 **/
public class Child {
    private String name;
    private Apple apple;

    public Child() {
    }

    public Child(String name, Apple apple) {
        this.name = name;
        this.apple = apple;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Apple getApple() {
        return apple;
    }

    public void setApple(Apple apple) {
        this.apple = apple;
    }

    public void eat() {
        System.out.println(this.name + "在吃来自" + this.apple.getLocation() + "的" + this.apple.getTitle());
    }
}
