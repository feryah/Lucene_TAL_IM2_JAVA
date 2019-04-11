package tests;

//test compatibilité eclipse/git

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;

public class Sandbox {

	public static void main(String[] args) throws IOException, ParseException {
		
        //fichiers à indexer
        String recettes = "/Users/milena/Documents/Travail/M2TAL/java/newindex";
         
        //index de référence
        //String index_recettes = "/Users/milena/Documents/Travail/M2TAL/java/newindex";
        
        //ouvrir index de référence et créer un searcher
        Directory dir = FSDirectory.open(Paths.get(recettes));
        
        IndexReader reader = DirectoryReader.open(dir);

        IndexSearcher searcher = new IndexSearcher(reader);
        
        //créer une requête
        QueryParser qp = new QueryParser("contents", new FrenchAnalyzer());
        Query query = qp.parse("SEICHE");
         
        //chercher dans index
        TopDocs hits = searcher.search(query, 10);
        
        System.out.println(hits.totalHits + " résultats");

        for (ScoreDoc doc : hits.scoreDocs) 
        {
            Document d = searcher.doc(doc.doc);
            System.out.println("Path : "+ d.get("path") + ", Score : " + doc.score);
        }

	}

}
