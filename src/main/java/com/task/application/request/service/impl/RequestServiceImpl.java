package com.task.application.request.service.impl;

import com.task.application.request.dto.CreateRequestDto;
import com.task.application.request.dto.RequestDto;
import com.task.application.request.entity.Request;
import com.task.application.request.entity.User;
import com.task.application.request.exception.BadRequestException;
import com.task.application.request.exception.RequestNotFoundException;
import com.task.application.request.exception.UserForbiddenException;
import com.task.application.request.exception.UserNotFoundException;
import com.task.application.request.mapper.RequestMapper;
import com.task.application.request.repository.RequestRepository;
import com.task.application.request.service.RequestService;
import com.task.application.request.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.task.application.request.dto.Status.DRAFT;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestMapper requestMapper;
    private final UserValidatePermission userValidate;
    private final RequestCheckStatus checkStatus;
    private final UserService userService;
    private final RequestRepository requestRepository;

    @Override
    public RequestDto addRequest(CreateRequestDto createRequestDto, Authentication authentication) {

        User user = userService.findUserByName(authentication.getName());

        if (userValidate.isUser(user)) {

            Request newRequest = Request.builder()
                    .title(createRequestDto.getTitle())
                    .description(createRequestDto.getDescription())
                    .createdAt(LocalDateTime.now())
                    .status(DRAFT.name())
                    .user(user)
                    .build();

            return requestMapper.entityToDto(requestRepository.save(newRequest));
        } else {
            throw new UserForbiddenException(user.getId());
        }
    }

    @Override
    public RequestDto updateRequest(Integer reqId,
                                    CreateRequestDto createRequestDto,
                                    Authentication authentication) {

        User user = userService.findUserByName(authentication.getName());

        if (userValidate.isUser(user)) {

            Request changeRequest = requestRepository.findByIdAndAndStatusAndUserId(reqId, DRAFT.name(), user.getId())
                    .orElseThrow(BadRequestException::new);

            changeRequest.setTitle(createRequestDto.getTitle());
            changeRequest.setDescription(createRequestDto.getDescription());

            requestRepository.save(changeRequest);
            return requestMapper.entityToDto(changeRequest);

        } else {
            throw new UserForbiddenException(user.getId());
        }
    }

    @Override
    public RequestDto getRequestById(Integer reqId, Authentication authentication) {

        User user = userService.findUserByName(authentication.getName());

        if (userValidate.isAdmin(user)) {
            throw new UserForbiddenException(user.getId());
        } else {

            Request findRequest = findRequestById(reqId);

            if (userValidate.isOperator(user) && checkStatus.isSent(findRequest)) {

                String newTitle = findRequest.getTitle().replace("", "-");
                newTitle = newTitle.substring(1, newTitle.length() - 1);
                findRequest.setTitle(newTitle);
                return requestMapper.entityToDto(findRequest);

            } else if (userValidate.isRequestOwner(user, findRequest)) {
                return requestMapper.entityToDto(findRequest);

            } else {
                throw new BadRequestException();
            }
        }

    }


    @Override
    public void setStatus(Integer reqId, String status, Authentication authentication) {
        User user = userService.findUserByName(authentication.getName());
        Request changeRequest = findRequestById(reqId);

        if (userValidate.isOperator(user)
                && checkStatus.isSent(changeRequest)
                && (checkStatus.isAccepted(status)) || checkStatus.isRejected(status)) {

            changeRequest.setStatus(status.toUpperCase());
            requestRepository.save(changeRequest);

        } else if (userValidate.isUser(user)
                && checkStatus.isSent(status)
                && checkStatus.isDraft(changeRequest)) {

            changeRequest.setStatus(status.toUpperCase());
            requestRepository.save(changeRequest);
        } else {
            throw new UserForbiddenException(user.getId());
        }
    }

    @Override
    public List<RequestDto> getAllUserRequests(Integer page,
                                               Authentication authentication,
                                               String sortBy,
                                               String orderBy) {

        User user = userService.findUserByName(authentication.getName());

        if (userValidate.isUser(user)) {
            List<Request> allRequests = requestDao.getAllUserRequest(page, user.getId(), sortBy, orderBy);
            return requestMapper.entityToDto(allRequests);
        } else {
            throw new UserForbiddenException(user.getId());
        }

    }

    @Override
    public List<RequestDto> getAllSentRequests(Integer page,
                                               Authentication authentication,
                                               String sortBy,
                                               String orderBy) {

        User user = userService.findUserByName(authentication.getName());

        if (userValidate.isOperator(user)) {
            List<Request> requests = requestDao.getAllSentRequests(page, sortBy, orderBy);
            return requestMapper.entityToDto(requests);
        } else {
            throw new UserForbiddenException(user.getId());
        }
    }

    @Override
    public List<RequestDto> getAllSentRequestsByPartUserName(Integer page,
                                                             String name,
                                                             Authentication authentication,
                                                             String sortBy, String orderBy) {

        User user = userService.findUserByName(authentication.getName());

        if (userValidate.isOperator(user)) {
            User reqUser = userDao.getUserByPartOfName(name)
                    .orElseThrow(() -> new UserNotFoundException(name));
            List<Request> requests = requestDao.getAllSentRequestByUser(reqUser.getId(), page, sortBy, orderBy);
            return requestMapper.entityToDto(requests);
        } else {
            throw new UserForbiddenException(user.getId());
        }
    }

    private Request findRequestById(Integer id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));
    }


}
