package ru.spbau.mit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StringSetImpl implements StringSet, StreamSerializable {

    private static class Node {

        private static final int alphLen = 26;

        Node[] lowCase = new Node[alphLen];
        Node[] uppCase = new Node[alphLen];
        boolean terminated = false;
        int prefixNumber = 0;
        static String curText = "";

        private Node getNext(char ch) {
            if (Character.isUpperCase(ch))
                return uppCase[ch - 'A'];
            else
                return lowCase[ch - 'a'];
        }

        private void addNode(char ch) {
            if (Character.isUpperCase(ch))
                uppCase[ch - 'A'] = new Node();
            else
                lowCase[ch - 'a'] = new Node();
        }

        private void print(OutputStream out) throws IOException {
            for (int i = 0; i < alphLen; i++) {
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
            for (int i = 0; i < alphLen; i++) {
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

    private Node root = new Node();

    public boolean contains(String element) {
        Node currNode = root;
        for (char ch : element.toCharArray()) {
            if (currNode.getNext(ch) == null)
                return false;
            else
                currNode = currNode.getNext(ch);
        }
        return (currNode.terminated);
    }

    public int howManyStartsWithPrefix(String prefix) {
        Node currNode = root;
        for (char ch : prefix.toCharArray()) {
            currNode = currNode.getNext(ch);
            if (currNode == null)
                return 0;
        }
        return (currNode.prefixNumber);
    }

    public boolean add(String element) {
        if (!contains(element)) {
            Node currNode = root;
            currNode.prefixNumber++;
            for (char ch: element.toCharArray()) {
                Node next = currNode.getNext(ch);
                if (next == null) {
                    currNode.addNode(ch);
                    next = currNode.getNext(ch);
                }
                next.prefixNumber++;
                currNode = next;
            }
            currNode.terminated = true;
            return true;
        }
        return false;
    }

    public boolean remove(String element) {
        if (contains(element)) {
            Node currNode = root;
            currNode.prefixNumber--;
            for (char ch: element.toCharArray()) {
                currNode = currNode.getNext(ch);
                currNode.prefixNumber--;
            }
            currNode.terminated = false;
            return true;
        }
        return false;
    }

    public int size() {
        return root.prefixNumber;
    }

    public void serialize(OutputStream out) {
        try {
            if (root.terminated) {
                out.write("#".getBytes("UTF-8"));
                out.write("\n".getBytes("UTF-8"));
            }
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
                if (ch == '#') {
                    add("");
                    continue;
                }
                if (ch != '\n' && ch != '\r')
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
