package com.task.application.request.controller;

import com.task.application.request.dto.CreateRequestDto;
import com.task.application.request.dto.RequestDto;
import com.task.application.request.service.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @Operation(summary = "addRequest", description = "Создание запроса пользователем", tags = "Request")
    @PostMapping
    public ResponseEntity<RequestDto> addRequest(@RequestBody CreateRequestDto createRequestDto,
                                                 Authentication authentication) {
        return ResponseEntity.ok(requestService.addRequest(createRequestDto, authentication));
    }

    @Operation(summary = "updateDraftRequest",
            description = "Редактирование запроса пользователем",
            tags = "Request")
    @PatchMapping("/{reqId}")
    public ResponseEntity<RequestDto> updateRequest(@PathVariable Integer reqId,
                                                    @RequestBody CreateRequestDto createRequestDto,
                                                    Authentication authentication) {
        return ResponseEntity.ok(
                requestService.updateRequest(reqId, createRequestDto, authentication));
    }

    @Operation(summary = "updateStatusRequest",
            description = "Изменение статуса заявки пользователем или оператором",
            tags = "Request")
    @PatchMapping("/{reqId}/status")
    public ResponseEntity<Void> setStatus(@PathVariable Integer reqId,
                                          @RequestParam() String status,
                                          Authentication authentication) {
        requestService.setStatus(reqId, status, authentication);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "getAllUserRequest", description = "Запрос всех заявок пользователем", tags = "Request")
    @GetMapping("/me")
    public ResponseEntity<List<RequestDto>> getAllUserRequests(@RequestParam Integer page,
                                                               @RequestParam(required = false, defaultValue = "date", name = "sort_by") String sortBy,
                                                               @RequestParam(required = false, defaultValue = "desc", name = "order_by") String orderBy,
                                                               Authentication authentication) {
        return ResponseEntity.ok(requestService.getAllUserRequests(page,authentication, sortBy, orderBy));
    }

    @Operation(summary = "getRequest", description = "Запрос заявки по Id", tags = "Request")
    @GetMapping("/{reqId}")
    public ResponseEntity<RequestDto> getRequestById(@PathVariable Integer reqId, Authentication authentication) {
        return ResponseEntity.ok(requestService.getRequestById(reqId, authentication));
    }

    @Operation(summary = "getAllActiveRequestForOperator", description = "Запрос заявок оператором", tags = "Request")
    @GetMapping //+ пагинация
    public ResponseEntity<List<RequestDto>> getAllActiveRequest(@RequestParam Integer page,
                                                                @RequestParam(required = false) String name,
                                                                @RequestParam(required = false, defaultValue = "date", name = "sort_by") String sortBy,
                                                                @RequestParam(required = false, defaultValue = "desc", name = "order_by") String orderBy,
                                                                Authentication authentication) {
        if (name == null) {
            return ResponseEntity.ok(requestService.getAllSentRequests(page,authentication, sortBy, orderBy));
        } else {
            return ResponseEntity.ok(requestService.getAllSentRequestsByPartUserName(page, name, authentication, sortBy, orderBy));
        }
    }

}
