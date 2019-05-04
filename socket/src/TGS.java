import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TGS {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("TGS server start at:" + new Date());
        TGSthread a = new TGSthread();
        while (true) {
            Socket socket = serverSocket.accept();
            a.setSocket(socket);
            new Thread(a).start();
        }
    }

}
class TGSthread  implements Runnable{
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
            String []result = kerberos.tgs_parse_client(receive);
            System.out.println("TGS 接收到 Client的报文: "+ receive);
            if(result[0].equals("02")){  //数据库查询ID判断
                //判断成功，TGS调用Kerberos类中函数，返回加密后报文
                output.writeUTF(kerberos.tgs_to_client("1234567",kerberos.ID_v,kerberos.TS,kerberos.get_Ticket_v()));
            }
            else{
                //数据库查询失败，返回出错码0000，不存在数据库中
                output.writeUTF("0000");
                System.out.println("Error: Client 访问 TGS失败");
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


