package gate.creole.semtagger;

import static org.junit.Assert.*;
import gate.creole.semtagger.rulemodel.Sentence;
import gate.creole.semtagger.rulemodel.Token;

import org.junit.Test;

public class TestRuleModel {
	@Test
	public void testToken() {
		Token token = new Token("Tokens", "token", "NN");
		assertNotNull(token);
	}
	@Test
	public void testSentence() {
		Sentence sentence = new Sentence();
		
		assertNotNull(sentence);
		
	}
}
