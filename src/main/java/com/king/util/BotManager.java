package com.king.util;


import com.king.model.BotStatusDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import static com.king.util.ThreadPoolExecutorUtil.getAQueueExecutor;

@Data
public class BotManager {

    private int size=5;

    private List<BotStatusDTO> botStatusDTOList;

    private ArrayList<ThreadPoolExecutor> poolExecutors;

    private AtomicInteger index = new AtomicInteger(0);

    public BotManager() {
        this(5);
    }

    public BotManager(int size) {
        this.size = size;
        botStatusDTOList = new ArrayList<>();
        for (int i=0;i<size;i++){
            botStatusDTOList.add(new BotStatusDTO());
        }
        poolExecutors = new ArrayList<>(size);
        for (int i=0;i<size;i++) {
            poolExecutors.add(getAQueueExecutor(10));
        }
    }

    public void addBotCount(int index){
        if (index>=size){
            index = index%size;
        }
        botStatusDTOList.get(index).getAndAdd(1);
    }

    public void decBotCount(int index){
        if (index>=size){
            index = index%size;
        }
        botStatusDTOList.get(index).getAndAdd(-1);
    }

    public void setFail(int index){
        if (index>=size){
            index = index%size;
        }
        botStatusDTOList.get(index).failSetTime();
    }

    public void removeFail(int index){
        if (index>=size){
            index = index%size;
        }
        botStatusDTOList.get(index).removeFail();
    }

    public ExecutorService getPoolExe(int index){
        if (index>=size){
            index = index%size;
        }
        return this.poolExecutors.get(index);
    }


    public int getNextBot(){
        int count = 0;
        int nowIndex = index.get();
        while (count<5){
            nowIndex = addAndGet(1);
            BotStatusDTO botStatusDTO = botStatusDTOList.get(nowIndex);
            if (botStatusDTO.mayAccessable()){
                return nowIndex;
            }
            count++;
        }
        return nowIndex;
    }

    private int addAndGet(int i){
        int now = index.addAndGet(1);
        if (now>4){
            index.set(1);
            return 1;
        }
        return now;
    }


}
