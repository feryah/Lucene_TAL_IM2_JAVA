package queriestests;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestLuceneQuery {

	Directory index;
	IndexReader reader;
	IndexSearcher searcher;
	Path path;

	/**
	 * un test 
	 * @throws IOException 
	 */

	@Before 
	public void OuvrePath() throws IOException {
		path = Paths.get("/Users/ferialyahiaoui/Documents/cours/S2/JAVA/IndexRecettes");
		index = FSDirectory.open(path);
		reader = DirectoryReader.open(index);
		searcher = new IndexSearcher(reader);
	}

	@Test
	public void unTest() throws IOException{


		//term query
		Term t = new Term("contents", "sucre"); //
		Query query = new TermQuery(t); 

		//TopDocs tops= searcher.search(query, 10); //
		TopDocs tops= searcher.search(query, 2);
		ScoreDoc[] scoreDoc = tops.scoreDocs;
		System.out.println("Cette requête se trouve dans " +scoreDoc.length+" document(s)"+"\n"); 
		for (ScoreDoc score : scoreDoc){
			System.out.println("Id DOC " + score.doc + " SCORE " + score.score+ "\n"+"\n");
			Document d = searcher.doc(score.doc);
			System.out.println("Le chemin vers le document: "+d.get("filename") + d.get("contents")+"\n"+"\n");//
			System.out.println("Le chemin vers le document: "+d.get("filename"));
		}

		// Get the frequency of the term
		int freq = reader.docFreq(t);//

		System.out.println("La FREQ de la requête : " + freq);//

	}

}

