package lune.handler;

import java.util.ArrayList;
import java.util.List;

import lune.bean.JsonBean;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * 用SAX解析xml文件时需要的handler
 * @author lune
 */
public class SAXParseHandler extends DefaultHandler {
	private List<JsonBean> list;         //存放解析到的book数组
	private JsonBean mJsonBean;               //存放当前解析的book
	
	private String content = null;   //存放当前节点值
	
	
	
	/**
	 * 开始解析xml文档时调用此方法
	 */
	@Override
	public void startDocument() throws SAXException {
		
		super.startDocument();
		System.out.println("开始解析xml文件");
		list = new ArrayList<JsonBean>();
	}



	/** 
	 * 文档解析完成后调用此方法
	 */
	@Override
	public void endDocument() throws SAXException {
		
		super.endDocument();
		System.out.println("xml文件解析完毕");
	}



	/**
	 * 开始解析节点时调用此方法
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		
		//当节点名为book时,获取book的属性id
		if(qName.equals("string")){
			mJsonBean = new JsonBean();
			String name = attributes.getValue("name");//System.out.println("id值为"+id);
			mJsonBean.setKey(name);
		}
		
	}



	/**
	 *节点解析完毕时调用此方法
	 *
	 *@param qName 节点名
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		super.endElement(uri, localName, qName);
		if(qName.equals("string")){			//当结束当前book解析时,将该book添加到数组后置为空，方便下一次book赋值
			mJsonBean.setValue(content);
			list.add(mJsonBean);
			mJsonBean = null;
		}	
		
	}



	/** 
	 * 此方法用来获取节点的值
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		
		super.characters(ch, start, length);
		
		content = new String(ch, start, length);
		//收集不为空白的节点值
//		if(!content.trim().equals("")){
//			System.out.println("节点值为："+content);
//		}
		
	}

	public List<JsonBean> getBooks(){
		return list;
	}
	
}
