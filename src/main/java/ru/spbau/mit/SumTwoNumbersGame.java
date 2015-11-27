package ru.spbau.mit;

import java.util.*;

public class SumTwoNumbersGame implements Game {
    private final GameServer gameServer;

    private final Random random = new Random();
    private Integer num1 = newNum(), num2 = newNum();

    private Integer newNum() {
        return Math.abs(random.nextInt());
    }

    public SumTwoNumbersGame(GameServer server) {
        gameServer = server;
    }

    @Override
    public void onPlayerConnected(String id) {
        gameServer.sendTo(id, num1.toString() + " " + num2.toString());
    }

    @Override
    public void onPlayerSentMsg(String id, String msg) {
        if (Integer.parseInt(msg) == num1 + num2) {

            num1 = newNum();
            num2 = newNum();

            gameServer.sendTo(id, "Right");
            gameServer.broadcast(id + " won");
            gameServer.broadcast(num1.toString() + " " + num2.toString());
        } else {
            gameServer.sendTo(id, "Wrong");
        }
    }
}
