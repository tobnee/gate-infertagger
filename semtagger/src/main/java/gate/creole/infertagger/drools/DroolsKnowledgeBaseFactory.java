package gate.creole.infertagger.drools;

import java.io.File;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;

public class DroolsKnowledgeBaseFactory {
private DroolsKnowledgeBaseFactory() {};
	
	public static KnowledgeBase initKnowledgeBase(File location) {
		return initKnowledgeBase(location, ResourceType.CHANGE_SET);
	}
	
	/**
	 * Build {@link KnowledgeBase}
	 * 
	 * @return
	 */
	public static KnowledgeBase initKnowledgeBase(File location, ResourceType type) {
		final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
//		Resource res = ResourceFactory.newClassPathResource(location,
//				RuleProcessor.class);
		Resource res = ResourceFactory.newFileResource(location);
		kbuilder.add(res, type);

		// Check the builder for errors
		if (kbuilder.hasErrors()) {
			Logger.getLogger(DroolsKnowledgeBaseFactory.class)
			 .error("errors in drools kbuilder "+ kbuilder.getErrors());
		}

		// get the compiled packages (which are serializable)
		final Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();

		// add the packages to a knowledgebase (deploy the knowledge packages).
		final KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

		kbase.addKnowledgePackages(pkgs);
		return kbase;
	}
}
