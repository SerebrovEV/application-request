package com.task.application.request.controller;

import com.task.application.request.dto.CreateRequestDto;
import com.task.application.request.dto.RequestDto;
import com.task.application.request.service.RequestService;
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

    /**
     * Создание заявки пользователем
     *
     * @param createRequestDto
     * @return
     */
    @PostMapping
    public ResponseEntity<RequestDto> addRequest(@RequestBody CreateRequestDto createRequestDto,
                                                 Authentication authentication) {
        return ResponseEntity.ok(requestService.addRequest(createRequestDto, authentication));
    }

    /**
     * Редактирование заявки черновика пользователем
     *
     * @param reqId
     * @param createRequestDto
     * @return
     */
    @PatchMapping("/{reqId}")
    public ResponseEntity<RequestDto> updateRequest(@PathVariable Integer reqId,
                                                    @RequestBody CreateRequestDto createRequestDto,
                                                    Authentication authentication) {
        return ResponseEntity.ok(requestService.updateRequest(
                reqId,
                createRequestDto,
                authentication));
    }

    /**
     * Изменение статуса заявки опереатором и пользователем
     *
     * @param reqId
     * @param status
     * @return
     */
    @PatchMapping("/{reqId}/status")
    public ResponseEntity<Void> setStatus(@PathVariable Integer reqId,
                                          @RequestParam String status,
                                          Authentication authentication) {
        requestService.setStatus(reqId, status, authentication);
        return ResponseEntity.ok().build();
    }

    /**
     * Пользователь может просматривать своисозданные заявки
     *
     * @param page
     * @param sort
     * @param authentication
     * @return
     */
    @GetMapping("/me")
    public ResponseEntity<List<RequestDto>> getAllUserRequests(@RequestParam Integer page,
                                                               @RequestParam String sort,
                                                               Authentication authentication) {
        return ResponseEntity.ok(requestService.getAllUserRequests(authentication));
    }

    /**
     * Просматривать все отправленные на рассмотрение заявки с возможностью сортировки по дате создания в оба
     * направления (как от самой старой к самой новой, так и наоборот) и пагинацией по 5 элементов
     * Просматривать отправленные заявки только конкретного пользователя по его имени/части имени (у пользователя,
     * соотетственно, должно быть поле name) с возможностью сортировки по дате создания в оба направления (как от самой
     * старой к самой новой, так и наоборот) и пагинацией по 5 элементов
     *
     * @param name
     * @param sort
     * @param page
     * @return
     */
    @GetMapping
    public ResponseEntity<List<RequestDto>> getAllActiveRequest(@RequestParam(required = false) String name,
                                                                @RequestParam String sort,
                                                                @RequestParam Integer page,
                                                                Authentication authentication) {
        if (name == null) {
            return ResponseEntity.ok(requestService.getAllRequests(authentication));
        } else {
            return ResponseEntity.ok(requestService.getAllRequestsByUser(authentication));
        }
    }

}
