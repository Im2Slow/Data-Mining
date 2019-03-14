import org.apache.jena.ontology.Individual;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.ontology.OntModel;

public class Jena1 {
    public static void getIndividuals(OntModel ontModel) {
    ExtendedIterator individuals = ontModel.listIndividuals();
    while(individuals.hasNext()){
        Individual singleIndividual = (Individual) individuals.next();
        if(singleIndividual.hasRDFType("http://www.semanticweb.org/datamining#persons")) {
            String nameProperty = singleIndividual.getLocalName();
            System.out.println(nameProperty);
        }
        }
    }
}
