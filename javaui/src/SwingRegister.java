import javax.swing.*;

public class SwingRegister {

    public static void main(String[] args) {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("注册");
        // Setting the width and height of frame
        frame.setSize(550, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        // 创建 JLabel
        JLabel userLabel = new JLabel("UserId:");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        userLabel.setBounds(10,20,80,25);
        panel.add(userLabel);

        /*
         * 创建文本域用于用户输入
         */
        JTextField userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        panel.add(userText);

        // 输入密码的文本域
        JLabel passwordLabel = new JLabel("口令:");
        passwordLabel.setBounds(10,50,80,25);
        panel.add(passwordLabel);

        /*
         *这个类似用于输入的文本域
         * 但是输入的信息会以点号代替，用于包含密码的安全性
         */
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100,50,165,25);
        panel.add(passwordText);
        // 创建注册按钮
        JButton registButton = new JButton("确定");
        registButton.setBounds(100, 80, 80, 25);
        registButton.setEnabled(false);
        panel.add(registButton);


        //创建显示文字的区域
        JTextArea textarea=new JTextArea("AS的公司证书\nid：1353\npublickey：483294206282386513516516593\nsignature：563453526321873121321651645103620354065406540658733324\n");
        JScrollPane jsp = new JScrollPane(textarea);
        //设置矩形大小.参数依次为(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)
        jsp.setBounds(10, 110, 500, 100);
        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        jsp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //把滚动条添加到容器里面
        panel.add(jsp);


        // 创建按钮
        JButton trueButton = new JButton("信任");
        trueButton.setBounds(10, 215, 80, 25);
        panel.add(trueButton);

        // 创建按钮
        JButton falseButton = new JButton("不信任");
        falseButton.setBounds(100, 215, 80, 25);
        panel.add(falseButton);
    }

}
