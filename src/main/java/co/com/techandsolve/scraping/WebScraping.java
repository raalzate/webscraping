package co.com.techandsolve.scraping;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class WebScraping {

    private static final String USER = "mhgpaler";
    private static final String PASS = "Historia1";

    private String docType;
    private String docId;
    private RemoteWebDriver driver;

    /**
     * Scraper para un DNI especifico y con un driver dado
     *
     * @param docType
     * @param docId
     */
    public WebScraping(String docType, String docId) {
        this.docType = docType;
        this.docId = docId;
        this.driver = new ChromeDriver();
    }

    /**
     * Realizar login con selenium y guardar las cookies
     */
    public void login() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver.get("https://www.bonospensionales.gov.co");
        driver.findElement(By.name("ssousername")).sendKeys(USER);
        driver.findElement(By.name("password")).sendKeys(PASS);
        driver.findElement(By.xpath("/html/body/center/form/div/center/input[1]")).click();
        SessionUtils.setCookies(driver.manage().getCookies(), new File("Cookie.data"));
    }

    /**
     * Cerrar la sesion
     */
    public void logout() {
        driver.get("https://www.bonospensionales.gov.co/BonosPensionales/jsp/Salir.jsp");
        driver.close();
    }


    /**
     * Scraper de un conjuto de acciones y determinado para un modelo dado
     *
     * @param modelState
     * @return Extractors
     */
    public Extractors consults(ModelState modelState) {

        /**
         * IMPORTANTE: revisar el archivo .json de resource para determinar las acciones que se debe ejecutar dentro de la pagina, este modelo tiene la misma llave que el extracto step. La ejecución es secuencial el ModelState guarda un memento temporal para poder pasar los valores entre los steps
         *
         * El resultado del step genera un documento que debe ser procesado y mapeado o persistido, el memento que se tiene puede transportal temporalmente los datos para que un step final lo persista
         *
         * Este scraping depende del analisis que se realice en la pagina para la construcción de los metamodelos
         */


        /**
         * TODO:
         * - Realizar la recuperación por excepciones
         * - Loggers
         * - Documentación
         * - Tests
         */
        return Extractors.builder()
                .setDNI(docType, docId)
                .step("consultarSolicitudesAfp",
                        document ->
                                modelState.setState("consultarSolicitudesAfp", document.html()))
                .step("consultarBonos",
                        document ->
                                modelState.setState("consultarBonos", document.html())
                ).build();
    }
}
