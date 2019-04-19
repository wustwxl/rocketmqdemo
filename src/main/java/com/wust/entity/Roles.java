package com.wust.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class Roles {
    private static final long serialVersionUID = 1L;

    private Integer role_id;

    private String role_name;

    // 一个角色对应多个权限
    private List<Permissions> permissionList;

    /**
     * 获得权限名：add/update等
     * @return
     */
    public List<String> getPermissionsName() {
        List<String> list = new ArrayList<String>();
        List<Permissions> perlist = getPermissionList();
        for (Permissions per : perlist) {
            list.add(per.getPermission_name());
        }
        return list;
    }

}