package gate.creole.semtagger.rulemodel;

import gate.Annotation;

public class AnnotationDelegate {
	protected final Annotation anno;

	public AnnotationDelegate(Annotation anno) {
		this.anno = anno;
	}

	public long getStartOffset() {
		return anno.getStartNode().getOffset();
	}

	public long getEndOffset() {
		return anno.getEndNode().getOffset();
	}

	public String getFeatureOrDefault(String feature, String defaultValue) {
		return (String) (anno.getFeatures().containsKey(feature) ? anno
				.getFeatures().get(feature) : defaultValue);
	}

	public String getFeature(String string) {
		return getFeatureOrDefault(string, "");
	}

}