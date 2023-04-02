package com.task.application.request.controller;

import com.task.application.request.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @GetMapping
    public ResponseEntity<UserDto> getUsers(@RequestParam(required = false) String name,
                                            Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> setUserStatus(@PathVariable Integer userId,
                                              @RequestParam String status,
                                              Authentication authentication) {
        return ResponseEntity.ok().build();
    }
}
