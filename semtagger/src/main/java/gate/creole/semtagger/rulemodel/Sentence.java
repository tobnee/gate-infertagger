package gate.creole.semtagger.rulemodel;

import gate.annotation.AnnotationImpl;

public class Sentence extends AnnotationDelegate {

	public Sentence(AnnotationImpl anno) {
		super(anno);
	}

	public boolean containsToken(Token t1) {
		return overlaps(t1);
	}

}
