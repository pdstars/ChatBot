package org.zhong.chatgpt.wechat.bot.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageCompressor {
    /**
     * 图片压缩
     * @param inputImage
     * @param outputImage
     * @param targetFileSizeKB
     * @throws IOException
     */
    public static void compressImage(File inputImage, File outputImage, long targetFileSizeKB) throws IOException {
        BufferedImage image = ImageIO.read(inputImage);
        Image compressedImage = image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH);

        BufferedImage bufferedCompressedImage = new BufferedImage(compressedImage.getWidth(null), compressedImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedCompressedImage.createGraphics();
        g2d.drawImage(compressedImage, 0, 0, null);
        g2d.dispose();

        float quality = 1.0f;
        long outputFileSize = outputImage.length() / 1024; // 当前文件大小
        long targetFileSize = targetFileSizeKB; // 目标文件大小，单位为KB

        if (outputFileSize > targetFileSize) {
            do {
                // 逐渐降低图片质量并重新压缩，直到目标文件大小满足要求
                quality -= 0.05f;
                ImageIO.write(bufferedCompressedImage, "jpeg", outputImage);
                outputFileSize = outputImage.length() / 1024;
            } while (outputFileSize > targetFileSize && quality > 0.05f);
        }
    }

}