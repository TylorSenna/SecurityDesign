import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.regex.Pattern;

public class AntChatUi {
    public static JFrame frame = new JFrame("物联网安全课程设计");
    public static final int roomwidth=700;//认证过程的窗口宽高
    public static final int roomheight=630;

    /*
    * 判断是否为整数
    * @param str 传入的字符串
    * @return 是整数返回true,否则返回false
    */


    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /*
    * 设置窗口生成大小位置
    * @param panel 传入的界面
    * @return 返回定位以后的界面
    */
    public static void resetFrame(int curWidth,int curHeight)
    {
        // Setting the width and height of frame
        frame.setSize(roomwidth, roomheight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //窗口在屏幕中间显示
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dimension = tk.getScreenSize();//获取屏幕大小
        int width=dimension.width;
        int height=dimension.height;
        int x=(width-curWidth)/2;
        int y=(height-curHeight)/2;
        frame.setLocation(x, y);
    }


    public static void chatroom() {
        // 创建 JFrame 实例

        resetFrame(roomwidth,roomheight);
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
        placeComponents(frame,panel);

        // 设置界面可见
        frame.setVisible(true);
    }

    /*
    *动态显示当前登陆的用户
    * @list 显示当前在线用户列表
    */
    public static void updataOnline(JList<String> list)
    {
        Vector vt=new Vector();
        vt.add("窗边月");

        vt.add("醉里论道");

        list.setListData(vt);
    }





    /*
    *生成注册本次匿名聊天的昵称界面
    * @param frame框架
    * @panel 面板
    * @jl1 显示kerberos数据框
    * @jl2 显示数据交流数据框
    */
    public static void Anonymousroom(JPanel panel,JTextArea jl1,JTextArea jl2,JLabel OnlineLabel,JList<String> list,String name)
    {
        updataOnline(list);
        //创建显示文字的区域
        JTextArea textarea0=new JTextArea("轻剑白马：你好\n窗边月：你好呀\n醉里论道：最近的复联4看了没\n轻剑白马：嗯，还不错\n窗边月：我觉得一般般\n醉里论道：2333\n");
        JScrollPane jsp0 = new JScrollPane(textarea0);
        textarea0.setEditable(false);
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
        JButton sendButton = new JButton("发送");
        // 实现"重置"按钮功能
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                JOptionPane.showMessageDialog(null, "OK!");
            }
        });
        sendButton.setBounds(185, 175, 80, 25);
        panel.add(sendButton);
        jl1.setText("客户\n" +
                "明文：长度0011类型1100数据段长度001111数据段 客户编号1010100000服务器编号110011110校验码001101\n" +
                "密文：31353232546526513523131235419832516519\n" +
                "服务器\n" +
                "明文：长度0011类型1100数据段长度011011数据段1010100010111000110011110校验码011101\n" +
                "密文：654168492568448892316981265235164174198516515\n");
        jl2.setText("客户\n" +
                "明文：你好\n" +
                "数据：长度0011类型1100数据段长度011011数据段1010100010111000110011110校验码011101\n" +
                ""+
                ".....\n......\n......\n");
        jl2.setForeground(Color.red);
        jl2.setEditable(false);
        jl2.setText(jl2.getText()+"haha\n");






    }


    /*
    *生成注册本次匿名聊天的昵称界面
    * @param frame框架
    * @panel 面板
    * @jl1 显示kerberos数据框
    * @jl2 显示数据交流数据框
    */
    public static void inputFunname(JFrame frame,JPanel panel,JTextArea textarea1,JTextArea textarea2,JLabel OnlineLabel,JList<String> list)
    {
        /*
         * 创建文本域用于用户输入
         */
        JLabel jl0 = new JLabel("输入本次聊天昵称\n");
        JTextField userText = new JTextField(20);
        userText.setBounds(10,60,165,25);
        jl0.setBounds(10, 30, 500, 40);
        panel.add(userText);
        panel.add(jl0);

            // 创建确认按钮
        JButton confirmButton = new JButton("确认");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                JOptionPane.showMessageDialog(null, "OK!");
                String name=userText.getText();
                frame.setTitle(name);
                jl0.setVisible(false);
                userText.setVisible(false);
                confirmButton.setVisible(false);
                Anonymousroom(panel,textarea1,textarea2,OnlineLabel,list,name);
            }
        });
        confirmButton.setBounds(185, 60, 80, 25);
        panel.add(confirmButton);
    }


    private static void placeComponents(JFrame frame,JPanel panel) {
        /*
         * 这边设置布局为 null
         */
        panel.setLayout(null);
        // 绝对布局
        // 定义一个容器
        // 文本域
        JLabel OnlineLabel = new JLabel("当前在线群友");
        OnlineLabel.setBounds(520,20,80,25);
        panel.add(OnlineLabel);

        //显示在线人员
        JList<String> list = new JList<String>();
        list.setListData(new String[]{"窗边月", "醉里论道"});
        list.setBounds(520, 50, 120, 455);
        panel.add(list);

        JLabel jl01 = new JLabel("用户ID(四位数字如0023)：");
        final JTextField jtf01 = new JTextField();
        JLabel jl02 = new JLabel("用户口令(六位字符串如anc123):");
        final JPasswordField jpf01 = new JPasswordField();
        // 设置密码字符为*
        jpf01.setEchoChar('*');
        // 创建"提交"按钮
        JButton jb01 = new JButton("认证");
        // 创建"重置"按钮
        JButton jb02 = new JButton("重置");
        // 创建"注册"按钮
        JButton jb03 = new JButton("注册");
        //kerberos数据
        JLabel jl1 = new JLabel("--------------------------------------------------Kerberos认证过程----------------------------------------------\n");

        JTextArea textarea1=new JTextArea(" "
        );
        textarea1.setEditable(false);
        //数据交流部分
        JLabel jl2 = new JLabel("----------------------------------------------------数据交流部分---------------------------------------------------");

        JTextArea textarea2=new JTextArea(" "
        );
        textarea2.setEditable(false);
        //添加监听
        jb01.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if (jtf01.getText().trim().length()==4 &&
                        isInteger(jtf01.getText().trim())&&
                        new String(jpf01.getPassword()).trim().length()>=6&&
                        new String(jpf01.getPassword()).trim().length()<12) {

                    JOptionPane.showMessageDialog(null, "验证成功！");
                    frame.setTitle("输入匿名昵称");
                    jl01.setVisible(false);
                    jl02.setVisible(false);
                    jb01.setVisible(false);
                    jb02.setVisible(false);
                    jb03.setVisible(false);
                    jtf01.setVisible(false);
                    jpf01.setVisible(false);
                    inputFunname(frame,panel,textarea1,textarea2,OnlineLabel,list);

                } else if (jtf01.getText().trim().length() == 0){
                    JOptionPane.showMessageDialog(null, "用户ID不能为空!");
                }
                  else if (new String(jpf01.getPassword()).trim().length() == 0){
                    JOptionPane.showMessageDialog(null, "用户口令不能为空!");
                } else {
                    JOptionPane.showMessageDialog(null, "格式错误");
                    // 清零
                    jtf01.setText("");
                    jpf01.setText("");
                }
            }
        });
        // 实现"重置"按钮功能
        jb02.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                jtf01.setText("");
                jpf01.setText("");
            }
        });
        // 将各组件添加到容器中
        panel.add(jl01);
        panel.add(jtf01);
        panel.add(jl02);
        panel.add(jpf01);
        panel.add(jb01);
        panel.add(jb02);
        panel.add(jb03);
        // 设置各组件的位置以及大小

        int jb_begin = 30;
        int jb_distanse = 110;
        int height = 30;
        jl01.setBounds(10, 30, 200, height);
        jtf01.setBounds(230, 30, 210, height);
        jl02.setBounds(10, 80, 200, height);
        jpf01.setBounds(230, 80, 210, height);
        jb01.setBounds(jb_begin, 130, 70, height);
        jb02.setBounds(jb_begin + jb_distanse, 130, 70, height);
        jb03.setBounds(jb_begin + 2*jb_distanse, 130, 70, height);


        //创建显示文字的区域

        JScrollPane jsp1 = new JScrollPane(textarea1);
        //设置矩形大小.参数依次为(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)
        jl1.setBounds(10, 205, 500, 30);
        jsp1.setBounds(10, 235, 500, 150);
        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        jsp1.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //把滚动条添加到容器里面
        panel.add(jsp1);
        panel.add(jl1);

                //创建显示文字的区域
        JScrollPane jsp2 = new JScrollPane(textarea2);
        //设置矩形大小.参数依次为(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)
        jl2.setBounds(10, 385, 500, 30);
        jsp2.setBounds(10, 415, 500, 150);
        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        jsp2.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //把滚动条添加到容器里面
        panel.add(jsp2);
        panel.add(jl2);

    }





    public static void main(String[]argc)
    {
        AntChatUi mainui=new AntChatUi();
        mainui.chatroom();
    }
}