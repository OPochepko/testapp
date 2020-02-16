package by.pochepko.hes.testapp.controller;

import by.pochepko.hes.testapp.dto.UserAccountDto;
import by.pochepko.hes.testapp.model.UserAccount;
import by.pochepko.hes.testapp.service.CreatedUserAccountDtoValidator;
import by.pochepko.hes.testapp.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/user")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private CreatedUserAccountDtoValidator createdUserValidator;

    @ModelAttribute(name = "authority")
    public String getAuthority() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities().toArray()[0].toString();
    }


    @ModelAttribute(name = "roles")
    public UserAccount.Role[] getRoles() {
        return UserAccount.Role.values();
    }

    @ModelAttribute(name = "lastPage")
    public int getLastPage() {
        return userAccountService.getTotalPages(PageRequest.of(0, 5));
    }

    @ModelAttribute(name = "statuses")
    public UserAccount.Status[] getStatuses() {
        return UserAccount.Status.values();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER','ADMIN') ")
    public String getUserAccountsList(
            @RequestParam(required = false) Optional<Integer> page,
            Model model) {
        PageRequest pageRequest = PageRequest.of(page.orElse(1) - 1, 2);
        model.addAttribute("pageRequest", pageRequest);

        List<UserAccountDto> users = userAccountService.findPaginated(pageRequest);
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
    public String createUser(@Valid @ModelAttribute final UserAccountDto user) {
        return "new";
    }


    @PostMapping(value = "/new")
    @PreAuthorize("hasAnyAuthority('ADMIN') ")
    public String createUserAccount(@Valid @ModelAttribute final UserAccountDto user, final BindingResult bindingResult, Model model) {

        createdUserValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "new";
        }
        model.addAttribute("pageRequest", PageRequest.of(0, 2));
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
