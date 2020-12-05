package by.bntu.borzdyko.carmax.controller;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.Order;
import by.bntu.borzdyko.carmax.model.Role;
import by.bntu.borzdyko.carmax.security.SecurityUser;
import by.bntu.borzdyko.carmax.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getOrders(@AuthenticationPrincipal SecurityUser user, Model model) {
        if (user.getAuthorities().equals(Role.ADMIN.getAuthorities())) {
            model.addAttribute("orders", orderService.findAll());
            model.addAttribute("isAdmin", true);
        } else {
            model.addAttribute("orders", orderService.findUserOrders(user.getUser()));
            model.addAttribute("isAdmin", false);
        }
        return "/order/orders";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('orders.add')")
    public String addNewOrder(@ModelAttribute("car") Car car,
                              @AuthenticationPrincipal SecurityUser user) {
        Order order = Order.builder().status(false).car(car).user(user.getUser()).build();
        if (!orderService.isPresent(order)) {
            orderService.save(order);
        }
        return "redirect:/user/profile";
    }

    @PostMapping("/confirm")
    @PreAuthorize("hasAuthority('orders.edit')")
    public String confirmOrder(@ModelAttribute("order") Order order) {
        Order actualOrder = orderService.findOne(order.getId());
        actualOrder.setStatus(true);
        orderService.save(actualOrder);
        return "redirect:/orders";
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('orders.delete')")
    public String deleteOrder(@PathVariable("id") Order order) {
        orderService.delete(order);
        return "redirect:/orders";
    }

}
