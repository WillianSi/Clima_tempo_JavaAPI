package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.thoughtworks.xstream.XStream;

import model.Cidade;
import model.Cidades;
import model.City;
import model.Previsao;
import model.service.WeatherForecastService;

public class ClimaTempo {

	private JFrame frame;
	private JTextField txtCodigo;
	private JTextField txtLocalizacao;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClimaTempo window = new ClimaTempo();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClimaTempo() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 922, 533);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Clima Tempo");
		lblNewLabel_1.setForeground(new Color(30, 144, 255));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 40));
		lblNewLabel_1.setBounds(10, 21, 275, 51);
		frame.getContentPane().add(lblNewLabel_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(506, 58, 392, 403);
		frame.getContentPane().add(scrollPane_1);
		
		JTextArea txtTempo = new JTextArea();
		txtTempo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTempo.setBackground(new Color(224, 255, 255));
		txtTempo.setForeground(new Color(0, 0, 0));
		txtTempo.setEditable(false);
		scrollPane_1.setViewportView(txtTempo);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 225, 392, 236);
		frame.getContentPane().add(scrollPane);
		
		JTextArea txtCidade = new JTextArea();
		txtCidade.setBackground(new Color(224, 255, 255));
		txtCidade.setForeground(Color.BLACK);
		scrollPane.setViewportView(txtCidade);
		
		JLabel lblCdigo = new JLabel("Código:");
		lblCdigo.setToolTipText("Defina o");
		lblCdigo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCdigo.setBounds(10, 97, 66, 17);
		frame.getContentPane().add(lblCdigo);
		
		txtCodigo = new JTextField();
		txtCodigo.setToolTipText("");
		txtCodigo.setColumns(10);
		txtCodigo.setBounds(84, 98, 226, 19);
		frame.getContentPane().add(txtCodigo);
		
		JButton btnClima = new JButton("Pequisar");
		btnClima.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtTempo.setText(null);
				try {
					int local = Integer.parseInt(txtCodigo.getText());
					
					String previsao = WeatherForecastService.sevenDaysWeatherForecast(local);
					
					XStream xstream = new XStream();
					
					// Ajuste de segurança do XStream
					Class<?>[] classes = new Class[] {Cidade.class, Previsao.class};
					xstream.allowTypes(classes);
					
					xstream.alias("cidade", Cidade.class);
					xstream.alias("previsao", Previsao.class);
					
					xstream.addImplicitCollection(Cidade.class, "previsoes");
					
					Cidade c = (Cidade) xstream.fromXML(previsao);
					
					txtTempo.append("Previsão para: ");
					txtTempo.append(c.getNome());
					txtTempo.append(c.getUf());
					txtTempo.append("\n");
					
					for (Previsao p : c.getPrevisoes()) {
						txtTempo.append("\nDia: " + p.getDia());
						txtTempo.append("\nMinima: " + p.getMinima());
						txtTempo.append("\nMaxima: " + p.getMaxima());
						txtTempo.append("\nTempo: ");
						String t = p.getTempo();
						txtTempo.append(decricao(t));
						txtTempo.append("\n");
					}
					
				} catch (IOException e1) {
					System.out.println("Erro ao consultar API de clima.");
					e1.printStackTrace();
				}
			}	
		});
		btnClima.setForeground(Color.WHITE);
		btnClima.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnClima.setBackground(new Color(255, 204, 0));
		btnClima.setBounds(320, 96, 107, 21);
		frame.getContentPane().add(btnClima);
		
		JLabel lblLocalizao = new JLabel("Localização:");
		lblLocalizao.setToolTipText("Defina o");
		lblLocalizao.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLocalizao.setBounds(10, 144, 89, 17);
		frame.getContentPane().add(lblLocalizao);
		
		txtLocalizacao = new JTextField();
		txtLocalizacao.setToolTipText("");
		txtLocalizacao.setColumns(10);
		txtLocalizacao.setBounds(104, 145, 203, 19);
		frame.getContentPane().add(txtLocalizacao);
		
		JButton btnLocalizacao = new JButton("Pequisar");
		btnLocalizacao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtCidade.setText(null);
				
				try {
					
					String city = txtLocalizacao.getText();
					String cidades = WeatherForecastService.citysearch(city);
					
					XStream xstream = new XStream();
					
					// Ajuste de segurança do XStream
					Class<?>[] classes = new Class[] {Cidades.class, City.class};
					xstream.allowTypes(classes);
					
					xstream.alias("cidades", Cidades.class);
					xstream.alias("cidade", City.class);
					
					xstream.addImplicitCollection(Cidades.class, "cidade");
					
					Cidades c = (Cidades) xstream.fromXML(cidades);
					
					for (City p : c.getCidade()) {
						txtCidade.append("\nNome: " + p.getNome());
						txtCidade.append("\nUf: " + p.getUf());
						txtCidade.append("\nId: " + p.getId());
						txtCidade.append("\n");
					}
					
					
				} catch (IOException e1) {
					System.out.println("Erro ao consultar API de clima.");
					e1.printStackTrace();
				}
				
				
			}
		});
		btnLocalizacao.setForeground(Color.WHITE);
		btnLocalizacao.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnLocalizacao.setBackground(new Color(255, 204, 0));
		btnLocalizacao.setBounds(320, 143, 107, 21);
		frame.getContentPane().add(btnLocalizacao);
		
		JLabel lblNewLabel_1_1 = new JLabel("Localização:");
		lblNewLabel_1_1.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_1.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblNewLabel_1_1.setBounds(26, 189, 158, 36);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Clima:");
		lblNewLabel_1_1_1.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_1_1.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblNewLabel_1_1_1.setBounds(506, 24, 158, 36);
		frame.getContentPane().add(lblNewLabel_1_1_1);
	}
	
	   private String decricao(String t) {
		   
		   if(t.equals("ec")) {
			   t = "Encoberto com Chuvas Isoladas";
		   }
		   if(t.equals("ci")) {
			   t = "Chuvas Isoladas";
		   }
		   if(t.equals("c")) {
			   t = "Chuva";
		   }
		   if(t.equals("in")) {
			   t = "Instável";
		   }
		   if(t.equals("pp")) {
			   t = "Poss. de Pancadas de Chuva";
		   }
		   if(t.equals("cm")) {
			   t = "Chuva pela Manhã";
		   }
		   if(t.equals("cn")) {
			   t = "Chuva a Noite";
		   }
		   if(t.equals("pt")) {
			   t = "Chuva a Noite";
		   }
		   if(t.equals("pm")) {
			   t = "Pancadas de Chuva pela Manhã";
		   }
		   if(t.equals("np")) {
			   t = "Nublado e Pancadas de Chuva";
		   }
		   if(t.equals("pc")) {
			   t = "Pancadas de Chuva";
		   }
		   if(t.equals("pn")) {
			   t = "Parcialmente Nublado";
		   }
		   if(t.equals("cv")) {
			   t = "Chuvisco";
		   }
		   if(t.equals("ch")) {
			   t = "Chuvoso";
		   }
		   if(t.equals("t")) {
			   t = "Tempestade";
		   }
		   if(t.equals("ps")) {
			   t = "Predomínio de Sol";
		   }
		   if(t.equals("e")) {
			   t = "Encoberto";
		   }
		   if(t.equals("n")) {
			   t = "Nublado";
		   }
		   if(t.equals("cl")) {
			   t = "Céu Claro";
		   }
		   if(t.equals("nv")) {
			   t = "Nevoeiro";
		   }
		   if(t.equals("g")) {
			   t = "Geada";
		   }
		   if(t.equals("ne")) {
			   t = "Neve";
		   }
		   if(t.equals("nd")) {
			   t = "Não Definido";
		   }
		   if(t.equals("pnt")) {
			   t = "Pancadas de Chuva a Noite";
		   }
		   if(t.equals("psc")) {
			   t = "Possibilidade de Chuva";
		   }
		   if(t.equals("pcm")) {
			   t = "Possibilidade de Chuva pela Manhã";
		   }
		   if(t.equals("pct")) {
			   t = "Possibilidade de Chuva a Tarde";
		   }
		   if(t.equals("pcn")) {
			   t = "Possibilidade de Chuva a Noite";
		   }
		   if(t.equals("npt")) {
			   t = "Nublado com Pancadas a Tarde";
		   }
		   if(t.equals("npn")) {
			   t = "Nublado com Pancadas a Noite";
		   }
		   if(t.equals("ncn")) {
			   t = "Nublado com Poss. de Chuva a Noite";
		   }
		   if(t.equals("nct")) {
			   t = "Nublado com Poss. de Chuva a Tarde";
		   }
		   if(t.equals("ncm")) {
			   t = "Nubl. c/ Poss. de Chuva pela Manhã";
		   }
		   if(t.equals("npm")) {
			   t = "Nublado com Pancadas pela Manhã";
		   }
		   if(t.equals("npp")) {
			   t = "Nublado com Possibilidade de Chuva";
		   }
		   if(t.equals("vn")) {
			   t = "Variação de Nebulosidade";
		   }
		   if(t.equals("ct")) {
			   t = "Chuva a Tarde";
		   }
		   if(t.equals("ppn")) {
			   t = "Poss. de Panc. de Chuva a Noite";
		   }
		   if(t.equals("ppt")) {
			   t = "Poss. de Panc. de Chuva a Noite";
		   }
		   if(t.equals("ppm")) {
			   t = "Poss. de Panc. de Chuva pela Manhã";
		   }   
	return t;
	}
}
