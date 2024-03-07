package com.king.api.messageapis.common.keyword;

import com.king.model.HttpApiContext;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MeiJuZhaiChaoApiHandler extends  AbstractKeyWordHandler{

    @Override
    protected HttpApiContext preRequest(MessageEventContext messageEventContext) {
        HttpApiContext httpApiContext = super.preRequest(messageEventContext);
        httpApiContext.setUrl("https://zj.v.api.aa1.cn/api/wenan-mj/?type=text");
        return httpApiContext;
    }

    @Override
    public String word() {
        return "文案";
    }
}
