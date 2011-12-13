package gate.creole.infertagger;

import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.infertagger.testdata.TestDataUtil;

import org.junit.Test;

public class TestRuleModelUsage {

	@Test
	public void testDefault() throws ResourceInstantiationException, ExecutionException {
		InferTaggerPR semTagger = new InferTaggerPR();
		semTagger.setDocument(TestDataUtil.getTestCourpus());
		semTagger.setRuleSet(TestRuleModelUsage.class.getClassLoader().
				getResource("rules/defaulChangeSet.xml"));
		semTagger.init();
		semTagger.execute();
	}
}
