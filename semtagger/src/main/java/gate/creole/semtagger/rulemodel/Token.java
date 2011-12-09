package gate.creole.semtagger.rulemodel;

import gate.Annotation;

public class Token extends AnnotationDelegate {
	Token(Annotation anno) {
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
}