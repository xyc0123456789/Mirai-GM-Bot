package com.king.api.memberapis;

public abstract class AbstractMemberEventApi implements MemberApi{


    @Override
    public boolean commandType() {
        return false;
    }

    @Override
    public boolean defaultStatus() {
        return true;
    }
}
