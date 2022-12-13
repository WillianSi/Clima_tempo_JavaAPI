package view;

import java.io.IOException;

import com.thoughtworks.xstream.XStream;

import model.Cidade;
import model.Previsao;
import model.service.WeatherForecastService;

public class Main {

	public static void main(String[] args) {	
		try {
			String previsao = WeatherForecastService.sevenDaysWeatherForecast(244);
			System.out.println(previsao);
			
			XStream xstream = new XStream();
			
			// Ajuste de segurança do XStream
			Class<?>[] classes = new Class[] {Cidade.class, Previsao.class};
			xstream.allowTypes(classes);
			
			xstream.alias("cidade", Cidade.class);
			xstream.alias("previsao", Previsao.class);
			
			xstream.addImplicitCollection(Cidade.class, "previsoes");
			
			Cidade c = (Cidade) xstream.fromXML(previsao);
			
			System.out.printf("Previsão para %s/%s.\n", c.getNome(), c.getUf());
			
			for (Previsao p : c.getPrevisoes()) {
				System.out.printf("Dia %s\nMinima %s\nMaxima %s\nTempo %s\n", p.getDia(), p.getMinima(), p.getMaxima(), p.getTempo());
			}
			
		} catch (IOException e) {
			System.out.println("Erro ao consultar API de clima.");
			e.printStackTrace();
		}
	}
}