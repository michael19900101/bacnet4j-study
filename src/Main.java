import test.MyBacnet;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MyBacnet myBacnet = new MyBacnet();

        // 创建Scanner对象来读取控制台输入
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入文本（输入 'exit' 退出程序）：");

        // 使用while循环来持续接收输入
        while (true) {
            String inputText = scanner.nextLine();
            // 检查输入是否为退出命令
            if ("exit".equalsIgnoreCase(inputText)) {
                System.out.println("程序退出。");
                break; // 退出循环
            }
            myBacnet.control(inputText);
        }

        myBacnet.end();

        // 关闭Scanner对象
        scanner.close();
    }
}