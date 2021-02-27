
package zad2;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Service {
	
	String country;
	String city;
	
	String idKey = "7b0bf14cbf7e77afbc78e136de5c5968";
	String code;
	String currency;
	//lista dla kodów dla kraju
	
	
	Map<String, String> countryCodes = new HashMap<String, String>() {	
		{
			put("Poland", "PL");
			put("Spain", "ES");
			put("USA", "US");
			put("Italy", "IT");
			put("Australia", "AU");
			put("Belgium", "BE");
			put("Brazil", "BR");
			put("Hong Kong", "HK");
			put("Ireland", "IE");
			put("United Kingdom", "UK");
			put("Germany", "DE");
		}
	};
	
	Map<String, String> currencyMap = new HashMap<String, String>() {	
		{
			put("Poland", "PLN");
			put("Spain", "EUR");
			put("USA", "USD");
			put("Italy", "EUR");
			put("Australia", "AUD");
			put("Belgium", "EUR");
			put("Brazil", "BRL");
			put("Hong Kong", "HKD");
			put("Ireland", "EUR");
			put("United Kingdom", "GBP");
			put("Germany", "EUR");
		}
	};
	

	public Service(String country) {
		this.country = country;
		this.code = getCode(country);
		this.currency = getCurrency(country);
	}

	public String getWeather(String city) {
		String urlName = "http://api.openweathermap.org//data//2.5//weather?q=";
		String appid = "&appid=7b0bf14cbf7e77afbc78e136de5c5968";
		
		String inline = ""; //przetrzymuje wynik api
		
		try {
			//URL urle = new java.net.URL(url2);
			URL url = new URL(urlName + city+ "," + code + appid);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			conn.connect();
			
			//metoda scanner do wczytania lini z API
			
			Scanner sc = new Scanner(url.openStream());
			while (sc.hasNext()) {
				inline+=sc.nextLine();
			}
			sc.close();
			
			
		} catch (IOException e) {
			System.out.println(e);
		}
		return inline;
	}

	public Double getRateFor(String currency) {
		// url jako obiekt
		String urlName = "http://data.fixer.io/api/latest";
		String accessKey = "?access_key=e3f5549e92dba2d17c21b802f1947c60";
		String symbols = "&symbols=" + currency;
		
		String inline = ""; //przetrzymujaca wynik api
		Double rate = null;
		
		try {
			URL url = new URL(urlName + accessKey + symbols);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			conn.connect();
			
			//metoda scanner do wczytania lini z API
			
			Scanner sc = new Scanner(url.openStream());
			while (sc.hasNext()) {
				inline+=sc.nextLine();
			}
			sc.close();
		} catch (IOException e) {
			System.out.println("Błąd w czytaniu metody");
		}
		
		
		//konwersja stringa na json object
		
		JsonObject jsonObject = new JsonParser().parse(inline).getAsJsonObject();
		
		rate = jsonObject.getAsJsonObject("rates").get(currency).getAsDouble();
		
		return rate;
	}

	public Double getNBPRate() {
		// TODO Auto-generated method stub
		
		//dwa adresy podane w tabeli
		String[] urlName = {"https://www.nbp.pl/kursy/xml/a071z200410.xml","https://www.nbp.pl/kursy/xml/b014z200408.xml"};
	
		int quecheck = 0; //jezeli wynosi 1 to wychodzimy z pętli
		Double rate = null;
		
		for(int i=0;i<2;i++) {
			try {
				DocumentBuilder db;
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				db = dbf.newDocumentBuilder();
				Document doc = db.parse(new URL(urlName[i]).openStream());
				
				Element rootElement = doc.getDocumentElement();
				
				NodeList nList = doc.getElementsByTagName("pozycja");
				
				for(int temp = 0; temp < nList.getLength();temp++) {
					Node node = nList.item(temp);
					 if (node.getNodeType() == Node.ELEMENT_NODE) {
						 Element eElement = (Element) node; 
						 if(eElement.getElementsByTagName("kod_waluty").item(0).getTextContent().equals(currency)) {
						
						//System.out.println("Waluta : "  + eElement.getElementsByTagName("kod_waluty").item(0).getTextContent());
						//System.out.println("Kurs : "  + eElement.getElementsByTagName("kurs_sredni").item(0).getTextContent());
						
						
						//zapis ze stringa na double/ zamian , na .
						String result = eElement.getElementsByTagName("kurs_sredni").item(0).getTextContent();
						result = result.replace(",", ".");
						rate = Double.valueOf(result);
						
						//System.out.println("Kurs  spr : "+ currency + " "  + rate);
						//quecheck = 1;
						 }
						 
					 }
				}
					
			} catch (ParserConfigurationException | IOException | SAXException e) {
				e.printStackTrace();
			}
					
		}
		
		return rate;
	}
	
	//metoda do odkrycia kodu kraju
	public String getCode(String country) {
		String countryFound = countryCodes.get(country);
		if(countryFound == null) {
			countryFound = null;
			System.out.println("Nie znaleziono poprawnego kraju");
		}
		return countryFound;
	}
	
	//odkrycie waluty kraju
	public String getCurrency(String currency) {
		String countryFound = currencyMap.get(country);
		if(countryFound == null) {
			countryFound = null;
			System.out.println("Nie znaleziono poprawnego kraju");
		}
		return countryFound;
	}
	
	String getCountry() {
		return this.country;
	}
	
	
}  
