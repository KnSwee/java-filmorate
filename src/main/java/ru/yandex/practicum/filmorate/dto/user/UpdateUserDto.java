package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

@Data
public class UpdateUserDto {
    private Long id;
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email;
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public boolean hasEmail() {
        return StringUtils.isNotBlank(this.email);
    }

    public boolean hasName() {
        return StringUtils.isNotBlank(this.name);
    }

    public boolean hasLogin() {
        return StringUtils.isNotBlank(this.login);
    }

    public boolean hasBirthdate() {
        return this.birthday != null;
    }
}
