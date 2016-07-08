package nl.gridshore.elastic;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jettrocoenradie on 08/07/2016.
 */
public class ResponseHits<T> {
    private Long took;

    @JsonProperty(value = "timed_out")
    private Boolean timedOut;

    @JsonProperty(value = "_shards")
    private Shards shards;

    private Hits<T> hits;

    public Hits<T> getHits() {
        return hits;
    }

    public void setHits(Hits<T> hits) {
        this.hits = hits;
    }

    public Long getTook() {
        return took;
    }

    public void setTook(Long took) {
        this.took = took;
    }

    public Shards getShards() {
        return shards;
    }

    public void setShards(Shards shards) {
        this.shards = shards;
    }

    public Boolean getTimedOut() {
        return timedOut;
    }

    public void setTimedOut(Boolean timedOut) {
        this.timedOut = timedOut;
    }
}
