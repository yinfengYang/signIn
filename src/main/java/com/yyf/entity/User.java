package com.yyf.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户实体类
 *
 * @author itdragon
 */
@Data
@TableName("gm_user")
public class User implements Serializable {
    /**
     * 自增长主键，默认ID为1的账号为超级管理员
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 注册的昵称
     */
    private String userName;
    /**
     * 登录时的密码，不持久化到数据库
     */
    @TableField(exist = false)
    private String plainPassword;
    /**
     * 加密后的密码
     */
    private String password;
    /**
     * 用于加密的盐
     */
    private String salt;
    /**
     * 手机号
     */
    private String iphone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 用户来自的平台
     */
    private String platform;
    /**
     * 用户注册时间
     */
    private String createdDate;
    /**
     * 用户最后一次登录时间
     */
    private String updatedDate;
    /**
     * 一个用户拥有多个角色
     */
    @TableField(exist = false)
    private List<Role> roleList;

    @TableField(exist = false)
    private String role;
    /**
     * 用户状态，0表示用户已禁用，1表示用户可用
     */
    private Integer status;
    /**
     * 真实姓名
     */
    private String realName;

    private String address;

    private String number;

    private String classs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getIphone() {
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getClasss() {
        return classs;
    }

    public void setClasss(String classs) {
        this.classs = classs;
    }
}