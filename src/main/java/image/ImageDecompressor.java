package image;

import huffman.HuffmanTree;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class ImageDecompressor {
    public static void decompressImage(String compressedFilePath, String outputImagePath) throws IOException {
        List<String> lines;
        try {
            lines = Files.readAllLines(new File(compressedFilePath).toPath());
        } catch (IOException e) {
            throw new IOException("Error reading the compressed file: " + e.getMessage(), e);
        }

        Iterator<String> iterator = lines.iterator();

        int width = Integer.parseInt(iterator.next().split(": ")[1]);  // Largura
        int height = Integer.parseInt(iterator.next().split(": ")[1]); // Altura

        iterator.next(); // Ignorar "Red Frequencies:"
        Map<Integer, Integer> redFrequency = parseFrequencies(iterator.next());

        iterator.next(); // Ignorar "Green Frequencies:"
        Map<Integer, Integer> greenFrequency = parseFrequencies(iterator.next());

        iterator.next(); // Ignorar "Blue Frequencies:"
        Map<Integer, Integer> blueFrequency = parseFrequencies(iterator.next());

        iterator.next(); // Ignorar "Compressed Data:"

        StringBuilder compressedDataBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            compressedDataBuilder.append(iterator.next());
        }
        BitSet compressedData = convertToBitSet(compressedDataBuilder.toString());

        HuffmanTree redTree = new HuffmanTree(redFrequency);
        HuffmanTree greenTree = new HuffmanTree(greenFrequency);
        HuffmanTree blueTree = new HuffmanTree(blueFrequency);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] bitIndex = {0};

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int red = redTree.decode(compressedData, bitIndex);
                int green = greenTree.decode(compressedData, bitIndex);
                int blue = blueTree.decode(compressedData, bitIndex);
                int rgb = (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, rgb);
            }
        }

        try {
            ImageIO.write(image, "png", new File(outputImagePath));
        } catch (IOException e) {
            throw new IOException("Error writing the output image: " + e.getMessage(), e);
        }
    }

    private static Map<Integer, Integer> parseFrequencies(String line) {
        Map<Integer, Integer> frequencies = new HashMap<>();
        String[] pairs = line.split(" ");
        for (String pair : pairs) {
            if (pair.contains(":")) { // Ignorar itens sem ":" (possivelmente rótulos)
                String[] kv = pair.split(":");
                int key = Integer.parseInt(kv[0]);  // Valor RGB
                int value = Integer.parseInt(kv[1]); // Frequência
                frequencies.put(key, value);
            }
        }
        return frequencies;
    }

    private static BitSet convertToBitSet(String binaryString) {
        BitSet bitSet = new BitSet(binaryString.length());
        for (int i = 0; i < binaryString.length(); i++) {
            if (binaryString.charAt(i) == '1') {
                bitSet.set(i);
            }
        }
        return bitSet;
    }
}
