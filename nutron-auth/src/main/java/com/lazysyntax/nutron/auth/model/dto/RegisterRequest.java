package com.lazysyntax.nutron.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lazysyntax.nutron.auth.model.entity.User;
import com.lazysyntax.nutron.auth.model.entity.UserSetup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @JsonProperty("id")
    private String uuid;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("userSetup")
    private UserSetupDto userSetup;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSetupDto {
        private String weight;
        private String height;
        private String gender;
        private String age;
        private String activity;
        private String goal;
        private String formula;
    }

    public User toEntity() {
        User user = User.builder()
                .id(this.uuid)
                .userName(this.userName)
                .fullName(this.fullName)
                .email(this.email)
                .password(this.password)
                .build();

        if (this.userSetup != null) {
            UserSetup setup = UserSetup.builder()
                    .weight(userSetup.getWeight())
                    .height(userSetup.getHeight())
                    .gender(userSetup.getGender())
                    .age(userSetup.getAge())
                    .activity(userSetup.getActivity())
                    .goal(userSetup.getGoal())
                    .formula(userSetup.getFormula())
                    .user(user)
                    .build();
            user.setUserSetup(setup);
        }
        return user;
    }
}
