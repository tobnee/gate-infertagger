package gate.creole.infertagger.rulemodel;


public class Marker {
	private final AnnotationDelegate target;
	private final String type;

	public static Marker mark(AnnotationDelegate target, String type) {
		return new Marker(target, type);
	}
	
	protected Marker(AnnotationDelegate target, String type) {
		this.target = target;
		this.type = type;
	}
	
	public AnnotationDelegate getTarget() {
		return target;
	}
	
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Marker [target=" + target + ", type=" + type + "]";
	}
	
	
}
