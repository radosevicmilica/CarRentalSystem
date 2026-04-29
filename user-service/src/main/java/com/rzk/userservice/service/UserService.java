package com.rzk.userservice.service;

import com.rzk.userservice.dto.*;
import com.rzk.userservice.exception.EmailAlreadyExistsException;
import com.rzk.userservice.exception.EntityDoesNotExistException;
import com.rzk.userservice.exception.RoleNotFoundException;
import com.rzk.userservice.model.Role;
import com.rzk.userservice.model.User;
import com.rzk.userservice.proxy.BillingClient;
import com.rzk.userservice.proxy.RentalClient;
import com.rzk.userservice.repository.RoleRepository;
import com.rzk.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository ur;
    private final RoleRepository rr;

    private final RentalClient rentalClient;
    private final BillingClient billingClient;

    public List<User> getAllUsers() {
        return ur.findAll();
    }

    public User getUserById(Long id) {
        return ur.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistException("User with id: " + id + " not found"));
    }

    public User createUser(User user) {
        if (ur.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }

        // Kreiraj korisnika sa ROLE_USER po defaultu
        Role defaultRole = rr.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        user.getRoles().add(defaultRole);
        return ur.save(user);
    }

    // ne moze da menja role
    public User updateUser(Long idUser, User updatedUser) {
        User user = getUserById(idUser);

        if (updatedUser.getFirstName() != null) {
            user.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            user.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getDriverLicenseNumber() != null) {
            user.setDriverLicenseNumber(updatedUser.getDriverLicenseNumber());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(updatedUser.getPassword());
        }

        return ur.save(user);
    }

    public void deleteUser(Long id) {
        ur.deleteById(id);
    }

    public User addRoleToUser(Long userId, String roleName) {
        User user = getUserById(userId);
        Role role = rr.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role " +roleName+ " not found"));
        user.getRoles().add(role);
        return ur.save(user);
    }

    public User removeRoleFromUser(Long userId, String roleName) {
        User user = getUserById(userId);
        Role role = rr.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role " +roleName+ " not found"));
        user.getRoles().remove(role);
        return ur.save(user);
    }

    public Set<Role> getRolesOfUser(Long userId) {
        User user = getUserById(userId);
        return user.getRoles();
    }

    public UserFullProfile getFullProfile(Long userId) {

        User user = getUserById(userId);
        List<RentalDto> rentals = rentalClient.getRentalsByUserId(userId);

        List<RentalWithBillDto> result = new ArrayList<>();

        for (RentalDto rental : rentals) {
            BillDto bill = billingClient.getBillByRentalId(rental.getId());

            RentalWithBillDto dto = new RentalWithBillDto();
            dto.setRental(rental);
            dto.setBill(bill);

            result.add(dto);
        }

        UserFullProfile profile = new UserFullProfile();
        profile.setUser(user);
        profile.setRentals(result);

        return profile;
    }
}


