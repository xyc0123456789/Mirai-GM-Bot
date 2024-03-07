package com.king.db.service;


import com.king.db.pojo.GroupSpecialList;
import com.king.db.pojo.GroupSpecialListExample;
import com.king.db.pojo.Members;
import com.king.db.subdao.SubGroupSpecialListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class GroupSpecialListService {

    @Autowired
    private SubGroupSpecialListMapper subGroupSpecialListMapper;

    @Autowired
    private MembersService membersService;

    public GroupSpecialList getOne(Long groupId,Long contactId){
        return subGroupSpecialListMapper.selectByPrimaryKey(groupId,contactId);
    }

    public GroupSpecialList getAndUpdateOrAddOne(Long groupId, Long contactId, Long permissionLevel){
        GroupSpecialList groupSpecialList = getOne(groupId, contactId);
        if (groupSpecialList!=null){
            groupSpecialList.setPermissionLevel(permissionLevel);
            subGroupSpecialListMapper.updateByPrimaryKey(groupSpecialList);
            return groupSpecialList;
        }
        Members members = membersService.selectMember(groupId, contactId);
        if (members==null){
            return null;
        }

        if (permissionLevel==null){
            permissionLevel=1L;
        }
        groupSpecialList = new GroupSpecialList();
        groupSpecialList.setGroupId(groupId);
        groupSpecialList.setContactId(contactId);
        groupSpecialList.setPermissionLevel(permissionLevel);
        groupSpecialList.setCtime(new Date());
        groupSpecialList.setEnable(true);

        groupSpecialList.setNickName(members.getNickName());
        groupSpecialList.setCardName(members.getNameCard());
        groupSpecialList.setSpecialTitle(members.getSpecialTitle());

        subGroupSpecialListMapper.insert(groupSpecialList);
        return groupSpecialList;
    }

    public List<GroupSpecialList> findAll(){
        GroupSpecialListExample groupSpecialListExample = new GroupSpecialListExample();
        return subGroupSpecialListMapper.selectByExample(groupSpecialListExample);
    }

}