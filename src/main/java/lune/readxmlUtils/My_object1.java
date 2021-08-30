package lune.readxmlUtils;

import lune.bean.JsonBean;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class My_object1 extends JFrame {
    public JTextField j3;
    public JTextField j4;
    public JTextField j33;
    public JTextField j44;
    //xml 文件输入路径
    private String xmlPathIn;
    //json 输出路径
    private String jsonPathOut;
    public My_object1() throws HeadlessException {
        setTitle("小工具");
        setSize(1150,350);
        JPanel p = new JPanel();
        getContentPane().add(p);
        JLabel j1 = new JLabel("输出文件路径：");
        JLabel j2 = new JLabel("输入文件路径：");

        j3 = new JTextField(30);
        j4 = new JTextField(30);

        JLabel j5 = new JLabel("xml 转 json:  ");
        JButton j6 = new JButton("确定");



        JLabel j11 = new JLabel("输出文件路径：");
        JLabel j22 = new JLabel("输入文件路径：");

        j33 = new JTextField(30);
        j33.setText("https://ard-static.flashexpress.com/client-kit/lang/th.json");
        j33.setText("/Users/stone/Documents/workspace_flashex/flash_express_backyard/flash_express_ui/src/main/res/values-en/strings.xml");
        j44 = new JTextField(30);

        JLabel j55 = new JLabel("json 转 xml:  ");
        JButton j66 = new JButton("确定");

        p.add(j5);
        p.add(j2);
        p.add(j3);
        p.add(j1);
        p.add(j4);
        p.add(j6);

        p.add(j55);
        p.add(j22);
        p.add(j33);
        p.add(j11);
        p.add(j44);
        p.add(j66);

        //生成json
        j6.addMouseListener(new Mouselen());

        //生成xml文件
        j66.addMouseListener(new Mouselen1());

        setVisible(true);
    }

    //生成json
    class Mouselen implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            xmlPathIn = j3.getText();
            jsonPathOut = j4.getText();

            //_______________生成json________________________________
            try {
                //src/res/th_in.xml
                ReadXmlBySAX.mJsonBeanList = new ReadXmlBySAX().getBooks(xmlPathIn);
                StringBuilder sb = new StringBuilder();
                sb.append("{");
                for (int i = 0; i < ReadXmlBySAX.mJsonBeanList.size(); i++) {
                    JsonBean bean = ReadXmlBySAX.mJsonBeanList.get(i);
                    sb.append("\""+ bean.getKey() +"\":\"" + bean.getValue() + "\"");
                    if (i < ReadXmlBySAX.mJsonBeanList.size() -1) {
                        sb.append(",");
                    }
                }
                sb.append("}");

                System.out.println(sb);

                ReadXmlBySAX.writeText(sb.toString(),jsonPathOut);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }
    }

    //生成xml文件
    class Mouselen1 implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            xmlPathIn = j33.getText();
            jsonPathOut = j44.getText();

            Document document = DocumentHelper.createDocument();
            Element root =  document.addElement("resources"); //默认根节点
            String STR_JSON = saveUrlAs(xmlPathIn,jsonPathOut,"GET");
            //String STR_JSON = "{}";
            Element el = null;
            try {
                el = WriteToXml.jsonToXml(STR_JSON, root);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(el.asXML());
            try {
                //生成xml文件
                String fileName = "strings.xml";
                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setEncoding("utf-8"); // 指定XML编码
                format.setExpandEmptyElements(true);//自动添加闭合标签
                document.setXMLEncoding("utf-8");
                //指定文件路径，名字，格式
                XMLWriter writer = new XMLWriter(
                        new FileOutputStream(new File(jsonPathOut + File.separator + fileName)), format);
                writer.write(document);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }
    }

    /**
     * @param filePath 文件将要保存的目录
     * @param method   请求方法，包括POST和GET
     * @param url      请求的路径
     * @return
     * @功能 下载临时素材接口
     */

    public static String saveUrlAs(String url, String filePath, String method) {
        //System.out.println("fileName---->"+filePath);
        //创建不同的文件夹目录
        File file = new File(filePath);
        //判断文件夹是否存在
        if (!file.exists()) {
            //如果文件夹不存在，则创建新的的文件夹
            file.mkdirs();
        }
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        String chunk = null;
        try {
            // 建立链接
            URL httpUrl = new URL(url);
            conn = (HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream = conn.getInputStream();
            BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));//转成utf-8格式

            //BufferedInputStream bis = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[1024];
            StringBuilder response = new StringBuilder();

            String line;
            //从文件中按字节读取内容，到文件尾部时read方法将返回-1
            while ((line = bis.readLine()) != null) {
                //将读取的字节转为字符串对象
                response.append(line);
            }
            System.out.println(response.toString());
            chunk = response.toString();
            bis.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("抛出异常！！");
        }

        return chunk;

    }

}
