/*
 author by D mortem
 Crawler.java
 Web Crawler for Pictures
 2017-10-14
*/

import java.io.*;
import java.net.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

class Thread1 extends Thread
{
	String str1;
	DataInputStream reader1;
	DataOutputStream writer1;
	
	Thread1(String ss) {
		str1 = ss;
		try {
			String filename, line1;
			int l, j;
			l = str1.indexOf("jpg");
			j = str1.lastIndexOf("-", l);
			filename = str1.substring(j + 1, l + 3);
			
			URL url = new URL(str1);
			reader1 = new DataInputStream(new BufferedInputStream(url.openStream()));
			
			writer1 = new DataOutputStream(new FileOutputStream(new File(filename)));
			System.out.println(filename);
			
		} catch (Exception e) {
			System.out.println("Error:\n" + e);
			return;
		}
	}
	
	public void run()
	{
		try {
			try {
				while (true) {
				writer1.writeByte(reader1.readByte());		//字节流文件输出
				}
			} catch (EOFException e){}
		reader1.close();
		writer1.close();
		} catch (Exception e) {
			System.out.println("Error:\n" + e);
			return;
		}
	}
}

public class Crawler_Picture
{
	String cate_url, pic_url;
	
	Crawler_Picture(String url1, String url2) {
		cate_url = url1;
		pic_url = url2;
	}
	
	public void save() {
		try	{
			Vector vt = new Vector(5);		
			URL url = new URL(cate_url);
			
			BufferedReader reader = new BufferedReader
			(new InputStreamReader(url.openStream()));

			String line, str, str1;
			int i, j;
			
			while ((line = reader.readLine()) != null) {
				if ((j = line.indexOf(".jpg")) >= 0) {
					i = line.lastIndexOf("/attach", j);
					str = pic_url + line.substring(i + 1, j + 4);		//产生图片链接
					if (vt.indexOf(str) < 0)		//搜索在vt中是否已存在该图片，若不存在则加入
						vt.add(str);
				}
			}
			
			Iterator itr = vt.iterator();		//迭代器，取出vt内部存着的字符
			while (itr.hasNext()) {
				str1 = (String)itr.next();		//读取下一个。强制类型转换，因为vector中有可能不是String
				new Thread1(str1).start();		//调用一个新的线程下载
				System.out.println(str1);
			}
			
	        reader.close();
	    } catch(Exception e) {
			System.out.println("ErrorL: \n" + e);
		}
	}
    
	
	public static void main(String[] args) {
		Crawler_Picture wcy = new Crawler_Picture("http://www.caefs.zju.edu.cn/office/", "http://www.caefs.zju.edu.cn/");
		wcy.save();
	}
}