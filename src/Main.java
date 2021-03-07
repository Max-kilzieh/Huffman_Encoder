// Max Kilzieh
// maki6493

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

        HuffmanCoder huffmanCoder = new HuffmanCoder();
        String test = "Hej och välkommen!";


        EncodedMessage<?, ?> encodedMessage = huffmanCoder.encode(test);
        System.out.println("encoded message in bitSet = " + encodedMessage.message);
        System.out.println("unencoded message = " + huffmanCoder.decode(encodedMessage));


        writeMessageToBinaryFile(encodedMessage, "test.bin");
        EncodedMessage<?, ?> fromBinaryFile = readMessageFromBinaryFile("test.bin");
        System.out.println("unencoded message from binary file = " + huffmanCoder.decode(fromBinaryFile));


        String file = readFileAsString("beskrivning.txt");
        EncodedMessage<?, ?> fromTextFile = huffmanCoder.encode(file);
        System.out.println("encoded message from beskrivning.txt= " + fromTextFile.message);
        System.out.println("unencoded message from beskrivning.txt = " + huffmanCoder.decode(fromTextFile));

    }

    private static void writeMessageToBinaryFile(EncodedMessage<?, ?> message, String fileName) {

        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(message);
            objectOut.close();
            System.out.println("The message was successfully written to test.bin");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static EncodedMessage<?, ?> readMessageFromBinaryFile(String fileName) {

        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(fileName))) {

            EncodedMessage<?, ?> message = (EncodedMessage<?, ?>) objectInputStream.readObject();
            objectInputStream.close();
            return message;

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String readFileAsString(String fileName) {
        String msg = "";

        try {
            msg = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return msg;
    }
}


