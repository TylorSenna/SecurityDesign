import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class AS {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("AS server start at:" + new Date());
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
        private static final Logger log = LogManager.getLogger(AS.class);
        public void setSocket(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run(){
                try {
                    InetAddress address = socket.getInetAddress();
                    System.out.println("connected with address:"+address.getHostAddress());
                    log.info("connected with address:"+address.getHostAddress());
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    String receive = input.readUTF(); //接收数据

                    Kerberos kerberos = new Kerberos();
                    String []result = kerberos.as_parse_client(receive);
                    System.out.println("AS 接收到 Client的报文"+ receive);
                    log.info("AS 接收到 Client的报文"+ receive);
                    String ID_C = result[0];
                    String[] AD_C_array = address.getHostAddress().split("\\.");
                    String AD_C = "";
                    for(int i=0; i<AD_C_array.length; i++){
                        while(AD_C_array[i].length()<3){
                            AD_C_array[i] = "0" + AD_C_array[i];
                        }
                        AD_C += AD_C_array[i];
                    }
                    System.out.println("Client 的地址："+ AD_C);
                    log.info("Client 的地址："+ AD_C);
                    String k_c_tgs = "1234567";//当生命周期过后要换密钥 K_c_tgs


                    if(result[0].equals("0001")){  //数据库查询ID判断
                        //判断成功，AS调用Kerberos类中函数，返回加密后报文
                        Date TS2 = new Date();
                        output.writeUTF(kerberos.as_to_client(k_c_tgs,kerberos.ID_tgs,TS2,kerberos.Lifetime,kerberos.
                                get_Ticket_tgs("",k_c_tgs,ID_C,AD_C,kerberos.ID_tgs,TS2,kerberos.Lifetime)));
                    }
                    else if(result[0].equals("0000")){
                        //返回证书
                        output.writeUTF(kerberos.get_Certification());

                        String user_information_encrpt = input.readUTF();
                        System.out.println("user_information_encrpt:"+user_information_encrpt);
                        log.info("user_information_encrpt:"+user_information_encrpt);
                        String []user_information = kerberos.parse_client_id_key(user_information_encrpt);

                        System.out.println("user_id:"+user_information[0]);
                        System.out.println("user_key:"+user_information[1]);
                        log.debug("user_id:"+user_information[0]);
                        log.debug("user_key:"+user_information[1]);
                        //receive = input.readUTF();
                        //调用插入数据库操作
                    }
                    else{
                        //数据库查询失败，返回出错码0000，不存在数据库中
                        output.writeUTF("0000");
                        System.out.println("Error: Client 访问 AS失败，不存在此IDc:"+result[0]);
                        log.error("Client 访问 AS失败，不存在此IDc:"+result[0]);
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


