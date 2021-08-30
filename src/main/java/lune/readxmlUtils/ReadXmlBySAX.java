package lune.readxmlUtils;


import lune.bean.JsonBean;
import lune.handler.SAXParseHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 用SAX方式读取xml文件
 * @author lune
 */
public class ReadXmlBySAX {
	public static List<JsonBean> mJsonBeanList = null;

	public List<JsonBean> getBooks(String fileName) throws Exception{
		SAXParserFactory sParserFactory = SAXParserFactory.newInstance();
		SAXParser parser = sParserFactory.newSAXParser();
		
		SAXParseHandler handler = new SAXParseHandler();
		parser.parse(fileName, handler);
		
		return handler.getBooks();
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//_______________生成json________________________________
		try {
			//src/res/th_in.xml
			mJsonBeanList = new ReadXmlBySAX().getBooks("src/res/zh_in.xml");
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
			writeText(sb.toString(),"src/res/zh_out.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//_____________________________生成xml________________________________
		//saveUrlAs();


		//___________________输出文件——————————————————————————————————

	}

	public static void writeText(String content,String outFile){
		try {
			// 保证创建一个新文件
			//path = "src/res/th_out.txt"
			File file = new File(outFile);
			if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
				file.getParentFile().mkdirs();
			}
			if (file.exists()) { // 如果已存在,删除旧文件
				file.delete();
			}
			file.createNewFile();
			// 将格式化后的字符串写入文件
			Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			write.write(content);
			write.flush();
			write.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



	/**
	 * @return
	 * @功能 下载临时素材接口
	 */

	public static File saveUrlAs() {
		String url = "https://ard-static.flashexpress.com/client-kit/lang/th.json";
		String filePath = "src/res/output/";
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
		try {
			// 建立链接
			URL httpUrl = new URL(url);
			conn = (HttpURLConnection) httpUrl.openConnection();
			//以Post方式提交表单，默认get方式
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			// post方式不能使用缓存
			conn.setUseCaches(false);
			//连接指定的资源
			conn.connect();
			//获取网络输入流
			inputStream = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			//从文件中按字节读取内容，到文件尾部时read方法将返回-1
			while ((bytesRead = bis.read(buffer)) != -1) {
				//将读取的字节转为字符串对象
				String string = new String(buffer, 0, bytesRead);
//				JSONObject jsonObject1 = JSONObject.fromObject(string);
//				System.out.println(jsonObject1);
				System.out.println(string);
			}
			//判断文件的保存路径后面是否以/结尾
			if (!filePath.endsWith("/")) {
				filePath += "/";
			}
			//写入到文件（注意文件保存路径的后面一定要加上文件的名称）
			fileOut = new FileOutputStream(filePath + "strings.xml");
			BufferedOutputStream bos = new BufferedOutputStream(fileOut);

			byte[] buf = new byte[4096];
			int length = bis.read(buf);
			//保存文件
			while (length != -1) {
				bos.write(buf, 0, length);
				length = bis.read(buf);
			}
			bos.close();
			bis.close();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("抛出异常！！");
		}

		return file;

	}

}
