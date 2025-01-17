package com.marklogic.jena.examples;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import com.marklogic.semantics.jena.MarkLogicDatasetGraph;

/**
 * How to run queries.
 *
 */
public class SPARQLUpdateExample {
    
    private MarkLogicDatasetGraph dsg;

    public SPARQLUpdateExample() {
        dsg = ExampleUtils.loadPropsAndInit();
    }

    private void run() {
        dsg.clear();
        
        String insertData = "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
                + "PREFIX : <http://example.org/> "
                +"INSERT DATA {GRAPH :g1 {"
                + ":charles a foaf:Person ; "
                + "        foaf:name \"Charles\" ;"
                + "        foaf:knows :jim ."
                + ":jim    a foaf:Person ;"
                + "        foaf:name \"Jim\" ;"
                + "        foaf:knows :charles ."
                + "} }";
        
        System.out.println("Running SPARQL update");
        
        UpdateRequest update = UpdateFactory.create(insertData);
        UpdateProcessor processor = UpdateExecutionFactory.create(update, dsg);
        processor.execute();
        
        System.out.println("Examine the data as JSON-LD");
        RDFDataMgr.write(System.out, dsg.getGraph(NodeFactory.createURI("http://example.org/g1")), RDFFormat.JSONLD_PRETTY);
        
        System.out.println("Remove it.");
        
        update = UpdateFactory.create("PREFIX : <http://example.org/> DROP GRAPH :g1");
        processor = UpdateExecutionFactory.create(update, dsg);
        processor.execute();
        dsg.close();
    }

    public static void main(String... args) {
        SPARQLUpdateExample example = new SPARQLUpdateExample();
        example.run();
     }

}
