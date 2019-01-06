 ## Scraping Library ##

Usando esta librería ayuda a realizar web scraping de un forma  mas fácil y ágil. Apoyándose de la librería JSpoup para poder extraer el documento HTML para generar un mecanismos de navegación usando los selectores. 

Proporciona una adaptación de conexión, ejecución y tranformación, para poder de forma diferente realizar la extracción ya sea de forma local o remota, usando diferentes mecanismo ya sea JSpoup o Htmlunit.

Internamente para la definiciones de las acciones para la cual se desea realiza web scraping se realiza a travez de un archivo JSON, con el objetivo de parametrizar y configurar de forma fácil. Este archivo contiene los modelos que puede ser modificados en tiempo de ejecución. 

## Interfaces ##

### WebScraping ###

Esta interfaz tiene como objetivo definir los pasos para la extracción, estos pasos se debe definir a travez del archivo JSON. A su vez recibe como argumento un estado del modelo, este tiene dos propósitos, alterar el modelo y acumular información en un mapa. 

public interface WebScraping {
    Extractors build(ModelState modelState);
}

#### Extractors Class ####

La clase Extractors es un builder que ayuda a construir los pasos que se ejecuta la extracción, estos paso son los definidos en el modelo de datos del archivo .JSON.

  
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
                .step("consultaBlogUX",func)
                .step("consultaBlogProd",func)
                .build();
 }