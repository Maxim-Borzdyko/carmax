package by.bntu.borzdyko.carmax.controller;

import by.bntu.borzdyko.carmax.model.Role;
import by.bntu.borzdyko.carmax.model.User;
import by.bntu.borzdyko.carmax.security.SecurityUser;
import by.bntu.borzdyko.carmax.service.OrderService;
import by.bntu.borzdyko.carmax.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal SecurityUser user) {
        model.addAttribute("user", user.getUser());

        if (user.getUser().getRole().equals(Role.ADMIN)) {
            model.addAttribute("users", userService.findAllByRole(Role.USER));
            model.addAttribute("orders", orderService.findAll());
        }

        return "profile";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('users.update')")
    public String updateUser(@AuthenticationPrincipal SecurityUser securityUser,
                             @ModelAttribute("user") User user) {
        securityUser.setUser(userService.updateUser(securityUser.getUser(), user));
        return "redirect:/user/profile";
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('users.delete')")
    public String deleteUser(@PathVariable("id") User user) {
        userService.delete(user);
        return "redirect:/user/profile";
    }
}
