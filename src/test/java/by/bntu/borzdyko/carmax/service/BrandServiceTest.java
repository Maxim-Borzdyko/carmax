package by.bntu.borzdyko.carmax.service;

import by.bntu.borzdyko.carmax.model.Car;
import by.bntu.borzdyko.carmax.model.description.Brand;
import by.bntu.borzdyko.carmax.repository.BrandRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BrandServiceTest {

    @Mock
    BrandRepository brandRepository;

    @InjectMocks
    BrandService brandService;

    @Test
    public void findOne_checkWithIdInDatabase_car() {
        Long id = 1L;
        Brand expected = Brand.builder().id(id).build();

        when(brandRepository.getOne(id)).thenReturn(expected);

        Brand actual = brandService.findOne(id);

        assertEquals(expected, actual);
        verify(brandRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(brandRepository);
    }

    @Test
    public void findOne_checkWithIdNotInDatabase_null() {
        Long id = 10L;
        Brand expected = null;

        Brand actual = brandService.findOne(id);

        assertEquals(expected, actual);
        verify(brandRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(brandRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOne_checkWithIdIsNull_IllegalArgumentException() {
        Long id = null;

        when(brandRepository.getOne(id)).thenThrow(new IllegalArgumentException());

        Brand actual = brandService.findOne(id);
        verify(brandRepository, times(1)).getOne(id);
        verifyNoMoreInteractions(brandRepository);
    }

    @Test
    public void findAll_carsExistsInDatabase_cars() {
        List<Brand> expected = List.of(new Brand(), new Brand());

        when(brandRepository.findAll()).thenReturn(expected);

        List<Brand> actual = brandService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(brandRepository, times(1)).findAll();
        verifyNoMoreInteractions(brandRepository);
    }

    @Test
    public void findAll_carsNotExistsInDatabase_emptyList() {
        List<Brand> expected = List.of();

        when(brandRepository.findAll()).thenReturn(expected);

        List<Brand> actual = brandService.findAll();

        assertArrayEquals(expected.toArray(), actual.toArray());
        verify(brandRepository, times(1)).findAll();
        verifyNoMoreInteractions(brandRepository);
    }

    @Test
    public void addBrand_tryWithNormalBrand_true() {
        Brand brand = new Brand(1L, "new_brand", List.of(new Car()));

        assertTrue(brandService.addBrand(brand));
        verify(brandRepository, times(1)).findByName(brand.getName());
        verify(brandRepository, times(1)).save(brand);
        verifyNoMoreInteractions(brandRepository);
    }

    @Test
    public void addBrand_tryWithNull_false() {
        assertFalse(brandService.addBrand(null));
    }

    @Test
    public void addBrand_tryWithBrandWhichIsAlsoInDatabase_false() {
        Brand brand = new Brand(1L, "also_in_database", List.of(new Car()));

        when(brandRepository.findByName(brand.getName())).thenReturn(Optional.of(brand));

        assertFalse(brandService.addBrand(brand));
        verify(brandRepository, times(1)).findByName(brand.getName());
        verify(brandRepository, times(0)).save(brand);
        verifyNoMoreInteractions(brandRepository);
    }

    @Test(expected = NullPointerException.class)
    public void addBrand_tryWithEmptyBrand_NullPointerException() {
        Brand brand = new Brand();

        assertFalse(brandService.addBrand(brand));
        verify(brandRepository, times(0)).findByName(brand.getName());
        verify(brandRepository, times(0)).save(brand);
        verifyNoMoreInteractions(brandRepository);
    }

    @Test
    public void isBrandEmpty_tryWithEmpty_true() {
        Brand brand = new Brand();

        assertTrue(brandService.isBrandEmpty(brand));
    }

    @Test(expected = NullPointerException.class)
    public void isBrandEmpty_tryWithNull_NullPointerException() {
        assertTrue(brandService.isBrandEmpty(null));
    }

    @Test
    public void isBrandEmpty_tryWithNormalBrand_false() {
        Brand brand = new Brand(1L, "brand", List.of(new Car()));

        assertFalse(brandService.isBrandEmpty(brand));
    }

    @Test
    public void isBrandNotInDatabase_tryWithBrandNotInDB_true() {
        Brand brand = new Brand(1L, "brand", List.of(new Car()));

        when(brandRepository.findByName(brand.getName())).thenReturn(Optional.empty());

        assertTrue(brandService.isBrandNotInDatabase(brand));
        verify(brandRepository, times(1)).findByName(brand.getName());
        verifyNoMoreInteractions(brandRepository);
    }

    @Test
    public void isBrandNotInDatabase_tryWithBrandInDB_false() {
        Brand brand = new Brand(1L, "brand", List.of(new Car()));

        when(brandRepository.findByName(brand.getName())).thenReturn(Optional.of(brand));

        assertFalse(brandService.isBrandNotInDatabase(brand));
        verify(brandRepository, times(1)).findByName(brand.getName());
        verifyNoMoreInteractions(brandRepository);
    }

    @Test(expected = NullPointerException.class)
    public void isBrandNotInDatabase_tryWithNull_NullPointerException() {
        brandService.isBrandNotInDatabase(null);
    }

    @Test(expected = NullPointerException.class)
    public void isBrandNotInDatabase_tryWithEmptyBrand_NullPointerException() {
        Brand brand = new Brand();

        brandService.isBrandNotInDatabase(brand);
    }

    @Test
    public void save_checkWithNormalOrder_saved() {
        Brand brand = new Brand();

        brandService.save(brand);

        verify(brandRepository, times(1)).save(brand);
        verifyNoMoreInteractions(brandRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void save_checkWithOrderIsNull_IllegalArgumentException() {
        Brand brand = null;

        when(brandRepository.save(brand)).thenThrow(new IllegalArgumentException());

        brandService.save(brand);

        verify(brandRepository, times(1)).save(brand);
        verifyNoMoreInteractions(brandRepository);
    }

    @Test
    public void delete_checkWithCorrectUser_deleted() {
        Brand brand = new Brand();

        brandService.delete(brand);

        verify(brandRepository, times(1)).delete(brand);
        verifyNoMoreInteractions(brandRepository);
    }
}