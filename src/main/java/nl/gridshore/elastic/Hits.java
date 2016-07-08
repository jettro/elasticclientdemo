package nl.gridshore.elastic;

import java.util.List;

/**
 * Created by jettrocoenradie on 08/07/2016.
 */
public class Hits<T> {
    private List<Hit<T>> hits;

    public List<Hit<T>> getHits() {
        return hits;
    }

    public void setHits(List<Hit<T>> hits) {
        this.hits = hits;
    }
}
