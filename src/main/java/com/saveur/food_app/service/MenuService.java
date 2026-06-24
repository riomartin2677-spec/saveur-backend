package com.saveur.food_app.service;

import com.saveur.food_app.model.MenuItem;
import com.saveur.food_app.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<MenuItem> getAllItems() {
        return menuRepository.findAll();
    }

    public List<MenuItem> getAvailableItems() {
        return menuRepository.findByAvailableTrue();
    }

    public MenuItem addItem(MenuItem item) {
        return menuRepository.save(item);
    }

    public MenuItem updateItem(Long id, MenuItem updated) {
        MenuItem item = menuRepository.findById(id).orElseThrow();
        item.setName(updated.getName());
        item.setDescription(updated.getDescription());
        item.setEmoji(updated.getEmoji());
        item.setCategory(updated.getCategory());
        item.setPrice(updated.getPrice());
        item.setPrepTime(updated.getPrepTime());
        item.setAvailable(updated.isAvailable());
        return menuRepository.save(item);
    }

    public void deleteItem(Long id) {
        menuRepository.deleteById(id);
    }

    public MenuItem toggleAvailability(Long id) {
        MenuItem item = menuRepository.findById(id).orElseThrow();
        item.setAvailable(!item.isAvailable());
        return menuRepository.save(item);
    }
}