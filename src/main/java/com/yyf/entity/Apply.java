package com.yyf.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 团队实体类
 *
 * @author itdragon
 */
@Data
@TableName("gm_apply")
public class Apply implements Serializable {
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 申请人编号
     */
    private String userId;
    /**
     * 申请人姓名
     */
    private String realNam;
    /**
     * 申请内容
     */
    private String main;
    /**
     * 申请时间
     */
    private String createdDate;
    /**
     * 状态
     */
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealNam() {
        return realNam;
    }

    public void setRealNam(String realNam) {
        this.realNam = realNam;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}