package com.king.model;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class BotStatusDTO {

    private AtomicInteger remainRequest = new AtomicInteger(0);


    private AtomicLong lastFailTime = new AtomicLong(0);

    public int getAndAdd(int i){
        return remainRequest.getAndAdd(i);
    }

    public boolean mayAccessable(){
        long deltaSeconds = getDeltaSeconds();

        return deltaSeconds<=0 || deltaSeconds >=3600000;
    }

    public long getDeltaSeconds(){
        if (lastFailTime.get()==0){
            return -1;
        }
        return (System.currentTimeMillis()-lastFailTime.get())/1000;
    }

    public void failSetTime(){
        lastFailTime.set(System.currentTimeMillis());
    }

    public void removeFail(){
        lastFailTime.set(0);
    }

}
