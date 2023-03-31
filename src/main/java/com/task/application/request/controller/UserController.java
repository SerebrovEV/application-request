package com.task.application.request.controller;

import com.task.application.request.dto.UserDto;
import com.task.application.request.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity getUsers(@PathVariable(required = false) Long userId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity getUsersByName(@RequestParam String name) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUserStatus(@PathVariable Long userId) {
        return ResponseEntity.ok().build();
    }
}
