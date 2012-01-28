package gate.creole.infertagger;

import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.Node;
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
public class InferTaggerPR extends AbstractLanguageAnalyser implements ProcessingResource {
  private static final long serialVersionUID = 208364328022562584L;

  private URL ruleSet;
  private KnowledgeBase knowledgeBase;
  private Logger logger = Logger.getLogger(getClass());

  @Override
  public Resource init() throws ResourceInstantiationException {
    File ruleSetFile = Files.fileFromURL(ruleSet);
    knowledgeBase = DroolsKnowledgeBaseFactory.initKnowledgeBase(ruleSetFile);
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

  void buildAnnotations(Collection<? extends Object> result) {
    for (Object resultObject : result) {
      if (resultObject instanceof AnnoMarker) {
        logger.info("Marker found " + resultObject);
        AnnoMarker marker = (AnnoMarker) resultObject;
        addAnnoMarker(marker);
      } else {
        Class<? extends Object> resultObjClass = resultObject.getClass();
        Marker resultObjAnnotations = resultObjClass.getAnnotation(Marker.class);
        if (resultObjAnnotations != null) {
          addCustomAnnoMarker(resultObject, resultObjClass, resultObjAnnotations);
        }
      }
    }
  }

  private void addCustomAnnoMarker(Object resultObject, Class<? extends Object> resultObjClass,
      Marker resultObjAnnotations) {
    // get meta object to access fields without reflection
    FactType resultDroolsMetaObj = getFactTypeFromKB(resultObjClass);
    if (resultDroolsMetaObj != null) { // type defined in drl
      Map<String, Object> resultFieldToValue = resultDroolsMetaObj.getAsMap(resultObject);
      if (resultFieldToValue.containsKey("anno")) {
        // get the obligatory anno which is use in the marker
        AnnotationDelegate anno = (AnnotationDelegate) resultDroolsMetaObj.get(
            resultObject, "anno");
        // get the optional anno which is used for marking relations
        AnnotationDelegate anno2 = (AnnotationDelegate) (resultFieldToValue
            .containsKey("anno2") ? resultFieldToValue.get("anno2") : null);

        // build annotation
        String annoSet = resultObjAnnotations.value();
        AnnotationSet type = document.getAnnotations(annoSet);
        FeatureMap fm = buildFeatureMapFromFields(resultObject, resultObjClass, resultDroolsMetaObj);
        Node startNode = anno.anno.getStartNode();
        if (anno2 == null) {
          type.add(startNode, anno.anno.getEndNode(), annoSet, fm);
        } else {
          type.add(startNode, anno2.anno.getEndNode(), annoSet, fm);
        }
      }
    }
  }

  private void addAnnoMarker(AnnoMarker marker) {
    FeatureMap featureMap = Factory.newFeatureMap();
    featureMap.putAll(marker.getFeatures());
    AnnotationSet type = document.getAnnotations();
    type.add(marker.getTarget().anno.getStartNode(),
        marker.getTarget().anno.getEndNode(), marker.getType(), featureMap);
  }

  private FeatureMap buildFeatureMapFromFields(Object object, Class<? extends Object> c,
      FactType ft) {
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

  private FactType getFactTypeFromKB(Class<? extends Object> c) {
    Package p = c.getPackage();
    String packagename = p.getName();
    String classname = c.getSimpleName();
    org.drools.definition.type.FactType ft = knowledgeBase.getFactType(packagename,
        classname);
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
