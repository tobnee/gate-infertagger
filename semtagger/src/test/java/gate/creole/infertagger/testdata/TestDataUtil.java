package gate.creole.infertagger.testdata;

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

public class TestDataUtil {
	
//	public static Annotation getWordAnno() {
//		DocumentImpl document = new DocumentImpl();
//		DefaultAnnotationFactory annoFac = new DefaultAnnotationFactory();;
//		AnnotationSet annoSet = new AnnotationSetImpl(document);
//		annoTok = (AnnotationImpl) annoFac.createAnnotationInSet(annoSet, 10,
//				new NodeImpl(0, 0l), new NodeImpl(1, 6l), "Token", t1Tok);
//	}
	
	public static Document getTestCourpus() {
		DefaultAnnotationFactory annoFac;
		AnnotationSet annoSet;
		AnnotationImpl annoSen;
		AnnotationImpl annoTok;
		AnnotationImpl annoTok2;
		AnnotationImpl annoLook;

		DocumentImpl document = new DocumentImpl();
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
		document.setDefaultAnnotations(annoSet);
		return document;
	}
}
