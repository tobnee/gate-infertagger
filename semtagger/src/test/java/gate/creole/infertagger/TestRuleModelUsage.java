package gate.creole.infertagger;

import org.junit.Test;

import gate.Document;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.infertagger.testdata.TestDataUtil;

import static org.junit.Assert.*;

public class TestRuleModelUsage {

	@Test
	public void testMarkerCreation() throws ResourceInstantiationException, ExecutionException {
		InferTaggerPR semTagger = new InferTaggerPR();
		semTagger.setDocument(TestDataUtil.getTestCourpus());
		semTagger.setRuleSet(TestRuleModelUsage.class.getClassLoader().
				getResource("rules/defaulChangeSet.xml"));
		semTagger.init();
		semTagger.execute();
		Document doc = semTagger.getDocument();
		assertEquals(1, doc.getAnnotations("Mark").size());
	}
}
