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
Para tener mas detalle de la ejecución del Scraping entrar [aquí](2.-Ejecutar-el-Web-Scraping-(Commands))

## Instalarla
 
Ir a los [tags](https://gitlab.techandsolve.com/techandsolve_arquetipos/scraping/tags) del repositorio para descargar el jar. 
 
## Contribución
 
1. Crea tu rama de características: `git checkout -b feature/my-feat`
2. Confirma tus cambios: `git commit -am 'Agrega alguna característica'
3. Empuje a la rama: `git push origin feature/my-feat`
4. Presentar una Merge Requests

 
## Historia
 
Versión 1.2 (2019-01-09) - Ajustes en los comandos
 
## Créditos
 
Lead Developer - Raul .A Alzate (@raul.alzate)
 
## Licencia 
 
The MIT License (MIT)

Copyright (c) 2015 Chris Kibble

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.