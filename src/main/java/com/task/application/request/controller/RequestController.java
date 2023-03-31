package com.task.application.request.controller;

import com.task.application.request.dto.CreateRequestDto;
import com.task.application.request.dto.RequestDto;
import com.task.application.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    /**
     * Для пользователя
     * @param requestDto
     * @return
     */
    @PostMapping
    public ResponseEntity<RequestDto> addRequest(@RequestBody CreateRequestDto requestDto) {
        return ResponseEntity.ok(requestService.addRequest(requestDto));
    }

    /**
     * Для пользователя
     * @param reqId
     * @return
     */
    @GetMapping("/{reqId}")
    public ResponseEntity<RequestDto> getRequest(@PathVariable(required = false) Long reqId) {
        if (reqId == null){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(requestService.get(reqId));
    }

    /**
     * Для пользователя
     * @param reqId
     * @param requestDto
     * @return
     */
    @PatchMapping("/{reqId}")
    public ResponseEntity<RequestDto> updateRequest(@PathVariable Long reqId,
                                                    @RequestBody RequestDto requestDto) {
        return ResponseEntity.ok(requestDto);
    }

    /**
     * Для пользователя и оператора
     * @param reqId
     * @param status
     * @return
     */
    @PatchMapping("/{reqId}/status")
    public ResponseEntity<RequestDto> sendRequest(@PathVariable Long reqId,
                                                  @RequestParam String status) {
        return ResponseEntity.ok().build();
    }

    /**
     * Для оператора
     * @param userName
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity<List<RequestDto>> getAllSendRequest(@RequestParam String userName) {
        if (userName == null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok().build();
    }


}
