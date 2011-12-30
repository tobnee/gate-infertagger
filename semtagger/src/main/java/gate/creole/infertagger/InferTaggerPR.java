package gate.creole.infertagger;

import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.ProcessingResource;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.infertagger.drools.DroolsKnowledgeBaseFactory;
import gate.creole.infertagger.drools.RuleProcessor;
import gate.creole.infertagger.rulemodel.AnnotationDelegate;
import gate.creole.infertagger.rulemodel.Feature;
import gate.creole.infertagger.rulemodel.Marker;
import gate.creole.infertagger.rulemodel.MarkerUtil;
import gate.creole.infertagger.rulemodel.RuleModelBuilder;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.util.Files;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.definition.type.FactField;

/**
 * @author Tobias Neef
 */
@CreoleResource(name = "InferTagger", comment = "A inference system for GATE based on JBoss Drools expert")
public class InferTaggerPR extends AbstractLanguageAnalyser implements
		ProcessingResource {
	private static final long serialVersionUID = 208364328022562584L;

	private URL ruleSet;
	private KnowledgeBase knowledgeBase;
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public Resource init() throws ResourceInstantiationException {
		File ruleSetFile = Files.fileFromURL(ruleSet);
		knowledgeBase = DroolsKnowledgeBaseFactory
				.initKnowledgeBase(ruleSetFile);
		return this;
	}

	@Override
	public void execute() throws ExecutionException {
		RuleModelBuilder rmb = new RuleModelBuilder(document.getAnnotations());
		List<Object> rulemodel = rmb.build();
		RuleProcessor rp = new RuleProcessor(knowledgeBase);
		Collection<Object> result = rp.assertAnnotations(rulemodel);
		buildAnnotations(result);
		logger.info(result);
	}

	private void buildAnnotations(Collection<Object> result) {
		for (Object object : result) {
			if (object instanceof MarkerUtil) {
				logger.info("Marker found " + object);
				MarkerUtil marker = (MarkerUtil) object;
				FeatureMap fm = Factory.newFeatureMap();
				fm.putAll(marker.getFeatures());
				AnnotationSet type = document.getAnnotations(marker.getType());
				type.add(marker.getTarget().anno.getStartNode(),
						marker.getTarget().anno.getEndNode(), marker.getType(),
						fm);
			} else {
				Class<? extends Object> c = object.getClass();
				Marker classAnnotations = c.getAnnotation(Marker.class);
				if (classAnnotations != null) {
					Package p = c.getPackage();
					String packagename = p.getName();
					String classname = c.getSimpleName();
					org.drools.definition.type.FactType ft = knowledgeBase
							.getFactType(packagename, classname);

					if (ft!=null) { // type defined in drl
						AnnotationDelegate anno = (AnnotationDelegate) ft.get(object, "anno");
						String annoSet = classAnnotations.value();
						AnnotationSet type = document.getAnnotations(annoSet);
						FeatureMap fm = Factory.newFeatureMap();
	
						for (Field tField : c.getDeclaredFields()) {
							if (tField.getAnnotation(Feature.class) != null) {
								String fName = tField.getName();
								Object fValue = ft.get(object, fName);
								fm.put(fName, fValue);
							}
						}
						type.add(anno.anno.getStartNode(),
								anno.anno.getEndNode(), annoSet,
								fm);
					}

				}
			}
		}
	}

	@CreoleParameter(comment = "path to Drools changeSet")
	public void setRuleSet(URL ruleSet) {
		this.ruleSet = ruleSet;
	}

	public URL getRuleSet() {
		return ruleSet;
	}
}
