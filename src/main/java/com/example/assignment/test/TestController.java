package com.example.assignment.test;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	@Autowired
	ProducerTemplate producerTemplate;
	
	
	@RequestMapping(value = "/getCityByCountry")
	public String getCityByCountry(@RequestParam(value="country", defaultValue="India") String country) {		
		String requestBody = ""
				+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://www.webserviceX.NET\">\r\n" + 
				"   <soapenv:Header/>\r\n" + 
				"   <soapenv:Body>\r\n" + 
				"      <web:GetCitiesByCountry>\r\n" + 
				"         <web:CountryName>"+country+"</web:CountryName>\r\n" + 
				"      </web:GetCitiesByCountry>\r\n" + 
				"   </soapenv:Body>\r\n" + 
				"</soapenv:Envelope>";
		
		Exchange exchange = producerTemplate.send("direct:remoteService", new Processor() {		
			@Override
			public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(requestBody);
            }
		});
		String stream = exchange.getProperty("Result").toString();		
		JSONObject data = org.json.XML.toJSONObject(stream);
		System.out.println("Response == "+data.toString());	
		return data.toString();
	}
	
	@RequestMapping(value = "/getWeather")
	public String getWeather(@RequestParam(value="country", defaultValue="India") String country, 
			@RequestParam(value="city", defaultValue="Delhi") String city) {		
		String requestBody = ""
				+ "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:web=\"http://www.webserviceX.NET\">\r\n" + 
				"   <soap:Header/>\r\n" + 
				"   <soap:Body>\r\n" + 
				"      <web:GetWeather>\r\n" + 
				"         <web:CityName>"+city+"</web:CityName>\r\n" + 
				"         <web:CountryName>"+country+"</web:CountryName>\r\n" + 
				"      </web:GetWeather>\r\n" + 
				"   </soap:Body>\r\n" + 
				"</soap:Envelope>";
		
		Exchange exchange = producerTemplate.send("direct:remoteService", new Processor() {		
			@Override
			public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(requestBody);
            }
		});
		String stream = exchange.getProperty("Result").toString();		
		JSONObject data = org.json.XML.toJSONObject(stream);
		System.out.println("Response == "+data.toString());		
		return data.toString();
	}	
}
