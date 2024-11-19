package huffman;

class HuffmanNode {
    int value;
    int frequency;
    HuffmanNode left, right;

    HuffmanNode(int value, int frequency) {
        this.value = value;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }
}
