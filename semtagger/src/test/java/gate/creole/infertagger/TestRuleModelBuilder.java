package gate.creole.infertagger;

import static org.junit.Assert.*;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.annotation.AnnotationImpl;
import gate.annotation.AnnotationSetImpl;
import gate.annotation.DefaultAnnotationFactory;
import gate.annotation.NodeImpl;
import gate.corpora.DocumentImpl;
import gate.creole.infertagger.rulemodel.RuleModelBuilder;
import gate.creole.infertagger.rulemodel.Word;

import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class TestRuleModelBuilder {
	@Test
	public void testRuleModelBuilder() {
		RuleModelBuilder rmb = new RuleModelBuilder(annoSet);
		List<Object> d = rmb.build();
		assertEquals(d.size(), 4);
		boolean foundWord = false;
		for(Object o : d) {
			foundWord = o instanceof Word;
			if(foundWord) break;
		}
		assertTrue(foundWord);
	}
	
	private DefaultAnnotationFactory annoFac;
	private AnnotationSet annoSet;
	private AnnotationImpl annoSen;
	private AnnotationImpl annoTok;
	private AnnotationImpl annoTok2;
	private AnnotationImpl annoLook;

	@Before
	public void before() {
		Document document = new DocumentImpl();
		annoSet = new AnnotationSetImpl(document);
		annoFac = new DefaultAnnotationFactory();
		FeatureMap t1Fm = Factory.newFeatureMap();
		annoSen = (AnnotationImpl) annoFac.createAnnotationInSet(annoSet, 9,
				new NodeImpl(0, 0l), new NodeImpl(9, 25l), "Sentence", t1Fm);

		FeatureMap t1Tok = Factory.newFeatureMap();
		t1Tok.put("string", "Token");
		t1Tok.put("root", "token");
		t1Tok.put("category", "NN");
		t1Tok.put("orth", "upperInitial");
		t1Tok.put("kind", "word");

		annoTok = (AnnotationImpl) annoFac.createAnnotationInSet(annoSet, 10,
				new NodeImpl(0, 0l), new NodeImpl(1, 6l), "Token", t1Tok);

		FeatureMap t2Tok = Factory.newFeatureMap();
		t2Tok.put("string", "super");
		t2Tok.put("root", "super");
		t2Tok.put("category", "NN");
		t2Tok.put("kind", "word");
		annoTok2 = (AnnotationImpl) annoFac.createAnnotationInSet(annoSet, 11,
				new NodeImpl(0, 7l), new NodeImpl(1, 12l), "Token", t2Tok);

		FeatureMap tok = Factory.newFeatureMap();
		annoLook = (AnnotationImpl) annoFac.createAnnotationInSet(annoSet, 12,
				new NodeImpl(0, 7l), new NodeImpl(1, 12l), "Lookup", tok);
	}
}
