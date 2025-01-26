package com.ruoyi.layim.enums;

public enum OnlineStatus {
    online,offline;
    private int value;

    public static OnlineStatus intToEnum(int value) {    //将数值转换成枚举值
        switch (value) {
            case 0:
                return offline;
            case 1:
                return online;
            default :
                return null;
        }
    }
}
