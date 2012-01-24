package gate.creole.infertagger;

import gate.Document;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.infertagger.testdata.AnnoTestBuilder;

import org.junit.Before;
import org.junit.Test;

public class TestJapeStyleRules {
	
	private Document doc;

	@Test
	public void testOrder() {
		System.out.println(doc);
	}
	
	@Before
	public void buildKb() throws ExecutionException, ResourceInstantiationException {
		AnnoTestBuilder testBuilder = new AnnoTestBuilder();
		doc = testBuilder.startSentence().
						addWord("AC").addWord("Milan").addWord("player").
						addWord("David", "NNP").addWord("Beckham", "NNP").addWord("is").
						addWord("english").endSentence().build();
		InferTaggerPR semTagger = new InferTaggerPR();
		semTagger.setDocument(doc);
		semTagger.setRuleSet(TestRuleModelUsageDirect.class.getClassLoader().
				getResource("rules/jape.xml"));
		semTagger.init();
		semTagger.execute();
		doc = semTagger.getDocument();
	}

}
