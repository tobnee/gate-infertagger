package gate.creole.infertagger;

import static org.junit.Assert.assertEquals;
import gate.AnnotationSet;
import gate.Document;
import gate.FeatureMap;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.infertagger.testdata.AnnoTestBuilder;

import org.junit.Before;
import org.junit.Test;

public class TestJapeStyleRules {
	
	private Document doc;

	@Test
	public void testOrder() {
		AnnotationSet annotations = doc.getAnnotations("MarkRel");
		assertEquals(1, annotations.size());
		FeatureMap features = annotations.iterator().next().getFeatures();
		assertEquals("David", features.get("name"));
		assertEquals("Beckham", features.get("surname"));
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
