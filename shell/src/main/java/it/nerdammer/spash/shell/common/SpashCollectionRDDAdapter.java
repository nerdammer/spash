package it.nerdammer.spash.shell.common;

import org.apache.spark.api.java.JavaRDD;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.function.Function;

/**
 * Adapts a {@code org.apache.spark.api.java.JavaRDD} to a {@code SpashCollection}.
 *
 * @author Nicola Ferraro
 */
public class SpashCollectionRDDAdapter<T> implements SpashCollection<T> {

    private JavaRDD<T> target;

    public SpashCollectionRDDAdapter(JavaRDD<T> target) {
        this.target = target;
    }

    @Override
    public void mkString(PrintWriter writer) {
        Iterator<T> it = target.toLocalIterator();
        while(it.hasNext()) {
            T el = it.next();
            writer.println(el != null ? el.toString() : "");
        }
    }

    @Override
    public <R> SpashCollection<R> map(Function<T, R> f) {
        return new SpashCollectionRDDAdapter<>(target.map(e -> f.apply(e)));
    }

    @Override
    public SpashCollection<T> union(SpashCollection<T> coll) {
        return new SpashCollectionUnionAdapter<>(this, coll);
    }

}
