package com.hazelcast.jet.pipeline.file;

import com.hazelcast.function.FunctionEx;
import com.hazelcast.jet.pipeline.BatchSource;
import com.hazelcast.jet.pipeline.Sources;

import java.io.InputStream;
import java.nio.file.Files;
import java.util.stream.Stream;

import static com.hazelcast.jet.impl.util.Util.uncheckRun;

class LocalFileSourceBuilderImpl<T> extends BaseFileSourceBuilderImpl<T> {

    public LocalFileSourceBuilderImpl(String path) {
        super(path);
    }

    @Override
    public BatchSource<T> build() {
        FunctionEx<? super InputStream, Stream<T>> mapFn = fileFormat.mapFn();
        return Sources.filesBuilder(path)
                .build(path -> {
                    InputStream inputStream = Files.newInputStream(path);
                    return mapFn.apply(inputStream).onClose(() -> uncheckRun(inputStream::close));
                });
    }
}