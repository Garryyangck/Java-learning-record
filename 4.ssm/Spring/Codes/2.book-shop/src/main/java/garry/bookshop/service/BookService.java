package garry.bookshop.service;

import garry.bookshop.dao.BookDao;

/**
 * @author Garry
 * ---------2024/3/1 9:45
 **/
public class BookService {
    private BookDao bookDao;

    public void purchase() {
        System.out.println("正在采购书籍...");
        bookDao.insert();
    }

    public BookDao getBookDao() {
        return bookDao;
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }
}
