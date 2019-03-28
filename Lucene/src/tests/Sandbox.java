package tests;

//test compatibilité eclipse/git

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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

public class Sandbox {

	public static void main(String[] args) throws IOException {
		
		// directory where your index is stored
		Path path = Paths.get("/Users/milena/Documents/Travail/M2TAL/java/newindex");

		Directory index = FSDirectory.open(path);
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		
		
		Term t = new Term("contents", "chocolat");

		// Get the top 10 docs
		Query query = new TermQuery(t);
		TopDocs tops= searcher.search(query, 10);
		ScoreDoc[] scoreDoc = tops.scoreDocs;
		System.out.println(scoreDoc.length); 
		for (ScoreDoc score : scoreDoc){
		    System.out.println("DOC " + score.doc + " SCORE " + score.score);
		}

		// Get the frequency of the term
		int freq = reader.docFreq(t);
		System.out.println("FREQ " + freq);
	}

}
