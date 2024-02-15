package com.api.role;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.auth.AuthenticationService;
import com.api.user.CustomUserDetails;
import com.api.utils.ApiConstant;
import com.api.utils.ApiResponse;

@RestController()
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping()
    public ResponseEntity<Object> getRoleList() {
        Optional<CustomUserDetails> userDetailsOptional = this.authenticationService.getUserDetails();

        if (userDetailsOptional.isPresent()) {
            Set<Role> roleSet = this.roleService.findByUserId(userDetailsOptional.get().getUser().getId());
            return ResponseEntity.status(ApiConstant.STATUS_200)
                    .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                            .data(roleSet)
                            .build());
        }

        return ResponseEntity.status(ApiConstant.STATUS_500)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                        .data("Something went wrong")
                        .build());
    }

}
