package com.likelion.mindiary.domain.diary.controller.dto.request;

import java.util.List;

public record OpenAiRequest(
        String model,
        List<MessageDto> messages
) {

    public record MessageDto(
            String role,
            String content
    ) {
    }

}
