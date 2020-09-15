package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Test {

    private static Test.Node node = new Test().new Node(1, null);

    class Node {
        public int value;
        public Node next;
        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

  public static Node reverseList(Node node) {
        Node prev = null;
        Node current = node;
        Node next = null;
        while (current != null) {
            node = current.next;
            current.next = prev;
            prev = current;
            current = node;
        }
        node = prev;
        return node;
    }

    public static boolean isAnagram(String word1, String word2) {
        Map<Character,Integer> word1map = new HashMap<Character,Integer>();
        Map<Character,Integer> word2map = new HashMap<Character,Integer>();
        //  build map
        for (int i = 0; i < word1.length(); i++) {
            char c1 = word1.charAt(i);
            char c2 = word2.charAt(i);
            Integer k1 = word1map.get(c1);
            if (k1 == null) {
                word1map.put(c1, 1);
            } else {
                word1map.put(c1, ++k1);
            }
            Integer k2 = word2map.get(c2);
            if (k2 == null) {
                word2map.put(c2, 1);
            } else {
                word2map.put(c2, ++k2);
            }
        }
        // compare
        for (Character k : word1map.keySet()) {
            Integer v1 = word1map.get(k);
            Integer v2 = word2map.get(k);
            if (!v1.equals(v2)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Node node2 = reverseList(node);
        while (node2 != null) {
            System.out.println(node2.value);
            node2 = node2.next;
        }
    }
}
