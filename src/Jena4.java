import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.shared.JenaException;

public class Jena4 {
    public static void movieSearch(String movieName, OntModel ontModel){
        try{
            String queryString ="PREFIX info: <http://www.semanticweb.org/datamining#>\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "\n" +
                    "SELECT ?name ?year ?country ?genre ?actorNames\n" +
                    "WHERE\n" +
                    "{\n" +
                    "?x rdf:type info:movie .\n" +
                    "?x info:Name ?name .\n" +
                    "?x info:Year ?year .\n" +
                    "?x info:Country ?country .\n" +
                    "?x info:Genre ?genre .\n" +
                    "?x info:hasActor ?actors .\n" +
                    "?actors info:Name ?actorNames .\n" +
                    "FILTER(?name = '"+movieName+"')\n" +
                    "}";
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query,ontModel);
            ResultSet res = qe.execSelect();
            if(res.hasNext()){
                QuerySolution sol = res.nextSolution();
                RDFNode name = sol.get("?name");
                Literal year = (Literal) sol.get("?year");
                RDFNode country = sol.get("?country");
                RDFNode genre = sol.get("?genre");
                RDFNode actor = sol.get("?actorNames");
                System.out.print("\n"+name + ", a(n) " + genre + " made in "+year.getInt() + ", "+country+" starring "+actor);
                while(res.hasNext()){
                    sol = res.nextSolution();
                    actor = sol.get("?actorNames");
                    System.out.print(", "+actor);
                }
            }else{
                System.err.println("ERROR : movie not found");
            }
        }   catch (JenaException je){
            System.err.println("ERROR" + je.getMessage());
            je.printStackTrace();
        }
    }
}
