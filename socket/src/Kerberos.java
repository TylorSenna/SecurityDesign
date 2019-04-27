import java.util.BitSet;
import java.util.Date;

public class Kerberos {

    String ID_tgs = "00";
    Date TS;
    String Lifetime = "0000000001000";

    public String[] parse_client(String message){
        String []result = new String[3];
        String ID_c = message.substring(0,4);
        String ID_tgs = message.substring(4,6);
        String TS1 = message.substring(6,19);
        result[0] = ID_c;
        result[1] = ID_tgs;
        result[2] = TS1;
        return result;
    }

    public String client_to_as(String ID_c, String ID_tgs,Date TS1){
        String message;

        TS1 = new Date();
        message = ID_c + ID_tgs + TS1.getTime();

        return  message;
    }

    public String as_to_client(String K_c_tgs,String ID_tgs,Date TS_2,String Lifetime_2,String Ticket_tgs){
        String message;

        TS_2 = new Date();
        message = K_c_tgs + ID_tgs + TS_2.getTime() + Lifetime_2 + Ticket_tgs;
        DES des = new DES("abcdefg");

        System.out.println("明文是："+message);
        message = des.encrypt_string(message);
        return  message;
    }

    public String get_Ticket_tgs(){
        String message;
        String K_tgs = "tgsmima";
        String K_c_tgs = "1234567";
        String ID_c = "0001";
        String AD_c = "192168000001";
        long TS_2 = new Date().getTime();
        DES des = new DES(K_tgs);
        message = des.encrypt_string(K_c_tgs + ID_c + AD_c + ID_tgs + TS_2 + Lifetime);
        return message;
    }

    public String get_Ticket_tgs( String K_tgs,String K_c_tgs,String ID_c,String AD_c,String ID_tgs,long TS_2,String Lifetime_2 ){
        String message;
        K_tgs = "tgsmima";
        K_c_tgs = "1234567";
        ID_c = "0001";
        AD_c = "192168000001";
        TS_2 = new Date().getTime();
        DES des = new DES(K_tgs);
        message = des.encrypt_string(K_c_tgs + ID_c + AD_c + ID_tgs + TS_2 + Lifetime);
        return message;
    }

    /**
     * 解析AS发给Client的报文message
     * 把Message解析到入口参数K_c_tgs || ID_tgs || TS_2 || Lifetime_2 || Ticket_tgs
     */
    public String[] client_parse_as(String message){
        String parse[] = new String[5];
        DES des = new DES("abcdefg");
        message = des.decrypt_string(message);
        System.out.println(message);
        parse[0] = message.substring(0,7);
        parse[1] = message.substring(7,9);
        parse[2] = message.substring(9,22);
        parse[3] = message.substring(22,35);
        parse[4] = message.substring(35,35+49);
        DES des1 = new DES("tgsmima");
        String enTicket = message.substring(35,message.length());
        System.out.println("enticket is:"+enTicket);
        String Ticket = des1.decrypt_string(enTicket);
        System.out.println("ticket is:"+Ticket);
        return parse;
    }
}
