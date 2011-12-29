package gate.creole.infertagger;

import org.junit.Before;
import org.junit.Test;

import gate.AnnotationSet;
import gate.Document;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.infertagger.testdata.TestDataUtil;

import static org.junit.Assert.*;

public class TestRuleModelUsage {

	private Document doc;

	@Test
	public void testMarkerCreation() {
		AnnotationSet annotations = doc.getAnnotations("Mark");
		assertEquals(1, annotations.size());
		assertEquals("good", annotations.iterator().next().getFeatures().get("feature"));
	}
	
//	@Test
//	public void testRelationCreation() {
//		assertEquals(1, doc.getAnnotations("Mark").size());
//	}
	
	@Before
	public void buildKb() throws ExecutionException, ResourceInstantiationException {
		InferTaggerPR semTagger = new InferTaggerPR();
		semTagger.setDocument(TestDataUtil.getTestCourpus());
		semTagger.setRuleSet(TestRuleModelUsage.class.getClassLoader().
				getResource("rules/defaulChangeSet.xml"));
		semTagger.init();
		semTagger.execute();
		doc = semTagger.getDocument();
	}
}
