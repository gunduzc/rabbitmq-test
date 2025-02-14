import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SendQuorum {

    private final static String QUEUE_NAME = "hello_quorum";
    private final static int NUMBER_OF_MESSAGES = 1_000_000;

    public static void main(String[] argv) throws Exception {

        long startTime = System.currentTimeMillis();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, Map.of("x-queue-type", "quorum"));

            String message = "";
            for (int i = 0; i < NUMBER_OF_MESSAGES; i++) {
                message = RandomStringGenerator.generateRandomString(i);
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            }

            System.out.println(" [x] Sent '" + message + "'");
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("SendQuorum execution time: " + duration + " milliseconds.");
    }
}