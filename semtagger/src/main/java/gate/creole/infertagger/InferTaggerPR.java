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
import gate.creole.infertagger.rulemodel.AnnoMarker;
import gate.creole.infertagger.rulemodel.RuleModelBuilder;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.util.Files;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.definition.type.FactType;

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
		for (Object resultObject : result) {
			if (resultObject instanceof AnnoMarker) {
				logger.info("Marker found " + resultObject);
				AnnoMarker marker = (AnnoMarker) resultObject;
				FeatureMap featureMap = Factory.newFeatureMap();
				featureMap.putAll(marker.getFeatures());
				AnnotationSet type = document.getAnnotations(marker.getType());
				type.add(marker.getTarget().anno.getStartNode(),
						marker.getTarget().anno.getEndNode(), marker.getType(),
						featureMap);
			} else {
				Class<? extends Object> c = resultObject.getClass();
				Marker classAnnotations = c.getAnnotation(Marker.class);
				if (classAnnotations != null) {
					FactType markerType = getFactTypeFromKB(c);
					if (markerType!=null) { // type defined in drl
						if (markerType.getAsMap(resultObject).containsKey("anno")) {
							
							Map<String, Object> typeMap = markerType.getAsMap(resultObject);
							AnnotationDelegate anno = (AnnotationDelegate) markerType
									.get(resultObject, "anno");
							AnnotationDelegate anno2 = (AnnotationDelegate) 
								(typeMap.containsKey("anno2") ? typeMap.get("anno2") : null);
							
							String annoSet = classAnnotations.value();
							AnnotationSet type = document
									.getAnnotations(annoSet);
							FeatureMap fm = buildFeatureMapFromFields(
									resultObject, c, markerType);
							if (anno2==null) {
								type.add(anno.anno.getStartNode(),
										anno.anno.getEndNode(), annoSet, fm);
							} else {
								type.add(anno.anno.getStartNode(),
										anno2.anno.getEndNode(), annoSet, fm);
							}
						}
					}

				}
			}
		}
	}

	private FeatureMap buildFeatureMapFromFields(Object object,
			Class<? extends Object> c, FactType ft) {
		FeatureMap fm = Factory.newFeatureMap();	
		for (Field tField : c.getDeclaredFields()) {
			if (tField.getAnnotation(Feature.class) != null) {
				String fName = tField.getName();
				Object fValue = ft.get(object, fName);
				fm.put(fName, fValue);
			}
		}
		return fm;
	}

	private FactType getFactTypeFromKB(
			Class<? extends Object> c) {
		Package p = c.getPackage();
		String packagename = p.getName();
		String classname = c.getSimpleName();
		org.drools.definition.type.FactType ft = knowledgeBase
				.getFactType(packagename, classname);
		return ft;
	}

	@CreoleParameter(comment = "path to Drools changeSet")
	public void setRuleSet(URL ruleSet) {
		this.ruleSet = ruleSet;
	}

	public URL getRuleSet() {
		return ruleSet;
	}
}
