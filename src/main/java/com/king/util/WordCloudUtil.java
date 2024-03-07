package com.king.util;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.ColorPalette;
import com.king.component.MyBot;
import com.king.db.service.MessageRecordService;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class WordCloudUtil {


    private static MessageRecordService messageRecordService;

    @Autowired
    public WordCloudUtil(MessageRecordService messageRecordService){
        WordCloudUtil.messageRecordService = messageRecordService;
    }

    private static final String[] exclude=new String[]{
            "我们","你们","他们","它们","因为","因而","所以","如果","那么",
            "如此","只是","但是","就是","这是","那是","而是","而且","虽然",
            "这些","有些","然后","已经","于是","一种","一个","一样","时候",
            "没有","什么","这样","这种","这里","不会","一些","这个","仍然",
            "不是","可以","自己","可能","这么","怎么","那个","还有","直接",
            "觉得","需要","主要","应该","不过","一般","差不多","好像","确实",
            "之前","比较","一点","不如","反正","其实","不能","哪里","还是",
            "知道","感觉","其他","出来","起来","一下","哈哈哈","bot","#gpt",
            "当时","甚至","有点","很多","哈哈","那些","突然","发现","为什么",
            "特别","里面","hhhhhh","出去","以后","那边","不然","怎么样","下来",
            "现在","以前","没什么","多少","肯定","真的","看看","基本","不错",
            "喜欢","平均","开始","至少","估计","左右","所有","以为","除非",
            "为了","平时","认识","后面","或者","看到","每个","各种","随便",
            "如何","要求","认为","永远","有效","正确","任何","不同","进行",
            "减少","——","最后","制定","表明","接受","坚持","承认","哪些",
            "有的","最近","以上","今天","昨天","正常","问题","一键","一起",
            "第一","正好","东西","意思","大家","别人","呃呃","天天","有人",
            "不行","小时","哪个","不要","分钟","一直","回来","无所谓","容易",
            "容易","刚刚","#emoji","是不是","还要","回去","10","50","记得",
    };

    public static void sendWordCloud(Long groupId){
        sendWordCloud(groupId, new Date());
    }

    public static void sendWordCloud(Long groupId, Date date){
        sendWordCloud(groupId, null, date);
    }
    public static void sendWordCloud(Long groupId, Long subContactId, Date date){
        Group group = MyBot.bot.getGroup(groupId);
        if (group==null){
            return;
        }
        Contact contact = group;

        String dateStr = DateFormateUtil.formatYYYYMMDD(date);
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        List<String> groupTextByDate;
        if (subContactId==null){
            groupTextByDate = messageRecordService.getGroupTextByDate(groupId, date);
        }else {
            contact = group.get(subContactId);
            groupTextByDate = messageRecordService.getMemberTextByDate(groupId, subContactId, date);
        }
        if (contact==null){
            return;
        }

        File img = new File(dateStr+"_"+groupId +"_word_cloud_"+uuid+".png");
        List<WordFrequency> wordFrequencies=null;
        try {
            wordFrequencies = generateWordFrequencies(groupTextByDate);
        }catch (Exception e){
            log.error("", e);
        }
        File imgFile = WordCloudUtil.generateWordCloudFileFromWordFrequencies(img, wordFrequencies);
        if (imgFile==null){
            contact.sendMessage("生成词云失败");
        }else {
            Image image = Contact.uploadImage(contact, imgFile);
            contact.sendMessage(image);
            FileUtil.deleteFile(imgFile);
        }

        if (!CollectionUtils.isEmpty(wordFrequencies)){
            StringBuilder wordRank = new StringBuilder("词频统计:\\\n");
            int maxRow = 200, minFrequencies = 5;
            if (subContactId!=null){
                minFrequencies = 1;
            }
            for (int i=0;i<Math.min(maxRow, wordFrequencies.size());i++){
                WordFrequency wordFrequency = wordFrequencies.get(i);
                if (wordFrequency.getFrequency()<minFrequencies){
                    break;
                }
                wordRank.append(MyStringUtil.getThreeStr(i+1)).append(". ").append(wordFrequency.getWord()).append(": ").append(wordFrequency.getFrequency()).append("\\\n");
            }
            GroupMembersUtil.sendImgFromString(contact, wordRank.toString(), null);
        }
    }


    public static File generateWordCloud(Long groupId, String date){
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        File txtFile = new File(date+"_"+groupId+".txt");
        File img = new File(date+"_"+groupId +"_word_cloud_"+uuid+".png");
        try {
            final List<WordFrequency> wordFrequencies = generateWordFrequencies(txtFile);
            return WordCloudUtil.generateWordCloudFileFromWordFrequencies(img, wordFrequencies);
        }catch (Exception e){
            log.error("generateWordCloud err", e);
        }
        return null;
    }

    public static List<WordFrequency> generateWordFrequencies(File txtFile) throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        frequencyAnalyzer.setMinWordLength(2);
        frequencyAnalyzer.setMaxWordLength(8);
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
        frequencyAnalyzer.setStopWords(Arrays.asList(exclude));

        return frequencyAnalyzer.load(txtFile);
    }

    public static List<WordFrequency> generateWordFrequencies(List<String> stringList) throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        frequencyAnalyzer.setMinWordLength(2);
        frequencyAnalyzer.setMaxWordLength(8);
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
        frequencyAnalyzer.setStopWords(Arrays.asList(exclude));

        return frequencyAnalyzer.load(stringList);
    }

    public static File generateWordCloudFile(File targetFile,File sourceFile){
        try {
            final List<WordFrequency> wordFrequencies = generateWordFrequencies(sourceFile);
            return WordCloudUtil.generateWordCloudFileFromWordFrequencies(targetFile, wordFrequencies);
        }catch (Exception e){
            log.error("generateWordCloud err", e);
        }
        return null;
    }

    public static File generateWordCloudFileFromWordFrequencies(File targetFile, List<WordFrequency> wordFrequencies){
        try {
            final Dimension dimension = new Dimension(1920, 1080);
            final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
            wordCloud.setPadding(0);
            wordCloud.setBackground(new RectangleBackground(dimension));
            wordCloud.setBackgroundColor(Color.WHITE);
            wordCloud.setColorPalette(new ColorPalette(ColorUtil.generateWithBase(7)));
            wordCloud.setFontScalar(new SqrtFontScalar(10, 250));
            wordCloud.setKumoFont(new KumoFont(new Font("宋体", Font.BOLD, 25)));
            wordCloud.build(wordFrequencies);
            wordCloud.writeToFile(targetFile.getAbsolutePath());
            return targetFile;
        }catch (Exception e){
            log.error("generateWordCloudFile err", e);
            return null;
        }
    }

    public static void main(String[] args) {
        generateWordCloudFile(new File("a.png"), new File("20230304_536348689.txt"));
    }
}
