package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class Client {

	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {

			URL realUrl = new URL(url);
			// 根据url生成URLConnection类
			URLConnection httpUrlConnection = realUrl.openConnection();
			// 设置通用的请求属性
			httpUrlConnection.setRequestProperty("accept", "*/*");
			httpUrlConnection.setRequestProperty("connection", "Keep-Alive");
			httpUrlConnection.setRequestProperty("user-agent",
					"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 10.0; WOW64; Trident/7.0)");
			// 发送POST请求必须设置如下两行
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setDoInput(true);
			// URLConnection类打开链接
			httpUrlConnection.connect();
			// 获得URLConnection类输出流对象
			OutputStream outStrm = httpUrlConnection.getOutputStream();
			// 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
			//out = new PrintWriter(httpUrlConnection.getOutputStream());
			 ObjectOutputStream objOutputStrm = new ObjectOutputStream(outStrm);
			// 向对象输出流写出数据，这些数据将存到内存缓冲区中
			 objOutputStrm.writeObject(param);
			//out.print(param);
			// flush输出流的缓冲,
			 objOutputStrm.flush();
			//out.flush();
			// close输出流

			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(), "gb2312"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;

	}
}
