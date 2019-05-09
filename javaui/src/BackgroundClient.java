import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;
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
    private static int receive=1;
    private static int send=0;
    private static String USER_VERIFY = "1007";
    private static String USER_CONTENT_SPILIT="#@#";
    private SelectionKey sk;
    private String SessionKey="";
    private JTextArea chattextarea;
    private JList<String> list;
    public JTextArea kerberostextarea;
    public JTextArea datatextarea;
    public JTextField userId;
    public JTextField userPass;

    private static final String AS_IP = "192.168.43.199";
    private static final String TGS_IP = "192.168.43.248";
    private static final String V_IP = "192.168.43.196";
    private static final int AS_PORT = 8888;
    private static final int TGS_PORT = 8889;
    static Socket socket = null;
    static DataOutputStream output = null;
    static DataInputStream input = null;

    private static final Logger log = LogManager.getLogger(BackgroundClient.class);

    /*
    * 初始化一个用户进程
    * */
    public void init() throws IOException {
        selector = Selector.open();
        //连接远程主机的IP和端口
        sc = SocketChannel.open(new InetSocketAddress(V_IP, port));
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
    }

    public static void main(String[] args){
        BackgroundClient backgroundClient = new BackgroundClient();
        try {
            backgroundClient.init();
            backgroundClient.Verify();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean Verify() throws InterruptedException, UnknownHostException {

        String Ticket_v;
        String ID_c = userId.getText();//从ui界面获取
        String K_c = userPass.getText();
        InetAddress address = InetAddress.getLocalHost();
        String[] AD_C_array = address.getHostAddress().split("\\.");
        String AD_c = "";
        for(int i=0; i<AD_C_array.length; i++){
            while(AD_C_array[i].length()<3){
                AD_C_array[i] = "0" + AD_C_array[i];
            }
            AD_c += AD_C_array[i];
        }
        Kerberos kerberos = new Kerberos();
        boolean verify_result = false;

        try {
            socket = new Socket(AS_IP, AS_PORT);
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            //AS
            Date TS1 = new Date();
            String message = kerberos.client_to_as(ID_c,kerberos.ID_tgs,TS1);  //调用Kerberos类函数生成消息字符串
            output.writeUTF(message);//发送信息
            kerberostextarea.setText(kerberostextarea.getText() + "\nClient发给AS的加密报文："+ message);
            String receive = input.readUTF();  //接受信息
            System.out.println("Client收到AS的加密报文："+ receive);  //输出接受信息->在ui界面显示
            log.info("Client收到AS的加密报文：" + receive);
            kerberostextarea.setText(kerberostextarea.getText() + "\nClient收到AS的加密报文："+ receive);
            String[] result1 = kerberos.client_parse_as(K_c, receive);  //调用Kerberos类中解析函数
            if(result1.length == 1){
                if(result1[0].length()==4){
                    log.error(" Client 访问 AS失败，不存在此IDc" + ID_c);
                    kerberostextarea.setText(kerberostextarea.getText() + "\n Client 访问 AS失败，不存在此IDc" + ID_c);
                }else {
                    log.error(" Client 访问 AS失败，密码错误:" + result1[0]);
                    kerberostextarea.setText(kerberostextarea.getText() + "\n Client 访问 AS失败，密码错误:" + result1[0]);
                }
                return false;
            }
            String k_c_tgs = result1[0];
            String Ticket_tgs = result1[4];  //未解密的ticket_tgs  只有TGS可以解开

            //TGS
            Socket socket2 = new Socket(TGS_IP, TGS_PORT);
            DataOutputStream output2 = new DataOutputStream(socket2.getOutputStream());
            DataInputStream input2 = new DataInputStream(socket2.getInputStream());
            Date TS3 = new Date();
            String message2 = kerberos.client_to_tgs(kerberos.ID_v,Ticket_tgs,kerberos.get_Authenticator_c(k_c_tgs,ID_c,AD_c,TS3));  //调用Kerberos类函数生成消息字符串
            output2.writeUTF(message2);//发送信息
            kerberostextarea.setText(kerberostextarea.getText() + "\nClient发给TGS的加密报文："+ message2);
            String receive2 = input2.readUTF();  //接受信息
            System.out.println("Client收到TGS的加密报文："+ receive2);  //输出接受信息->在ui界面显示
            log.info("Client收到TGS的加密报文：" + receive2);
            kerberostextarea.setText(kerberostextarea.getText() + "\nClient收到TGS的加密报文："+ receive2);
            String[] result2 = null;
            result2 = kerberos.client_parse_tgs(k_c_tgs, receive2);  //调用Kerberos类中解析函数
            SessionKey = result2[0];
            Ticket_v = result2[3];  //未解密的ticket_v  只有Server V可以解开

            //Server V
            Date TS5 = new Date();
            String message3 = kerberos.client_to_v(Ticket_v,kerberos.get_Authenticator_c(SessionKey,ID_c,AD_c,TS5));  //调用Kerberos类函数生成消息字符串
            sc.write(charset.encode(USER_VERIFY + message3));//发送信息给Server V，表示要认证
            kerberostextarea.setText(kerberostextarea.getText() + "\nClient发给V的加密报文："+ message3);
            ByteBuffer buff = ByteBuffer.allocate(1024);
            String receive3 = "";//接受信息
            sleep(100);
            while(sc.read(buff) > 0)
            {
                buff.flip();
                receive3 += charset.decode(buff);
            }
            System.out.println("Client收到Server V的加密报文："+ receive3);  //输出接受信息->在ui界面显示
            log.info("Client收到Server V的加密报文："+ receive3);
            kerberostextarea.setText(kerberostextarea.getText() + "\nClient收到Server V的加密报文："+ receive3);
            String[] result3 = null;
            result3 = kerberos.client_parse_v(SessionKey, receive3);  //调用Kerberos类中解析函数
            String TS6 = result3[0];
            if(TS6.equals(String.valueOf(TS5.getTime()+1))){
                System.out.println("Client 与 Server V 认证成功，开始提供聊天室服务......");
                log.info("Client 与 Server V 认证成功，开始提供聊天室服务");
                kerberostextarea.setText(kerberostextarea.getText() + "\nClient 与 Server V 认证成功，开始提供聊天室服务");
                verify_result = true;
            }else {
                System.out.println("认证失败: TS5 不符合 ，Log时间:" + new Date());
                log.error("认证失败: TS5 不符合");
                kerberostextarea.setText(kerberostextarea.getText() + "\n认证失败: TS5 不符合");
                verify_result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                input.close();
                output.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return verify_result;
    }

    public boolean Register(){
        try {
            socket = new Socket(AS_IP, AS_PORT);
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            Kerberos kerberos = new Kerberos();
            String message = kerberos.register;  //Kerberos类请求注册字符串
            output.writeUTF(message);//发送注册请求
            //System.out.println(message.length());  //检验数据长度
            String receive = input.readUTF();  //接受证书
            System.out.println("证书："+ receive);
            //调用Kerberos类中解析函数,解析证书
            String pk = kerberos.parse_Certification(receive);
            String ID_c = userId.getText();//从ui界面获取
            String K_c = userPass.getText();

            if(!pk.equals(null)){
                output.writeUTF(kerberos.client_id_key(ID_c,K_c,pk));
                receive = input.readUTF();  //注册情况
                if(receive.equals("0002")){
                    log.error(" 注册失败，错误原因: 已存在此用户ID_c:" + ID_c);
                    return false;
                }else {
                    System.out.println("注册成功，ID_C: "+ ID_c);
                    return true;
                }
            }else {
                log.error(" 注册失败，错误原因: 证书认证失败");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                input.close();
                output.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
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

    }
    /*
    *将界面接口赋值
    */
    public void update(JTextArea kerberostextarea0,JTextArea datatextarea0)
    {
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
        DES d=new DES(SessionKey);
        String hash="0000000000";
        String str=""+message;
        String result="";
        int len=message.length();
        if(type==USER_EXIT) {
            str=IntToString(len)+str;
            str="1000"+str;
            str=str+hash;
            result=d.encrypt_string(str);
            UiTextAreaCiphertext(result,send);
            UiTextAreaPlaintext(str,USER_EXIT,send);
            return result;
        }
        else if(type==USER_LOGIN) {
            str=IntToString(len)+str;
            str="1002"+str;
            str=str+hash;
            result=d.encrypt_string(str);
            UiTextAreaCiphertext(result,send);
            UiTextAreaPlaintext(str,USER_LOGIN,send);
            return result;
        }
        else if(type==USER_SEND){
            str=IntToString(len)+str;
            str="1001"+str;
            str=str+hash;
            result=d.encrypt_string(str);
            UiTextAreaCiphertext(result,send);
            UiTextAreaPlaintext(str,USER_SEND,send);
            return result;
        }else if(type==USER_LIST){
            str=IntToString(len)+str;
            str="1004"+str;
            str=str+hash;
            result=d.encrypt_string(str);
            UiTextAreaCiphertext(result,send);
            UiTextAreaPlaintext(str,USER_LIST,send);
            return result;
        }else if(type==USER_REQUIRE){
            str=IntToString(len)+str;
            str="1006"+str;
            str=str+hash;
            result=d.encrypt_string(str);
            UiTextAreaCiphertext(result,send);
            UiTextAreaPlaintext(str,USER_REQUIRE,send);
            return result;
        }else if(type==USER_REGIST_SUCC){
            str=IntToString(len)+str;
            str="1005"+str;
            str=str+hash;
            result=d.encrypt_string(str);
            UiTextAreaCiphertext(result,send);
            UiTextAreaPlaintext(str,USER_REGIST_SUCC,send);
            return result;
        }else if(type==USER_EXIST){
            str=IntToString(len)+str;
            str="1003"+str;
            str=str+hash;
            result=d.encrypt_string(str);
            UiTextAreaCiphertext(result,send);
            UiTextAreaPlaintext(str,USER_EXIST,send);
            return result;
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
     *判断字符串是否为纯数字
     */
    public static boolean isNumeric(String str)
    {
        for (int i = 0; i < str.length(); i++)
        {
            if (!Character.isDigit(str.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }



    /*
    *将消息解封
    */
    public boolean unPackage(String message) throws IOException, InterruptedException {
        if(message.length()==0){
            return false;
        }
        DES d=new DES(SessionKey);
        UiTextAreaCiphertext(message,receive);
        message=d.decrypt_string(message);
        if(message.indexOf(0) >=0){
            message = message.substring(0,message.indexOf(0));    //这一步很重要，由于在result中含有ascll码为0的值，导致转换为byte[]时出错，所以要先剔除掉0
        }
        System.out.println(message);
        if(message.length()<4)
            return false;
        String type0=message.substring(0,4);
        if(!isNumeric(type0))
            return false;
        int type=Integer.parseInt(type0);
        if(type == USER_SEND){
            if(name.length()==0){
                return true;
            }
            UiTextAreaPlaintext(message,USER_SEND,receive);
            String len=message.substring(4,12);
            int length=Integer.parseInt(len);
            if(length>0){
                String info=message.substring(12,12+length);
                if(chattextarea == null){
                    return false;
                }
                String str=chattextarea.getText();
                chattextarea.setText(str+"\n"+info);
                chattextarea.setCaretPosition(chattextarea.getText().length());
            }
            String hash=message.substring(12+length,12+length+10);
            if(message.length()>12+length+10){
                String remain=message.substring(12+length+10);
                unPackage(remain);
            }
            return true;
        }
        else if(type == USER_EXIST){
            UiTextAreaPlaintext(message,USER_EXIST,receive);
            String len=message.substring(4,12);
            int length=Integer.parseInt(len);
            String hash=message.substring(12+length,12+length+10);
            if(message.length()>12+length+10){
                String remain=message.substring(12+length+10);
                unPackage(remain);
            }
            name="";
            sym=-1;
            return false;
        }
        else if(type == USER_REGIST_SUCC){
            UiTextAreaPlaintext(message,USER_REGIST_SUCC,receive);
            String len=message.substring(4,12);
            int length=Integer.parseInt(len);
            String info=message.substring(12,12+length);
            String hash=message.substring(12+length,12+length+10);
            if(message.length()>12+length+10){
                String remain=message.substring(12+length+10);
                unPackage(remain);
            }
            sym=1;
            name=info;
            return true;
        }
        else if(type == USER_LOGIN){
            UiTextAreaPlaintext(message,USER_LOGIN,receive);
            String len=message.substring(4,12);
            int length=Integer.parseInt(len);
            String hash=message.substring(12+length,12+length+10);
            if(message.length()>12+length+10){
                String remain=message.substring(12+length+10);
                unPackage(remain);
            }
            AquireList(list);
            return true;
        }
        else if(type == USER_EXIT){
            UiTextAreaPlaintext(message,USER_EXIT,receive);
            String len=message.substring(4,12);
            int length=Integer.parseInt(len);
            String hash=message.substring(12+length,12+length+10);
            if(message.length()>12+length+10){
                String remain=message.substring(12+length+10);
                unPackage(remain);
            }
            AquireList(list);
            return true;
        }
        else if(type == USER_LIST){
            UiTextAreaPlaintext(message,USER_LIST,receive);
            String len=message.substring(4,12);

            int length=Integer.parseInt(len);
            if(length>0){
                String info=message.substring(12,12+length);
                UntagList(list,info);
            }
            String hash=message.substring(12+length,12+length+10);
            if(message.length()>12+length+10){
                String remain=message.substring(12+length+10);
                unPackage(remain);
            }
            return true;
        }
        return false;
    }

    /*
     * 将字符串传入ui前端的数据交流区
     * type  数据包的种类
     * sor   发送还是接收
     * */
    private void UiTextAreaPlaintext(String text,int type,int sor)
    {
        text="  plaintext: "+text;
        if(sor==receive){
            if(type==USER_SEND){
                text="(receive) "+text+"(用户接收的聊天消息)";
                String str=datatextarea.getText();
                datatextarea.setText(str+"\n"+text);
                datatextarea.setCaretPosition(datatextarea.getText().length()-text.length());
            }
            else if(type==USER_EXIST){
                text="(receive) "+text+"(用户接收的申请的昵称已经存在的消息)";
                String str=datatextarea.getText();
                datatextarea.setText(str+"\n"+text);
                datatextarea.setCaretPosition(datatextarea.getText().length()-text.length());
            }
            else if(type==USER_REGIST_SUCC){
                text="(receive) "+text+"(用户接收的申请昵称成功的消息)";
                String str=datatextarea.getText();
                datatextarea.setText(str+"\n"+text);
                datatextarea.setCaretPosition(datatextarea.getText().length()-text.length());
            }
            else if(type==USER_LIST){
                text="(receive) "+text+"(用户接收的聊天室当前在线用户列表)";
                String str=datatextarea.getText();
                datatextarea.setText(str+"\n"+text);
                datatextarea.setCaretPosition(datatextarea.getText().length()-text.length());
            }
            else if(type==USER_EXIT){
                text="(receive) "+text+"(用户接收的其他用户退出的消息)";
                String str=datatextarea.getText();
                datatextarea.setText(str+"\n"+text);
                datatextarea.setCaretPosition(datatextarea.getText().length()-text.length());
            }
            else if(type==USER_LOGIN){
                text="(receive) "+text+"(用户接收的其他用户登陆的消息)";
                String str=datatextarea.getText();
                datatextarea.setText(str+"\n"+text);
                datatextarea.setCaretPosition(datatextarea.getText().length()-text.length());
            }
            else{
                return;
            }
        }
        else{
            if(type==USER_SEND){
                text="(send)    "+text+"(用户发送的聊天消息)";
                String str=datatextarea.getText();
                datatextarea.setText(str+"\n"+text);
                datatextarea.setCaretPosition(datatextarea.getText().length()-text.length());
            }
            else if(type==USER_LIST){
                text="(send)    "+text+"(用户发送的请求当前在线用户)";
                String str=datatextarea.getText();
                datatextarea.setText(str+"\n"+text);
                datatextarea.setCaretPosition(datatextarea.getText().length()-text.length());
            }
            else if(type==USER_EXIT){
                text="(send)    "+text+"(用户发送的退出聊天室消息)";
                String str=datatextarea.getText();
                datatextarea.setText(str+"\n"+text);
                datatextarea.setCaretPosition(datatextarea.getText().length()-text.length());
            }
            else if(type==USER_REQUIRE){
                text="(send)    "+text+"(用户发送的请求用户名消息)";
                String str=datatextarea.getText();
                datatextarea.setText(str+"\n"+text);
                datatextarea.setCaretPosition(datatextarea.getText().length()-text.length());
            }
            else{
                return;
            }
        }

    }


    /*
     * 将字符串传入ui前端的数据交流区
     * type  数据包的种类
     * sor   发送还是接收
     * */
    private void UiTextAreaCiphertext(String text,int sor)
    {
        text="ciphertext: "+text;
        if(sor==receive){
            text="(receive) "+text;
            String str=datatextarea.getText();
            datatextarea.setText(str+"\n"+text);
            datatextarea.setCaretPosition(datatextarea.getText().length()-text.length());
        }
        else{
            text="(send)    "+text;
            String str=datatextarea.getText();
            datatextarea.setText(str+"\n"+text);
            datatextarea.setCaretPosition(datatextarea.getText().length()-text.length());
        }
    }


}