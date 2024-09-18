package garry.train.business.pojo;

import java.util.Date;

public class UndoLog {
    private Long id;

    private Long branchId;

    private String xid;

    private String context;

    private Integer logStatus;

    private Date logCreated;

    private Date logModified;

    private String ext;

    private byte[] rollbackInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Integer getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(Integer logStatus) {
        this.logStatus = logStatus;
    }

    public Date getLogCreated() {
        return logCreated;
    }

    public void setLogCreated(Date logCreated) {
        this.logCreated = logCreated;
    }

    public Date getLogModified() {
        return logModified;
    }

    public void setLogModified(Date logModified) {
        this.logModified = logModified;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public byte[] getRollbackInfo() {
        return rollbackInfo;
    }

    public void setRollbackInfo(byte[] rollbackInfo) {
        this.rollbackInfo = rollbackInfo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", branchId=").append(branchId);
        sb.append(", xid=").append(xid);
        sb.append(", context=").append(context);
        sb.append(", logStatus=").append(logStatus);
        sb.append(", logCreated=").append(logCreated);
        sb.append(", logModified=").append(logModified);
        sb.append(", ext=").append(ext);
        sb.append(", rollbackInfo=").append(rollbackInfo);
        sb.append("]");
        return sb.toString();
    }
}