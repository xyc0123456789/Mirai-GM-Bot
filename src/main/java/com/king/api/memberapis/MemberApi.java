package com.king.api.memberapis;

import com.king.model.CommonResponse;
import com.king.model.MemberEventContext;

public interface MemberApi {

    boolean condition(MemberEventContext memberEventContext);

    CommonResponse handler(MemberEventContext memberEventContext);

    int sortedOrder();

    String commandName();

    boolean commandType();//好友是否可用

    boolean defaultStatus();//默认是否开启
}
