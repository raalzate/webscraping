 ## Scraping Library ##

Usando esta librería ayuda a realizar [Web Scraping](https://es.wikipedia.org/wiki/Web_scraping) de un forma  mas fácil y ágil, apoyándose de la librería [JSoup](https://jsoup.org/) para poder extraer el documento HTML. En este caso no es necesario generar un mecanismos de navegación usando WebDriver. 

Proporciona una adaptación de conexión, ejecución y transformación, para poder de forma diferente realizar la extracción ya sea de forma local o remota, usando diferentes mecanismo ya sea [JSoup](https://jsoup.org/) o [Htmlunit](http://htmlunit.sourceforge.net/).

Internamente para la definiciones de las acciones para la cual se desea realiza Web Scraping se realiza a través de un archivo externo JSON, con el objetivo de parametrizar y configurar de forma fácil, este archivo contiene las acciones que puede ser modificados en tiempo de ejecución. 

**EJEMPLO PARA OBTENER EL PRONOSTICO DEL TIEMPO**

Usando el buscador de google podemos obtener el pronostico del tiempo en donde estemos.

```java

String url = "https://www.google.com.co/search?q=pronostico+de+tiempo";

MetaModel metaModel = new MetaModel("consult", url, "GET");
metaModel.setSelector("#wob_tm");

Map<String, Object> result = new ScraperCommand()
            .execute(modelState -> Extractors.builder(new JSoupAdapter())
                        .setState(modelState)
                        .buildExtractor("dato_c", new TextSelector()), 
             metaModel);

System.out.println(result);
```
Para tener mas detalle de la ejecución del Scraping entrar [aquí](https://gitlab.techandsolve.com/techandsolve_arquetipos/scraping/wikis/home)

## Instalarla
 
Ir a los [tags](https://gitlab.techandsolve.com/techandsolve_arquetipos/scraping/tags) del repositorio para descargar el jar. 
 
## Contribución
 
1. Crea tu rama de características: `git checkout -b feature/my-feat`
2. Confirma tus cambios: `git commit -am 'Agrega alguna característica'
3. Empuje a la rama: `git push origin feature/my-feat`
4. Presentar una Merge Requests

 
## Historia
 
Versión 1.0 (2019-01-09) - Ajustes en los comandos
 
## Créditos
 
Lead Developer - Raul .A Alzate (@raul.alzate)
