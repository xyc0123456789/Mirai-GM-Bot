package com.king.db.service;


import com.king.db.pojo.ContactListenList;
import com.king.db.subdao.SubContactListenListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Service
public class ContactListenListService {

    @Autowired
    private SubContactListenListMapper subContactListenListMapper;

    /**
     * 获取群id
     * @return
     */
    public Map<Long,String> getLinsteningGroups(){
        return getByType(1, 1);
    }

    /**
     * 获取好友id
     * @return
     */
    public Map<Long,String> getLinsteningFriends(){
        return getByType(0, 1);
    }


    public Map<Long,String> getByType(int type){
        return getByType(type, -1);
    }

    private Map<Long,String> getByType(int type, int flag){
        List<ContactListenList> contactListenLists = subContactListenListMapper.selectByContactType(type);
        Map<Long,String> ans = new HashMap<>();
        for (ContactListenList contactListenList:contactListenLists){
            if (flag < 0){
                ans.put(contactListenList.getContactId(), contactListenList.getRemark());
            }else {
                if ((flag==1&&contactListenList.getEnable())||(flag==0&&!contactListenList.getEnable())) {
                    ans.put(contactListenList.getContactId(), contactListenList.getRemark());
                }
            }

        }
        return ans;
    }

    public ContactListenList getGroupByPrimaryKey(Long groupId){
        return subContactListenListMapper.selectByPrimaryKey(1,groupId);
    }

    public void update(ContactListenList contactListenList){
        contactListenList.setUtime(new Date());
        subContactListenListMapper.updateByPrimaryKeySelective(contactListenList);
    }
}