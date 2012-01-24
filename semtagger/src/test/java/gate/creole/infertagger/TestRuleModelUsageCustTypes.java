package gate.creole.infertagger;

import static org.junit.Assert.assertEquals;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.infertagger.testdata.TestDataUtil;

import org.junit.Before;
import org.junit.Test;

public class TestRuleModelUsageCustTypes {
	private Document doc;

	@Test
	public void testMarkerCreation() {
		AnnotationSet annotations = doc.getAnnotations("Mark");
		assertEquals(1, annotations.size());
		assertEquals("good", annotations.iterator().next().getFeatures().get("feature"));
	}
	
	@Test
	public void testMarkerRelCreation() {
		AnnotationSet annotations = doc.getAnnotations("MarkRel");
		assertEquals(1, annotations.size());
		Annotation annotation = annotations.iterator().next();
		assertEquals("good2", annotation.getFeatures().get("feature"));
		assertEquals(0L, annotation.getStartNode().getOffset().longValue());
		assertEquals(12L, annotation.getEndNode().getOffset().longValue());
	}
	
	
	@Before
	public void buildKb() throws ExecutionException, ResourceInstantiationException {
		InferTaggerPR semTagger = new InferTaggerPR();
		semTagger.setDocument(TestDataUtil.getTestCourpus());
		semTagger.setRuleSet(TestRuleModelUsageDirect.class.getClassLoader().
				getResource("rules/custTypeMarker.xml"));
		semTagger.init();
		semTagger.execute();
		doc = semTagger.getDocument();
	}
}
