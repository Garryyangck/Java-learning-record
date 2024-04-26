package garry.spring.ioc.pojo;

/**
 * @author Garry
 * ---------2024/3/1 11:29
 **/
public class Computer {
    private String brand;
    private String type;
    private String order;
    private String price;

    public Computer() {
    }

    public Computer(String brand, String type, String order, String price) {
        this.brand = brand;
        this.type = type;
        this.order = order;
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "brand='" + brand + '\'' +
                ", type='" + type + '\'' +
                ", order='" + order + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
