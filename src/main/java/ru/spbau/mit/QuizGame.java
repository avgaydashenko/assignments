package ru.spbau.mit;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class QuizGame implements Game {
    private final GameServer gameServer;

    private int delayUntilNextLetter;
    private int maxLettersToOpen;
    private String dictionaryFilename;

    private Thread thread;

    private List<String> questions = new ArrayList<>();
    private List<String> answers = new ArrayList<>();
    private Integer round;

    private Boolean isStopped;

    private class Task implements Runnable {
        @Override
        public void run() {
            while (!isStopped) {

                if (questions.size() == 0) {
                    try {
                        round = -1;
                        Scanner scanner = new Scanner(new File(dictionaryFilename));
                        while (scanner.hasNext()) {
                            String[] pair = (scanner.nextLine()).split(";");
                            questions.add(pair[0]);
                            answers.add(pair[1]);
                        }
                    } catch (FileNotFoundException _) {}
                }

                round = (round + 1) % questions.size();
                gameServer.broadcast("New round started: "
                        + questions.get(round) + " ("
                        + String.valueOf(answers.get(round).length())
                        + " letters)");

                Boolean stopped = false;

                for (int i = 0; i < maxLettersToOpen; i++) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(delayUntilNextLetter);
                    } catch (InterruptedException _) {
                        stopped = true;
                        break;
                    }
                    if (!Thread.interrupted()) {
                        gameServer.broadcast("Current prefix is "
                                + answers.get(round).substring(0, i + 1));
                    } else {
                        stopped = true;
                        break;
                    }
                }

                if (stopped) {
                    continue;
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(delayUntilNextLetter);
                } catch (InterruptedException _) {
                    continue;
                }

                if (!Thread.interrupted()) {
                    gameServer.broadcast("Nobody guessed, the word was "
                            + answers.get(round));
                }

            }
        }
    }

    public QuizGame(GameServer server) {
        gameServer = server;
        isStopped = true;
    }

    public void setDelayUntilNextLetter(Integer delay) {
        delayUntilNextLetter = delay;
    }

    public void setMaxLettersToOpen(Integer lettersToOpen) {
        maxLettersToOpen = lettersToOpen;
    }

    public void setDictionaryFilename(String fileName) {
        dictionaryFilename = fileName;
    }

    @Override
    public void onPlayerConnected(String id) {}

    @Override
    public synchronized void onPlayerSentMsg(String id, String msg) {

        if (msg.equals("!start")) {

            if (isStopped) {
                if (thread != null) {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isStopped = false;
                thread = new Thread(new Task());
                thread.start();
            }
        } else if (msg.equals("!stop")) {
            isStopped = true;
            thread.interrupt();
            gameServer.broadcast("Game has been stopped by " + id);
        } else if (msg.equals(answers.get(round))) {
            gameServer.broadcast("The winner is " + id);
            thread.interrupt();
        } else {
            gameServer.sendTo(id, "Wrong try");
        }
    }
}
