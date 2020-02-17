package by.pochepko.hes.testapp.controller;

import by.pochepko.hes.testapp.dto.UserAccountDto;
import by.pochepko.hes.testapp.model.UserAccount;
import by.pochepko.hes.testapp.service.CreatedUserAccountDtoValidator;
import by.pochepko.hes.testapp.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/user")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private CreatedUserAccountDtoValidator createdUserValidator;

    @ModelAttribute(name = "roles")
    public UserAccount.Role[] getRoles() {
        return UserAccount.Role.values();
    }

    @ModelAttribute(name = "statuses")
    public UserAccount.Status[] getStatuses() {
        return UserAccount.Status.values();
    }

    private int size = 2;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER','ADMIN') ")
    public String getUserAccountsList(
            @RequestParam(required = false, defaultValue = "1") int page,
            Integer count,
            Model model) {
        long totalCount = userAccountService.getTotalCount();
        long lastPage = totalCount / size;
        model.addAttribute("lastPage", lastPage);
        count = page;
        model.addAttribute("count", count);
        List<UserAccountDto> users = userAccountService.getUserAccountsList(page - 1, size);
        model.addAttribute("users", users);
        return "user";
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN') ")
    public String getUserAccountDetails(@PathVariable final long id, final Model model) {
        model.addAttribute("user", userAccountService.getUserAccountById(id));
        return "view";
    }

    @GetMapping(value = "/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createUser(@ModelAttribute final UserAccountDto user) {
        return "new";
    }


    @PostMapping(value = "/new")
    @PreAuthorize("hasAnyAuthority('ADMIN') ")
    public String createUserAccount(@ModelAttribute final UserAccountDto user, final BindingResult bindingResult, Model model) {

        createdUserValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "new";
        }
        model.addAttribute("pageRequest", PageRequest.of(0, 2));
        userAccountService.createUser(user);
        return "success";

    }

    @GetMapping(value = "/{id}/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN') ")
    public String editUser(@PathVariable long id, final Model model) {
        model.addAttribute("user", userAccountService.getUserAccountById(id));

        return "edit";
    }

    @PostMapping(value = "/{id}/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN') ")
    public String updateUserAccount(@ModelAttribute(name = "user") UserAccountDto updatedUserAccountDto, @PathVariable long id, Model model, final BindingResult bindingResult) {
        updatedUserAccountDto.setPassword("mockpassword1");
        createdUserValidator.validate(updatedUserAccountDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return "edit";
        }


        userAccountService.updateUserAccount(updatedUserAccountDto, id);

        return "success";
    }

}
