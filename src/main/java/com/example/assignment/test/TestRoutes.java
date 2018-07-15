package com.example.assignment.test;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TestRoutes extends RouteBuilder {
	@Autowired
	private Environment env;
	
	@Override
	public void configure() throws Exception {
		
		String endpoint = env.getProperty("SOAP.Endpoint");
		
		from("direct:remoteService")
		  .setHeader(Exchange.HTTP_URI, simple(endpoint)) // configure this
		  .setHeader(Exchange.HTTP_METHOD, constant("POST"))
		  .setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
		  .log(">>> ${body}")
		  .to("http://dummyhost")
		  .process(new Processor() {				
			  @Override
			  public void process(Exchange exchange) throws Exception {
				  String result = exchange.getIn().getBody(String.class);
				  System.out.println("-->"+result);
				  exchange.setProperty("Result", result); // Hack to pass response to controller :)
			  }
		  });	
		
	}
}