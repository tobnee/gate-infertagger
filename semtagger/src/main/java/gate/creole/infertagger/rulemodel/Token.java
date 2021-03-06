package gate.creole.infertagger.rulemodel;

import gate.annotation.AnnotationImpl;

public class Token extends Annotation {
	Token(AnnotationImpl anno) {
		super(anno);
	}

	public String getString() {
		return getFeature("string");
	}

	public String getRoot() {
		return getFeature("root");
	}
	
	public String getCategory() {
		return getFeature("category");
	}
	
	public String getOrth() {
		return getFeature("orth");
	}
}