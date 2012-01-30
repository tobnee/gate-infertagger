package gate.creole.infertagger.rulemodel;

import java.util.Collections;
import java.util.Map;


public class AnnoMarker {
	private final Annotation target;
	private final String type;
	private final Map<String, String> features;

	public static AnnoMarker mark(Annotation target, String type) {
		Map<String, String> m = Collections.emptyMap();
		return new AnnoMarker(target, type, m);
	}
	
	public static AnnoMarker mark(Annotation target, String type, Map<String, String> features) {
		return new AnnoMarker(target, type, features);
	}
	
	protected AnnoMarker(Annotation target, String type, Map<String, String> features) {
		this.target = target;
		this.type = type;
		this.features = features;
	}
	
	public Annotation getTarget() {
		return target;
	}
	
	public String getType() {
		return type;
	}
	
	public Map<String, String> getFeatures() {
		return features;
	}

	@Override
	public String toString() {
		return "Marker [target=" + target + ", type=" + type + ", features="
				+ features + "]";
	}	
	
}
