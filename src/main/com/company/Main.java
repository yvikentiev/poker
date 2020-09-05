package main.com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static final String CARD_DIR_NAME = "./cards";
    //public static final String COMPARE_DIR_NAME = "./compare/";
    public static final String EXTRACT_DIR_NAME = "./extract/";
    private static Map<String, BufferedImage> cardsMap = new HashMap<String, BufferedImage>();

    public static void loadCards(String dir) throws IOException {
        Files.find(Paths.get(dir),
            Integer.MAX_VALUE,
            (filePath, fileAttr) -> fileAttr.isRegularFile())
            .forEach(path -> loadCard(path));
    }

    public static void loadCard(Path path)  {
        String code = null;
        try {
            BufferedImage img = ImageIO.read(path.toFile());
            String k = path.toFile().getName();
            code = k.substring(k.lastIndexOf("/")+1, k.indexOf(".png"));
            cardsMap.put(code, img);
            //System.out.println(path.toString());
        } catch (IOException e) {
            System.out.println("Error reading file " + code);
        }
    }

    public static int isEqualImages(BufferedImage img1, BufferedImage img2) {
        int w = 0;
        for (int i = 0; i < img1.getWidth(); i++)
            for (int j = 0; j < img1.getHeight(); j++) {
                Color c1 = new Color(img1.getRGB(i, j));
                Color c2 = new Color(img2.getRGB(i, j));
                if (!c1.equals(c2)) {
                    w++;
                }
            }
        return w;
    }

    public static void testTable(String name) throws IOException {
        int w = 20;
        int h = 20;
        File file = new File(name);
        loadCards(CARD_DIR_NAME + "/Clubs");
        System.out.print(name + "-");
        BufferedImage img = ImageIO.read(file);
        for (int i = 0; i < 5; i++) {
            BufferedImage img1 = img.getSubimage(
                143 + i * 72 + w, 585 + h, 63 - w, 89 - h);
            //ImageIO.write(img1, "png", new File(EXTRACT_DIR_NAME + file.getName() + "-"  + i));
            cardsMap.forEach((k, v) -> {
                System.out.println("(" + k + ',' + isEqualImages(img1, v) + ")");
            });
        }
        System.out.println();
    }


    public static void main(String[] args) throws IOException {
        //ImageIO.write(img1, "png", new File(COMPARE_DIR_NAME + file.getName() + "-"  + counter));
        //ImageIO.write(img1, "png", new File(EXTRACT_DIR_NAME + file.getName() + "-"  + i));
        File fileName = new File(args[0]);
        fileName.listFiles();
        loadCards(CARD_DIR_NAME);
        int w = 10;
        int h = 10;
        for (File file : fileName.listFiles()) {
            System.out.print(file + "-");
            BufferedImage img = ImageIO.read(file);
            for (int i = 0; i < 5; i++) {
                BufferedImage img1 = img.getSubimage(
                    143 + i * 72 + w, 585 + h, 63 - w, 89 - h);
                ImageIO.write(img1, "png", new File(EXTRACT_DIR_NAME + file.getName() + "-"  + i));
                cardsMap.forEach((k, v) -> {
                    if (isEqualImages(img1, v) < 100) {
                        System.out.print(k);
                    }
                });
            }
            System.out.println();
        }
    }
}
