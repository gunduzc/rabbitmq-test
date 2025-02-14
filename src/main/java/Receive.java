import com.rabbitmq.stream.ByteCapacity;
import com.rabbitmq.stream.Consumer;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.OffsetSpecification;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Receive {
    private static final int MAX_MESSAGES = 1_000_000;

    public static void main(String[] args) throws IOException {

        long startTime = System.currentTimeMillis();

        Environment environment = Environment.builder().build();
        String stream = "hello-java-stream";
        environment.streamCreator().stream(stream).maxLengthBytes(ByteCapacity.GB(5)).create();

        AtomicInteger mod = new AtomicInteger();
        Consumer consumer = environment.consumerBuilder()
            .stream(stream)
            .offset(OffsetSpecification.first())
            .messageHandler((unused, message) -> {
                mod.getAndIncrement();
                if (mod.get() % 100000 == 0) System.out.println("Received message: " + new String(message.getBodyAsBinary()));
                if (mod.get() == MAX_MESSAGES) {
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    System.out.println("ReceiveStream execution time: " + duration + " milliseconds.");
                    System.exit(0);
                }

            }).build();


    }
}
