package com.boot.init;

import com.boot.entity.Item;
import com.boot.entity.Role;
import com.boot.repository.ItemRepository;
import com.boot.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static com.boot.entity.Role.Name.ROLE_ADMIN;
import static com.boot.entity.Role.Name.ROLE_USER;

@Component
public class DataLoader implements ApplicationRunner {

    private RoleRepository roleRepository;
    private ItemRepository itemRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository, ItemRepository itemRepository) {
        this.roleRepository = roleRepository;
        this.itemRepository = itemRepository;
    }

    public void run(ApplicationArguments args) {
        roleRepository.save(new Role(ROLE_ADMIN));
        roleRepository.save(new Role(ROLE_USER));

        itemRepository.save(new Item(1L,"ASUS TUF Gaming F15 FX506HC-HN011","15.6\" 1920 x 1080 IPS, 144 Гц, несенсорный, Intel Core i5 11400H 2700 МГц, 8 ГБ, SSD 512 ГБ, видеокарта NVIDIA GeForce RTX 3050 Max-Q 4 ГБ, без ОС, цвет крышки черный","intel"));
        itemRepository.save(new Item(2L,"Lenovo IdeaPad Gaming 3 15ARH05 82EY00F6RE","15.6\" 1920 x 1080 IPS, 120 Гц, несенсорный, AMD Ryzen 5 4600H 3000 МГц, 16 ГБ, SSD 512 ГБ, видеокарта NVIDIA GeForce GTX 1650 Ti 4 ГБ, Windows 10, цвет крышки черный","lenovo,ryzen,16,nvidia"));
        itemRepository.save(new Item(3L,"HP Pavilion 15-eg0045ur 2P1P2EA","15.6\" 1920 x 1080 IPS, 60 Гц, несенсорный, Intel Core i3 1115G4 3000 МГц, 8 ГБ, SSD 256 ГБ, видеокарта встроенная, без ОС, цвет крышки серебристый","hp,2p1p2ea,15,6,60,intel"));
        itemRepository.save(new Item(4L,"a","a","a"));
    }
}
