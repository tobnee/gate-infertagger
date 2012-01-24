package gate.creole.infertagger.rulemodel;

import java.util.ArrayList;
import java.util.List;

import gate.Annotation;
import gate.AnnotationSet;
import gate.annotation.AnnotationImpl;

public class RuleModelBuilder {
	final AnnotationSet annoSet;
	
	public RuleModelBuilder(AnnotationSet annoSet) {
		this.annoSet = annoSet;
	}

	public List<Object> build() {
		//TODO: add registration possibility for annotation transformer
		// which can be selected based on user preferences 
		List<Object> obs = new ArrayList<Object>(annoSet.size());
		AnnotationSet set = annoSet.get("Token");
		for(Annotation a : set) {			
			obs.add(a.getFeatures().get("kind").equals("word") ? 
					RuleModelFactory.fromWordToken((AnnotationImpl) a) :
						RuleModelFactory.fromToken((AnnotationImpl) a));
		}
		set = annoSet.get("SpaceToken");
		for(Annotation a : set) {			
			obs.add(a.getFeatures().get("kind").equals("space") ? 
					RuleModelFactory.fromSpaceToken((AnnotationImpl) a) :
						RuleModelFactory.fromToken((AnnotationImpl) a));
		}
		
		set = annoSet.get("Sentence");
		for(Annotation a : set) {
			obs.add(RuleModelFactory.fromSentence((AnnotationImpl) a));
		}
		set = annoSet.get("Lookup");
		for(Annotation a : set) {
			obs.add(RuleModelFactory.fromLookup((AnnotationImpl) a));
		}
		return obs;
	}
}
