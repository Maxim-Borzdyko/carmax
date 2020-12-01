package by.bntu.borzdyko.carmax.controller;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.service.*;
import by.bntu.borzdyko.carmax.util.CarFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("cars", carFilter.filter(brand, sort));
        return "carmax";
    }

    @GetMapping("/{id}/more")
    public String getCarPage(@PathVariable("id") Car car, Model model) {
        model.addAttribute("car", car);
        return "more";
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('cars.write')")
    public String getCars(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("cars", carService.findAll());
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("colors", colorService.getAll());
        model.addAttribute("countries", countryService.getAll());
        model.addAttribute("models", modelService.getAll());
        model.addAttribute("transmissions", transmissionService.getAll());
        model.addAttribute("fuels", fuelService.getAll());
        return "car/cars";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('cars.write')")
    public String addCar(@ModelAttribute Car car,
                         @RequestParam("file") MultipartFile file) {
        car.setFileName(fileService.saveImage(file));
        carService.addCar(car);
        return "redirect:/carmax/list";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('cars.write')")
    public String editCar(@ModelAttribute("id") Car car) {
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
}
