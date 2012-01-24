package gate.creole.infertagger.testdata;

import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.annotation.AnnotationSetImpl;
import gate.annotation.DefaultAnnotationFactory;
import gate.annotation.NodeImpl;
import gate.corpora.DocumentImpl;

public class AnnoTestBuilder {
	private DefaultAnnotationFactory annoFac;
	private AnnotationSet annoSet;
	private DocumentImpl document;

	private long currPos = 0;
	private int currAnnoId = 0;
	private long senStart = -1;
	private boolean senStartFlag = true;

	public AnnoTestBuilder() {
		document = new DocumentImpl();
		annoSet = new AnnotationSetImpl(document);
		annoFac = new DefaultAnnotationFactory();
		document.setDefaultAnnotations(annoSet);
	}

	public AnnoTestBuilder addWord(String content) {
		addWord(content, "");
		return this;
	}

	public AnnoTestBuilder addWord(String content, String pos) {
		if (!senStartFlag) {
			FeatureMap t2Tok = Factory.newFeatureMap();
			t2Tok.put("kind", "space");
			annoFac.createAnnotationInSet(annoSet, newId(), new NodeImpl(
					newId(), currPos), new NodeImpl(newId(), posEnd(1)),
					"SpaceToken", t2Tok);
		}
		senStartFlag = false;

		FeatureMap t1Tok = Factory.newFeatureMap();
		t1Tok.put("string", content);
		t1Tok.put("root", content);
		t1Tok.put("category", pos);
		t1Tok.put("orth", "upperInitial");
		t1Tok.put("kind", "word");
		annoFac.createAnnotationInSet(annoSet, newId(), new NodeImpl(newId(),
				currPos), new NodeImpl(newId(), posEnd(content)), "Token",
				t1Tok);
		return this;
	}

	public AnnoTestBuilder startSentence() {
		senStart++;
		senStartFlag = true;
		return this;
	}

	public AnnoTestBuilder endSentence() {
		FeatureMap t1Fm = Factory.newFeatureMap();
		annoFac.createAnnotationInSet(annoSet, newId(), new NodeImpl(newId(),
				senStart), new NodeImpl(newId(), currPos + 1), "Sentence", t1Fm);
		return this;
	}

	public DocumentImpl build() {
		return document;
	}

	private int newId() {
		return ++currAnnoId;
	}

	private long posStart() {
		return ++currPos;
	}

	private long posEnd(String word) {
		currPos += word.length();
		return currPos;
	}

	private long posEnd(int length) {
		currPos += length;
		return currPos;
	}
}
