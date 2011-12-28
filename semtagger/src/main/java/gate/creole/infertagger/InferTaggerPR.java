package gate.creole.infertagger;

import gate.AnnotationSet;
import gate.Factory;
import gate.ProcessingResource;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.infertagger.drools.DroolsKnowledgeBaseFactory;
import gate.creole.infertagger.drools.RuleProcessor;
import gate.creole.infertagger.rulemodel.Marker;
import gate.creole.infertagger.rulemodel.RuleModelBuilder;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.util.Files;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;

/**
 * @author Tobias Neef
 */
@CreoleResource(name = "InferTagger", 
		comment = "A inference system for GATE based on JBoss Drools expert") 
public class InferTaggerPR extends AbstractLanguageAnalyser implements ProcessingResource
{
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
    	for (Object object : result) {
			if(object instanceof Marker) {
				logger.info("Marker found "+object);
				Marker marker = (Marker) object;
				AnnotationSet type = document.getAnnotations(marker.getType());
				type.add(marker.getTarget().anno.getStartNode(), 
						marker.getTarget().anno.getEndNode(), marker.getType(), 
						Factory.newFeatureMap());
			}
		}
       	logger.info(result);
    }
    
    @CreoleParameter(comment = "path to Drools changeSet")
    public void setRuleSet(URL ruleSet) {
		this.ruleSet = ruleSet;
	}
    
    public URL getRuleSet() {
		return ruleSet;
	}    
}
