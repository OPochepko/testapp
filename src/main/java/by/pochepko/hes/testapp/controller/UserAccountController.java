package by.pochepko.hes.testapp.controller;

import by.pochepko.hes.testapp.dto.UserAccountDto;
import by.pochepko.hes.testapp.model.UserAccount;
import by.pochepko.hes.testapp.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/user")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;
    @Qualifier("createdUserAccountDtoValidator")
    @Autowired
    private Validator createdUserValidator;

    @ModelAttribute(name = "roles")
    public UserAccount.Role[] getRoles() {
        return UserAccount.Role.values();
    }

    @ModelAttribute(name = "statuses")
    public UserAccount.Status[] getStatuses() {
        return UserAccount.Status.values();
    }

    @ModelAttribute(name = "users")
    public List<UserAccountDto> getUsers() {
        return userAccountService.getUserAccountList();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER','ADMIN') ")
    public String getUserAccountList(UserAccountDto userAccountDto, final Model model, final BindingResult bindingResult) {
        return "user";
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN') ")
    public String getUserAccountById(@PathVariable final long id, final Model model, final BindingResult bindingResult) {
        model.addAttribute("user", userAccountService.getUserAccountById(id));
        return "view";
    }

    @GetMapping(value = "/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createUser(@Valid @ModelAttribute final UserAccountDto user, final Model model, final BindingResult bindingResult) {
        return "new";
    }


    @PostMapping(value = "/new")
    @PreAuthorize("hasAnyAuthority('ADMIN') ")

    public String createUserAccount(@Valid @ModelAttribute final UserAccountDto user, final BindingResult bindingResult, final Model model) {

        createdUserValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "new";
        }

        userAccountService.createUser(user);
        return "user";

    }

    @GetMapping(value = "/{id}/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN') ")

    public String editUser(@PathVariable long id, final Model model) {
        model.addAttribute("user", userAccountService.getUserAccountById(id));

        return "edit";
    }

    @PostMapping(value = "/{id}/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN') ")

    public String updateUserAccount(UserAccountDto updatedUserAccountDto, @PathVariable long id, Model model) {
        model.addAttribute("user", updatedUserAccountDto);

        userAccountService.updateUserAccount(updatedUserAccountDto, id);

        return "success";
    }

}
