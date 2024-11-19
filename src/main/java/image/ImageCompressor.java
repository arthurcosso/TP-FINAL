package image;

import huffman.HuffmanTree;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ImageCompressor {
    public static void compressImage(String imagePath, String outputFilePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        int width = image.getWidth();
        int height = image.getHeight();

        Map<Integer, Integer> redFrequency = new HashMap<>();
        Map<Integer, Integer> greenFrequency = new HashMap<>();
        Map<Integer, Integer> blueFrequency = new HashMap<>();

        // Calcular frequências
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                redFrequency.put(red, redFrequency.getOrDefault(red, 0) + 1);
                greenFrequency.put(green, greenFrequency.getOrDefault(green, 0) + 1);
                blueFrequency.put(blue, blueFrequency.getOrDefault(blue, 0) + 1);
            }
        }

        // Construir árvores de Huffman
        HuffmanTree redTree = new HuffmanTree(redFrequency);
        HuffmanTree greenTree = new HuffmanTree(greenFrequency);
        HuffmanTree blueTree = new HuffmanTree(blueFrequency);

        Map<Integer, String> redCodes = redTree.getCodes();
        Map<Integer, String> greenCodes = greenTree.getCodes();
        Map<Integer, String> blueCodes = blueTree.getCodes();

        // Codificar os dados
        StringBuilder encodedData = new StringBuilder();
        encodedData.append("Width: ").append(width).append("\n");
        encodedData.append("Height: ").append(height).append("\n");
        encodedData.append("Red Frequencies:\n");
        redFrequency.forEach((key, value) -> encodedData.append(key).append(":").append(value).append(" "));
        encodedData.append("\n");
        encodedData.append("Green Frequencies:\n");
        greenFrequency.forEach((key, value) -> encodedData.append(key).append(":").append(value).append(" "));
        encodedData.append("\n");
        encodedData.append("Blue Frequencies:\n");
        blueFrequency.forEach((key, value) -> encodedData.append(key).append(":").append(value).append(" "));
        encodedData.append("\n");
        encodedData.append("Compressed Data:\n");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                encodedData.append(redCodes.get(red));
                encodedData.append(greenCodes.get(green));
                encodedData.append(blueCodes.get(blue));
            }
        }

        // Escrever no arquivo
        FileWriter writer = new FileWriter(outputFilePath);
        writer.write(encodedData.toString());
        writer.close();
    }
}
