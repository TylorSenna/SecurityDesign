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
            /* //AS
            String message = kerberos.client_to_as(ID_c,kerberos.ID_tgs,kerberos.TS);  //调用Kerberos类函数生成消息字符串
            output.writeUTF(message);//发送信息
            //System.out.println(message.length());  //检验数据长度
            String receive = input.readUTF();  //接受信息
            System.out.println("Client收到AS的加密报文："+ receive);  //输出接受信息->在ui界面显示
            kerberos.client_parse_as(receive);  //调用Kerberos类中解析函数
            */


            /* //TGS
            String message2 = kerberos.client_to_tgs(kerberos.ID_v,kerberos.get_Ticket_tgs(),kerberos.get_Authenticator_c());  //调用Kerberos类函数生成消息字符串
            output.writeUTF(message2);//发送信息
            //System.out.println(message.length());  //检验数据长度
            String receive2 = input.readUTF();  //接受信息
            System.out.println("Client收到AS的加密报文："+ receive2);  //输出接受信息->在ui界面显示
            if(receive2.substring(0,4).equals("0000")){
                System.out.println("Error: Client 接收到错误信息0000");
            }else {
                kerberos.client_parse_tgs(receive2);  //调用Kerberos类中解析函数
            }*/

             //V
            String message3 = kerberos.client_to_v(kerberos.get_Ticket_v(),kerberos.get_Authenticator_c());  //调用Kerberos类函数生成消息字符串
            output.writeUTF(message3);//发送信息
            //System.out.println(message.length());  //检验数据长度
            String receive2 = input.readUTF();  //接受信息
            System.out.println("Client收到Server V的加密报文："+ receive2);  //输出接受信息->在ui界面显示
            if(receive2.substring(0,4).equals("0000")){
                System.out.println("Error: Client 接收到错误信息0000");
            }else {
                kerberos.client_parse_v(receive2);  //调用Kerberos类中解析函数
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
    }

    public static void main(String[] args){
        connect();
    }
}
