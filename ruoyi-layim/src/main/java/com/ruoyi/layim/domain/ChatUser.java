package com.ruoyi.layim.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * layim通讯用户信息
 */
public class ChatUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 用户id*/
    @Excel(name = "用户主键", cellType = Excel.ColumnType.NUMERIC)
    private Long userId;

    @Excel(name = "用户名", cellType = Excel.ColumnType.STRING)
    private String userName;

    @Excel(name = "签名", cellType = Excel.ColumnType.STRING)
    private String sign;

    @Excel(name = "头像地址", cellType = Excel.ColumnType.STRING)
    private String avatar;

    @Excel(name = "在线状态", cellType = Excel.ColumnType.NUMERIC)
    private Integer status;

}
