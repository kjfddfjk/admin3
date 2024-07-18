package tech.wetech.admin3.infra.storage;

import software.amazon.awssdk.core.ServiceConfiguration;
import software.amazon.awssdk.services.s3.S3Configuration;

public class OSSExtraConfig extends ExtraConfig {
    private boolean pathStyleAccessEnabled;
    private boolean chunkedEncodingEnabled;

    public boolean isPathStyleAccessEnabled() {
        return pathStyleAccessEnabled;
    }

    public void setPathStyleAccessEnabled(boolean pathStyleAccessEnabled) {
        this.pathStyleAccessEnabled = pathStyleAccessEnabled;
    }

    public boolean isChunkedEncodingEnabled() {
        return chunkedEncodingEnabled;
    }

    public void setChunkedEncodingEnabled(boolean chunkedEncodingEnabled) {
        this.chunkedEncodingEnabled = chunkedEncodingEnabled;
    }

    @Override
    public ServiceConfiguration toServiceConfiguration() {
        return S3Configuration.builder()
            .pathStyleAccessEnabled(this.pathStyleAccessEnabled)
            .chunkedEncodingEnabled(this.chunkedEncodingEnabled)
            .build();
    }
}

