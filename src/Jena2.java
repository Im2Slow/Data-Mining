import org.apache.jena.query.*;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.shared.JenaException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Jena2 {
    public static void queryPersons(OntModel ontModel) throws IOException

    {
        try{
            String queryString = new String(Files.readAllBytes(Paths.get("./data/query.txt")));
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query,ontModel);
            ResultSet res = qe.execSelect();
            while(res.hasNext()){
                QuerySolution sol = res.nextSolution();
                RDFNode x = sol.get("?name");
                System.out.println(x);
            }
        }   catch (JenaException je){
            System.err.println("ERROR" + je.getMessage());
            je.printStackTrace();
        }
    }
}
