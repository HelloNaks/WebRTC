package com.tmax.rg.abook.global.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@Builder
public class DataResponse<T> {
    private boolean success;
    private T data;

    public static<T> DataResponse<T> response(final boolean success, final T data) {
        return DataResponse.<T>builder()
                .success(success)
                .data(data)
                .build();
    }

    public static<T> ResponseEntity<DataResponse<T>> success() {
        return ResponseEntity.ok(response(true, null));
    }

    public static<T> ResponseEntity<DataResponse<T>> success(final T data) {
        return ResponseEntity.ok(response(true, data));
    }
}