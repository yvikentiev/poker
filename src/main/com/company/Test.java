package com.company;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Test {

    private static Test.Node node = new Test().
        new Node(1, new Test().new Node(2, new Test().new Node(3,null)));

    class Node {
        public int value;
        public Node next;
        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    // 1 -> 2 -> 3 -> 4 -> 5
    //
    public static Node centerNode(Node head) {
        Node center = head;
        Node current = head;
        while (current.next != null) {
            Node temp = current;
            center = current.next;
            current = temp.next.next;
        }
        return center;
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

    public static void sort(int[] array) {
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array.length; j++) {
                if (array[j] < array[i]) {
                    int temp = array[i];
                    array[i] = array[j];
                }
            }
    }

    public static boolean isPolyndrom(String a) {
        for (int i = 0; i < a.length() /2; i++) {
            if (a.charAt(i) != a.charAt(a.length() -i))
                return false;
        }
        return true;
    }

    public static void main(String[] args)  {
        System.out.println(centerNode(node).value);
    }
}
