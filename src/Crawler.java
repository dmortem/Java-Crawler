/*
 author by D mortem
 Crawler.java
 Web Crawler
 2017-10-14
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {
	String cate_url, book_url;
	
	Crawler(String url1, String url2) {
		cate_url = url1;
		book_url = url2;
	}
	
	public void save() {
		try {
			// book_url是小说目录的网址，chap_url用来存各章节的具体网址
			//String cate_url = "http://www.xxbiquge.com/1_1413/";
			//String book_url = "http://www.xxbiquge.com";
			String chap_url;
			URL url = new URL(cate_url);
			
			// 输入输出字符流
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter("斗破苍穹.txt"));
			
			String line, name = null;
			int i, j, k = 0;
			
			while ((line = reader.readLine()) != null) {	// 在目录主页上找链接
				//line = new String(line.getBytes(), "utf-8");
				while ((j = line.indexOf("<dd>", k)) >= 0) {	// 找到某一章节的目录
					// 先从目录主页爬取具体章节网址。链接代码：<dd><a href="/1_1413/1049417.html">第一章 陨落的天才</a>
					i = line.indexOf("href", j) + 6;		
					k = line.indexOf(">", i) - 1;
					if (i >= k)						// 若是第二个"</dd>"，则continue
						continue;
					name = line.substring(i, k);	// 得到1049417.html子目录
					chap_url = book_url + name;		// 和目录网址合并，得到各章节网址http://www.xxbiquge.com/1_1413/1049417.html
					
					// 具体进入章节网址爬取内容
					URL url_new = new URL(chap_url);
					BufferedReader reader1 = new BufferedReader(new InputStreamReader(url_new.openStream()));
					String line_new, content, str;
					int ii, jj, kk = 0;
					while ((line_new = reader1.readLine()) != null) {
						//byte[] b = line_new.getBytes("utf-8");
						//line_new = new String(b, "gb2312");
						//line_new = new String(line_new.getBytes(), "gb2312");
						// 爬取章节标题。网页：<h1>第一章 陨落的天才</h1>
						if ((jj = line_new.indexOf("<h1>")) >= 0) {
							// 获取首尾位置
							ii = jj + 4;
							kk = line_new.indexOf("h1", ii) - 2;
							content = line_new.substring(ii, kk);
							
							// 输出内容
							System.out.println(content);
							writer.write(content);
							writer.newLine();
							continue;
						}
						// 爬取正文。网页：&nbsp;&nbsp;&nbsp;&nbsp;“斗之力，三段！”<br />
						while ((jj = line_new.indexOf("&nbsp;&nbsp;&nbsp;&nbsp;", kk)) >= 0) {
							ii = jj + 24;
							kk = line_new.indexOf("<br />", ii);
							if (kk < ii)
								kk = line_new.indexOf("</div>", ii);
							content = line_new.substring(ii, kk);
							
							//正则处理
							Pattern pattern = Pattern.compile("<.+?>", Pattern.DOTALL);	//define the part which needs removing
							Matcher matcher = pattern.matcher(content);		//正则去html元素
							str = matcher.replaceAll("");
					
							pattern = Pattern.compile("\\&[a-zA-Z]{1,10};", Pattern.DOTALL);	//define the part which needs removing
							matcher = pattern.matcher(str);		//正则去html元素
							str = matcher.replaceAll("");
							
							// 输出内容
							System.out.println(str);
							writer.write(str);
							writer.newLine();
						}
					}
					writer.newLine();	
					reader1.close();
				}
			}
			writer.close();
		} catch(Exception e) {
			System.out.println("ErrorL: \n" + e);
		}
	}
	
	
	
	public static void main(String[] args) {
		Crawler wcy = new Crawler("http://www.xxbiquge.com/1_1413/", "http://www.xxbiquge.com");
		wcy.save();
	}
}

