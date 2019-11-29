package lzlz000.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioServerHandler{
    private Selector [] selectors = new Selector[2];
    private volatile boolean started;
    private ExecutorService selectorThreadPool = Executors.newFixedThreadPool(2);
    private ExecutorService workThreadPool = Executors.newFixedThreadPool(4);
    /**
     * 构造方法
     * @param port 指定要监听的端口号
     */
    NioServerHandler(int port) {
        try {

            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port));
            for (int i = 0; i < selectors.length; i++) {
                selectors[i] = Selector.open();
                serverChannel.register(selectors[i],SelectionKey.OP_ACCEPT);
            }
            started = true;
            System.out.println("服务器已启动，端口号："+port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    void stop(){
        started = false;
    }

    public void start(){
        for (Selector selector : selectors) {
            selectorThreadPool.execute(()->start(selector));
        }
    }

    private void start(Selector selector) {
        //循环遍历selector
        while(started){
            try{
                //阻塞,只有当至少一个注册的事件发生的时候才会继续.
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key;
                while(it.hasNext()){
                    key = it.next();
                    it.remove();
                    try{
                        handleInput(key,selector);
                    }catch(Exception e){
                        if(key != null){
                            key.cancel();
                            if(key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
        //selector关闭后会自动释放里面管理的资源
        if(selector != null)
            try{
                selector.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
    }
    private void handleInput(SelectionKey key,Selector selector){
        workThreadPool.execute(()->{
            try {
                if(key.isValid()){
                    //处理新接入的请求消息
                    if(key.isAcceptable()){
                        //获得关心当前事件的channel

                        //通过ServerSocketChannel的accept创建SocketChannel实例
                        //完成该操作意味着完成TCP三次握手，TCP物理链路正式建立
                        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                        SocketChannel sc = ssc.accept();
//                        if (sc != null) {
                            System.out.println("socket channel 建立连接");
                            //设置为非阻塞的
                            sc.configureBlocking(false);
                            //连接已经完成了，可以开始关心读事件了
                            sc.register(selector,SelectionKey.OP_READ);
//                        }
                    }
                    //读消息
                    if(key.isReadable()){
                        System.out.println("socket channel 数据准备完成,读取数据：");
                        SocketChannel sc = (SocketChannel) key.channel();
                        //创建ByteBuffer，并开辟一个1M的缓冲区
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        //读取请求码流，返回读取到的字节数
                        int readBytes = sc.read(buffer);
                        //读取到字节，对字节进行编解码
                        if(readBytes>0){
                            //将缓冲区当前的limit设置为position=0，
                            // 用于后续对缓冲区的读取操作
                            buffer.flip();
                            //根据缓冲区可读字节数创建字节数组
                            byte[] bytes = new byte[buffer.remaining()];
                            //将缓冲区可读字节数组复制到新建的数组中
                            buffer.get(bytes);
                            String message = new String(bytes,"UTF-8");
                            String result = "这里是服务器，收到你发送的消息：" + message;
                            System.out.println(result);
                            //处理数据...
                            //发送应答消息
                            doWrite(sc,result);
                        }
                        //链路已经关闭，释放资源
                        else if(readBytes<0){
                            key.cancel();
                            sc.close();
                        }
                    }

                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }
    //发送应答消息
    private void doWrite(SocketChannel channel,String response)
            throws IOException {
        //将消息编码为字节数组
        byte[] bytes = response.getBytes();
        //根据数组容量创建ByteBuffer
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        //将字节数组复制到缓冲区
        writeBuffer.put(bytes);
        //flip操作
        writeBuffer.flip();
        //发送缓冲区的字节数组
        channel.write(writeBuffer);
    }
}
