package com.yyf.entity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import java.util.List;
/**
 * 权限表，决定用户的具体操作
 * @author itdragon
 */
@TableName("gm_permission")
@Data
public class Permission {
	@TableId(value = "PermissionID",type = IdType.UUID)
	private String permissionId;
	/**
	 * 名称
	 */
    private String permissionName;
	/**
	 * 类型
	 */
	private String permissionType;
	/**
	 *  资源路径
	 */
	private String url;
	/**
	 *  权限字符串 如：employees:create,employees:update,employees:delete
	 */
	private String permissionMark;
	/**
	 * 父类Id
	 */
	private String parentId;
	/**
	 * 优先级
	 */
	private Integer priority;
	/**
	 * 是否启用
	 */
	private Integer available;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 最后一次修改时间
	 */
	private String lastTime;
	/**
	 * 权限下拥有的角色
	 */
	@TableField(exist = false)
	private List<Role> roles;

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPermissionMark() {
		return permissionMark;
	}

	public void setPermissionMark(String permissionMark) {
		this.permissionMark = permissionMark;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}