package ru.spbau.mit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


public class GameServerImpl implements GameServer {

    private Game game;
    private Map<String, Connection> connectionMap = new HashMap<String, Connection>();
    private Integer id = 0;

    private class Task implements Runnable {
        private final String id;
        private final Connection connection;

        public Task (String id, Connection connection) {
            this.id = id;
            this.connection = connection;
        }

        @Override
        public void run() {
            game.onPlayerConnected(id);
            while (!connection.isClosed()) {
                try {
                    synchronized (connection) {
                        if (!connection.isClosed()) {
                            String msg = connection.receive(1000);
                            if (msg != null) {
                                game.onPlayerSentMsg(id, msg);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
            synchronized (connectionMap) {
                connectionMap.remove(id);
            }
        }
    }

    public GameServerImpl(String gameClassName, Properties properties)
            throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException,
            InstantiationException {

        Class<?> pluginClass = Class.forName(gameClassName);
        Object plugin = pluginClass.getConstructor(GameServer.class).newInstance(this);

        if (!(plugin instanceof Game)) {
            throw new IllegalArgumentException();
        }

        for (String key: properties.stringPropertyNames()) {

            String value = properties.getProperty(key);
            String name = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);

            try {
                int intValue = Integer.parseInt(value);
                Method setter = pluginClass.getMethod(name, Integer.class);
                setter.invoke(plugin, intValue);
            } catch (NumberFormatException _) {
                Method setter = pluginClass.getMethod(name, String.class);
                setter.invoke(plugin, value);
            }
        }

        game = (Game) plugin;
    }

    @Override
    public void accept(final Connection connection) {
        synchronized (connectionMap) {
            connectionMap.put(id.toString(), connection);
        }

        connection.send(id.toString());

        Thread thread = new Thread(new Task(id.toString(), connection));
        thread.start();
        id++;
    }

    @Override
    public void broadcast(String message) {
        synchronized (connectionMap) {
            for (Connection connection: connectionMap.values()) {
                connection.send(message);
            }
        }
    }

    @Override
    public void sendTo(String id, String message) {
        synchronized (connectionMap) {
            connectionMap.get(id).send(message);
        }
    }
}
