package games;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Properties;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class GetAllPlayers {

    private static Properties config = new ReadProperties().loadProperties(PropertiesFiles.RECEPCIONAR_PROPERTIES_FILE);
    private static final String baseUrl = config.getProperty("baseUrlRecepcionar");
    private static final String articulo = "04919";
    private static final int varlog = 0;

    private static final String getUriArticuloVarlog = config.getProperty("baseUriArticulosApi");
    private static final String getUriPathArticuloVarlog = config.getProperty("baseUriArticulosPathVarlog");

    private static final int terminal = 291;
    private static final int almacen = 1018;
    private static final int operario = 307740;

    private static final String formato = "01";

    private static final int varlog2 = 2;
    private static final String numArticulo = "01811";
    private static final String formatoError = "0";

    private static final String denominacion = "huevos";

    @BeforeEach
    public void before() {
        RestAssured.baseURI = baseUrl;
//        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    @Order(1)
    @Disabled // Faltan datos en el plan de pruebas
    public void getBuscadorArticulo() throws IOException, ParseException {

        Map<String, Object> output = JSONParserUtility
                .fileToJSONObject(new File("src/test/resources/buscadorArticulos/obtenerArticulosResponse.json"));

        given().log().all().contentType(ContentType.JSON).queryParam("operario", operario)
                .queryParam("terminal", terminal).queryParam("almacen", almacen).get(getUriArticuloVarlog).then()
                .statusCode(200).body("", equalTo(output));

    }
}
