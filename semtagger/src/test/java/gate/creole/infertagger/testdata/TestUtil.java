package gate.creole.infertagger.testdata;

import gate.Document;
import gate.creole.infertagger.InferTaggerPR;
import gate.creole.infertagger.TestRuleModelUsageDirect;

public class TestUtil {

  public static Document getDocumentForTestdata(Document doc, String ruleSet) {
    InferTaggerPR semTagger = new InferTaggerPR();
    semTagger.setDocument(doc);
    semTagger.setRuleSet(TestRuleModelUsageDirect.class.getClassLoader().getResource(
        ruleSet));
    try {
      semTagger.init();
      semTagger.execute();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    doc = semTagger.getDocument();
    return doc;
  }

}
