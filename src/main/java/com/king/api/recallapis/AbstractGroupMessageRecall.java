package com.king.api.recallapis;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/9 15:32
 **/
public abstract class AbstractGroupMessageRecall implements  GroupMessageRecallApi{

    @Override
    public boolean defaultStatus() {
        return false;
    }
}
