import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8888;
    static Socket socket = null;
    static DataOutputStream output = null;
    static DataInputStream input = null;

    public static void connect() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            String ID_c = "0001";//从ui界面获取
            Kerberos kerberos = new Kerberos();
            String message = kerberos.client_to_as(ID_c,kerberos.ID_tgs,kerberos.TS);  //调用Kerberos类函数生成消息字符串
            output.writeUTF(message);//发送信息
            //System.out.println(message.length());  //检验数据长度
            String receive = input.readUTF();  //接受信息
            System.out.println(receive);  //输出接受信息->在ui界面显示
            kerberos.client_parse_as(receive);  //调用Kerberos类中解析函数
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
    }

    public static void main(String[] args){
        connect();
    }
}
