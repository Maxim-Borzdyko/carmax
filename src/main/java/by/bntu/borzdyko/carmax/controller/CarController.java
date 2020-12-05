package by.bntu.borzdyko.carmax.controller;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.service.*;
import by.bntu.borzdyko.carmax.util.CarFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/carmax")
public class CarController {

    private final CarService carService;
    private final BrandService brandService;
    private final ColorService colorService;
    private final ModelService modelService;
    private final CountryService countryService;
    private final TransmissionService transmissionService;
    private final FuelService fuelService;

    private final CarFilter carFilter;
    private final FileService fileService;

    @Autowired
    public CarController(CarService carService, BrandService brandService,
                         ColorService colorService, ModelService modelService, CountryService countryService,
                         TransmissionService transmissionService, FuelService fuelService, CarFilter carFilter,
                         FileService fileService) {
        this.carService = carService;
        this.brandService = brandService;
        this.colorService = colorService;
        this.modelService = modelService;
        this.countryService = countryService;
        this.transmissionService = transmissionService;
        this.fuelService = fuelService;
        this.carFilter = carFilter;
        this.fileService = fileService;
    }

    @GetMapping
    public String getMainPage(@RequestParam(value = "brand", required = false) Brand brand,
                              @RequestParam(value = "sort", required = false) String sort,
                              Model model) {
        model.addAttribute("brands", brandService.findAll());
        List<Car> cars = carFilter.filter(brand);
        cars = carFilter.sort(cars, sort);
        model.addAttribute("cars", cars);
        return "carmax";
    }

    @GetMapping("/{id}/more")
    public String getCarPage(@PathVariable("id") Car car, Model model) {
        model.addAttribute("car", car);
        return "more";
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('cars.write')")
    public String getCars(@ModelAttribute("car") Car car, Model model) {
        getAllCategories(model);
        return "car/cars";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('cars.write')")
    public String addCar(@RequestParam("file") MultipartFile file,
                         @ModelAttribute("car") @Valid Car car,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/carmax/list";
        }
        car.setFileName(fileService.saveImage(file));
        carService.addCar(car);
        return "redirect:/carmax/list";
    }

    @GetMapping("/edit")
    @PreAuthorize("hasAuthority('cars.write')")
    public String getEditPage(@ModelAttribute Car car, Model model) {
        model.addAttribute("car", carService.findOne(car.getId()));
        getAllCategories(model);
        return "car/edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('cars.write')")
    public String editCar(@RequestParam("file") MultipartFile file,
                          @ModelAttribute("car") @Valid Car car,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "car/edit";
        }
        if (!file.isEmpty()) {
            if (car.getFileName() != null) {
                fileService.deleteImage(car.getFileName());
            }
            car.setFileName(fileService.saveImage(file));
        }
        carService.save(car);
        return "redirect:/carmax/list";
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('cars.delete')")
    public String deleteCar(@PathVariable("id") Car car) {
        fileService.deleteImage(car.getFileName());
        carService.delete(car);
        return "redirect:/carmax/list";
    }

    private void getAllCategories(Model model) {
        model.addAttribute("cars", carService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("countries", countryService.findAll());
        model.addAttribute("models", modelService.findAll());
        model.addAttribute("transmissions", transmissionService.findAll());
        model.addAttribute("fuels", fuelService.findAll());
    }
}
