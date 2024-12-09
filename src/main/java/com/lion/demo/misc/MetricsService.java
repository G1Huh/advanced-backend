package com.lion.demo.misc;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {
    @Autowired
    private MeterRegistry meterRegistry;

    public void recordCustomMetrics() {
        // Counter Example
        meterRegistry.counter("custom.metric.counter", "tag", "example").increment();

        // Timer Example
        meterRegistry.timer("custom.metric.timer", "tag", "example")
                .record(() -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
