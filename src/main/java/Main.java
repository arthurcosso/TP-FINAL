import image.ImageCompressor;
import image.ImageDecompressor;
import metrics.Metrics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Diretório de saída fixo
        String outputDir = "src/main/resources/output";

        // Garantir que o diretório de saída exista
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs(); // Criar diretórios se não existirem
        }

        System.out.println("Escolha uma opção:");
        System.out.println("1 - Comprimir imagem");
        System.out.println("2 - Descomprimir arquivo");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha após o número

        try {
            if (choice == 1) {
                // Compressão
                System.out.println("Digite o caminho da imagem (BMP ou PNG):");
                String imagePath = scanner.nextLine(); // Caminho da imagem original

                String outputFilePath = outputDir + File.separator + "imagem_compactada.txt";

                long startTime = System.currentTimeMillis();
                ImageCompressor.compressImage(imagePath, outputFilePath);
                long endTime = System.currentTimeMillis();

                long originalSize = new File(imagePath).length();
                long compressedSize = new File(outputFilePath).length();
                double compressionRate = Metrics.calculateCompressionRate(originalSize, compressedSize);

                System.out.println("Imagem compactada com sucesso!");
                System.out.println("Taxa de compressão: " + String.format("%.2f", compressionRate) + "%");
                System.out.println("Tempo de compressão: " + (endTime - startTime) + " ms");

            } else if (choice == 2) {
                // Descompressão
                System.out.println("Digite o caminho do arquivo compactado:");
                String compressedFilePath = scanner.nextLine();

                System.out.println("Digite o caminho da imagem original (para cálculo de qualidade):");
                String imagePath = scanner.nextLine();

                String outputImagePath = outputDir + File.separator + "imagem_restaurada.png";

                long startTime = System.currentTimeMillis();
                ImageDecompressor.decompressImage(compressedFilePath, outputImagePath);
                long endTime = System.currentTimeMillis();

                BufferedImage originalImage = ImageIO.read(new File(imagePath));
                BufferedImage decompressedImage = ImageIO.read(new File(outputImagePath));

                double mse = Metrics.calculateMSE(originalImage, decompressedImage);
                double psnr = Metrics.calculatePSNR(mse);

                System.out.println("Imagem descompactada com sucesso!");
                System.out.println("Tempo de descompressão: " + (endTime - startTime) + " ms");
                System.out.println("Erro médio quadrático (MSE): " + mse);
                System.out.println("Pico de Sinal-Ruído (PSNR): " + String.format("%.2f", psnr) + " dB");

            } else {
                System.out.println("Opção inválida! Escolha 1 ou 2.");
            }
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}


