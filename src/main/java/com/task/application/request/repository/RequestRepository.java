package com.task.application.request.repository;

import com.task.application.request.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    Optional<Request> findByIdAndAndStatusAndUserId(Integer reqId, String status, Integer userId);
}
