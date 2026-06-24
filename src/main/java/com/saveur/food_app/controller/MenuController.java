package com.saveur.food_app.controller;

import com.saveur.food_app.model.MenuItem;
import com.saveur.food_app.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = {"http://localhost:3000", "https://saveur-frontend.vercel.app"})
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    public List<MenuItem> getAvailable() {
        return menuService.getAvailableItems();
    }

    @GetMapping("/all")
    public List<MenuItem> getAll() {
        return menuService.getAllItems();
    }

    @PostMapping
    public MenuItem addItem(@RequestBody MenuItem item) {
        return menuService.addItem(item);
    }

    @PutMapping("/{id}")
    public MenuItem updateItem(@PathVariable Long id, @RequestBody MenuItem item) {
        return menuService.updateItem(id, item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        menuService.deleteItem(id);
    }

    @PatchMapping("/{id}/toggle")
    public MenuItem toggleAvailability(@PathVariable Long id) {
        return menuService.toggleAvailability(id);
    }
}