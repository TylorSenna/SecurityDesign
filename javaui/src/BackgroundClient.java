
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
    private static final int port = 9999;
    private static Charset charset = Charset.forName("UTF-8");
    private SocketChannel sc = null;
    private String name = "";
    private int sym=0;
    private static int USER_EXIST = 1003;
    private static int USER_LIST=1004;
    private static int USER_REGIST_SUCC = 1005;
    private static int USER_REQUIRE = 1006;
    private static int USER_EXIT = 1000;
    private static int USER_SEND = 1001;
    private static int USER_LOGIN = 1002;
    private static String USER_CONTENT_SPILIT="#@#";
    private SelectionKey sk;
    private JTextArea chattextarea;
    private JList<String> list;
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
            sc.write(charset.encode(PackageMessage(anonymousname,USER_REQUIRE)));//sc既能写也能读，这边是写
        }
        ByteBuffer buff = ByteBuffer.allocate(1024);
        sleep(200);
        String content = "";
        int num=0;
        while (sym==0) {
            if(num>20)
                return false;
            num++;
            sleep(10);
        }
            //若系统发送通知名字已经存在，则需要换个昵称
        if(sym==1){
            sym=0;
            return true;
        }
        else{
            sym=0;
            return false;
        }
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
                        sk = (SelectionKey) keyIterator.next();
                        keyIterator.remove();
                        try {
                            dealWithSelectionKey(sk);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
        line = name+": "+line;
        sc.write(charset.encode(PackageMessage(line,USER_SEND)));//sc既能写也能读，这边是写
    }


    /*
    *用户退出时发送包
    */
    public void UserExit() throws IOException {
        sc.write(charset.encode(PackageMessage(name,USER_EXIT)));
    }



    /*
    *处理接收到的字符串
     */
    private void dealWithSelectionKey(SelectionKey sk) throws IOException, InterruptedException {
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
            unPackage(content);
        }
    }
    /*
    *解开在线用户列表
    */
    private void UntagList(JList<String> list, String content)
    {
        String[] arrayContent = content.toString().split(USER_CONTENT_SPILIT);
        updataOnline(list,arrayContent);
    }
    /*
    *请求在线用户列表
    */
    public void AquireList(JList<String> list0) throws IOException, InterruptedException {
        list=list0;
        sc.write(charset.encode(PackageMessage("",USER_LIST)));//sc既能写也能读，这边是写
        ByteBuffer buff = ByteBuffer.allocate(1024);
        sleep(100);
        String content = "";
        while(sc.read(buff) > 0)
        {
            buff.flip();
            content += charset.decode(buff);
        }
        if(content.length()>0)
            unPackage(content);
    }
    public static void updataOnline(JList<String> list,String []s)
    {
        list.setListData(s);
    }

    /*
    *将消息封装
    */
    public String PackageMessage(String message,int type)
    {
        String str=""+message;
        int len=message.length();
        if(type==USER_EXIT) {
            str=IntToString(len)+str;
            str="1000"+str;
            return str;
        }
        else if(type==USER_LOGIN) {
            str=IntToString(len)+str;
            str="1002"+str;
            return str;
        }
        else if(type==USER_SEND){
            str=IntToString(len)+str;
            str="1001"+str;
            return str;
        }else if(type==USER_LIST){
            str=IntToString(len)+str;
            str="1004"+str;
            return str;
        }else if(type==USER_REQUIRE){
            str=IntToString(len)+str;
            str="1006"+str;
            return str;
        }else if(type==USER_REGIST_SUCC){
            str=IntToString(len)+str;
            str="1005"+str;
            return str;
        }else if(type==USER_EXIST){
            str=IntToString(len)+str;
            str="1003"+str;
            return str;
        }else{
            return str;
        }

    }

    public static String IntToString(int num)
    {
        String str=String.valueOf(num);
        int len=str.length();
        for(;len<8;len++)
        {
            str="0"+str;
        }
        return str;
    }

    /*
    *将消息解封
    */
    public boolean unPackage(String message) throws IOException, InterruptedException {
        System.out.println(message);
        System.out.println(message.length());
        String type0=message.substring(0,4);
        int type=Integer.parseInt(type0);
        if(type == USER_SEND){
            String len=message.substring(4,12);
            int length=Integer.parseInt(len);
            String info=message.substring(12,12+length);
            String hash=message.substring(12+length);
            String str=chattextarea.getText();
            chattextarea.setText(str+"\n"+info);
            chattextarea.setCaretPosition(chattextarea.getText().length());
            sk.interestOps(SelectionKey.OP_READ);
        }
        else if(type == USER_EXIST){
            name="";
            return false;
        }
        else if(type == USER_REGIST_SUCC){
            String len=message.substring(4,12);
            int length=Integer.parseInt(len);
            String info=message.substring(12,12+length);
            String hash=message.substring(12+length);
            System.out.println(info);
            sym=1;
            name=info;
            return true;
        }
        else if(type == USER_LOGIN){
            AquireList(list);
            return true;
        }
        else if(type == USER_EXIT){
            AquireList(list);
            return true;
        }
        else if(type == USER_LIST){
            String len=message.substring(4,12);
            int length=Integer.parseInt(len);
            String info=message.substring(12,12+length);
            String hash=message.substring(12+length);
            UntagList(list,info);
            return true;
        }
        return false;
    }
}