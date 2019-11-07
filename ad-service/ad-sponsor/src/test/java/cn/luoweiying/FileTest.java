package cn.luoweiying;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileTest {
    //F:\暂时存放\com.luoweiying\luoweiying-ad-spring-cloud
    //f:\暂时存放\com.luoweiying\luoweiying-ad-spring-cloud\mysql_data\test.txt

    public static void main(String[] args) throws IOException {
        nioTest();
    }

    public static void IOTest() throws IOException {
        //		1.通过File类定义文件路径
        File file = new File("e:" + File.separator + "hello.txt");
        if (!file.getParentFile().exists()) {//保证父目录存在
            file.getParentFile().mkdirs();//创建目录
        }
//		2.OutputStream类是一个抽象类，用子类进行父类的实例化
        OutputStream output = new FileOutputStream(file, true);//有true为追加
//		3.进行文件的输出处理操作
        String msg = "ceshi";
        output.write(msg.getBytes(), 1, 4);//会进行自动创建普通文件和输出内容eshi
//		4.关闭输出，关闭IO
        output.close();
    }

    public static void pathsTest() throws IOException {
        File file = new File("f:" + File.separator + "暂时存放" + File.separator +
                "com.luoweiying" + File.separator + "luoweiying-ad-spring-cloud" + File.separator +
                "mysql_data" + File.separator + "test.txt");
        if (file.getParentFile().exists()) {
            file.getParentFile().mkdirs();

            Writer writer = new PrintWriter(file);
            writer.write("test,测试");
            writer.close();
        }
    }
    public static void nioTest() throws IOException {
        Path path = Paths.get("f:" + File.separator + "暂时存放" + File.separator +
                "com.luoweiying" + File.separator + "luoweiying-ad-spring-cloud" + File.separator +
                "mysql_data" + File.separator + "test.txt");
        System.out.println(path.toString());
        BufferedWriter bufferedWriter = Files.newBufferedWriter(path);
        bufferedWriter.write("test ,test ,test 测试");
        bufferedWriter.close();
    }
}
