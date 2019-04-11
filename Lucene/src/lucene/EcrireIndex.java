package lucene;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class EcrireIndex {

	public static void main(String[] args) throws IOException {
		
        //répertoire des fichiers à indexer
        String recettes = "/Users/milena/Documents/Travail/M2TAL/java/recettes-utf-8";
         
        //répertoire qui contiendra l'index lucene
        String indexRecettes = "/Users/milena/Documents/Travail/M2TAL/java/index-recettes";
 
        //chemin vers l'index
        final Path indexChemin = Paths.get(recettes);
        Directory dir = FSDirectory.open(indexChemin);
        
        //analyseur
        Analyzer analyseur = new FrenchAnalyzer();
         
        //configurer l'indexWriter qui va indexer les fichiers
        IndexWriterConfig indexConfig = new IndexWriterConfig(analyseur);
        indexConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
         
        IndexWriter writer = new IndexWriter(dir, indexConfig);

	}
}
