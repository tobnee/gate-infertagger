package gate.creole.infertagger.rulemodel;

import gate.annotation.AnnotationImpl;

public class Annotation {
	public final AnnotationImpl anno;

	public Annotation(AnnotationImpl anno) {
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
	
	public boolean overlaps(Annotation that) {
		return that==null ? false : this.anno.overlaps(that.anno);
	}

	public boolean after(Annotation that) {
		return that==null ? false : 
			this.anno.getEndNode().getOffset()>that.anno.getStartNode().getOffset();
	}

	public boolean before(Annotation that) {
		return !after(that);
	}
	
	public boolean contains(Annotation that) {
		return that==null ? false : that.anno.withinSpanOf(anno);
	}
	
	public boolean within(Annotation that) {
		return that==null ? false : this.anno.withinSpanOf(that.anno);
	}
	
	public AnnotationImpl getAnno() {
		return anno;
	}
	
	@Override
	public String toString() {
		return anno.toString();
	}
	
	@Override
	public int hashCode() {
		return anno.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(this.getClass().isInstance(obj)) {
			return super.equals(((Annotation) obj).anno);
		} 
		return false;
	}
}