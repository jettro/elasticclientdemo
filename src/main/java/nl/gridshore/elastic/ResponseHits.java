package nl.gridshore.elastic;

/**
 * Created by jettrocoenradie on 08/07/2016.
 */
public class ResponseHits<T> {
    private Hits<T> hits;

    public Hits<T> getHits() {
        return hits;
    }

    public void setHits(Hits<T> hits) {
        this.hits = hits;
    }
}
