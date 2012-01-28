package gate.creole.infertagger;

import org.junit.Before;
import org.junit.Test;

import gate.AnnotationSet;
import gate.Document;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.infertagger.testdata.TestDataUtil;

import static org.junit.Assert.*;

public class TestRuleModelUsageDirect {

	private Document doc;

	@Test
	public void testMarkerCreation() {
		AnnotationSet annotations = doc.getAnnotations().get("Mark");
		System.out.println(doc);
		assertEquals(1, annotations.size());
		assertEquals("good", annotations.iterator().next().getFeatures().get("feature"));
	}
	
	@Before
	public void buildKb() throws ExecutionException, ResourceInstantiationException {
		InferTaggerPR semTagger = new InferTaggerPR();
		semTagger.setDocument(TestDataUtil.getTestCourpus());
		semTagger.setRuleSet(TestRuleModelUsageDirect.class.getClassLoader().
				getResource("rules/directMarker.xml"));
		semTagger.init();
		semTagger.execute();
		doc = semTagger.getDocument();
	}
}
