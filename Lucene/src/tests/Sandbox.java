package tests;

//test compatibilité eclipse/git

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
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

public class Sandbox {

	public static void main(String[] args) throws IOException {
		
		// directory where your index is stored
		Path path = Paths.get("/Users/ferialyahiaoui/Documents/cours/S2/JAVA/IndexRecettes");

		Directory index = FSDirectory.open(path);
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		
		
		
		
		
		//term query
		//Term t = new Term("contents", "sucre"); //
		
		//2)phrase query : il faut que les mots chercher soient vraiment l'un à côté de l'autre dans la phrase. Aussi, il n'accepte pas les majuscules en début du mot
		//PhraseQuery.Builder phraseQuery = new PhraseQuery.Builder();//
		//phraseQuery.add(new Term("contents", "oignon"), 0);
		//phraseQuery.add(new Term("contents", "échalote"), 1);//
		//phraseQuery.add(new Term("contents", "ciboulette"), 2);//
		//phraseQuery.add(new Term("contents", "câpres"), 3);//
		//PhraseQuery query = phraseQuery.build();//
	
		
		//3)boolean query
		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
		booleanQuery.add(new FuzzyQuery(new Term("contents", "chs")), BooleanClause.Occur.MUST);
		
		
		
		
		
		//booleanQuery.add(new TermQuery(new Term("contents", "carotte")), BooleanClause.Occur.MUST);
		//booleanQuery.add(new WildcardQuery(new Term("contents", "poivre?")), BooleanClause.Occur.MUST);
		//booleanQuery.add(new RegexpQuery(new Term("contents","s[a|e]+l[é|er|ée|ez]*")), BooleanClause.Occur.MUST_NOT);
		
		BooleanQuery query = booleanQuery.build();
		
		//4)wildcardQuery
		//booleanQuery.add(new WildcardQuery(new Term("contents", "poivre?")), BooleanClause.Occur.MUST);
		
		//5)RegexQuery
		//regexq.add(new RegexpQuery(new Term("contents","n[a-z]+")));
		
		//6)TermRangeQuery
		
		
		//7)FuzzyQuery
		
		
		
		
		
		
		
		


		// Get the top 10 docs
		//Query query = new TermQuery(t); //
		
		//Query query = new TermQuery(t); //
		//query.add(new Term ("contents", word));
	
		
		//TopDocs tops= searcher.search(query, 10); //
		TopDocs tops= searcher.search(query, 10);
		ScoreDoc[] scoreDoc = tops.scoreDocs;
		System.out.println("Cette requête se trouve dans " +scoreDoc.length+" document(s)"+"\n"); 
		for (ScoreDoc score : scoreDoc){
		    System.out.println("Id DOC " + score.doc + " SCORE " + score.score+ "\n"+"\n");
		    Document d = searcher.doc(score.doc);
		    System.out.println("Le chemin vers le document: "+d.get("path") + d.get("contents")+"\n"+"\n");//
		    //System.out.println("Le chemin vers le document: "+d.get("path"));
		}

		// Get the frequency of the term
		//int freq = reader.docFreq(t);//
		
		//System.out.println("La FREQ de la requête : " + freq);//
		
	}

}
