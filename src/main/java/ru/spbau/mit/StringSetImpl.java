package ru.spbau.mit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StringSetImpl implements StringSet, StreamSerializable {

    static class Node {
        Node[] lowCase = new Node[26];
        Node[] uppCase = new Node [26];
        boolean terminated = false;
        int prefixNumber = 0;
        static String curText = "";

        public Node getNext(char ch) {
            if (Character.isUpperCase(ch))
                return uppCase[ch - 'A'];
            else
                return lowCase[ch - 'a'];
        }

        public void addNode(char ch) {
            if (Character.isUpperCase(ch))
                uppCase[ch - 'A'] = new Node();
            else
                lowCase[ch - 'a'] = new Node();
        }

        public void print(OutputStream out) throws IOException {
            for (int i = 0; i < 26; i++) {
                if (uppCase[i] != null) {
                    curText += (char)('A' + i);
                    if (uppCase[i].terminated) {
                        out.write(curText.getBytes("UTF-8"));
                        out.write("\n".getBytes("UTF-8"));
                    }
                    uppCase[i].print(out);
                    curText = curText.substring(0, curText.length() - 1);
                }
            }
            for (int i = 0; i < 26; i++) {
                if (lowCase[i] != null) {
                    curText += (char) ('a' + i);
                    if (lowCase[i].terminated) {
                        out.write(curText.getBytes("UTF-8"));
                        out.write("\n".getBytes("UTF-8"));
                    }
                    lowCase[i].print(out);
                    curText = curText.substring(0, curText.length() - 1);
                }
            }
        }
    }

    private Node root;

    public StringSetImpl() {
        root = new Node();
    }

    public boolean contains(String element) {
        Node v = root;
        for (char ch : element.toCharArray()) {
            if (v.getNext(ch) == null)
                return false;
            else
                v = v.getNext(ch);
        }
        return (v.terminated);
    }

    public boolean add(String element) {
        if (!contains(element)) {
            Node v = root;
            v.prefixNumber++;
            for (char ch: element.toCharArray()) {
                Node next = v.getNext(ch);
                if (next == null) {
                    v.addNode(ch);
                    next = v.getNext(ch);
                }
                next.prefixNumber++;
                v = next;
            }
            v.terminated = true;
            return true;
        }
        return false;
    }

    public boolean remove(String element) {
        if (contains(element)) {
            Node v = root;
            v.prefixNumber--;
            for (char ch: element.toCharArray()) {
                v = v.getNext(ch);
                v.prefixNumber--;
            }
            v.terminated = false;
            return true;
        }
        return false;
    }

    public int size() {
        return root.prefixNumber;
    }

    public int howManyStartsWithPrefix(String prefix) {
        Node v = root;
        for (char ch : prefix.toCharArray())
            v = v.getNext(ch);
        return (v.prefixNumber);
    }

    public void serialize(OutputStream out) {
        try {
            root.print(out);
        }
        catch (IOException exp) {
            throw new SerializationException();
        }
    }

    public void deserialize(InputStream in) {
        try {
            root = new Node();
            StringBuilder sb = new StringBuilder();
            char ch;
            while (in.available() != 0) {
                ch = (char)in.read();
                if (ch != '\n')
                    sb.append(ch);
                else {
                    add(sb.toString());
                    sb.setLength(0);
                }
            }
        }
        catch (IOException exp) {
            throw new SerializationException();
        }
    }
}
