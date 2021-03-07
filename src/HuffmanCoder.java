// Max Kilzieh
// maki6493

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanCoder {
    private final int ASCII_TABLE_SIZE = 256;

    public EncodedMessage<?, ?> encode(String msg) {

        if (msg == null || msg.equals(""))
            throw new NullPointerException("Empty message, Nothing to encode.");

        int[] freq = buildFrequencyTable(msg);
        Node root = buildHuffmanTree(freq);
        Map<Character, String> lookupTable = buildLookupTable(root);

        return new EncodedMessage<>(root, generateEncodedData(msg, lookupTable));
    }

    private int[] buildFrequencyTable(String data) {

        int[] frequency = new int[ASCII_TABLE_SIZE];
        for (char character : data.toCharArray()) {
            frequency[character]++;
        }

        return frequency;
    }

    private Node buildHuffmanTree(int[] frequency) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

        for (char i = 0; i < ASCII_TABLE_SIZE; i++) {
            if (frequency[i] > 0) {
                priorityQueue.add(new Node(i, frequency[i], null, null));
            }
        }

        if (priorityQueue.size() == 1) {
            priorityQueue.add(new Node('\0', 1, null, null));
        }

        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            Node parent = new Node('\0', left.frequency + right.frequency, left, right);
            priorityQueue.add(parent);
        }

        return priorityQueue.poll();
    }

    private Map<Character, String> buildLookupTable(Node root) {
        Map<Character, String> lookupTable = new HashMap<>();
        buildLookupTableBinaryCode(root, "", lookupTable);

        return lookupTable;
    }

    private void buildLookupTableBinaryCode(Node node,
                                            String s,
                                            Map<Character, String> lookupTable) {

        if (!node.isLeaf()) {
            buildLookupTableBinaryCode(node.leftChild, s + '0', lookupTable);
            buildLookupTableBinaryCode(node.rightChild, s + '1', lookupTable);

        } else {
            lookupTable.put(node.character, s);
        }
    }

    private BitSet generateEncodedData(String data, Map<Character, String> lookupTable) {
        StringBuilder builder = new StringBuilder();
        for (char character : data.toCharArray()) {
            builder.append(lookupTable.get(character));

        }
        return stringToBitset(builder.toString());
    }

    private BitSet stringToBitset(String binary) {
        BitSet bitset = new BitSet(binary.length());
        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(i) == '1') {
                bitset.set(i);
            }
            //because Bitset length() return the index of last bit set to true;
            bitset.set(binary.length(), true);
        }
        return bitset;
    }


    public String decode(EncodedMessage<?, ?> msg) {
        // To pass the test!.
        EncodedMessage<Node, BitSet> encodedMsg = (EncodedMessage<Node, BitSet>) msg;

        StringBuilder messageBuilder = new StringBuilder();
        Node current = encodedMsg.header;

        int i = 0;
        while (i < encodedMsg.message.length() - 1) {
            while (!current.isLeaf()) {
                boolean bit = encodedMsg.message.get(i);

                current = bit ? current.rightChild : current.leftChild;

                i++;
            }
            messageBuilder.append(current.character);
            current = encodedMsg.header;
        }
        return messageBuilder.toString();
    }


    private static class Node implements Comparable<Node>, Serializable {
        private final char character;
        private final int frequency;
        private final Node leftChild;
        private final Node rightChild;

        Node(char character, int frequency, Node leftChild, Node rightChild) {
            this.character = character;
            this.frequency = frequency;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        boolean isLeaf() {
            return leftChild == null && rightChild == null;
        }

        @Override
        public int compareTo(Node other) {
            int frequencyComparison = Integer.compare(this.frequency, other.frequency);
            if (frequencyComparison != 0) {
                return frequencyComparison;
            }

            return Integer.compare(this.character, other.character);
        }
    }
}
