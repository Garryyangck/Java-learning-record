package garry.bookshop.application;

import garry.bookshop.service.BookService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Garry
 * ---------2024/3/1 10:47
 **/
public class BookshopApplication {
    public static void main(String[] args) {
        String[] configLocations = new String[]{"applicationContext-dao.xml", "applicationContext-service.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
        BookService bookService = context.getBean("bookService", BookService.class);
        bookService.purchase();
    }
}
