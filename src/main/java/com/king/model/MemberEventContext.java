package com.king.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.mamoe.mirai.event.events.GroupMemberEvent;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberEventContext extends CommonEventContext{

    private GroupMemberEvent groupMemberEvent;
}
