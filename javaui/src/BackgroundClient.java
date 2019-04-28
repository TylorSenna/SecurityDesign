
import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import static java.lang.Thread.sleep;


public class BackgroundClient {
    private Selector selector = null;
    static final int port = 9999;
    private Charset charset = Charset.forName("UTF-8");
    private SocketChannel sc = null;
    private String name = "";
    private static String USER_EXIST = "system message: user exist, please change a name";
    private static String USER_REGIST_SUCC = "system message: user regist success";
    private static String USER_CONTENT_SPILIT = "#@#";
    private JTextArea chattextarea;
    private JTextArea kerberostextarea;
    private JTextArea datatextarea;
    /*
    * 初始化一个用户进程
    * */
    public void init() throws IOException {
        selector = Selector.open();
        //连接远程主机的IP和端口
        sc = SocketChannel.open(new InetSocketAddress("127.0.0.1", port));
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
    }

    /*
    *申请匿名昵称
     */
    public boolean RequestAnonymous(String anonymousname) throws IOException, InterruptedException {
        if ("".equals(anonymousname)) return false; //不允许发空消息
        else if ("".equals(name)) {
            name = anonymousname;
            anonymousname = name + USER_CONTENT_SPILIT;
        }
        sc.write(charset.encode(anonymousname));//sc既能写也能读，这边是写

        ByteBuffer buff = ByteBuffer.allocate(1024);
        sleep(200);
        String content = "";
        while(sc.read(buff) > 0)
        {
            buff.flip();
            content += charset.decode(buff);
        }
            //若系统发送通知名字已经存在，则需要换个昵称
            if(USER_EXIST.equals(content)) {
                name = "";
                return false;
                }
            else if(USER_REGIST_SUCC.equals(content)){
                return true;
            }
        System.out.println("??????????");
        return false;
    }

    /*
    *将界面接口赋值
     */
    public void update(JTextArea chattextarea0,JTextArea kerberostextarea0,JTextArea datatextarea0)
    {
        chattextarea=chattextarea0;
        kerberostextarea=kerberostextarea0;
        datatextarea=datatextarea0;
    }

    /*
    *重载用户进程代码
     */
    private class ClientThread implements Runnable {
        public void run() {
            try {
                while (true) {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) continue;
                    Set selectedKeys = selector.selectedKeys();  //可以通过这个方法，知道可用通道的集合
                    Iterator keyIterator = selectedKeys.iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey sk = (SelectionKey) keyIterator.next();
                        keyIterator.remove();
                        dealWithSelectionKey(sk);
                    }
                }
            } catch (IOException io) {
            }
        }
    }

    /*
    *开启进程进行监听服务器发来的数据
     */
    public void StartThread()
    {
        //开辟一个新线程来读取从服务器端的数据
        new Thread(new BackgroundClient.ClientThread()).start();
    }

    /*
    *将用户信息发送到服务器端
     */
    public void SendMessage(String line) throws IOException {
        line = name+USER_CONTENT_SPILIT+line;
        sc.write(charset.encode(line));//sc既能写也能读，这边是写
    }


    /*
    *处理接收到的字符串
     */
    private void dealWithSelectionKey(SelectionKey sk) throws IOException {
        if(sk.isReadable())
        {
            //使用 NIO 读取 Channel中的数据，这个和全局变量sc是一样的，因为只注册了一个SocketChannel
            //sc既能写也能读，这边是读
            SocketChannel sc = (SocketChannel)sk.channel();
            ByteBuffer buff = ByteBuffer.allocate(1024);
            String content = "";
            while(sc.read(buff) > 0)
            {
                buff.flip();
                content += charset.decode(buff);
            }
            //若系统发送通知名字已经存在，则需要换个昵称
            if(USER_EXIST.equals(content)) {
                name = "";
            }
            String str=chattextarea.getText();
            chattextarea.setText(str+"\n"+content);
            chattextarea.setCaretPosition(chattextarea.getText().length());
            sk.interestOps(SelectionKey.OP_READ);
        }
    }
    /*
    *解开在线用户列表
    */
    private void UntagList(JList<String> list,String content)
    {
        String[] arrayContent = content.toString().split(USER_CONTENT_SPILIT);
        updataOnline(list,arrayContent);
    }
    /*
    *请求在线用户列表
    */
    public void AquireList(JList<String> list) throws IOException, InterruptedException {
        String meg = "userlist" + USER_CONTENT_SPILIT;
        sc.write(charset.encode(meg));//sc既能写也能读，这边是写
        ByteBuffer buff = ByteBuffer.allocate(1024);
        sleep(100);
        String content = "";
        while(sc.read(buff) > 0)
        {
            buff.flip();
            content += charset.decode(buff);
        }
        UntagList(list,content);
    }
    public static void updataOnline(JList<String> list,String []s)
    {
        list.setListData(s);
    }


}