import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RecvQuroumMod {

    private final static String QUEUE_NAME = "hello_quorum";
    private static final int MAX_MESSAGES = 1_000_000;

    public static void main(String[] argv) throws Exception {

        long startTime = System.currentTimeMillis();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, Map.of("x-queue-type", "quorum"));
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        AtomicInteger messageCount = new AtomicInteger(0);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            // System.out.println(" [x] Received '" + message + "'");

            int currentCount = messageCount.incrementAndGet();
            if (currentCount % 1000 == 0) {
                System.out.println("Completed " + currentCount + " messages");
            }
            if (currentCount == MAX_MESSAGES) {
                System.out.println("Received " + MAX_MESSAGES + " messages. Exiting...");
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                System.out.println("RecvQuorumMod execution time: " + duration + " milliseconds.");
                System.exit(0);
            }
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});


    }
}