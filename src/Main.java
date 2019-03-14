import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.JenaException;
import org.apache.jena.util.FileManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
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
            System.out.println("\n\nMENU - Choose a program to execute\n1)Jena1\n2)Jena2\n3)Jena3\n4)Jena4\n5)Jena5\n6)Jena6\n7)Exit");
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
                    stop = true;
                    break;
                default:
                    break;
            }
        }
    }
}
