package gate.creole.infertagger;

import gate.Document;
import gate.creole.infertagger.testdata.AnnoTestBuilder;
import gate.creole.infertagger.testdata.TestUtil;

import org.junit.Test;

public class TestLiveUpdate {
  @Test
  public void testLiveUpdate() {
    AnnoTestBuilder testBuilder = new AnnoTestBuilder();
    Document doc = testBuilder.startSentence().addWord("We").addWord("have")
        .addWord("no").addWord("house").endSentence().build();
    TestUtil.getDocumentForTestdata(doc, "rules/liveUpdate.xml");
    System.out.println(doc);
  }

}
