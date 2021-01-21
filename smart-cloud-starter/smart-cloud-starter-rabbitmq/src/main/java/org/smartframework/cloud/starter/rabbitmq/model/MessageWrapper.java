package org.smartframework.cloud.starter.rabbitmq.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.smartframework.cloud.utility.JacksonUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 消息传输对象
 *
 * @author liyulin
 * @date 2020-08-12
 */
@Getter
@Setter
@ToString
public class MessageWrapper implements Serializable {

    /**
     * 消息唯一id（用于幂等校验）
     */
    private String msgId;
    /**
     * 消息体
     */
    private String payload;

    public MessageWrapper() {
        this.msgId = UUID.randomUUID().toString();
    }

    public MessageWrapper(String payload) {
        this();
        this.payload = payload;
    }

    /**
     * 将消息传输对象转化成rabbitmq的{@link Message}
     *
     * @return
     */
    public Message toAmqpMessage() {
        byte[] body = JacksonUtil.toJson(this).getBytes(StandardCharsets.UTF_8);
        return MessageBuilder.withBody(body).build();
    }

    /**
     * 将rabbitmq的{@link Message}转化为{@link MessageWrapper}
     *
     * @param message
     * @return
     */
    public static MessageWrapper convert(Message message) {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        return JacksonUtil.parseObject(body, MessageWrapper.class);
    }

}