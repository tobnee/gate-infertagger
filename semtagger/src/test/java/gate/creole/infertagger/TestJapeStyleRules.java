package gate.creole.infertagger;

import static org.junit.Assert.assertEquals;
import gate.AnnotationSet;
import gate.Document;
import gate.FeatureMap;
import gate.creole.infertagger.testdata.AnnoTestBuilder;
import gate.creole.infertagger.testdata.TestUtil;

import org.junit.Test;

public class TestJapeStyleRules {

  @Test
  public void testOrder() {
    AnnoTestBuilder testBuilder = new AnnoTestBuilder();
    Document doc = testBuilder.startSentence().addWord("AC").addWord("Milan")
        .addWord("player").addWord("David", "NNP").addWord("Beckham", "NNP")
        .addWord("is").addWord("english").endSentence().build();
    TestUtil.getDocumentForTestdata(doc, "rules/jape.xml");
    AnnotationSet annotations = doc.getAnnotations("MarkRel");
    assertEquals(1, annotations.size());
    FeatureMap features = annotations.iterator().next().getFeatures();
    assertEquals("David", features.get("name"));
    assertEquals("Beckham", features.get("surname"));
  }

}
