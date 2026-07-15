package com.rzk.userservice.controller;

import com.rzk.userservice.dto.LoginRequest;
import com.rzk.userservice.dto.UserFullProfile;
import com.rzk.userservice.model.Role;
import com.rzk.userservice.model.User;
import com.rzk.userservice.service.UserService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(service.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable @Min(1) Long id) {
        return new ResponseEntity<>(service.getUserById(id), HttpStatus.OK);
    }

    //http://localhost:8765/api/users  body {
//        {
//            "firstName": "Bojana",
//                "lastName": "Bojanic",
//                "email": "bojana@mail.com",
//                "driverLicenseNumber": "SRB123456",
//                "password": "12345"
//        }
    @PostMapping
    @Retry(name = "userServiceRetry", fallbackMethod = "userServiceFallback")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        return new ResponseEntity<>(service.createUser(user), HttpStatus.CREATED);
    }

    public ResponseEntity<User> userServiceFallback(RequestNotPermitted ex) {
        return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
    }

    //http://localhost:8765/api/users/5  {
    //  "firstName": "Sara",
    //  "lastName": "Maric",
    //  "email": "test@mail.com"
    //}
    @PutMapping("/{idUser}")
    public ResponseEntity<User> updateUser(@PathVariable Long idUser, @RequestBody User user) {
        return new ResponseEntity<>(service.updateUser(idUser, user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
    }

    //http://localhost:8765/api/users/5/roles?roleName=ROLE_ADMIN
    @PostMapping("/{id}/roles")
    public ResponseEntity<User> addRole(@PathVariable Long id, @RequestParam String roleName) {
        return new ResponseEntity<>(service.addRoleToUser(id, roleName), HttpStatus.CREATED);
    }

    // DELETE /api/users/{id}/roles?roleName=ROLE_ADMIN
    @DeleteMapping("/{id}/roles")
    public ResponseEntity<User> removeRole(@PathVariable Long id, @RequestParam String roleName) {
        return new ResponseEntity<>(service.removeRoleFromUser(id, roleName), HttpStatus.OK);
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<Set<Role>> getRoles(@PathVariable Long id) {
        return new ResponseEntity<>(service.getRolesOfUser(id), HttpStatus.OK);
    }

    //  http://localhost:8765/api/users/fullProfile/5
    @GetMapping("/fullProfile/{id}")
    public ResponseEntity<UserFullProfile> getFullProfile(@PathVariable Long id){
        return new ResponseEntity<>(service.getFullProfile(id), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) {
        String token = service.login(loginRequest);
        // Vraćamo čist token nazad frontu (Angularu)
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/byEmail")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email){
        return new ResponseEntity<>(service.getUserByEmail(email), HttpStatus.OK);
    }
}
