package gate.creole.semtagger;

import gate.ProcessingResource;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleResource;

/**
 * @author Tobias Neef
 */
@CreoleResource(name = "", comment = "") 
public class SemanticTaggerPR extends AbstractLanguageAnalyser implements ProcessingResource
{
	private static final long serialVersionUID = 208364328022562584L;
    
    @Override
    public Resource init() throws ResourceInstantiationException {
    	return super.init();
    }
    
    @Override
    public void execute() throws ExecutionException {
    	super.execute();
    }
    
}
