package br.com.caelum.camel;


import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaHttpPollingNegociacoes {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("timer://negociacoes?fixedRate=true&delay=1s&period=360s").
				to("http4://argentumws.caelum.com.br/negociacoes").
				convertBodyTo(String.class).
				split().
				xpath("/list/negociacao").
				filter().
				xpath("/negociacao/quantidade[text()='21']").
				log("${id} \n ${body}").
				setHeader(Exchange.FILE_NAME, simple("negociacao - ${header.CamelSplitIndex}.xml")).
				to("file:saida");
			}
		});
		
		context.start();
		Thread.sleep(2000);
		context.stop();

	}	
}
