package vn.edu.hust.sis.khangnv.firealarmapp.dto.baseDto;

public abstract class AbstractDto {
    protected String createdAt;
    protected String updateAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }
}
