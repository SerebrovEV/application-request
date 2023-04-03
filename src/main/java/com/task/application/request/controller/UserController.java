package com.task.application.request.controller;

import com.task.application.request.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "getUsers", description = "Запрос пользователей администратором", tags = "User")
    @GetMapping
    public ResponseEntity getUsers(@RequestParam(required = false) String name,
                                   Authentication authentication) {
        if (name == null) {
            return ResponseEntity.ok(userService.getAllUser(authentication));
        }
        return ResponseEntity.ok(userService.getUserByName(name, authentication));
    }

    @Operation(summary = "SetUserStatus", description = "Назначение правами оператора администратором", tags = "User")
    @PatchMapping("/{userId}")
    public ResponseEntity<Void> setUserStatus(@PathVariable Integer userId,
                                              @RequestParam String status,
                                              Authentication authentication) {
        userService.setUserStatus(userId, status, authentication);
        return ResponseEntity.ok().build();
    }
}
