package com.king.api.messageapis.common.keyword;

import com.king.db.service.KeyWordResultRecordService;
import com.king.model.HttpApiContext;
import com.king.model.MessageEventContext;
import com.king.model.Response;
import com.king.util.HttpClient;
import com.king.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractKeyWordHandler implements KeyWordHandler{

    @Autowired
    protected KeyWordResultRecordService keyWordResultRecordService;

    @Override
    public Response handler(MessageEventContext messageEventContext) {
        HttpApiContext httpApiContext = preRequest(messageEventContext);
        boolean dealResponse;
        do {
            request(httpApiContext);
            dealResponse = dealResponse(httpApiContext);
        } while (!dealResponse);
        postRequest(httpApiContext);
        return null;
    }

    protected boolean dealResponse(HttpApiContext httpApiContext) {
        if (httpApiContext.getResponse().contains("<!DOCTYPE html>")||httpApiContext.getStatusCode()!=200||httpApiContext.getResponse().contains("<html>")){
            httpApiContext.setCount(1000);
            String res = keyWordResultRecordService.randomGetOne(this.word());
            httpApiContext.setResponse(res);
            return true;
        }
        if (httpApiContext.getStatusCode()==200&&httpApiContext.getCount()<3) {
            String oriResponse = httpApiContext.getResponse();
            try {
                oriResponse = StringEscapeUtils.unescapeHtml(oriResponse);
            } catch (Exception e) {
            }
            String response = oriResponse.replaceAll("<p>|</p>", "").replaceAll("<br>", "\n").trim();
            boolean add = keyWordResultRecordService.add(this.word(), response);
            httpApiContext.setResponse(response);
            httpApiContext.setCount(httpApiContext.getCount()+1);
            if (httpApiContext.getCount()>=3){
                return true;
            }
            return add;
        }
        return true;
    }

    protected HttpApiContext preRequest(MessageEventContext messageEventContext){
        HttpApiContext httpApiContext = new HttpApiContext();
        httpApiContext.setMessageEventContext(messageEventContext);
        return httpApiContext;
    }

    protected void request(HttpApiContext httpApiContext){
        if (MyStringUtil.isEmpty(httpApiContext.getUrl())){
            return;
        }
        try {
            HttpClient httpClient = new HttpClient(httpApiContext.getUrl(), httpApiContext.getConnectTimeOut(), httpApiContext.getReadTimeOut(),false);
            int statusCode = httpClient.sendGet();
            httpApiContext.setStatusCode(statusCode);
            log.info(this.word() + " : {}", statusCode);
            String result = httpClient.getResult();
            if (statusCode!=200){
                result = "";
            }
            httpApiContext.setResponse(result);
        }catch (Exception e){
            log.error(this.word()+" request err", e);
            httpApiContext.setResponse(this.word() + " error: "+e.getMessage());
        }
    }

    protected void postRequest(HttpApiContext httpApiContext){
        if (MyStringUtil.isEmpty(httpApiContext.getResponse())){
            return;
        }
        MessageEventContext messageEventContext = httpApiContext.getMessageEventContext();
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        QuoteReply quoteReply = messageEventContext.getQuoteReply();
        MessageChain messages = new MessageChainBuilder().append(quoteReply).append(httpApiContext.getResponse()).build();
        messageEvent.getSubject().sendMessage(messages);
    }


}
