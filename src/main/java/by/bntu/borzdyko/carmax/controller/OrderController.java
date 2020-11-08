package by.bntu.borzdyko.carmax.controller;

import by.bntu.borzdyko.carmax.model.Order;
import by.bntu.borzdyko.carmax.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // TODO add order get
    @GetMapping("/add")
    @PreAuthorize("hasAuthority('orders.write')")
    public String addNewOrder(Model model) {
        model.addAttribute("order", new Order());
        return "order/add";
    }

    // TODO add order post
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('orders.write')")
    public String addNewOrder(@ModelAttribute("order") Order order) {
        orderService.save(order);
        return "redirect:/user/profile";
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('orders.delete')")
    public String deleteOrder(@PathVariable("id") Order order) {
        orderService.delete(order);
        return "redirect:/user/profile";
    }

}
