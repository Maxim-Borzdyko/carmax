package by.bntu.borzdyko.carmax.controller;

import by.bntu.borzdyko.carmax.model.Role;
import by.bntu.borzdyko.carmax.model.User;
import by.bntu.borzdyko.carmax.security.SecurityUser;
import by.bntu.borzdyko.carmax.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('users.read')")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.findAllByRole(Role.USER));
        return "user/users";
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal SecurityUser user, Model model) {
        model.addAttribute("user", user.getUser());
        return "profile";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('users.update')")
    public String updateUser(@AuthenticationPrincipal SecurityUser securityUser,
                             @ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "profile";
        }
        securityUser.setUser(userService.updateUser(securityUser.getUser(), user));
        return "redirect:/user/profile";
    }

    @PostMapping("/ban")
    @PreAuthorize("hasAuthority('users.ban')")
    public String banUser(@ModelAttribute("user") User user) {
        User actualUser = userService.findOne(user.getId());
        actualUser.setStatus(!actualUser.getStatus());
        userService.save(actualUser);
        return "redirect:/user";
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('users.delete')")
    public String deleteUser(@PathVariable("id") User user) {
        userService.delete(user);
        return "redirect:/user/profile";
    }
}
