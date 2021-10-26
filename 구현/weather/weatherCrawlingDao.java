package application.weather;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class weatherCrawlingDao {

	public static void Crawling(weatherVO wVO) throws IOException {
		String url = "https://weather.naver.com/today/02360111";
		Document doc = null;
		Elements tmp;
		String location_temp;
		int temperature_temp;
		String status_tmp;
		
		try {
			doc = Jsoup.connect(url).get();
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		
		Elements element = doc.select("strong.current");
		
		wVO.setLocation(doc.select("strong.location_name").get(0).text());
		wVO.setTemperature(Integer.parseInt((element.get(0).text()).substring(5,7)));
		wVO.setStatus(doc.select("span.weather.before_slash").get(0).text());
	}
	
}
