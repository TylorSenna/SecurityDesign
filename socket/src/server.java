import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("server start at:" + new Date());
        Mythread a = new Mythread();
        while (true) {
            Socket socket = serverSocket.accept();
            a.setSocket(socket);
            new Thread(a).start();
        }
    }

}
    class Mythread  implements Runnable{
        Socket socket;
        public void setSocket(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run(){
            //System.out.println(1);
                try {
                    InetAddress address = socket.getInetAddress();
                    System.out.println("connected with address:"+address.getHostAddress());
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    String receive = input.readUTF(); //接收数据

                    Kerberos kerberos = new Kerberos();
                    String []result = kerberos.parse_client(receive);
                    System.out.println(receive);
                    if(result[0].equals("0001")){  //数据库查询ID判断
                        //判断成功，as调用Kerberos类中函数，返回加密后报文
                        output.writeUTF(kerberos.as_to_client("1234567",kerberos.ID_tgs,kerberos.TS,kerberos.Lifetime,kerberos.get_Ticket_tgs()));
                    }
                    else if(result[0].equals("0000")){
                        //返回证书
                        output.writeUTF(kerberos.get_Certification());

                        String user_information_encrpt = input.readUTF();
                        System.out.println("user_information_encrpt:"+user_information_encrpt);

                        String []user_information = kerberos.parse_client_id_key(user_information_encrpt);

                        System.out.println("user_id:"+user_information[0]);
                        System.out.println("user_key:"+user_information[1]);

                        //receive = input.readUTF();
                        //调用插入数据库操作
                    }
                    else{
                        //数据库查询失败，返回出错码0000，不存在数据库中
                        output.writeUTF("0000");
                    }
                    output.flush();
                    socket.shutdownOutput();
                    socket.close();
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
            }
    }


