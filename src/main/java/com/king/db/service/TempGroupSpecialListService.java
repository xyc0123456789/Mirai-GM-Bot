package com.king.db.service;


import com.king.db.pojo.GroupSpecialList;
import com.king.db.pojo.Members;
import com.king.db.pojo.TempGroupSpecialList;
import com.king.db.pojo.TempGroupSpecialListExample;
import com.king.db.subdao.SubTempGroupSpecialListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TempGroupSpecialListService {

    @Autowired
    private SubTempGroupSpecialListMapper subTempGroupSpecialListMapper;

    @Autowired
    private MembersService membersService;

    public TempGroupSpecialList getOne(Long groupId, Long contactId){
        return subTempGroupSpecialListMapper.selectByPrimaryKey(groupId,contactId);
    }

    public TempGroupSpecialList getAndUpdateOrAddOne(Long groupId, Long contactId, Long permissionLevel){
        TempGroupSpecialList tempGroupSpecialList = getOne(groupId, contactId);
        if (tempGroupSpecialList!=null){
            tempGroupSpecialList.setPermissionLevel(permissionLevel);
            subTempGroupSpecialListMapper.updateByPrimaryKey(tempGroupSpecialList);
            return tempGroupSpecialList;
        }
        Members members = membersService.selectMember(groupId, contactId);
        if (members==null){
            return null;
        }

        if (permissionLevel==null){
            permissionLevel=1L;
        }
        tempGroupSpecialList = new TempGroupSpecialList();
        tempGroupSpecialList.setGroupId(groupId);
        tempGroupSpecialList.setContactId(contactId);
        tempGroupSpecialList.setPermissionLevel(permissionLevel);
        tempGroupSpecialList.setCtime(new Date());
        tempGroupSpecialList.setEnable(true);

        tempGroupSpecialList.setNickName(members.getNickName());
        tempGroupSpecialList.setCardName(members.getNameCard());
        tempGroupSpecialList.setSpecialTitle(members.getSpecialTitle());

        subTempGroupSpecialListMapper.insert(tempGroupSpecialList);
        return tempGroupSpecialList;
    }

    public List<TempGroupSpecialList> findAll(){
        TempGroupSpecialListExample tempGroupSpecialListExample = new TempGroupSpecialListExample();
        return subTempGroupSpecialListMapper.selectByExample(tempGroupSpecialListExample);
    }
}