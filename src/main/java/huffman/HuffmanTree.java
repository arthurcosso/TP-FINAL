package huffman;

import java.util.*;

public class HuffmanTree {
    private final Map<Integer, String> huffmanCodes = new HashMap<>();
    private final HuffmanNode root;

    public HuffmanTree(Map<Integer, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.frequency));

        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            queue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (queue.size() > 1) {
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();
            assert right != null;
            HuffmanNode newNode = new HuffmanNode(-1, left.frequency + right.frequency);
            newNode.left = left;
            newNode.right = right;
            queue.add(newNode);
        }

        root = queue.poll();
        generateCodes(root, "");
    }

    private void generateCodes(HuffmanNode node, String code) {
        if (node == null) return;
        if (node.value != -1) {
            huffmanCodes.put(node.value, code);
        }
        generateCodes(node.left, code + "0");
        generateCodes(node.right, code + "1");
    }

    public Map<Integer, String> getCodes() {
        return huffmanCodes;
    }

    public int decode(BitSet data, int[] bitIndex) {
        HuffmanNode current = root;
        while (current.left != null && current.right != null) {
            if (data.get(bitIndex[0])) {
                current = current.right;
            } else {
                current = current.left;
            }
            bitIndex[0]++;
        }
        return current.value;
    }
}
