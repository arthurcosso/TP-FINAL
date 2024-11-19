package metrics;

import java.awt.image.BufferedImage;

public class Metrics {
    public static double calculateCompressionRate(long originalSize, long compressedSize) {
        return 100.0 * (1.0 - ((double) compressedSize / originalSize));
    }

    public static double calculateMSE(BufferedImage original, BufferedImage decompressed) {
        int width = original.getWidth();
        int height = original.getHeight();
        double mse = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgbOriginal = original.getRGB(x, y);
                int rgbDecompressed = decompressed.getRGB(x, y);

                int rDiff = ((rgbOriginal >> 16) & 0xFF) - ((rgbDecompressed >> 16) & 0xFF);
                int gDiff = ((rgbOriginal >> 8) & 0xFF) - ((rgbDecompressed >> 8) & 0xFF);
                int bDiff = (rgbOriginal & 0xFF) - (rgbDecompressed & 0xFF);

                mse += rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
            }
        }

        mse /= (width * height * 3); // Três canais de cor
        return mse;
    }

    public static double calculatePSNR(double mse) {
        if (mse == 0) return Double.POSITIVE_INFINITY; // Sem perda
        return 10 * Math.log10((255 * 255) / mse);
    }
}
