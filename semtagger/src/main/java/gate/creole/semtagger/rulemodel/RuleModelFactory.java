package gate.creole.semtagger.rulemodel;

import gate.Annotation;

public class RuleModelFactory {
	public static Token fromToken(Annotation anno) {
		checkTypeOrThrowException(anno, "Token");
		return new Token(anno);
	}
	
	private static void checkTypeOrThrowException(Annotation anno, String string) {

	}
}
