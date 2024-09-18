package garry.train.business.pojo;

import lombok.Data;
import java.util.Date;

/**
 * @author Garry
 * 2024-09-18 16:29
 */
@Data
public class UndoLog {

    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private Long branchId;

    /**
     * 
     */
    private String xid;

    /**
     * 
     */
    private String context;

    /**
     * 
     */
    private Long rollbackInfo;

    /**
     * 
     */
    private Integer logStatus;

    /**
     * 
     */
    private Date logCreated;

    /**
     * 
     */
    private Date logModified;

    /**
     * 
     */
    private String ext;

}