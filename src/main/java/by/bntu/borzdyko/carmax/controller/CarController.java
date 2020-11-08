package by.bntu.borzdyko.carmax.controller;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.service.CarService;
import by.bntu.borzdyko.carmax.util.CarFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carmax")
public class CarController {

    private final CarService carService;
    private final CarFilter carFilter;

    @Autowired
    public CarController(CarService carService, CarFilter carFilter) {
        this.carService = carService;
        this.carFilter = carFilter;
    }

    @GetMapping
    public String getMainPage(@RequestParam(value = "brand", required = false) Brand brand,
                              @RequestParam(value = "sort", required = false) String sortType,
                              Model model) {
        model.addAttribute("brands", carService.findAllBrands());

        if (sortType == null) {
            model.addAttribute("cars", carFilter.filterByBrand(brand));
        } else {
            model.addAttribute("cars", carService.sortByType(sortType));
        }

        return "carmax";
    }

    @GetMapping("/{id}/more")
    public String getCarPage(@PathVariable("id") Car car, Model model) {
        model.addAttribute("car", car);
        return "more";
    }

    // TODO Add new Car get
    @GetMapping("/add")
    @PreAuthorize("hasAuthority('cars.write')")
    public String addNewCar(Model model) {
        model.addAttribute("car", new Car());
        return "";
    }

    // TODO Add new Car post
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('cars.write')")
    public String addNewCar(@ModelAttribute("car") Car car) {
        carService.addCar(car);
        return "redirect:/carmax";
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('cars.delete')")
    public String deleteCar(@PathVariable("id") Car car) {
        carService.delete(car);
        return "redirect:/carmax";
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAuthority('cars.write')")
    public String updateUser(@PathVariable("id") Car car,
                             @ModelAttribute("car") Car newCar) {
        carService.updateCar(car, newCar);
        return "redirect:/carmax";
    }
}
