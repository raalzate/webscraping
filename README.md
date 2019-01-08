 ## Scraping Library ##

Usando esta librería ayuda a realizar web scraping de un forma  mas fácil y ágil. Apoyándose de la librería JSpoup para poder extraer el documento HTML para generar un mecanismos de navegación usando los selectores. 

Proporciona una adaptación de conexión, ejecución y transformación, para poder de forma diferente realizar la extracción ya sea de forma local o remota, usando diferentes mecanismo ya sea JSpoup o Htmlunit.

Internamente para la definiciones de las acciones para la cual se desea realiza web scraping se realiza a travez de un archivo JSON, con el objetivo de parametrizar y configurar de forma fácil. Este archivo contiene los modelos que puede ser modificados en tiempo de ejecución. 

## Uso de la Interfaz WebScraping ##

Esta interfaz tiene como objetivo definir los pasos para la extracción, estos pasos se debe definir a travez del archivo JSON. A su vez recibe como argumento un estado del modelo, este tiene dos propósitos, alterar el modelo y acumular información en un mapa. 

```java
public interface WebScraping {
    Extractors build(ModelState modelState);
}
```

#### Ejemplo del Extractors Class ####


La clase ***Extractors**** es un builder que ayuda a construir los pasos que se ejecuta la extracción, estos paso son los definidos en el modelo de datos del archivo .JSON.

```java  
Extractors.builder(jSoupAdapter) // Adaptador que tiene el puerto para poder realizar la conexión, ejecución y parse
                .setState(modelState) // el objeto que tiene como objetivo cambiar los estados
                .step("label",func) // el primer paso con su respectiva funciona selectora y el label del metamodelo
                .build() // el constructor de los pasos
```  

** OTRO EJEMPLO SIN UNSAR LOS LABEL DE META MODELO**

Este ejemplo no requiere tener un flujo de pasos.

```java
Extractors.builder(jSoupAdapter)// Adaptador que tiene el puerto para poder realizar la conexión, ejecución y parse
                .setState(modelState)// el objeto que tiene como objetivo cambiar los estados
                .setSelector("label",func)// funciona selectora y el label
                .build() //el constructor 
```  

**EJEMPLO DE USO PARA EL METODO BUILD**

```java  
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

**IMPORTANTE**: El estado del modelo trabaja como un memento entre steps, donde se acumula datos o resultados del scraping, en el ejemplo  ``` modelState1.getExtra().put(label, listTitle) ```, estamos guardando una llave según el label y el valor con el resultado esperado.

#### Ejemplo de la Meta Data ####

Este archivo se ubica en ***resource/metadata.json***

```json
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

| Propiedad  | Descripción |
| ------------- | ------------- |
| action  | url absoluta  |
| type  | cualquier cadena  |
| header  | objecto key-value |
| data  | objecto key-value  |
| method  | GET,POST  |
| selector  | Selector CSS  |
| path  | Parametro amigable  |
| query  | Query Param del Request  |

**MODELO CLASS**

```java
public class MetalModel {
    private String type;
    private String action;
    private String method;
    private String path;
    private String query;
    private String selector;
    private Map<String, String> data;
    private Map<String, String> header;
}
```
## Uso de la Interfaz DocumentPort ##

DocumentPort es una interfaz que permite determinar el Documento(org.jsoup.nodes.Document), funciona como protocolo del Extractors dado que por cada step(paso) se realiza el connect, execute y parse respectivamente. 

```java
public interface DocumentPort {
    void connect(MetalModel model);
    void execute();
    Document parse();
}
```

**EJEMPLO DE USO**

```java
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

## Ejecutar el Web Scraping (Commands) ##

Para ejecutar el scraping en necesario usar el comando que provee la librería. La clase ScraperCommand tiene 4 configuraciones para ejecutarse:

- Ejecuta con los valores definidos del Modelo(metadata) y el archivo por defecto(metadata.json) en la ruta por defecto
- Ejecuta definiendo el archivo .JSON (metadata) partiendo del directorio resource
- Ejecuta definiendo un modelo inicial 
- Ejecuta definiendo el archivo .JSON (metadata) y el modelo inicial   

El resultado de los comando siempre es el valor de extras que tiene acumulado en el estado del modelo (el objeto estado de modelo es un memento que permite tener en memora datos que son compartidos a través de los steps).


**EJEMPLO DE USO**

```java
 JSoupAdapter adapter = new JSoupAdapter();
 ScraperCommand scraperCommand = new ScraperCommand();
 Map<String, Object> result = scraperCommand.execute(new ImpWebScraping(adapter));
 
```

```java
JSoupAdapter adapter = new JSoupAdapter();
MetalModel metalModel = new MetalModel("consult", "https://techandsolve.com/category/developer-e1533574812739/", "GET");
metalModel.setSelector("body > div > div.container");
SingleScraperCommand scraperCommand = new SingleScraperCommand(metalModel);
Map<String, Object> result = scraperCommand.execute(new ImpWebScraping(adapter)); // este scraping debe proporcinal el unico selector (.setSelector) 
```

## Uso de la Interfaz Selector ##

Un selector es un función que permite consumir el elemento extraído por el scraping, un selector es el encargado en muchas de las ocaciones de acumular la respuesta requerida, por ese motivo el método de la interfaz recibe como argumento el label o tag, el estado del modelo y elemento.

```java

public interface Selector<E extends Element> {
    void accept(String label, ModelState modelState, E element);
    default Selector<E> andThen(Selector<Element> after) {
        Objects.requireNonNull(after);
        return (String l, ModelState m, E e) -> { accept(l, m, e); after.accept(l, m, e); };
    }
}
```
**EJEMPLO DE USO**

```java

public class DefaultSelector implements Selector<Element> {
    @Override
    public void accept(String label, ModelState modelState, Element element) {
        modelState.setStateModel(null)
                .putExtra(label, element.html());
    }
}
```

## Uso de la clase ModelState ##

La clase ModelState tiene dos propósitos, el primero es guardar estados de los steps a través del metodo Extra (Map<String, Object>), y el segundo es alterar el modelo del siguiente step.

El siguiente ejemplo no definimos ningún modelo y agregamos un valor al extra con la llave tag.

```java

modelState.setStateModel(null)
                .putExtra("tag", element.html());
```

**IMPORTANTE:** al ejecutar el comando siempre retorna los extras, por cada selector se debería tener uno o mas extra como resulta final del scraping.

