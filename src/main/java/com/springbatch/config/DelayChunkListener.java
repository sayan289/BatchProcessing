package com.springbatch.config;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.context.annotation.Configuration;

public class DelayChunkListener implements ChunkListener{
    private final long delayMiliSecond;
    public DelayChunkListener(long delayMiliSecond)
    {
        this.delayMiliSecond=delayMiliSecond;
    }
    @Override
    public void beforeChunk(ChunkContext context) {
        //No action befor chunk
    }

    @Override
    public void afterChunk(ChunkContext context) {
        try {
            Thread.sleep(delayMiliSecond);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
