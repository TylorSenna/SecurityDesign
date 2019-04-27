import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;






public class SwingLoginExample{
    JFrame jFrame;
    public void Login() {
        // 设置标题
        jFrame = new JFrame();
        jFrame.setTitle("物联网安全登陆界面");
        // 绝对布局
        jFrame.setLayout(null);
        // 定义一个容器
        Container c = jFrame.getContentPane();
        JLabel jl1 = new JLabel("用户ID：");
        final JTextField jtf1 = new JTextField();
        JLabel jl2 = new JLabel("用户口令:");
        final JPasswordField jpf1 = new JPasswordField();
        // 设置密码字符为*
        jpf1.setEchoChar('*');
        // 创建"提交"按钮
        JButton jb1 = new JButton("认证");
        // 创建"重置"按钮
        JButton jb2 = new JButton("重置");
        // 创建"注册"按钮
        JButton jb3 = new JButton("注册");
        // 当用户名为"admin",密码为"123456"时点击"提交"按钮弹出"登录成功"提示对话框
        jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if ((jtf1.getText().trim().equals("") && new String(jpf1.getPassword()).trim().equals(""))) {
                    JOptionPane.showMessageDialog(null, "验证成功！");
                    AnonymousChat anonymousChat = new AnonymousChat();

                    AnonymousChat.chatroom();
                    //jFrame.dispose();
                } /*else if (jtf1.getText().trim().length() == 0){
                    JOptionPane.showMessageDialog(null, "用户ID不能为空!");
                }
                  else if (new String(jpf1.getPassword()).trim().length() == 0){
                    JOptionPane.showMessageDialog(null, "用户口令不能为空!");
                }*/ else {
                    JOptionPane.showMessageDialog(null, "验证错误");
                    // 清零
                    jtf1.setText("");
                    jpf1.setText("");
                }
            }
        });
        // 实现"重置"按钮功能
        jb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                jtf1.setText("");
                jpf1.setText("");
            }
        });
        // 将各组件添加到容器中
        c.add(jl1);
        c.add(jtf1);
        c.add(jl2);
        c.add(jpf1);
        c.add(jb1);
        c.add(jb2);
        c.add(jb3);
        // 设置各组件的位置以及大小
        int jb_begin = 30;
        int jb_distanse = 110;
        int height = 30;
        jl1.setBounds(10, 20, 90, height);
        jtf1.setBounds(120, 20, 210, height);
        jl2.setBounds(10, 60, 90, height);
        jpf1.setBounds(120, 60, 210, height);
        jb1.setBounds(jb_begin, 110, 70, height);
        jb2.setBounds(jb_begin + jb_distanse, 110, 70, height);
        jb3.setBounds(jb_begin + 2*jb_distanse, 110, 70, height);
        // 设置窗体大小、关闭方式、不可拉伸
        jFrame.setSize(380, 230);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dimension = tk.getScreenSize();//获取屏幕大小
        int dimensionwidth=dimension.width;
        int dimensionheight=dimension.height;
        int x=(dimensionwidth-350)/2;
        int y=(dimensionheight-250)/2;
        jFrame.setLocation(x, y);
        jFrame.setVisible(true);
        jFrame.setResizable(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        SwingLoginExample start = new SwingLoginExample();
        start.Login();
    }
}

