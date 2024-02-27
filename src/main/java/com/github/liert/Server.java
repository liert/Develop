package com.github.liert;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static ServerSocket socket = null;
    private final int port;

    Server(int port) {
        this.port = port;
    }
    public void connect() {
        Thread thread = new Thread(new PortListener(port));
        thread.start();
        Develop.p.sendMessage("正在监听 " + port + " 端口");
    }
    static class PortListener implements Runnable {
        private final int port;
        PortListener(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(this.port)) {
                Server.socket = serverSocket;
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    handleClient(clientSocket);
                }
            } catch (IOException e) {
                Develop.p.sendMessage("连接建立失败...");
                e.printStackTrace();
            }
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        // 获取输入流，从客户端读取数据
        InputStream inputStream = clientSocket.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        StringBuilder message = new StringBuilder();
        // 读取客户端发送的数据
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            message.append(new String(buffer, 0, bytesRead));
        }

        Develop.p.sendMessage("正在执行: " + message);
        Develop.p.chat("/" + message);
        // 关闭连接
        clientSocket.close();
    }
    public static void close() {
        if (Server.socket != null) {
            try {
                Server.socket.close();
                Develop.p.sendMessage("关闭监听");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}