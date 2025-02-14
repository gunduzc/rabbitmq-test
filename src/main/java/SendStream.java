import com.rabbitmq.stream.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SendStream {

    private static final int MAX_MESSAGES = 1_000_000;


    public static void main(String[] args) throws IOException {

        long startTime = System.currentTimeMillis();

        Environment environment = Environment.builder().build();
        String stream = "hello-java-stream";
        environment.streamCreator().stream(stream).maxLengthBytes(ByteCapacity.GB(5)).create();

        Producer producer = environment.producerBuilder().stream(stream).build();
        String message = "";
        for (int i = 0; i < MAX_MESSAGES; i++) {
            message = RandomStringGenerator.generateRandomString(i);
            producer.send(producer.messageBuilder().addData(message.getBytes()).build(), null);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("SendQuorum execution time: " + duration + " milliseconds.");

        System.out.println(" [x] Press Enter to close the producer...");
        System.in.read();
        producer.close();
        environment.close();
    }
}
