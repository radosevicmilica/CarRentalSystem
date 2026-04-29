package com.rzk.userservice.dto;

import com.rzk.userservice.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserFullProfile {
    private User user;
    private List<RentalWithBillDto> rentals;
}
