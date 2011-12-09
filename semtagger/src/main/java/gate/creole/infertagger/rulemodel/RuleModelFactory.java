package gate.creole.infertagger.rulemodel;

import gate.Annotation;
import gate.annotation.AnnotationImpl;

public class RuleModelFactory {
	public static Token fromToken(AnnotationImpl anno) {
		checkTypeOrThrowException(anno, "Token");
		return new Token(anno);
	}

	public static Sentence fromSentence(AnnotationImpl anno) {
		checkTypeOrThrowException(anno, "Sentence");
		return new Sentence(anno);
	}

	public static Word fromWordToken(AnnotationImpl anno) {
		checkTypeOrThrowException(anno, "Token");
		checkFeatureOrThrowException(anno, "kind", "word");
		return new Word(anno);
	}
	
	private static void checkTypeOrThrowException(Annotation anno, String type) {
		if(!anno.getType().equals(type)) {
			wrongAnnotation(anno, "required type "+type);
		}
	}
	
	private static void checkFeatureOrThrowException(Annotation anno, String featureKey, String value) {
		boolean checked = false;
		if(anno.getFeatures().containsKey(featureKey)) {
			checked = anno.getFeatures().get(featureKey).equals(value);
		}
		if(!checked) wrongAnnotation(anno, "no feature "+featureKey+" of value"+value);
	}

	private static void wrongAnnotation(Annotation anno, String string) {
		throw new IllegalArgumentException(string +" "+anno);
	}
}
