import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AnonymousChat {

    public static void chatroom() {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("轻剑白马");
        // Setting the width and height of frame
        frame.setSize(700, 630);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //窗口在屏幕中间显示
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dimension = tk.getScreenSize();//获取屏幕大小
        int width=dimension.width;
        int height=dimension.height;
        int x=(width-700)/2;
        int y=(height-630)/2;
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

        //创建显示文字的区域
        JTextArea textarea0=new JTextArea("轻剑白马：你好\n窗边月：你好呀\n醉里论道：最近的复联4看了没\n轻剑白马：嗯，还不错\n窗边月：我觉得一般般\n醉里论道：2333\n");
        JScrollPane jsp0 = new JScrollPane(textarea0);
        //设置矩形大小.参数依次为(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)
        jsp0.setBounds(10, 20, 500, 150);
        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        jsp0.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(jsp0);
        //把滚动条添加到容器里面




        /*
         * 创建文本域用于用户输入
         */
        JTextField userText = new JTextField(20);
        userText.setBounds(10,175,165,25);
        panel.add(userText);

        // 创建发送按钮
        JButton loginButton = new JButton("发送");
        loginButton.setBounds(185, 175, 80, 25);
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
        jl1.setBounds(10, 205, 500, 30);
        jsp1.setBounds(10, 235, 500, 150);
        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        jsp1.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //把滚动条添加到容器里面
        panel.add(jsp1);
        panel.add(jl1);

        JLabel jl2 = new JLabel("----------------------------------------------------数据交流部分---------------------------------------------------");
        //创建显示文字的区域
        JTextArea textarea2=new JTextArea("客户\n" +
                "明文：你好\n" +
                "数据：长度0011类型1100数据段长度011011数据段1010100010111000110011110校验码011101\n" +
                ""+
                ".....\n......\n......\n");
        JScrollPane jsp2 = new JScrollPane(textarea2);
        //设置矩形大小.参数依次为(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)
        jl2.setBounds(10, 385, 500, 30);
        jsp2.setBounds(10, 415, 500, 150);
        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        jsp2.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //把滚动条添加到容器里面
        panel.add(jsp2);
        panel.add(jl2);


        // 输入密码的文本域
        JLabel OnlineLabel = new JLabel("当前在线群友");
        OnlineLabel.setBounds(520,20,80,25);
        panel.add(OnlineLabel);

        //显示在线人员
        JList<String> list = new JList<String>();
        list.setListData(new String[]{"轻剑白马", "窗边月", "醉里论道"});
        list.setBounds(520, 50, 120, 455);
        panel.add(list);
    }

}
