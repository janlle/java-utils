package com.leone.util.jdbc;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * @author leone
 * @since 2019-04-26
 **/
@Data
@Entity
@Table(name = "t_user")
@MappedSuperclass
public class ObjEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long permissionId;

    @Column(columnDefinition = "varchar(128) NOT NULL COMMENT '权限名称'")
    private String permissionName;

    @Column(columnDefinition = "varchar(128) NOT NULL COMMENT '资源路径'")
    private String url;

    @Column(columnDefinition = "integer NOT NULL COMMENT '父编号'")
    private Long parentId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "integer NOT NULL COMMENT '资源类型，[menu|button]'")
    private ResourceType type;

    @Column(columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    private Date createTime;

    @Column(columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'")
    private Date updateTime;

    @Column(columnDefinition = "bit NOT NULL COMMENT '是否可用'")
    private Boolean disable;

    @Column(columnDefinition = "bit NOT NULL COMMENT '是否删除'")
    private Boolean deleted;

    private enum ResourceType {
        MENU, BUTTON
    }

    @Version
    @Column(columnDefinition = "bigint NOT NULL COMMENT '乐观锁专用字段'")
    private Long version = 0L;

}
