package gate.creole.semtagger;

import static org.junit.Assert.*;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.annotation.AnnotationSetImpl;
import gate.annotation.DefaultAnnotationFactory;
import gate.annotation.NodeImpl;
import gate.corpora.DocumentImpl;
import gate.creole.semtagger.rulemodel.RuleModelFactory;
import gate.creole.semtagger.rulemodel.Sentence;
import gate.creole.semtagger.rulemodel.Token;
import gate.util.GateException;

import org.junit.Before;
import org.junit.Test;

public class TestRuleModel {
	private DefaultAnnotationFactory annoFac;
	private AnnotationSet annoSet;
	
	@Test
	public void testToken() {
		FeatureMap t1Fm = Factory.newFeatureMap();
		t1Fm.put("string", "Token");
		t1Fm.put("root", "token");
		t1Fm.put("category", "NN");
		t1Fm.put("orth", "upperInitial");

		Annotation anno =
			annoFac.createAnnotationInSet(annoSet, 0, new NodeImpl(0, 0l), new NodeImpl(1, 6l), 
				"Token", t1Fm);
		Token t1 = RuleModelFactory.fromToken(anno);
		assertNotNull(t1);
		
		assertEquals(0, t1.getStartOffset());
		assertEquals(6, t1.getEndOffset());
		assertEquals("Token", t1.getString());
		assertEquals("token", t1.getRoot());
		assertEquals("NN", t1.getCategory());
		assertEquals("upperInitial", t1.getFeature("orth"));
		
		assertEquals("", t1.getFeature("noneExsisting"));
		assertEquals("default", t1.getFeatureOrDefault("noneExsisting", "default"));
	}
	@Test
	public void testSentence() {
		Sentence sentence = new Sentence();
		assertNotNull(sentence);
	}
	@Test
	public void testLookup() {
		Sentence sentence = new Sentence();
		assertNotNull(sentence);
	}
	
	@Before
	public void before() {
		Document document = new DocumentImpl();
		annoSet = new AnnotationSetImpl(document);
		annoFac = new DefaultAnnotationFactory();
	}
}
