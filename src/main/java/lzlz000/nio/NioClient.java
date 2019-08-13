package lzlz000.nio;

import java.util.Scanner;

import static lzlz000.Const.*;

public class NioClient {

    private static NioClientHandler nioClientHandler;

    private static void start(){
        if(nioClientHandler !=null)
            nioClientHandler.stop();
        nioClientHandler = new NioClientHandler(DEFAULT_SERVER_IP,DEFAULT_PORT);
        new Thread(nioClientHandler,"EchoClient").start();
    }
    public static void main(String[] args) throws Exception {
        start();

        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入消息:");
        String input = scanner.next();
        while (!Thread.currentThread().isInterrupted()){
            nioClientHandler.sendMsg(input);
            input = scanner.next();
        }

    }

}
