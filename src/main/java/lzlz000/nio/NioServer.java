package lzlz000.nio;

import static lzlz000.Const.DEFAULT_PORT;

public class NioServer {
    private static NioServerHandler nioServerHandler;

    private static void start(){
        if(nioServerHandler !=null)
            nioServerHandler.stop();
        nioServerHandler = new NioServerHandler(DEFAULT_PORT);
        new Thread(nioServerHandler,"Server").start();
    }
    public static void main(String[] args){
        start();
    }
}
