 ## Scraping Library ##

Usando esta librería ayuda a realizar web scraping de un forma  mas fácil y ágil. Apoyándose de la librería JSpoup para poder extraer el documento HTML para generar un mecanismos de navegación usando los selectores. 

Proporciona una adaptación de conexión, ejecución y tranformación, para poder de forma diferente realizar la extracción ya sea de forma local o remota, usando diferentes mecanismo ya sea JSpoup o Htmlunit.

Internamente para la definiciones de las acciones para la cual se desea realiza web scraping se realiza a travez de un archivo JSON, con el objetivo de parametrizar y configurar de forma fácil. Este archivo contiene los modelos que puede ser modificados en tiempo de ejecución. 

## Uso de la Interfacez WebScraping ##

Esta interfaz tiene como objetivo definir los pasos para la extracción, estos pasos se debe definir a travez del archivo JSON. A su vez recibe como argumento un estado del modelo, este tiene dos propósitos, alterar el modelo y acumular información en un mapa. 

```
public interface WebScraping {
    Extractors build(ModelState modelState);
}
```

#### Ejemplo del Extractors Class ####


La clase Extractors es un builder que ayuda a construir los pasos que se ejecuta la extracción, estos paso son los definidos en el modelo de datos del archivo .JSON.

```  
Extractors.builder(jSoupAdapter) // Adaptador que tiene el puerto para poder realizar la conexión, ejecución y parse
                .setState(modelState) // el objeto que tiene como objetivo cambiar los estados
                .step("tag",func) // el primer paso con su respectiva funciona selectora 
                .build() // el constructor de los pasos
```  

**EJEMPLO DE USO PARA EL METODO BUILD**

```  
public Extractors build(ModelState modelState) {
        Selector<Element> func = new DefaultSelector().andThen((label, modelState1, element) -> {
            List<String> listTitle = element.select(".panel-heading")
                    .stream()
                    .map(Element::text)
                    .collect(Collectors.toList());
            modelState1.getExtra().put(label, listTitle);
        });


        return Extractors.builder(jSoupAdapter)
                .setState(modelState)
                .step("consultaBlogDeveloper",func)
                .step("consultaBlogMachineLearning",func)
                .step("consultaBlogIoT",func)
                .build();
 }
```

#### Ejemplo de la Meta Data ####

Este archivo se ubica en resource/metadata.json

``` 
{

 "consultaBlogDeveloper": {
   "type": "consult",
   "action": "https://techandsolve.com/category/developer-e1533574812739/",
   "method": "GET",
   "selector": "body > div > div.container"
 },

  "consultaBlogs": {
    "type": "consult",
    "action": "https://techandsolve.com",
    "method": "GET",
    "query": "category=developer-e1533574812739",
   "selector": "body > div > div.container"
  },

  "consultaBlogMachineLearning": {
    "type": "consult",
    "action": "https://techandsolve.com/category/machinelearn/",
    "method": "GET",
    "selector": "body > div > div.container"
  },
}
``` 

## Uso de la Interfacez DocumentPort ##

DocumentPort es una interfaz que permite determinar el Documento(org.jsoup.nodes.Document), funciona como protocolo del Extractors dado que por cada step(paso) se realiza la connect, execute y parse respectivamente. 

```
public interface DocumentPort {
    void connect(MetalModel model);
    void execute();
    Document parse();
}
```

**EJEMPLO DE USO**

```
public class JSoupAdapter implements DocumentPort {

    private Connection connection;
    private Connection.Response result;

    @Override
    public void connect(MetalModel model) {
        connection =  Jsoup.connect(model.getAction())
                .cookies(CookieUtils.getCookies())
                .method(getMethod(model.getMethod()))
                .data(model.getData())
                .followRedirects(true);
    }

    @Override
    public void execute() {
        try {
            result = connection.execute();
        } catch (IOException e) {
           throw new DocumentException(e.getMessage());
        }
    }

    @Override
    public Document parse() {
        try {
            return result.parse();
        } catch (IOException e) {
            throw new DocumentException(e.getMessage());
        }
    }

    private Connection.Method getMethod(String method){
        return Connection.Method.valueOf(method.toUpperCase());
    }
}
```



