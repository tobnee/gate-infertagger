package gate.creole.infertagger;

import static org.junit.Assert.*;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.annotation.AnnotationImpl;
import gate.annotation.AnnotationSetImpl;
import gate.annotation.DefaultAnnotationFactory;
import gate.annotation.NodeImpl;
import gate.corpora.DocumentImpl;
import gate.creole.infertagger.rulemodel.Lookup;
import gate.creole.infertagger.rulemodel.RuleModelFactory;
import gate.creole.infertagger.rulemodel.Sentence;
import gate.creole.infertagger.rulemodel.Token;
import gate.creole.infertagger.rulemodel.Word;

import org.junit.Before;
import org.junit.Test;

public class TestRuleModel {
	
	@Test
	public void testToken() {
		Token t1 = RuleModelFactory.fromToken((AnnotationImpl) annoTok);
		assertNotNull(t1);
		
		assertEquals(0, t1.getStartOffset());
		assertEquals(6, t1.getEndOffset());
		assertEquals("Token", t1.getString());
		assertEquals("token", t1.getRoot());
		assertEquals("NN", t1.getCategory());
		assertEquals("upperInitial", t1.getFeature("orth"));
		
		assertEquals("", t1.getFeature("noneExsisting"));
		assertEquals("default", t1.getFeatureOrDefault("noneExsisting", "default"));

		Word w1 = RuleModelFactory.fromWordToken((AnnotationImpl) annoTok);
		Token w2 = RuleModelFactory.fromWordToken((AnnotationImpl) annoTok2);
		assertTrue(w2.after(w1));
		assertTrue(w1.before(w2));
		
		assertNotNull(w1);
	}
	
	@Test
	public void testSentence() {
		Token t1 = RuleModelFactory.fromToken((AnnotationImpl) annoTok);
		Sentence sentence = RuleModelFactory.fromSentence((AnnotationImpl) annoSen);
		assertNotNull(sentence);
		assertTrue(sentence.overlaps(t1));
		assertTrue(t1.overlaps(sentence));
		assertTrue(sentence.contains(t1));
		assertTrue(t1.within(sentence));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongTypeException() {
		RuleModelFactory.fromToken((AnnotationImpl) annoSen);
	}
	
	@Test
	public void testLookup() {
		Sentence sen = RuleModelFactory.fromSentence((AnnotationImpl) annoSen);
		Lookup look = RuleModelFactory.fromLookup((AnnotationImpl) annoLook);
		assertNotNull(look);
		look.within(sen);
	}
	
	private DefaultAnnotationFactory annoFac;
	private AnnotationSet annoSet;
	private Annotation annoSen;
	private Annotation annoTok;
	private Annotation annoTok2;
	private Annotation annoLook;
	
	@Before
	public void before() {
		Document document = new DocumentImpl();
		annoSet = new AnnotationSetImpl(document);
		annoFac = new DefaultAnnotationFactory();
		FeatureMap t1Fm = Factory.newFeatureMap();
		annoSen = annoFac.createAnnotationInSet(annoSet, 9, new NodeImpl(0, 0l), new NodeImpl(9, 25l), 
			"Sentence", t1Fm);

		FeatureMap t1Tok = Factory.newFeatureMap();
		t1Tok.put("string", "Token");
		t1Tok.put("root", "token");
		t1Tok.put("category", "NN");
		t1Tok.put("orth", "upperInitial");
		t1Tok.put("kind", "word");

		annoTok = annoFac.createAnnotationInSet(annoSet, 0, new NodeImpl(0, 0l), new NodeImpl(1, 6l), 
			"Token", t1Tok);
		
		FeatureMap t2Tok = Factory.newFeatureMap();
		t2Tok.put("string", "super");
		t2Tok.put("root", "super");
		t2Tok.put("category", "NN");
		t2Tok.put("kind", "word");		
		annoTok2 = annoFac.createAnnotationInSet(annoSet, 0, new NodeImpl(0, 7l), new NodeImpl(1, 12l), 
				"Token", t2Tok);
		
		FeatureMap tok = Factory.newFeatureMap();
		annoLook = annoFac.createAnnotationInSet(annoSet, 0, new NodeImpl(0, 7l), new NodeImpl(1, 12l), 
				"Lookup", tok);
	}
}
