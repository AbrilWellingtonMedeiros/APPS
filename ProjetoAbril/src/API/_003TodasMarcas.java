package API;
import static io.restassured.RestAssured.given;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import org.json.JSONObject;
import org.testng.annotations.Test;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;

import ftor_testng.AbrilUtils;
import ftor_testng.ConfigureExecution;
import ftor_testng.DataDriven;
import ftor_testng.ReportLog;
import ftor_testng.Utils;

public class _003TodasMarcas extends AbrilUtils {
	
	/// Caso de Teste
		@Test
		
		public void TodasMarcas() throws Exception {

			ConfigureExecution.configurarExecucaoCasosTeste("003");
		}
		

		// CHAMA M�TODO DE TESTE ESTA SETADO 1 DIA, ALTERAR NA CLASSE ConfigureExecution , LINHA 175.
		//ct003.GeraHtml(null, 1);
	
	public static void GeraHtml(String[] args, Integer dias) throws IOException {
	    //Scanner ler = new Scanner(System.in);
		
		    String File = Utils.userDir + "\\saidas\\API\\" + DataDriven.nomeCaso + ".html";
		    FileWriter arq = new FileWriter(File);
		    PrintWriter gravarArq = new PrintWriter(arq);
		    
			// Configura a Data e Hora para posteriormente adicion�-la ao
			// relat�rio
		    Calendar cal = Calendar.getInstance();
		    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			// gera arquivo html
		    String bodyEmail = "<html><body>"
		    		+ "<p style=\"text-align: center;\">"
		    		+ "<img src=\"https://abril.com.br/wp-content/uploads/2022/05/grupo-abril.jpg\" alt=\"\"></p>"
		    		+ "<p style=\"text-align: center; text-transform: uppercase; padding: 10px; font-size: 24px;\">Relat�rio do paywall - 2 dias - "+ dateFormat.format(cal.getTime()) +"</p>"
		    		+ "<table width=\"700\" align=\"center\" cellspacing=\"0\" cellpadding=\"5\" border=\"1\" style=\"border-collapse: collapse;\">"
					+ "<tr>"
						+ "<td>Marca</td>"
						+ "<td>Aberta</td>"
						+ "<td>Fechadas</td>"
						+ "<td>Contabilizados</td>"
						+ "<td>Total</td>"						
					+ "</tr>";
		    
		    DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
		    String dataFinal = dateFormat2.format(cal.getTime());
		    Integer horas = cal.AM_PM; // horas de 1 a 24h
		    String dataInicial = dateFormat2.format(cal.getTime());
		    // se for menor que 12 pega a data do dia anterior 
		    // se for maior considera a data atual	
		    if (horas < 12) {
		    	cal.add( cal.DAY_OF_MONTH, -dias);
		    	dataInicial = dateFormat2.format(cal.getTime());
			}
		    String dataUrl = "?dataInicio=" + dataInicial + "&dataFim=" + dataFinal;		
		    System.out.println(dataUrl);
			String marcas[][] = {
				{ "Veja", "https://veja.abril.com.br/wp-json/paywall/type_content" },
				{ "Veja Rio", "https://vejario.abril.com.br/wp-json/paywall/type_content" },
				{ "Quatro Rodas", "https://quatrorodas.abril.com.br/wp-json/paywall/type_content" },
				{ "Veja SP", "https://vejasp.abril.com.br/wp-json/paywall/type_content" },
				{ "Veja Saude", "https://saude.abril.com.br/wp-json/paywall/type_content" },
				{ "Super", "https://super.abril.com.br/wp-json/paywall/type_content" },
				{ "Voce SA", "https://vocesa.abril.com.br/wp-json/paywall/type_content" },
				{ "Voce RH", "https://vocerh.abril.com.br/wp-json/paywall/type_content" },				
				{ "GE", "https://guiadoestudante.abril.com.br/wp-json/paywall/type_content" }
			};
			
			for (int i = 0; i < marcas.length; i++) {
				//System.out.println(getUrl);
				String getUrl = marcas[i][1] + dataUrl;				
				String dados = given().when().get(getUrl).getBody().asString();
				
				try{
					JSONObject jsonObject = new JSONObject(dados);				
					//System.out.println("� array");					
					
					String abertos = !jsonObject.has("Abertos") || jsonObject.isNull("Abertos") ? "0%"
							: jsonObject.getString("Abertos");
					
					Integer AbertosTotal = !jsonObject.has("AbertosTotal") || jsonObject.isNull("AbertosTotal") ? 0
							: jsonObject.getInt("AbertosTotal");
		
					String contabilizados = !jsonObject.has("Contabilizados") || jsonObject.isNull("Contabilizados") ? "0%"
							: jsonObject.getString("Contabilizados");
					
					Integer ContabilizadosTotal = !jsonObject.has("ContabilizadosTotal") || jsonObject.isNull("ContabilizadosTotal") ? 0
							: jsonObject.getInt("ContabilizadosTotal");
					
					String fechados = !jsonObject.has("Fechados") || jsonObject.isNull("Fechados") ? "0%"
							: jsonObject.getString("Fechados");
					
					Integer FechadosTotal = !jsonObject.has("FechadosTotal") || jsonObject.isNull("FechadosTotal") ? 0
							: jsonObject.getInt("FechadosTotal");
					
					Integer Total = !jsonObject.has("Total") || jsonObject.isNull("Total") ? 0
							: jsonObject.getInt("Total");
					
					bodyEmail = bodyEmail + "<tr>"
								+ "<td>"+ marcas[i][0] +"</td>";
					
							
					if (Double.parseDouble(abertos.replace("%", "").replace(",", ".")) < 5.00) {
						bodyEmail = bodyEmail + "<td>"+ "Quantidade: " + AbertosTotal + "<br>"+  "Percentual: " + abertos +"</td>";
					} else {
						bodyEmail = bodyEmail + "<td style=\"background:red;color:#fff;\">" +  "Quantidade: " + AbertosTotal + "<br>"+  "Percentual: " + abertos + "</td>";
					}
					// Add cells in table
					bodyEmail = bodyEmail + "<td>" + "Quantidade: " + FechadosTotal + "<br>"+  "Percentual: " + fechados + "</td>"
										  + "<td>" + "Quantidade: " + ContabilizadosTotal + "<br>"+  "Percentual: " + contabilizados + "</td>"
					                      + "<td>" +String.valueOf(Total)+ "</td>"					                     
					                      + "</tr>";
				}catch(Exception e ){
				//	System.out.println("n�o � array");
					bodyEmail = bodyEmail + "<tr>"
							+ "<td>"+ marcas[i][0] +"</td>"
							+ "<td>0 = 0%</td>"
							+ "<td>0 = 0%</td>"
							+ "<td>0 = 0%</td>"
							+ "<td>0</td>"
						+ "</tr>";
				}
			
			}
			bodyEmail = bodyEmail + "</table></body><html>";	    
		    gravarArq.print(bodyEmail);
			arq.close();
	    	System.out.printf("Relat�rio de marcas gerado com sucesso!");	
			
	  }
}