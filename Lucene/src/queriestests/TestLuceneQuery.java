package queriestests;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
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
	public void unTest() throws IOException {


		//term query
		Term t = new Term("contents", "abricoter"); 
		Query query = new TermQuery(t); 

		
		
		TopDocs tops= searcher.search(query, 3);
		ScoreDoc[] scoreDoc = tops.scoreDocs;
		System.out.println("************début de la première requête*************"+"\n"+"\n");
		System.out.println("Cette requête se trouve dans " +scoreDoc.length+" document(s)"+"\n"); 
		for (ScoreDoc score : scoreDoc){
		    System.out.println("Id DOC " + score.doc + " SCORE " + score.score+ "\n"+"\n");
		    Document d = searcher.doc(score.doc);
		    System.out.println("Le chemin vers le document: "+d.get("path") + d.get("contents")+"\n"+"\n");//
		    System.out.println("Le chemin vers le document: "+d.get("path"));
		}

		// Get the frequency of the term
		int freq = reader.docFreq(t);
		
		System.out.println("La FREQ de la requête : " + freq+"\n"+"\n");
		System.out.println("************fin de la première requête*************"+"\n"+"\n");
}	
	

	
	@Test
	public void DeuxTest() throws IOException {
		//2)phrase query : il faut que les mots chercher soient vraiment l'un à côté de l'autre dans la phrase. Aussi, il n'accepte pas les majuscules en début du mot
		PhraseQuery.Builder phraseQuery = new PhraseQuery.Builder();
		
		phraseQuery.add(new Term("contents", "cuire"), 0);
		phraseQuery.add(new Term("contents", "à"), 1);
		phraseQuery.add(new Term("contents", "l'anglaise"), 2);
		//phraseQuery.add(new Term("contents", "beurre"), 3);
		PhraseQuery query = phraseQuery.build();
		
		TopDocs tops= searcher.search(query, 3);
		ScoreDoc[] scoreDoc = tops.scoreDocs;
		System.out.println("************début de la deuxième requête*************"+"\n"+"\n");
		System.out.println("Cette requête se trouve dans " +scoreDoc.length+" document(s)"+"\n"); 
		for (ScoreDoc score : scoreDoc){
		    System.out.println("Id DOC " + score.doc + " SCORE " + score.score+ "\n"+"\n");
		    Document d = searcher.doc(score.doc);
		    System.out.println("Le chemin vers le document: "+d.get("path") + d.get("contents")+"\n"+"\n");//
		    System.out.println("Le chemin vers le document: "+d.get("path")+"\n"+"\n");
		}

		System.out.println("************fin de la deuxième requête*************"+"\n"+"\n");
	}
	
	@Test
	public void TroisTest() throws IOException {
		//3)boolean query
		
		
		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
		booleanQuery.add(new FuzzyQuery(new Term("contents", "dégraisser~")), BooleanClause.Occur.MUST);
		
		//booleanQuery.add(new TermQuery(new Term("contents", "carotte")), BooleanClause.Occur.MUST);
		//booleanQuery.add(new WildcardQuery(new Term("contents", "dég*")), BooleanClause.Occur.MUST);
		//booleanQuery.add(new RegexpQuery(new Term("contents","s[a|e]+l[é|er|ée|ez]*")), BooleanClause.Occur.MUST_NOT);
		
		BooleanQuery query = booleanQuery.build();
		TopDocs tops= searcher.search(query, 3);
		ScoreDoc[] scoreDoc = tops.scoreDocs;
		System.out.println("************début de la troisième requête*************"+"\n"+"\n");
		System.out.println("Cette requête se trouve dans " +scoreDoc.length+" document(s)"+"\n"); 
		for (ScoreDoc score : scoreDoc){
		    System.out.println("Id DOC " + score.doc + " SCORE " + score.score+ "\n"+"\n");
		    Document d = searcher.doc(score.doc);
		    System.out.println("Le chemin vers le document: "+d.get("path") + d.get("contents")+"\n"+"\n");//
		    System.out.println("Le chemin vers le document: "+d.get("path")+"\n"+"\n");
		}

		System.out.println("************fin de la troisième requête*************"+"\n"+"\n");	
		
	}
	
	@Test
	public void QuatreTest() throws IOException {
		//PrefixeQuery
		Term term = new Term("contents", "cui");
	    Query query = new PrefixQuery(term);
	    
	    TopDocs tops= searcher.search(query, 3);
		ScoreDoc[] scoreDoc = tops.scoreDocs;
		System.out.println("************début de la quatrième requête*************"+"\n"+"\n");
		System.out.println("Cette requête se trouve dans " +scoreDoc.length+" document(s)"+"\n"); 
		for (ScoreDoc score : scoreDoc){
		    System.out.println("Id DOC " + score.doc + " SCORE " + score.score+ "\n"+"\n");
		    Document d = searcher.doc(score.doc);
		    System.out.println("Le chemin vers le document: "+d.get("path") + d.get("contents")+"\n"+"\n");//
		    System.out.println("Le chemin vers le document: "+d.get("path"));
		}

		// Get the frequency of the term
		int freq = reader.docFreq(term);
		
		System.out.println("La FREQ de la requête : " + freq+"\n"+"\n");
		System.out.println("************fin de la quatrième requête*************"+"\n"+"\n");
}	
	}
	
	
	



