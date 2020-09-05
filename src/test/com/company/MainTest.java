package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import main.com.company.Main;
import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    public void testCompare() throws IOException {
        Main.testTable("./tables/20180821_120219.175_0x1FE201D8.png");
    }
}
