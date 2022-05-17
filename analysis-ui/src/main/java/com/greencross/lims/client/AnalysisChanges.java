package com.greencross.lims.client;

import lombok.Builder;
import lombok.experimental.Accessors;

@lombok.Data
@Builder
@Accessors(fluent = true)
final class AnalysisChanges {
    private long sample;
    private String service;
    private String info;
}
