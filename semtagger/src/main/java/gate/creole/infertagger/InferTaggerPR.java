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
import gate.creole.infertagger.rulemodel.AnnoMarker;
import gate.creole.infertagger.rulemodel.Annotation;
import gate.creole.infertagger.rulemodel.Feature;
import gate.creole.infertagger.rulemodel.From;
import gate.creole.infertagger.rulemodel.Marker;
import gate.creole.infertagger.rulemodel.RuleModelBuilder;
import gate.creole.infertagger.rulemodel.To;
import gate.creole.infertagger.util.LruCache;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.util.Files;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
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

  private final LruCache<Class<? extends Object>,  MarkerClassInfo> makerClassInfoCache = 
    new LruCache<Class<? extends Object>,  MarkerClassInfo>(100);

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

  private void addCustomAnnoMarker(Object resultObject,
      Class<? extends Object> resultObjClass, Marker resultObjAnnotations) {
    // get meta object to access fields without reflection
    FactType resultDroolsMetaObj = getFactTypeFromKB(resultObjClass);
    if (resultDroolsMetaObj != null) { // type defined in drl
      MarkerClassInfo markerClassInfo = getOrBuildMarkerClass(resultObject,
          resultObjClass);
      if (markerClassInfo.from != null) {
        String annoSet = resultObjAnnotations.value();
        addAnnotatons(resultObject, resultDroolsMetaObj, markerClassInfo, annoSet);
      } else {
        logger.warn("Custom marker class " + resultObjClass
            + " defined without @From annotation");
      }
    }
  }

  private MarkerClassInfo getOrBuildMarkerClass(Object resultObject, Class<? extends Object> resultObjClass) {
    if(!makerClassInfoCache.containsKey(resultObjClass)) 
      makerClassInfoCache.put(resultObjClass, extractMarkerClassInfo(resultObject));
    return makerClassInfoCache.get(resultObjClass);
  }

  private void addAnnotatons(Object resultObject, FactType resultDroolsMetaObj,
      MarkerClassInfo markerClassInfo, String annoSet) {
    Map<String, Object> resultFieldToValue = resultDroolsMetaObj.getAsMap(resultObject);
    if (resultFieldToValue.containsKey(markerClassInfo.from)) {
      // get the obligatory anno which is use in the marker
      Annotation anno = (Annotation) resultDroolsMetaObj.get(
          resultObject, markerClassInfo.from);
      // get the optional anno which is used for marking relations
      Annotation anno2 = (Annotation) (markerClassInfo.isRelation() ? resultFieldToValue
          .get(markerClassInfo.to) : null);
      // build annotation

      AnnotationSet type = document.getAnnotations(annoSet);
      FeatureMap fm = buildFeatureMapFromFields(markerClassInfo, resultFieldToValue);
      Node startNode = anno.anno.getStartNode();
      if (anno2 == null) {
        type.add(startNode, anno.anno.getEndNode(), annoSet, fm);
      } else {
        type.add(startNode, anno2.anno.getEndNode(), annoSet, fm);
      }
    }
  }

  private FeatureMap buildFeatureMapFromFields(MarkerClassInfo markerClassInfo,
      Map<String, Object> resultFieldToValue) {
    FeatureMap fm = Factory.newFeatureMap();
    for (String featureName : markerClassInfo.features) {
      fm.put(featureName, resultFieldToValue.get(featureName));
    }
    return fm;
  }

  private MarkerClassInfo extractMarkerClassInfo(Object resultObject) {
    MarkerClassInfo markerClassInfo = new MarkerClassInfo();
    Field[] fields = resultObject.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(Feature.class)) {
        markerClassInfo.features.add(field.getName());
      } else if (field.isAnnotationPresent(From.class)) {
        markerClassInfo.from = field.getName();
      } else if (field.isAnnotationPresent(To.class)) {
        markerClassInfo.to = field.getName();
      }
    }
    return markerClassInfo;
  }

  private void addAnnoMarker(AnnoMarker marker) {
    FeatureMap featureMap = Factory.newFeatureMap();
    featureMap.putAll(marker.getFeatures());
    AnnotationSet type = document.getAnnotations();
    type.add(marker.getTarget().anno.getStartNode(),
        marker.getTarget().anno.getEndNode(), marker.getType(), featureMap);
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

  private static class MarkerClassInfo {
    String to;
    String from;
    final List<String> features = new ArrayList<String>();

    boolean isRelation() {
      return !(to == null);
    }
  }
}
