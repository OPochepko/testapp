package by.pochepko.hes.testapp.controller;

import by.pochepko.hes.testapp.dto.UserAccountDto;
import by.pochepko.hes.testapp.model.UserAccount;
import by.pochepko.hes.testapp.service.CreatedUserAccountDtoValidator;
import by.pochepko.hes.testapp.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @ModelAttribute(name = "totalCount")
    public long getTotalCount() {
        return userAccountService.getTotalCount();
    }

    @ModelAttribute(name = "authority")
    public String getAuthority() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities().toArray()[0].toString();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER','ADMIN') ")
    public String getUserAccountsList(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "4") int size,
            Model model) {
        List<UserAccountDto> users = userAccountService.getUserAccountsList(page - 1, size);
        model.addAttribute("size", size);
        model.addAttribute("currentPage", page);
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
    public String createUserAccount(@ModelAttribute final UserAccountDto user, final BindingResult bindingResult) {
        createdUserValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "new";
        }
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
