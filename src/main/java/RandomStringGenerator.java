import java.util.Random;

public class RandomStringGenerator {
    private static final int LENGTH = 1000;
    public static String generateRandomString(int id) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();

        // Generate a 10-digit ID
        String idString = String.format("%010d", id); // 10 digits
        randomString.append(idString);
        randomString.append("_");

        // Generate the random alphanumeric string of the remaining length
        for (int i = 0; i < (LENGTH - 11); i++) {
            int randomIndex = random.nextInt(characters.length());
            randomString.append(characters.charAt(randomIndex));
        }

        return randomString.toString();
    }
}