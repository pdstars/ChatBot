package cn.zhouyafeng.itchat4j.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Core管理类,单例模式
 */
public class CoreManage {
    private static CoreManage instance;

    List<Core> coreList = new ArrayList<>();

    private CoreManage() {

    }
    public static CoreManage getInstance() {
		if (instance == null) {
			synchronized (Core.class) {
				instance = new CoreManage();
			}
		}
		return instance;
	}

    public Core getCoreByUserName(String userName){
        for(Core c: coreList){
            if(userName.equals(c.getUserName())){
                return c;
            }
        }
        return null;
    }

    public Core getCoreByNickName(String nickName){
        for(Core c: coreList){
            if(!c.isAlive()){
                continue;
            }
            if(nickName.equals(c.getNickName())){
                return c;
            }
        }
        return null;
    }

    public void removeCore(Core core){
        coreList.remove(core);
    }

    public List<Core> getCoreList(){
        return coreList;
    }

    public void putCore(Core core){
        this.coreList.add(core);
    }
}
