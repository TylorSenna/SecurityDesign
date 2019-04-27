import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class InputFunName {

    public static void inputname() {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("注册本次匿名聊天的昵称");
        // Setting the width and height of frame
        frame.setSize(530, 475);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //窗口在屏幕中间显示
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dimension = tk.getScreenSize();//获取屏幕大小
        int width=dimension.width;
        int height=dimension.height;
        int x=(width-530)/2;
        int y=(height-475)/2;
        frame.setLocation(x, y);
        /* 创建面板，这个类似于 HTML 的 div 标签
         * 我们可以创建多个面板并在 JFrame 中指定位置
         * 面板中我们可以添加文本字段，按钮及其他组件。
         */
        JPanel panel = new JPanel();
        // 添加面板
        frame.add(panel);
        /*
         * 调用用户定义的方法并添加组件到面板
         */
        placeComponents(panel);

        // 设置界面可见
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {

        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panel.setLayout(null);


        /*
         * 创建文本域用于用户输入
         */
        JLabel jl0 = new JLabel("输入本次聊天昵称\n");
        JTextField userText = new JTextField(20);
        userText.setBounds(10,40,165,25);
        jl0.setBounds(10, 10, 500, 30);
        panel.add(userText);
        panel.add(jl0);

        // 创建发送按钮
        JButton loginButton = new JButton("确认");
        loginButton.setBounds(185, 40, 80, 25);
        panel.add(loginButton);



        //创建显示文字的区域
        JLabel jl1 = new JLabel("--------------------------------------------------Kerberos认证过程----------------------------------------------\n");
        JTextArea textarea1=new JTextArea("客户\n" +
                "明文：长度0011类型1100数据段长度001111数据段 客户编号1010100000服务器编号110011110校验码001101\n" +
                "密文：31353232546526513523131235419832516519\n" +
                "服务器\n" +
                "明文：长度0011类型1100数据段长度011011数据段1010100010111000110011110校验码011101\n" +
                "密文：654168492568448892316981265235164174198516515\n"
        );
        JScrollPane jsp1 = new JScrollPane(textarea1);
        //设置矩形大小.参数依次为(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)
        jl1.setBounds(10, 70, 500, 30);
        jsp1.setBounds(10, 100, 500, 150);
        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        jsp1.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //把滚动条添加到容器里面
        panel.add(jsp1);
        panel.add(jl1);

        JLabel jl2 = new JLabel("----------------------------------------------------数据交流部分---------------------------------------------------");
        //创建显示文字的区域
        JTextArea textarea2=new JTextArea("");
        JScrollPane jsp2 = new JScrollPane(textarea2);
        //设置矩形大小.参数依次为(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)
        jl2.setBounds(10, 250, 500, 30);
        jsp2.setBounds(10, 280, 500, 150);
        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        jsp2.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //把滚动条添加到容器里面
        panel.add(jsp2);
        panel.add(jl2);


    }
    public static void main(String[] args) {
        InputFunName start = new InputFunName();
        start.inputname();
    }

}
