package by.bntu.borzdyko.carmax.controller;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.*;
import by.bntu.borzdyko.carmax.repository.CarRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-car-before.sql", "/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-car-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CarControllerTest {

    private static final Car CAR = Car.builder()
            .id(2L).price(new BigDecimal("200")).yearOfIssue(2001).mileage(200D).fileName("")
            .brand(Brand.builder().id(1L).name("Audi").build())
            .color(Color.builder().id(1L).name("White").build())
            .country(Country.builder().id(1L).name("Belarus").build())
            .model(Model.builder().id(1L).name("Minivan").build())
            .transmission(Transmission.builder().id(1L).type("Automatic").build())
            .fuel(Fuel.builder().id(1L).type("Diesel").build())
            .build();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CarRepository carRepository;

    private static MultiValueMap<String, String> correctCarParams;
    private static MultiValueMap<String, String> wrongCarParams;
    private static MultiValueMap<String, String> categories;
    private static MockMultipartFile file;
    private static MockMultipartFile emptyFile;

    @BeforeClass
    public static void setUp() {
        correctCarParams = new LinkedMultiValueMap<>();
        correctCarParams.add("price", "100");
        correctCarParams.add("mileage", "100");
        correctCarParams.add("yearOfIssue", "2000");
        correctCarParams.add("fileName", "");

        wrongCarParams = new LinkedMultiValueMap<>();
        wrongCarParams.add("price", "-100");
        wrongCarParams.add("mileage", "-200");
        wrongCarParams.add("yearOfIssue", "10000");
        wrongCarParams.add("fileName", "");

        categories = new LinkedMultiValueMap<>();
        categories.add("brand", "1");
        categories.add("color", "1");
        categories.add("country", "1");
        categories.add("fuel", "1");
        categories.add("transmission", "1");
        categories.add("model", "1");

        file = new MockMultipartFile(
                "file", "test.jpg",
                MediaType.IMAGE_JPEG_VALUE, "test.jpg".getBytes()
        );

        emptyFile = new MockMultipartFile(
                "file", "",
                MediaType.IMAGE_JPEG_VALUE, "".getBytes()
        );
    }

    @Test
    public void getMainPage_tryToGetMainPage_mainPage() throws Exception {
        this.mockMvc.perform(get("/carmax"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("cars", hasSize(4)))
                .andExpect(model().attribute("cars", hasItem(CAR)))
                .andExpect(model().attribute("brands", hasSize(2)))
                .andExpect(model().attribute("brands", hasItem(CAR.getBrand())))
                .andExpect(view().name("carmax"));
    }

    @Test
    public void getMainPage_tryToGetMainPageWithBrand_mainPageFiltered() throws Exception {
        this.mockMvc.perform(get("/carmax")
                .param("brand", CAR.getBrand().getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("cars", hasSize(2)))
                .andExpect(model().attribute("cars", hasItem(CAR)))
                .andExpect(model().attribute("brands", hasSize(2)))
                .andExpect(model().attribute("brands", hasItem(CAR.getBrand())))
                .andExpect(view().name("carmax"));
    }

    @Test
    public void getMainPage_tryToGetMainPageWithSort_mainPageSorted() throws Exception {
        this.mockMvc.perform(get("/carmax")
                .param("sort", "price"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("cars", hasSize(4)))
                .andExpect(model().attribute("cars", hasItem(CAR)))
                .andExpect(model().attribute("brands", hasSize(2)))
                .andExpect(model().attribute("brands", hasItem(CAR.getBrand())))
                .andExpect(view().name("carmax"));

        assertTrue(carRepository.findAll().contains(CAR));
    }

    @Test
    public void getMainPage_tryToGetMainPageWithBrandAndSort_mainPageSortedAndFiltered() throws Exception {
        this.mockMvc.perform(get("/carmax")
                .param("brand", CAR.getBrand().getId().toString())
                .param("sort", "price"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("cars", hasSize(2)))
                .andExpect(model().attribute("cars", hasItem(CAR)))
                .andExpect(model().attribute("brands", hasSize(2)))
                .andExpect(view().name("carmax"));

        assertTrue(carRepository.findAllByBrand(CAR.getBrand()).contains(CAR));
    }

    @Test
    public void getCarPage_tryToGetMoreInfo_morePage() throws Exception {
        this.mockMvc.perform(get("/carmax/" + CAR.getId().toString() + "/more"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("car", CAR))
                .andExpect(view().name("more"));

        assertEquals(CAR, carRepository.getOne(CAR.getId()));
    }

    @Test
    public void getCarPage_tryToGetMoreInfoOnNotExistedCarId_morePage() throws Exception {
        this.mockMvc.perform(get("/carmax/100/more"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/carmax"));
    }

    @Test
    public void getCars_tryNotAuthenticated_redirect3xxLogin() throws Exception {
        this.mockMvc.perform(get("/carmax/list"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }

    @Test
    @WithUserDetails("user1@test.com")
    public void getCars_tryToGetCarsWithoutPermissions_forbidden() throws Exception {
        this.mockMvc.perform(get("/carmax/list"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin@test.com")
    public void getCars_tryWithAdmin_carsPage() throws Exception {
        this.mockMvc.perform(get("/carmax/list"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("car"))
                .andExpect(model().attribute("cars", hasSize(4)))
                .andExpect(model().attribute("cars", hasItem(CAR)))
                .andExpect(model().attribute("brands", hasSize(2)))
                .andExpect(model().attribute("colors", hasSize(2)))
                .andExpect(model().attribute("countries", hasSize(2)))
                .andExpect(model().attribute("models", hasSize(2)))
                .andExpect(model().attribute("fuels", hasSize(2)))
                .andExpect(model().attribute("transmissions", hasSize(2)))
                .andExpect(view().name("car/cars"));

        assertTrue(carRepository.findAll().contains(CAR));
    }

    @Test
    @WithUserDetails("admin@test.com")
    public void addCar_tryWithCorrectInputs_carsPage() throws Exception {
        int expectedSize = carRepository.findAll().size() + 1;

        this.mockMvc.perform(multipart("/carmax/add").file(file)
                .params(correctCarParams).params(categories))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/carmax/list"));

        assertEquals(expectedSize, carRepository.findAll().size());
    }

    @Test
    @WithUserDetails("admin@test.com")
    public void addCar_tryWithWrongInputs_carsPage() throws Exception {
        int expectedSize = carRepository.findAll().size();

        this.mockMvc.perform(multipart("/carmax/add").file(file).params(wrongCarParams).params(categories))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(flash().attributeExists("car"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/carmax/list"));

        assertEquals(expectedSize, carRepository.findAll().size());
    }

    @Test
    public void getEditPage_tryNotAuthenticated_redirect3xxLogin() throws Exception {
        this.mockMvc.perform(get("/carmax/edit")
                .param("id", CAR.getId().toString()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }

    @Test
    @WithUserDetails("user1@test.com")
    public void getEditPage_tryWithoutPermissions_forbidden() throws Exception {
        this.mockMvc.perform(get("/carmax/edit")
                .param("id", CAR.getId().toString()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin@test.com")
    public void getEditPage_tryWithAdmin_editPage() throws Exception {
        this.mockMvc.perform(get("/carmax/edit")
                .param("id", CAR.getId().toString()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attribute("brands", hasSize(2)))
                .andExpect(model().attribute("colors", hasSize(2)))
                .andExpect(model().attribute("countries", hasSize(2)))
                .andExpect(model().attribute("models", hasSize(2)))
                .andExpect(model().attribute("fuels", hasSize(2)))
                .andExpect(model().attribute("transmissions", hasSize(2)))
                .andExpect(model().attribute("car", CAR))
                .andExpect(view().name("car/edit"));

        assertEquals(CAR, carRepository.getOne(CAR.getId()));
    }

    @Test
    @WithUserDetails("admin@test.com")
    public void getEditPage_tryWithAdmin_listPage() throws Exception {
        this.mockMvc.perform(get("/carmax/edit")
                .param("id", "-100"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/carmax/list"));
    }

    @Test
    @WithUserDetails("admin@test.com")
    public void editCar_tryWithCorrectInputs_carsPage() throws Exception {
        Integer expectedYear = 2000;

        this.mockMvc.perform(multipart("/carmax/edit").file(file)
                .param("id", CAR.getId().toString())
                .params(correctCarParams).params(categories))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/carmax/list"));

        assertEquals(expectedYear, carRepository.getOne(CAR.getId()).getYearOfIssue());
    }

    @Test
    @WithUserDetails("admin@test.com")
    public void editCar_tryWithWrongInputs_editPageWithErrorMsg() throws Exception {
        this.mockMvc.perform(multipart("/carmax/edit").file(file)
                .param("id", CAR.getId().toString())
                .params(wrongCarParams).params(categories))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("car/edit"));

        assertEquals(CAR, carRepository.getOne(CAR.getId()));
    }

    @Test
    @WithUserDetails("admin@test.com")
    public void editCar_tryWithEmptyFile_carsPage() throws Exception {
        String expectedFileName = "";

        this.mockMvc.perform(multipart("/carmax/edit").file(emptyFile)
                .param("id", CAR.getId().toString())
                .params(correctCarParams).params(categories))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/carmax/list"));

        assertEquals(expectedFileName, carRepository.getOne(CAR.getId()).getFileName());
    }

    @Test
    public void deleteCar_tryNotAuthenticated_redirect3xxLogin() throws Exception {
        this.mockMvc.perform(delete("/carmax/2/delete"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }

    @Test
    @WithUserDetails("user1@test.com")
    public void deleteCar_tryWithoutPermissions_forbidden() throws Exception {
        this.mockMvc.perform(delete("/carmax/2/delete"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin@test.com")
    public void deleteCar_tryWithAdmin_carsPage() throws Exception {
        int size = carRepository.findAll().size() - 1;

        this.mockMvc.perform(delete("/carmax/2/delete"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/carmax/list"));

        assertEquals(size, carRepository.findAll().size());
    }
}