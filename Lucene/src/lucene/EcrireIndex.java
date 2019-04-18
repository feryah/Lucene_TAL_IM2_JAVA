package lucene;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class EcrireIndex {

	public static void main(String[] args) throws IOException {
		
        //répertoire des fichiers à indexer
        String docRecettes = args[0];
        System.out.println("Dossier à indexer : " + args[0]);
         
        //répertoire qui contiendra l'index lucene

        String indexRecettes = args[1];
        System.out.println("Chemin de l'index Lucene : " + args[1]);

        //chemin vers l'index
        final Path docChemin = Paths.get(docRecettes);
		DirectoryStream<Path> docStream = Files.newDirectoryStream(docChemin);
        
        final Path indexChemin = Paths.get(indexRecettes);
        Directory indexDir = FSDirectory.open(indexChemin);
        
        System.out.println("Indexation en cours");
        
        //analyseur
        Analyzer analyseur = new FrenchAnalyzer();
        System.out.println("Analyseur Lucene : FrenchAnalyzer");
        
        //configurer l'indexWriter qui va indexer les fichiers
        IndexWriterConfig indexConfig = new IndexWriterConfig(analyseur);
        indexConfig.setOpenMode(OpenMode.CREATE);
         
        IndexWriter writer = new IndexWriter(indexDir, indexConfig);
        
        for (Path fichier : docStream) {
        		System.out.println("Fichier indexé : " + fichier.getFileName().toString());
        		indexDoc(writer, fichier);
        }
        
        System.out.println("Fermeture de l'index en cours");
        writer.close();
                
	}
	
	static void indexDoc(IndexWriter w, Path f) throws IOException {
		
		//créer document Lucene
		Document doc = new Document();
		
		//ajouter champs
		doc.add(new StringField("path", f.toString(), Store.YES));
		doc.add(new StringField("filename", f.getFileName().toString(), Store.YES));
		String fileContent = new String(Files.readAllBytes(f));
		doc.add(new TextField("contents", fileContent, Store.YES));
		
        w.addDocument(doc);
	}
}
