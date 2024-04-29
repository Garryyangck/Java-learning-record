import java.io.IOException;

/**
 * @author Garry
 * ---------2024/4/29 15:20
 **/
public class Test {
    public static void main(String[] args) {
        System.out.println("the return value of f is " + f()); // 3
    }

    static int f() {
        try {
            throw new ArrayIndexOutOfBoundsException();
        } catch (NullPointerException e) {
            System.out.println();
        } finally {
            System.out.println();
        }
        return 0;
    }
}
