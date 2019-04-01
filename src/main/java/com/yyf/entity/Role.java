package com.yyf.entity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色表，决定用户可以访问的页面
 * @author itdragon
 */
@Data
@TableName("gm_role")
public class Role implements Serializable {
    @TableId(value = "id",type = IdType.UUID)
	private String id;
	/**
	 * 角色
	 */
	private String role;
	/**
	 * 角色描述
	 */
	private String description;
	/**
	 * 角色中的权限
	 */
	@TableField(exist = false)
	private List<Permission> permissions;
	/**
	 * 用户 - 角色关系
	 */
	@TableField(exist = false)
    private List<User> users;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}