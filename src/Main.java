import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.shared.JenaException;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void GUI_movieList(ArrayList included, ArrayList excluded, OntModel ontModel){
        try {
            String base_uri = "http://www.semanticweb.org/datamining#";
            ExtendedIterator individuals = ontModel.listIndividuals();
            String queryString = "PREFIX info: <http://www.semanticweb.org/datamining#>\n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                        "\n" +
                        "SELECT ?movieName\n" +
                        "WHERE\n" +
                        "{\n" +
                        "?x rdf:type info:movie .\n" +
                        "?x info:Name ?movieName .\n" +
                        "?x info:Genre ?genre .\n" +
                        "?x info:hasActor ?actors .\n" +
                        "?actors info:Name ?actorNames .\n" +
                        "?x info:hasDirector ?directors .\n" +
                        "?directors info:Name ?directorNames.\n" +
                        "FILTER(";
            while(individuals.hasNext()) {
                Individual singleIndividual = (Individual) individuals.next();
                for(int i = 0 ; i<included.size();i++){
                    if(included.get(i) == singleIndividual.getLocalName()){
                        if(singleIndividual.hasRDFType(base_uri+"actor")){
                            queryString += "?actorsNames = '"+included.get(i)+"'";
                        }
                        if(singleIndividual.hasRDFType(base_uri+"director")){
                            queryString +="?directorNames ='"+included.get(i)+"'";
                        }
                    }
                }
                for(int i = 0; i<excluded.size();i++){
                    if(excluded.get(i) == singleIndividual.getLocalName()){
                        if(singleIndividual.hasRDFType(base_uri+"actor")){
                            queryString += "?actorsNames != '"+excluded.get(i)+"'";
                        }
                        if(singleIndividual.hasRDFType(base_uri+"director")){
                            queryString +="?directorNames != '"+excluded.get(i)+"'";
                        }
                    }
                }
            }
            queryString += ")}";
            System.out.println(queryString);
            Query  query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query,ontModel);
            ResultSet res = qe.execSelect();
            if(res.hasNext()){
                while(res.hasNext()){
                    QuerySolution sol = res.nextSolution();
                    RDFNode movieName = sol.get("?movieName");
                    System.out.println(movieName);

                }
            }else{
                System.err.println("ERROR : movie not found");
            }
        } catch(JenaException je){
            System.err.println("ERROR" + je.getMessage());
            je.printStackTrace();
        }
    }
    public static ArrayList GUI_Exclude() throws IOException {
        ArrayList excluded = new ArrayList();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean stop = false;
        while (!stop) {
            System.out.println("Do you want to exclude actors/directors/genres ? Yes/No");
            String answer = br.readLine();
            switch (answer) {
                case "Yes":
                    boolean stop2 = false;
                    while (!stop2) {
                        System.out.println("Enter what you want to exclude");
                        String toExclude = br.readLine();
                        excluded.add(toExclude);
                        System.out.println("Do you want to exclude another one ?");
                        String answer2 = br.readLine();
                        switch (answer2) {
                            case "Yes":
                                break;
                            case "No":
                                stop2 = true;
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case "No":
                    stop = true;
                    break;
                default:
                    break;
            }
        }
        return excluded;
    }
    public static ArrayList GUI_Include() throws IOException {
        ArrayList included = new ArrayList();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean stop2 = false;
        while (!stop2) {
            System.out.println("Do you want to include actors/directors/genres ? Yes/No");
            String answer = br.readLine();
            switch (answer) {
                case "Yes":
                    boolean stop3 = false;
                    while (!stop3) {
                        System.out.println("Enter what you want to include");
                        String toInclude = br.readLine();
                        included.add(toInclude);
                        System.out.println("Do you want to include another one ?");
                        String answer2 = br.readLine();
                        switch (answer2) {
                            case "Yes":
                                break;
                            case "No":
                                stop3 = true;
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case "No":
                    stop2 = true;
                    break;
                default:
                    break;
            }
        }
        return included;
    }
    public static OntModel getOntologyModel(String ontoFile)
    {
        OntModel ontoModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
        try
        {
            InputStream in = FileManager.get().open(ontoFile);
            try
            {
                ontoModel.read(in, null);
                logger.info(ontoFile + " successfully loaded");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        catch (JenaException je)
        {
            System.err.println("ERROR" + je.getMessage());
            je.printStackTrace();
            System.exit(0);
        }
        return ontoModel;
    }
    public static void main(String[] args) throws IOException {
        OntModel ontModel = getOntologyModel("./data/data.owl");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean stop = false;
        while(!stop) {
            System.out.println("\n\nMENU - Choose a program to execute\n1)Jena1\n2)Jena2\n3)Jena3\n4)Jena4\n5)Jena5\n6)Jena6\n7)Console GUI\n8)Exit");
            String choice = br.readLine();
            switch (choice) {
                case "1":
                    Jena1.getIndividuals(ontModel);
                    break;
                case "2":
                    Jena2.queryPersons(ontModel);
                    break;
                case "3":
                    break;
                case "4":
                    System.out.println("Search for a movie :");
                    String movieName = br.readLine();
                    Jena4.movieSearch(movieName,ontModel);
                    break;
                case "5":
                    break;
                case "6":
                    break;
                case "7":
                    ArrayList included = GUI_Include();
                    ArrayList excluded = GUI_Exclude();
                    GUI_movieList(included,excluded,ontModel);
                    break;
                case "8":
                    stop = true;
                    break;
                default:
                    break;
            }
        }
    }
}
