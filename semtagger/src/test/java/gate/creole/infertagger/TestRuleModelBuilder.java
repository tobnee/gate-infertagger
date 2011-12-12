package gate.creole.infertagger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gate.creole.infertagger.rulemodel.RuleModelBuilder;
import gate.creole.infertagger.rulemodel.Word;
import gate.creole.infertagger.testdata.TestDataUtil;

import java.util.List;

import org.junit.Test;


public class TestRuleModelBuilder {
	@Test
	public void testRuleModelBuilder() {
		RuleModelBuilder rmb = new RuleModelBuilder(TestDataUtil.getTestCourpus().getAnnotations());
		List<Object> d = rmb.build();
		assertEquals(d.size(), 4);
		boolean foundWord = false;
		for(Object o : d) {
			foundWord = o instanceof Word;
			if(foundWord) break;
		}
		assertTrue(foundWord);
	}
}
