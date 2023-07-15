package com.siukatech.poc.react.backend.app.web.controller.extended;

import com.siukatech.poc.react.backend.parent.business.service.UserService;
import com.siukatech.poc.react.backend.parent.data.entity.UserEntity;
import com.siukatech.poc.react.backend.parent.data.repository.UserRepository;
import com.siukatech.poc.react.backend.parent.web.annotation.v1.ProtectedApiV1Controller;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;

@ProtectedApiV1Controller
public class UserController extends com.siukatech.poc.react.backend.parent.web.controller.UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    private final UserRepository userRepository;
    private UserService userService;
    public UserController(UserService userService
//            , UserRepository userRepository
    ) {
        super(userService);
//        this.userRepository = userRepository;
    }

////    @PostMapping("/users/my/public-key")
////    public ResponseEntity<?> getMyPublicKey(Authentication authentication) {
////        String userId = authentication.getName();
////        final String finalUserId = userId;
////        UserEntity userEntity = userRepository.findByUserId(userId)
////                .orElseThrow(() -> new EntityNotFoundException("No such user [" + finalUserId + "]"));
////        ;
////        String publicKeyBase64 = userEntity.getPublicKey();
////        return ResponseEntity.ok(publicKeyBase64);
////    }
//    @PostMapping("/users/{targetUserId}/public-key")
//    public ResponseEntity<?> getPublicKey(@PathVariable String targetUserId) {
//        return super.getPublicKey(targetUserId);
//    }


//    @PostMapping("/users/{userId}")
//    public ResponseEntity<?> getPublicKey(Authentication authentication
//            , @PathVariable(name = "userId") String reqUserId) {
//        String userId = authentication.getName();
//        final String finalUserId = userId;
//        UserEntity userEntity = userRepository.findByUserId(userId)
//                .orElseThrow(() -> new EntityNotFoundException("No such user [" + finalUserId + "]"));
//        ;
//        String publicKeyBase64 = userEntity.getPublicKey();
//        return ResponseEntity.ok(publicKeyBase64);
//    }
    @PostMapping("/users/{targetUserId}/user-info")
    public ResponseEntity<?> getUserInfo(@PathVariable(name = "targetUserId") String reqUserId) {
        return super.getUserInfo(reqUserId);
    }

}
