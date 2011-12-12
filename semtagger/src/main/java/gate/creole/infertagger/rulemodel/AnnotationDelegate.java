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
	
	public boolean overlaps(AnnotationDelegate that) {
		return that==null ? false : this.anno.overlaps(that.anno);
	}

	public boolean after(AnnotationDelegate that) {
		return that==null ? false : 
			this.anno.getEndNode().getOffset()>that.anno.getStartNode().getOffset();
	}

	public boolean before(AnnotationDelegate that) {
		return !after(that);
	}
	
	public boolean contains(AnnotationDelegate that) {
		return that==null ? false : that.anno.withinSpanOf(anno);
	}
	
	public boolean within(AnnotationDelegate that) {
		return that==null ? false : this.anno.withinSpanOf(that.anno);
	}
	
	@Override
	public String toString() {
		return anno.toString();
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj instanceof AnnotationDelegate) {
			return super.equals(((AnnotationDelegate) obj).anno);
		} 
		return false;
	}
}