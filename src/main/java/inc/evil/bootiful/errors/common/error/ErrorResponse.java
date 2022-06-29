package inc.evil.bootiful.errors.common.error;

import lombok.Builder;

import java.util.List;

public record ErrorResponse(String statusCode, String path, List<String> messages) {
    @Builder
    public ErrorResponse {
    }
}
