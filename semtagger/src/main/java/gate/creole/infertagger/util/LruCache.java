package gate.creole.infertagger.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<A, B> extends LinkedHashMap<A, B> {
  private static final long serialVersionUID = 2958069579738172459L;
  private final int maxEntries;

  public LruCache(final int maxEntries) {
      super(maxEntries + 1, 0.75f, true);
      this.maxEntries = maxEntries;
  }

  @Override
  protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
      return super.size() > maxEntries;
  }
}