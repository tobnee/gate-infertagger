package gate.creole.infertagger.drools;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.event.rule.DebugAgendaEventListener;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.runtime.StatefulKnowledgeSession;

public class RuleProcessor {
	private Logger logger = Logger.getLogger(getClass());
	final private KnowledgeBase kb;

	public RuleProcessor(KnowledgeBase kb) {
		this.kb = kb;
	}

	public Collection<Object> assertAnnotations(List<Object> annotations) {
		StatefulKnowledgeSession ksession;
		ksession = buildSession(kb, annotations);
		ksession.fireAllRules();
		Collection<Object> obj = ksession.getObjects();
		ksession.dispose();
		return obj;
	}

	private StatefulKnowledgeSession buildSession(KnowledgeBase kbase, List<Object> annotations) {
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		if (logger.isDebugEnabled()) {
			ksession.addEventListener(new DebugAgendaEventListener());
			ksession.addEventListener(new DebugWorkingMemoryEventListener());
		}

		for (Object o : annotations) {
			ksession.insert(o);
		}
		return ksession;
	}
}
