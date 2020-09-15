package main.com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
            new Color(193,75,110), // red
            new Color(120,120,120), // greyed white
            new Color(16,16,18), // grayed black
            new Color(96,96,34) // grayed black
        ));


    private static class CardValue {
        public String card;
        public int value;

        public CardValue(String card, int value) {
            this.card = card;
            this.value = value;
        }
    }

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

    public static boolean similarTo(Color c,Color v){
        double distance = Math.sqrt((c.getRed() - v.getRed())*(c.getRed() - v.getRed()) + (c.getGreen() - v.getGreen())*(c.getGreen() - v.getGreen()) + (c.getBlue() - v.getBlue())*(c.getBlue() - v.getBlue()));
        if(distance < 100 ) {
            return true;
        } else{
            return false;
        }
    }

    public static int diffImages(BufferedImage img1,
                                 BufferedImage img2,
                                 BufferedImage diff) {
        int w = 0;
        int d = 5;
        Map<Color, Integer> colorHashMap = new HashMap<Color, Integer>();
        for (int i = d; i < img1.getWidth() - d; i++)
            for (int j = d; j < img1.getHeight() - d; j++) {
                Color c1 = new Color(img1.getRGB(i, j));
                Color c2 = new Color(img2.getRGB(i, j));

                double distance = Math.sqrt(
                    Math.pow(c1.getRed()  - c2.getRed(),   2) +
                        Math.pow(c1.getGreen()- c2.getGreen(), 2) +
                        Math.pow(c1.getBlue() - c2.getBlue(),  2)
                );

                if (colors.contains(c1) && distance > 100) {
                    // count pixels of different colors
                    diff.setRGB(i, j, Color.WHITE.getRGB());
                    w++;
                }
            }
        return w;
    }

    public static void testTable(String name) throws IOException {
        int w = 0;
        int h = 0;
        File file = new File(name);
        loadCards(CARD_DIR_NAME + "");
        System.out.print(name + "-");
        BufferedImage img = ImageIO.read(file);
        for (int i = 0; i < 6; i++) {
            BufferedImage img1 = img.getSubimage(
                143 + i * 72 + w, 585 + h, 63 - w, 89 - h);
            //ImageIO.write(img1, "png", new File(EXTRACT_DIR_NAME + file.getName() + "-"  + i));
            int index = i;
            List<CardValue> list = new ArrayList<>();
            cardsMap.forEach((k, v) -> {
                BufferedImage diff = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_BGR);
                int value = diffImages(img1, v, diff);
                //System.out.println("(" + k + ',' + index + ',' + value + ")");
                list.add(new CardValue(k,value));
                try {
                    ImageIO.write(diff, "png", new File(DIFF_DIR_NAME + file.getName() + "-"  + index + "-" + k));
                } catch (IOException e) {
                    System.out.println("Error write file " + index);
                }
            });
            CardValue[] array = list.toArray(new CardValue[0]);
            Arrays.sort(array,
                (card1, card2) -> card2.value < card1.value ? 1 : -1);
            System.out.print(array[0].card);
            /*for (CardValue card: array) {
                System.out.println("(" + card.card + ',' + index+ "," +card.value + ")");
            }*/
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
                    if (diffImages(img1, v,diff) > 100) {
                        System.out.print(k);
                    }
                });
            }
            System.out.println();
        }
    }
}
