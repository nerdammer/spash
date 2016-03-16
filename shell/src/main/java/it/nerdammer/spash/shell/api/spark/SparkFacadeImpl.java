package it.nerdammer.spash.shell.api.spark;

import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.common.SpashCollectionRDDAdapter;
import it.nerdammer.spash.shell.common.SpashCollectionStreamAdapter;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * The default implementation of the {@code SparkFacade}.
 *
 * @author Nicola Ferraro
 */
public class SparkFacadeImpl implements SparkFacade {

    private JavaSparkContext sc;

    public SparkFacadeImpl(JavaSparkContext sc) {
        this.sc = sc;
    }

    @Override
    public SpashCollection<String> read(Path file) {
        URI uri = SpashFileSystem.get().getURI(file.normalize().toString());
        JavaRDD<String> rdd = sc.textFile(uri.toString());

        return new SpashCollectionRDDAdapter<>(rdd);
    }

    @Override
    public SpashCollection<String> head(Path file, int lines) {
        URI uri = SpashFileSystem.get().getURI(file.normalize().toString());
        JavaRDD<String> rdd = sc.textFile(uri.toString());
        Stream<String> stream = rdd.take(lines).stream();

        return new SpashCollectionStreamAdapter<>(stream);
    }

    @Override
    public <T> SpashCollection<T> parallelize(List<T> coll) {
        return new SpashCollectionRDDAdapter<>(sc.parallelize(coll));
    }
}
