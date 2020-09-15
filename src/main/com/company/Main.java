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
    public static final String DIFF_DIR_NAME = "./diff/";

    private static Map<String, BufferedImage> cardsMap = new HashMap<String, BufferedImage>();
    private static Set<Color> colors = new HashSet<>(
        Arrays.asList(
            new Color(255,255,255), // white
            new Color(35,35,38), // black
            new Color(245,166,35), // border
            new Color(205,73,73) // red
        ));

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

    public static int isEqualImages(BufferedImage img1, BufferedImage img2, BufferedImage diff) {
        int w = 0;
        int d = 0;
        Map<Color, Integer> colorHashMap = new HashMap<Color, Integer>();
        System.out.println(colors.contains(new Color(255,255,255)));
        for (int i = d; i < img1.getWidth() - d; i++)
            for (int j = d; j < img1.getHeight() - d; j++) {
                Color c1 = new Color(img2.getRGB(i, j));
                Color c2 = new Color(img2.getRGB(i, j));

                Integer count = colorHashMap.get(c1);
                if (count == null) {
                    colorHashMap.put(c1, 1);
                } else {
                    colorHashMap.put(c1, ++count);
                }
                System.out.println(c1 + "-" + colors.contains(c1));

                if (colors.contains(c1)) {
                    if (!c1.equals(c2)) {
                        diff.setRGB(i, j, c2.getRGB());
                        w++;
                    }
                }
            }

        for (Color color : colorHashMap.keySet()) {
            Integer count = colorHashMap.get(color);
            if (count > 100) {
                System.out.println(color + "-" + count);
            }
        }
        return w;
    }

    public static void testTable(String name) throws IOException {
        int w = 0;
        int h = 0;
        File file = new File(name);
        loadCards(CARD_DIR_NAME + "/Diamonds");
        System.out.print(name + "-");
        BufferedImage img = ImageIO.read(file);
        for (int i = 1; i < 2; i++) {
            BufferedImage img1 = img.getSubimage(
                143 + i * 72 + w, 585 + h, 63 - w, 89 - h);
            //ImageIO.write(img1, "png", new File(EXTRACT_DIR_NAME + file.getName() + "-"  + i));
            int index = i;
            cardsMap.forEach((k, v) -> {
                BufferedImage diff = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_BGR);
                System.out.println("(" + k + ',' + isEqualImages(img1, v, diff) + ")");
                try {
                    ImageIO.write(diff, "png", new File(DIFF_DIR_NAME + file.getName() + "-"  + k));
                } catch (IOException e) {
                    System.out.println("Error write file " + index);
                }
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
        int w = 0;
        int h = 0;
        for (File file : fileName.listFiles()) {
            System.out.print(file + "-");
            BufferedImage img = ImageIO.read(file);
            for (int i = 0; i < 5; i++) {
                BufferedImage img1 = img.getSubimage(
                    143 + i * 72 + w, 585 + h, 63 - w, 89 - h);
                ImageIO.write(img1, "png", new File(EXTRACT_DIR_NAME + file.getName() + "-"  + i));
                BufferedImage diff = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_BGR);
                cardsMap.forEach((k, v) -> {
                    if (isEqualImages(img1, v,diff) < 100) {
                        System.out.print(k);
                    }
                });
            }
            System.out.println();
        }
    }
}
