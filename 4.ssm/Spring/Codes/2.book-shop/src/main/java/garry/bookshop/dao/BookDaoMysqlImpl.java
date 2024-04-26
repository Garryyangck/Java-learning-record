package garry.bookshop.dao;

/**
 * @author Garry
 * ---------2024/3/1 9:44
 **/
public class BookDaoMysqlImpl implements BookDao{
    public BookDaoMysqlImpl() {
    }

    public void insert() {
        System.out.println("向Mysql Book表插入一条数据");
    }
}
