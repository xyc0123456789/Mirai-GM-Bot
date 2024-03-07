package com.king.util;

import com.king.config.CommonConfig;
import com.king.db.pojo.SensitiveWord;
import com.king.db.service.SensitiveWordService;
import com.king.model.GroupId;
import com.king.model.QQFriendId;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class DFAUtil {

    HashMap<String, Object> dfaMap;

    public static final int minMatchType=1;

    public static final int maxMatchType=2;

    private static final HashMap<String,Long> sensitiveSet = new HashMap<>();

    @Autowired
    private SensitiveWordService sensitiveWordService;

    public DFAUtil() {}

    @PostConstruct
    public synchronized void reload(){
        List<SensitiveWord> all = sensitiveWordService.getAll();
        sensitiveSet.clear();
        for (SensitiveWord sensitiveWord:all){
            sensitiveSet.put(sensitiveWord.getMseeage(),sensitiveWord.getId());
        }
        createDFAHashMap(sensitiveSet.keySet());
        log.info("敏感词初始化成功，共{}个", sensitiveSet.size());
    }

    public static String constructMessage(MessageEvent messageEvent,String content,Set<String> sensitiveWordByDFAMap){
        long memberId = messageEvent.getSender().getId();
        Group group = (Group) messageEvent.getSubject();
        NormalMember normalMember = group.get(memberId);
        if (normalMember == null){
            return null;
        }
        String nick = normalMember.getNick();
        String nameCard = normalMember.getNameCard();
        String groupName = group.getName();
        String stringBuilder = groupName+ "(" + group.getId() + ")["+nameCard+"][" + nick + "](" + memberId + "):{" + content + "} 触发敏感词共"+sensitiveWordByDFAMap.size()+"个," + sensitiveWordByDFAMap;
        return stringBuilder;
    }
    public static void notifyFriend(MessageEvent messageEvent, String content, Set<String> sensitiveWordByDFAMap){
        try {
            String message = constructMessage(messageEvent, content, sensitiveWordByDFAMap);
            Group group = messageEvent.getBot().getGroup(GroupId.TEST);
            if (group==null){
                group = messageEvent.getBot().getGroup(GroupId.DEV);
                if (group==null){
                    messageEvent.getBot().getFriend(QQFriendId.ME).sendMessage(message);
                }else {
                    group.sendMessage(message);
                }
            }
//            FriendNotifyUtil.notifyFriendMessage(messageEvent.getBot(), QQFriendId.ME, message);
//            FriendNotifyUtil.notifyFriendMessage(messageEvent.getBot(), QQFriendId.QUN_ZHU, message);
//            FriendNotifyUtil.notifyFriendMessage(messageEvent.getBot(), QQFriendId.BUAA, message);
//            FriendNotifyUtil.notifyFriendMessage(messageEvent.getBot(), QQFriendId.LIN_HAO, message);
        }catch (Exception e){
            log.error("notifyFriend",e);
        }

    }

    public synchronized String remove(String word){
        Long id = sensitiveSet.remove(word);
        if (id!=null){
            try {
                createDFAHashMap(sensitiveSet.keySet());
                sensitiveWordService.deleteByWord(id);
                return "修正+导出成功"+sensitiveSet.size();
            }catch (Exception e){
                return "出错了"+e.getMessage();
            }
        }else {
            return word+"不存在";
        }
    }

    public synchronized String addWord(String word){
        boolean contains = sensitiveSet.containsKey(word);
        if (!contains){
            try {
                Long id = sensitiveWordService.addOneWord(word);
                reload();
                return "修正成功,当前set大小["+sensitiveSet.size()+"]";
            }catch (Exception e){
                return "出错了"+e.getMessage();
            }
        }else {
            return word+"已存在";
        }
    }


    /*{日=
     * 	{本=
     * 		{人={isEnd=1},
     * 		鬼=
     * 			{子={isEnd=1},
     * 			isEnd=0},
     * 		isEnd=0},
     * 	isEnd=0},
     *
     * 大=
     * 	{汉=
     * 		{民={isEnd=0,
     * 			族={isEnd=1}},
     * 		isEnd=0},
     * 	isEnd=0,
     * 	中={isEnd=0,
     * 		华={isEnd=1,
     * 			帝={isEnd=0,
     * 				国={isEnd=1}}}}}}*/
    /**set作为敏感词，创建出对应的dfa的Map，以供检验敏感词
     * @param set
     */
    public void createDFAHashMap(Set<String> set){
        HashMap<String, Object> nowMap;
        //根据set的大小，创建map的大小
        dfaMap=new HashMap<>(set.size());
        //对set里的字符串进行循环
        for(String key:set){
            //对每个字符串最初，nowMap就是dfaMap
            nowMap=dfaMap;
            for(int i=0;i<key.length();i++){
                //一个个字符循环
                String nowChar=String.valueOf(key.charAt(i));
                //根据nowChar得到nowMap里面对应的value
                HashMap<String, Object> map=(HashMap<String, Object>)nowMap.get(nowChar);
                //如果map为空，则说明nowMap里面没有以nowChar开头的东西，则创建一个新的hashmap，
                //以nowChar为key，新的map为value，放入nowMap
                if(map==null){
                    map=new HashMap<String,Object>();
                    nowMap.put(nowChar, map);
                }
                //nowMap=map，就是nowChar对应的对象
                nowMap=map;
                //最后在nowMap里设置isEnd
                //如果nowMap里面已经有isEnd，并且为1，说明以前已经有关键字了，就不再设置isEnd
                //因为如果没有这一步，大中华和大中华帝国，先设置大中华
                //在大中华帝国设置的时候，华对应的map有isEnd=1，如果这时对它覆盖，就会isEnd=0，导致大中华这个关键字失效
                if(nowMap.containsKey("isEnd")&&nowMap.get("isEnd").equals("1")){
                    continue;
                }
                if(i!=key.length()-1){
                    nowMap.put("isEnd", "0");
                }
                else{
                    nowMap.put("isEnd", "1");
                }
            }
        }
//        System.out.println(dfaMap);
    }


    /** 用创建的dfaMap，根据matchType检验字符串string是否包含敏感词，返回包含所有对于敏感词的set
     * @param string 要检查是否有敏感词在内的字符串
     * @param matchType 检查类型，如大中华帝国牛逼对应大中华和大中华帝国两个关键字，1为最小检查，会检查出大中华，2位最大，会检查出大中华帝国
     * @return
     */
    public Set<String> getSensitiveWordByDFAMap(String string,int matchType){
        Set<String> set=new HashSet<>();
        for(int i=0;i<string.length();i++){
            //matchType是针对同一个begin的后面，在同一个begin匹配最长的还是最短的敏感词
            int length=getSensitiveLengthByDFAMap(string,i,matchType);
            if(length>0){
                set.add(string.substring(i,i+length));
                //这个对应的是一个敏感词内部的关键字（不包括首部），如果加上，大中华帝国，对应大中华和中华两个敏感词，只会对应大中华而不是两个
                //i=i+length-1;//减1的原因，是因为for会自增
            }
        }
        if (!CollectionUtils.isEmpty(set)) {
            log.warn(set.toString());
        }
        return set;
    }

    /**如果存在，则返回敏感词字符的长度，不存在返回0
     * @param string
     * @param beginIndex
     * @param matchType  1：最小匹配规则，2：最大匹配规则
     * @return
     */
    public int getSensitiveLengthByDFAMap(String string,int beginIndex,int matchType){
        //当前匹配的长度
        int nowLength=0;
        //最终匹配敏感词的长度，因为匹配规则2，如果大中华帝，对应大中华，大中华帝国，在华的时候，nowLength=3，因为是最后一个字，将nowLenth赋给resultLength
        //然后在帝的时候，now=4，result=3，然后不匹配，resultLength就是上一次最大匹配的敏感词的长度
        int resultLength=0;
        HashMap<String, Object> nowMap=dfaMap;
        for(int i=beginIndex;i<string.length();i++){
            String nowChar=String.valueOf(string.charAt(i));
            //根据nowChar得到对应的map，并赋值给nowMap
            nowMap=(HashMap<String, Object>)nowMap.get(nowChar);
            //nowMap里面没有这个char，说明不匹配，直接返回
            if(nowMap==null){
                break;
            }
            else{
                nowLength++;
                //如果现在是最后一个，更新resultLength
                if("1".equals(nowMap.get("isEnd"))){
                    resultLength=nowLength;
                    //如果匹配模式是最小，直接匹配到，退出
                    //匹配模式是最大，则继续匹配，resultLength保留上一次匹配到的length
                    if(matchType==minMatchType){
                        break;
                    }
                }
            }
        }
        return resultLength;
    }

    public static void outPutSet(Set<String> stringSet){
        StringBuilder stringBuilder = new StringBuilder();
        for(String s:stringSet){
            if (s.length()==1)continue;
            stringBuilder.append(s).append("\n");
        }
//        System.out.println(stringBuilder);
        FileUtil.outPutToFile(stringBuilder.toString(),new File(CommonConfig.dfaPath+"/sum.txt"));
    }

    public static void main(String[] args) {
//        Set<String> set=new HashSet<>();
//        set.add("大中华");
//        set.add("大中华帝国");
//        set.add("大汉民族");
//        set.add("日本人");
//        set.add("日本鬼子");
//
        File dir = new File(CommonConfig.dfaPath+"Bak");
        Set<String> sensitiveSet = new HashSet<>();
        if (dir.isDirectory()&&dir.exists()){
            File[] files = dir.listFiles();
            if (files ==null) {
                log.warn("敏感词初始化失败");
                return;
            }
            for (File file: files){
                if (!file.getName().endsWith("txt")){
                    continue;
                }
                if (file.getName().equals("sum.txt")){
                    continue;
                }
                System.out.println(file.getAbsolutePath());
                String s = FileUtil.readFile(file);
                if (s==null)continue;
                String[] split = s.split("\\s*\n\\s*",-1);
                for (String word:split){
//                    System.out.println(word);
                    if (!MyStringUtil.isEmpty(word)){
                        word = word.replaceAll("\r","\\r");
                        sensitiveSet.add(word);
                    }
                }
            }
//            System.out.println(sensitiveSet);
//            System.out.println(sensitiveSet.size());
        }
        sensitiveSet.add("ak47");
        sensitiveSet.add("AK47");
        sensitiveSet.add("aK47");
        sensitiveSet.add("Ak47");
        sensitiveSet.add("xijinpin");
        sensitiveSet.remove("QQ");
        sensitiveSet.remove("qq");
        sensitiveSet.remove("小姐");
        sensitiveSet.remove("公告");
        sensitiveSet.remove("管理");
        sensitiveSet.remove("管理员");
        sensitiveSet.remove("ro");
        sensitiveSet.remove("UR");
        sensitiveSet.remove("ur");
        sensitiveSet.remove("消息");
        sensitiveSet.remove("招聘");
        sensitiveSet.remove("天畅");
        sensitiveSet.remove("一起玩");
        sensitiveSet.remove("然后");
        sensitiveSet.remove("test");
        sensitiveSet.remove("语句");
        sensitiveSet.remove("姐姐");
        sensitiveSet.remove("爸爸");
        sensitiveSet.remove("妈妈");
        sensitiveSet.remove("美女");
        outPutSet(sensitiveSet);

    }
}
