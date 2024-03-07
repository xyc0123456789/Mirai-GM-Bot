package com.king.api.messageapis.common.keyword;

import com.king.model.MessageEventContext;
import com.king.model.Response;

public interface KeyWordHandler {

    Response handler(MessageEventContext messageEventContext);

    String word();
}
