package com.king.util.xunfei.spark;

import io.github.briqt.spark4j.listener.SparkBaseListener;
import io.github.briqt.spark4j.model.SparkSyncChatResponse;
import io.github.briqt.spark4j.model.request.SparkRequest;
import io.github.briqt.spark4j.model.response.SparkResponse;
import io.github.briqt.spark4j.model.response.SparkResponseUsage;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.jetbrains.annotations.NotNull;

/**
 * @description: on message
 * @author: xyc0123456789
 * @create: 2023/10/9 19:28
 **/
public class SparkSyncChatListenerImpl extends SparkBaseListener {

    private final StringBuilder stringBuilder = new StringBuilder();

    private final SparkSyncChatResponse sparkSyncChatResponse;

    public static final String ERROR_STR = "99aff0bf548a43ae881d753d7bdebce7";

    public SparkSyncChatListenerImpl(SparkSyncChatResponse sparkSyncChatResponse) {
        this.sparkSyncChatResponse = sparkSyncChatResponse;
    }

    @Override
    public void onMessage(String content, SparkResponseUsage usage, Integer status, SparkRequest sparkRequest, SparkResponse sparkResponse, WebSocket webSocket) {
        stringBuilder.append(content);
        if (2 == status) {
            sparkSyncChatResponse.setContent(stringBuilder.toString());
            sparkSyncChatResponse.setTextUsage(usage.getText());
            sparkSyncChatResponse.setOk(true);
        }
    }
    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, Response response) {
        sparkSyncChatResponse.setContent("ERROR: " + t.getMessage());
        sparkSyncChatResponse.setOk(true);
        sparkSyncChatResponse.setTextUsage(null);
    }
}
