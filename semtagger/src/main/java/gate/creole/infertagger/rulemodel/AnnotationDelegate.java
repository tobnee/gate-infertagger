package gate.creole.infertagger.rulemodel;

import gate.annotation.AnnotationImpl;

public class AnnotationDelegate {
	protected final AnnotationImpl anno;

	public AnnotationDelegate(AnnotationImpl anno) {
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
	
	public boolean overlaps(AnnotationDelegate delegate) {
		return this.anno.overlaps(delegate.anno);
	}

	public boolean after(AnnotationDelegate w1) {
		return this.anno.getEndNode().getOffset()>w1.anno.getStartNode().getOffset();
	}

	public boolean before(AnnotationDelegate w1) {
		return !after(w1);
	}
}