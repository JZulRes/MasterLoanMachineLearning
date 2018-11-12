package io.github.jhipster.masterloanmachinelearning.web.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MachineLearningResource controller
 */
@RestController
@RequestMapping("/api/machine-learning")
public class MachineLearningResource {
    public static String wml_service_credentials_url = "https://us-south.ml.cloud.ibm.com";
    public static final String wml_service_credentials_username = "d97c3565-ef9e-4b9e-abda-7a45ef5fc128";
    public static final String wml_service_credentials_password = "166ba193-1001-48bd-a9a9-42723c803036";
    private final Logger log = LoggerFactory.getLogger(MachineLearningResource.class);

    /**
    * GET getScoreAndRiskForLoan
     * @throws IOException 
    */
    @GetMapping("/get-score-and-risk-for-loan/{genero}/{paisNacimiento}/{estratoSocioeconomico}/{numeroDePrestamosPagados}/{numeroDeCuotasPagadasTotal}/{montoPrestamo}")
    public String getScoreAndRiskForLoan(@PathVariable("genero") String genero,@PathVariable("paisNacimiento") String paisNacimiento,@PathVariable("estratoSocioeconomico") String estratoSocioeconomico,@PathVariable("numeroDePrestamosPagados") String numeroDePrestamosPagados,@PathVariable("numeroDeCuotasPagadasTotal") String numeroDeCuotasPagadasTotal,@PathVariable("montoPrestamo") String montoPrestamo) throws IOException {
        String response = "";
        Map<String, String> wml_credentials = new HashMap<String, String>()
        {{
            put("url", wml_service_credentials_url);
            put("username", wml_service_credentials_username);
            put("password", wml_service_credentials_password);
        }};

        String wml_auth_header = "Basic " +
            Base64.getEncoder().encodeToString((wml_credentials.get("username") + ":" +
                wml_credentials.get("password")).getBytes(StandardCharsets.UTF_8));
        String wml_url = wml_credentials.get("url") + "/v3/identity/token";
        HttpURLConnection tokenConnection = null;
        HttpURLConnection scoringConnection = null;
        BufferedReader tokenBuffer = null;
        BufferedReader scoringBuffer = null;
        try {
            // Getting WML token
            URL tokenUrl = new URL(wml_url);
            tokenConnection = (HttpURLConnection) tokenUrl.openConnection();
            tokenConnection.setDoInput(true);
            tokenConnection.setDoOutput(true);
            tokenConnection.setRequestMethod("GET");
            tokenConnection.setRequestProperty("Authorization", wml_auth_header);
            tokenBuffer = new BufferedReader(new InputStreamReader(tokenConnection.getInputStream()));
            StringBuffer jsonString = new StringBuffer();
            String line;
            while ((line = tokenBuffer.readLine()) != null) {
                jsonString.append(line);
            }
            // Scoring request
            URL scoringUrl = new URL("https://us-south.ml.cloud.ibm.com/v3/wml_instances/e40db5a9-01c4-411d-9ae5-776a52500598/deployments/a68a23f3-a7b2-4534-933a-f06c22d140f4/online");
            String wml_token = "Bearer " +
                jsonString.toString()
                    .replace("\"","")
                    .replace("}", "")
                    .split(":")[1];
            scoringConnection = (HttpURLConnection) scoringUrl.openConnection();
            scoringConnection.setDoInput(true);
            scoringConnection.setDoOutput(true);
            scoringConnection.setRequestMethod("POST");
            scoringConnection.setRequestProperty("Accept", "application/json");
            scoringConnection.setRequestProperty("Authorization", wml_token);
            scoringConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStreamWriter writer = new OutputStreamWriter(scoringConnection.getOutputStream(), "UTF-8");

            // NOTE: manually define and pass the array(s) of values to be scored in the next line
            String payload = "{\"fields\": [\"GENERO\", \"PAIS_NACIMIENTO\", \"ESTRATO_SOCIECONOMICO\", \"NUMERO_DE_PRESTAMOS_PAGADOS\", \"NUMERO_DE_CUOTAS_PAGADAS_TOTAL\", \"MONTO_DE_PRESTAMO\"], "
            		+ "\"values\": [\""+genero+"\",\""+paisNacimiento+"\","+estratoSocioeconomico+","+numeroDePrestamosPagados+","+numeroDeCuotasPagadasTotal+","+montoPrestamo+"]}";
            writer.write(payload);
            writer.close();

            scoringBuffer = new BufferedReader(new InputStreamReader(scoringConnection.getInputStream()));
            StringBuffer jsonStringScoring = new StringBuffer();
            String lineScoring;
            while ((lineScoring = scoringBuffer.readLine()) != null) {
                jsonStringScoring.append(lineScoring);
            }
            response = jsonStringScoring.toString();
        } catch (IOException e) {
            System.out.println("The URL is not valid.");
            System.out.println(e.getMessage());
        }
        finally {
            if (tokenConnection != null) {
                tokenConnection.disconnect();
            }
            if (tokenBuffer != null) {
                tokenBuffer.close();
            }
            if (scoringConnection != null) {
                scoringConnection.disconnect();
            }
            if (scoringBuffer != null) {
                scoringBuffer.close();
            }
        }
        return response;
    }

}
