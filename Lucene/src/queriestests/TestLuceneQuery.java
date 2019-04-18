package queriestests;
import static org.junit.Assert.assertEquals;

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
	 *
	 */
	// afin de ne pas ouvrir l'index à chaque query, on l'ouvre une fois avant d'effectuer toutes les requêtes.
	@Before 
	public void OuvrePath() throws IOException {
		path = Paths.get("/Users/milena/Documents/Travail/M2TAL/java/index-recettes");
		index = FSDirectory.open(path);
		reader = DirectoryReader.open(index);
		searcher = new IndexSearcher(reader);
	}

	@Test
	public void unTest() throws IOException {


		//1) Ce premier test est effectué sur une term query
		Term t = new Term("contents", "aigre"); 
		Query query = new TermQuery(t); 

		
		
		TopDocs tops= searcher.search(query, 10);
		ScoreDoc[] scoreDoc = tops.scoreDocs;
		System.out.println("************début de la première requête*************"+"\n"+"\n");
		System.out.println("Cette requête se trouve dans " +scoreDoc.length+" document(s)"+"\n"); 
		for (ScoreDoc score : scoreDoc){
		    System.out.println("Id DOC " + score.doc + " SCORE " + score.score+ "\n"+"\n");
		    Document d = searcher.doc(score.doc);
		    System.out.println("Le chemin vers le document: "+d.get("path")+"\n"+"\n");
		    System.out.println("Le contenu du document: " + d.get("contents")+ "\n"+"\n");
		}

		// donne la fréquence du terme
		int freq = reader.docFreq(t);
		
		// doit renvoyer exactement 4 fréquences car ce terme n'apparaît que 4 fois dans tout le corpus.
		assertEquals(4,freq);
		
		System.out.println("La FREQ de la requête : " + freq+"\n"+"\n");
		System.out.println("************fin de la première requête*************"+"\n"+"\n");
}	
	

	
	@Test
	public void DeuxTest() throws IOException {
		//2)phrase query : il faut que les mots cherchés soient vraiment l'un à côté de l'autre dans la phrase. Aussi, il n'accepte pas les majuscules en début du mot
		PhraseQuery.Builder phraseQuery = new PhraseQuery.Builder();
		
		phraseQuery.add(new Term("contents", "cuire"), 0);
		phraseQuery.add(new Term("contents", "à"), 1);
		phraseQuery.add(new Term("contents", "l'anglaise"), 2);
		PhraseQuery query = phraseQuery.build();
		
		TopDocs tops= searcher.search(query, 10);
		ScoreDoc[] scoreDoc = tops.scoreDocs;
		System.out.println("************début de la deuxième requête*************"+"\n"+"\n");
		System.out.println("Cette requête se trouve dans " +scoreDoc.length+" document(s)"+"\n"); 
		//doit renvoyer exactement 2 documents
		assertEquals(2,scoreDoc.length);
		for (ScoreDoc score : scoreDoc){
		    System.out.println("Id DOC " + score.doc + " SCORE " + score.score+ "\n"+"\n");
		    Document d = searcher.doc(score.doc);
		    System.out.println("Le chemin vers le document: "+d.get("path")+"\n"+"\n");//
		    System.out.println("Le contenu du document: " + d.get("contents")+ "\n"+"\n");
		}

		System.out.println("************fin de la deuxième requête*************"+"\n"+"\n");
	}
	
	@Test
	public void TroisTest() throws IOException {
		//3) Ce troisième test est effectué sur une boolean query
		// cette requête se combine avec d'autres types de queries notamment : Fuzzy, Wildcard et Regexp queries.
		// dans cet exemple, nous voulons un résultat avec un terme similaire à hacher, un autre terme commençant par dég (ex : dégraisser)
		// la présence de ces deux mots est obligatoire, tandis que l'absence du dernier est obligatoire. Nous ne voulons aucun mot se rapportant à "sel"
		
		
		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
		booleanQuery.add(new FuzzyQuery(new Term("contents", "hacher~")), BooleanClause.Occur.MUST);
		booleanQuery.add(new WildcardQuery(new Term("contents", "dég*")), BooleanClause.Occur.MUST);
		booleanQuery.add(new RegexpQuery(new Term("contents","s[a|e]+l[é|er|ée|ez]*")), BooleanClause.Occur.MUST_NOT);
		
		BooleanQuery query = booleanQuery.build();
		TopDocs tops= searcher.search(query, 3);
		ScoreDoc[] scoreDoc = tops.scoreDocs;
		System.out.println("************début de la troisième requête*************"+"\n"+"\n");
		System.out.println("Cette requête se trouve dans " +scoreDoc.length+" document(s)"+"\n");
		// doit renvoyer exactement 2 documents. Si l'on change de nombre, le test va échouer.
		assertEquals(2,scoreDoc.length);
		for (ScoreDoc score : scoreDoc){
		    System.out.println("Id DOC " + score.doc + " SCORE " + score.score+ "\n"+"\n");
		    Document d = searcher.doc(score.doc);
		    System.out.println("Le chemin vers le document: "+d.get("path")+"\n"+"\n");//
		    System.out.println("Le contenu du document: " + d.get("contents")+ "\n"+"\n");
		}

		System.out.println("************fin de la troisième requête*************"+"\n"+"\n");	
		
	}
	
	@Test
	public void QuatreTest() throws IOException {
		//4) ce test est effectué  sur une PrefixeQuery
		// il doit renvoyer des résultats avec un préfixe "saint" donc saint-honoré, saint-jacques, etc
		Term term = new Term("contents", "saint");
	    Query query = new PrefixQuery(term);
	    
	    TopDocs tops= searcher.search(query, 10);
		ScoreDoc[] scoreDoc = tops.scoreDocs;
		System.out.println("************début de la quatrième requête*************"+"\n"+"\n");
		System.out.println("Cette requête se trouve dans " +scoreDoc.length+" document(s)"+"\n"); 
		// il doit renvoyer exactement 7 documents pour cette requête.
		assertEquals(7,scoreDoc.length);
		for (ScoreDoc score : scoreDoc){
		    System.out.println("Id DOC " + score.doc + " SCORE " + score.score+ "\n"+"\n");
		    Document d = searcher.doc(score.doc);
		    System.out.println("Le chemin vers le document: "+d.get("path")+"\n"+"\n");
		    System.out.println("Le contenu du document: " + d.get("contents")+ "\n"+"\n");
		}

		System.out.println("************fin de la quatrième requête*************"+"\n"+"\n");
}	
	@Test
	public void CinqTest() throws IOException {


		//1) on effectue un test sur le champ filename
		Term t = new Term("filename", "SAINT-HONORE-utf-8.txt"); 
		Query query = new TermQuery(t); 

		
		
		TopDocs tops= searcher.search(query, 10);
		ScoreDoc[] scoreDoc = tops.scoreDocs;
		System.out.println("************début de la cinquième requête*************"+"\n"+"\n");
		System.out.println("Cette requête se trouve dans " +scoreDoc.length+" document(s)"+"\n"); 
		for (ScoreDoc score : scoreDoc){
		    System.out.println("Id DOC " + score.doc + " SCORE " + score.score+ "\n"+"\n");
		    Document d = searcher.doc(score.doc);
		    System.out.println("Le chemin vers le document: "+d.get("path")+"\n"+"\n");
		    System.out.println("Le contenu du document: " + d.get("contents")+ "\n"+"\n");
		}

		// donne la fréquence du terme
		int freq = reader.docFreq(t);
		
		// doit renvoyer exactement 1 fréquence (un seul document porte ce titre)
		assertEquals(1,freq);
		
		System.out.println("La FREQ de la requête : " + freq+"\n"+"\n");
		System.out.println("************fin de la cinquième requête*************"+"\n"+"\n");
}	
	}
	
	
	




