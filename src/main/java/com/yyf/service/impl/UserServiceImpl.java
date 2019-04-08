package com.yyf.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yyf.entity.Permission;
import com.yyf.entity.Role;
import com.yyf.entity.User;
import com.yyf.mapper.PermissionMapper;
import com.yyf.mapper.RoleMapper;
import com.yyf.mapper.UserMapper;
import com.yyf.service.UserService;
import com.yyf.util.DateUtil;
import com.yyf.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户service实现类
 *
 * @author
 * @create 2018/10/7 15:40
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ItdragonUtils itdragonUtils;

    @Override
    public User findUserByUserName(String userName) {
        User searchUser = new User();
        searchUser.setUserName(userName);
        User user = userMapper.selectOne(searchUser);
        if (user != null) {
            List<Role> roleList = roleMapper.selectRoleListByUserId(user.getId());
            if (!roleList.isEmpty()) {
                for (Role role : roleList) {
                    List<Permission> permissionList = permissionMapper.selectPermissionByRoleId(role.getId());
                    role.setPermissions(permissionList);
                }
            }
            user.setRoleList(roleList);
            return user;
        }
        return null;
    }

    @Override
    public User getUserByUserName(String userName) {
        User searchUser = new User();
        searchUser.setUserName(userName);
        User user = userMapper.selectOne(searchUser);
        return user;
    }

    @Override
    public Page<User> getUserList(User user, int page, int limit) {
        EntityWrapper<User> searchInfo = new EntityWrapper<>();
        if (user != null) {
            if (user.getUserName() != null && !"".equals(user.getUserName())) {
                searchInfo.eq("userName", user.getUserName());
            }
            if (user.getStatus() != null && !"".equals(user.getStatus())) {
                searchInfo.eq("status", user.getStatus());
            }
        }
        searchInfo.ne("userName", "adminStrator");
        searchInfo.orderBy("createdDate desc");
        Page<User> pageInfo = new Page<>(page, limit);
        List<User> userList = userMapper.selectPage(pageInfo, searchInfo);
        if (!userList.isEmpty()) {
            pageInfo.setRecords(userList);
        }
        return pageInfo;
    }

    @Override
    public Page<User> getUserListForRole(User user, int page, int limit, String roleId) {
        EntityWrapper<User> searchInfo = new EntityWrapper<>();
        if (user != null) {
            if (user.getStatus() != null && !"".equals(user.getStatus())) {
                searchInfo.eq("status", user.getStatus());
            }
        }
        searchInfo.ne("userName", "adminStrator");
        searchInfo.orderBy("createdDate desc");
        Page<User> pageInfo = new Page<>(page, limit);
        List<User> userList = userMapper.selectPage(pageInfo, searchInfo);
        List<String> userIdList = roleMapper.slectRoleAndUser(roleId);
        List<User> resultList = new ArrayList<>();
        for (User user1 : userList) {
            if (!userIdList.contains(user1.getId())) {
                resultList.add(user1);
            }
        }
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public Page<User> getUserListByRoleId(int page, int limit,User user) {

        Page<User> pageInfo = new Page<>(page, limit);
        if (user.getKeyType() != null || user.getKeyWord() != null) {
            if(user.getKeyType().equals("") || user.getKeyWord().equals("")){

            }else if (user.getKeyType().equals("userName")) {
                    user.setUserName(user.getKeyWord());
            }else if (user.getKeyType().equals("number")) {
                    user.setNumber(user.getKeyWord());
            } else if (user.getKeyType().equals("classs")) {
                user.setClasss(user.getKeyWord());
            } else if (user.getKeyType().equals("realName")) {
                user.setRealName(user.getKeyWord());
            }
                user.setKeyWord(null);
                user.setKeyType(null);
            }


        List<User> userList = userMapper.selectUserListByRoleId(pageInfo,user);
        if (!userList.isEmpty()) {
            pageInfo.setRecords(userList);
        }
        return pageInfo;
    }

    @Override
    public boolean changePass(String newPas, String userName) {
        User user = new User();
        user.setPlainPassword(newPas);
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        entityWrapper.eq("userName", userName);
        itdragonUtils.entryptPassword(user);
        int result = userMapper.update(user, entityWrapper);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateUserStatus(String id, Integer status) {
        User user = new User();
        user.setStatus(status);
        user.setId(id);
        int result = userMapper.updateById(user);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean insert(User user) {
        user.setStatus(1);
        user.setCreatedDate(DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
        itdragonUtils.entryptPassword(user);
        int result = userMapper.insert(user);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * EntityWrapper 是Mybatis-plus的条件构造器动态Sql
     * entityWrapper.eq("userName", userName)的意思是查询userName 等于传入的userName
     * 拼接sql
     * @param userName
     * @param time
     */
    @Override
    public void updateLoginTime(String userName, String time) {
        User user = new User();
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        user.setUpdatedDate(time);
        entityWrapper.eq("userName", userName);
        userMapper.update(user, entityWrapper);
    }

    @Override
    public List<User> selectList() {
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        entityWrapper.ne("userName", "adminStrator");
        entityWrapper.ne("userName", "admin");
        return userMapper.selectList(entityWrapper);
    }

    @Override
    public boolean isAdmin(String userId) {
        List<Role> roleList = roleMapper.selectRoleListByUserId(userId);
        for (Role role : roleList) {
            if ("管理员".equals(role.getRole())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isStudent(String userId) {
        List<Role> roleList = roleMapper.selectRoleListByUserId(userId);
        for (Role role : roleList) {
            if ("5".equals(role.getId())) {
                return true;
            }
        }
        return false;

    }

    @Override
    public String zumNumber() {
        return userMapper.selectList(null).size() + "";
    }

    @Override
    public User getUserByNumber(String userName,String roleId) {
        return userMapper.getUserByNumber(userName,roleId);
    }


    @Override
    public boolean deleteByPrimaryKey(String ids) {
        String[] idList = ids.split(",");
        int result = 0;
        for (String s : idList) {
            result = userMapper.deleteById(s);
            if (result > 0) {
                roleMapper.delUserByUserId(s);
            }
        }
        if (result > 0) {

            return true;
        } else {
            return false;
        }
    }

    @Override
    public User selectByPrimaryKey(String id) {
        return userMapper.selectById(id);
    }

    @Override
    public boolean updateByPrimaryKey(User user) {
        User userVerify = userMapper.selectById(user.getId());
        if (user.getPlainPassword() != null && !"".equals(user.getPlainPassword())) {
            if (!user.getPlainPassword().equals(userVerify.getPassword())) {
                itdragonUtils.entryptPassword(user);
            }
        }
        int result = userMapper.updateById(user);

        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }


}