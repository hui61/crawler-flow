package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import model.Client;

public class Run {

	

	public static void main(String[] args) throws InvalidFormatException, IOException {
		
		//新建http发送客户Client类
		Client client=new Client();
		
		String url = "http://219.140.162.169:8800/rw4/report/ma02.asp";
		String param = "";

		SaveData sd = new SaveData();
		System.out.println("请输入需要爬取站点名：");
		// System.out.println("寸滩");

		Scanner sc = new Scanner(System.in);
		// new Scanner(System.in).next();
		String sitename = sc.nextLine();
		System.out.println("请输入起始年份：");
		int bnian = sc.nextInt();
		System.out.println("请输入终止年份：");
		int enian = sc.nextInt();
		int finish;
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.0000");
		int rate = 0;
		int maxri = 0;
		int byue = 1, bri = 1, bshi = 8;
		int eyue = 12, eri = 31, eshi = 8;
		int nian = bnian, yue = byue, ri = bri, shi = bshi;
		int beijian = 0;
		for (int ni = bnian; ni <= enian; ni++) {
			for (int yi = byue; yi <= 12; yi++) {

				if (yi == 2) {
					if ((ni % 4 == 0 && ni % 100 != 0) || (ni % 400 == 0)) {
						maxri = 29;
						beijian = 29;
					} else {
						maxri = 28;
						beijian = 28;
					}

				} else if (yi == 1 || yi == 3 || yi == 5 || yi == 7 || yi == 8 || yi == 10 || yi == 12) {
					maxri = 31;
					beijian = 31;
				} else {
					maxri = 30;
					beijian = 30;
				}
				for (int rj = bri; rj <= maxri; rj++) {
					if (rj != 1) {
						beijian = rj - 1;
					}
					param = "date1=" + ni + "%2F" + yi + "%2F" + rj + "&h1=" + shi + "%3A00%3A00&sj1=" + ni + "%2F" + yi
							+ "%2F" + rj + "+" + shi + "%3A00%3A00&sj2=" + ni + "%2F" + yi + "%2F" + beijian + "+" + shi
							+ "%3A00%3A00";

					try {
						String html = client.sendPost(url, param);
						Document doc = Jsoup.parse(html);
						Elements content = doc.getElementsContainingOwnText(sitename);
						// System.out.println(content);
						Elements all = content.first().parent().siblingElements();
						if (all.size() == 8) {
							all.remove(0);
						}
						String shuju[] = new String[8];
						shuju[0] = ni + "/" + yi + "/" + rj + "/" + shi;
						for (int i = 0; i < all.size(); i++) {
							String a = all.get(i).child(0).text();
							if (a.equals("") || a.equals(" ")) {
								// System.out.print("null");
								shuju[i + 1] = "null";
							}
							// System.out.println(a);
							shuju[i + 1] = a;
						}

						sd.savedata(shuju, sitename + "(" + bnian + "-" + enian + ")" + ".xlsx");

					} catch (Exception e) {
						System.out.println("该天数据无法请求！");
						String shuju[] = new String[8];
						shuju[0] = ni + "/" + yi + "/" + rj + "/" + shi;
						for (int i = 0; i < 7; i++) {
							shuju[i + 1] = " ";
						}
						sd.savedata(shuju, sitename + "(" + bnian + "-" + enian + ")" + ".xlsx");
						continue;

					} finally {
						rate++;
						finish = rate * 100 / ((enian - bnian + 1) * 365);
						// System.err.println(finish);
						// df.format(finish);
					}

					System.out.println("爬取数据中...进度：" + finish + "%");
				}
			}

		}
		System.out.println("爬取数据中...进度：" + 100 + "%");
		System.out.println("数据爬取完毕！");

	}
}
