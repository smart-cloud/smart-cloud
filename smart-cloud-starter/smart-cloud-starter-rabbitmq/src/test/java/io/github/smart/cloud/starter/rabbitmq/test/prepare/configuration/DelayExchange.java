package io.github.smart.cloud.starter.rabbitmq.test.prepare.configuration;

import com.github.fridujo.rabbitmq.mock.AmqArguments;
import com.github.fridujo.rabbitmq.mock.ReceiverRegistry;
import com.github.fridujo.rabbitmq.mock.exchange.MockDirectExchange;
import com.rabbitmq.client.AMQP;
import io.github.smart.cloud.starter.rabbitmq.MqConstants;
import org.springframework.amqp.core.MessageProperties;

import java.util.concurrent.TimeUnit;

/**
 * mock 延迟交换机
 *
 * @author collin
 * @date 2022-02-21
 */
public class DelayExchange extends MockDirectExchange {

    public DelayExchange(String name, AmqArguments arguments, ReceiverRegistry receiverRegistry) {
        super(name, arguments, receiverRegistry);
    }

    @Override
    public String getType() {
        return MqConstants.DELAY_MESSAGE_TYPE;
    }

    @Override
    public boolean publish(String previousExchangeName, String routingKey, AMQP.BasicProperties props, byte[] body) {
        try {
            TimeUnit.MILLISECONDS.sleep((long) props.getHeaders().get(MessageProperties.X_DELAY));
        } catch (InterruptedException e) {
        }
        return super.publish(previousExchangeName, routingKey, props, body);
    }

}