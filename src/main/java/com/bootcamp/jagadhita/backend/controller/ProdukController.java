package com.bootcamp.jagadhita.backend.controller;

import com.bootcamp.jagadhita.backend.dto.ProdukDto;
import com.bootcamp.jagadhita.backend.entity.Produk;
import com.bootcamp.jagadhita.backend.service.ProdukService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produk")
@Slf4j

public class ProdukController {

    @Autowired
    ProdukService service;

    @GetMapping("/{id}")
    public ResponseEntity<?> findId(@PathVariable Integer id) {
        try {
            Produk produk = service.findId(id);
            return ResponseEntity.ok(produk);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body("Data tidak ditemukan");
        }
    }

    @GetMapping("")
    public List<Produk> findAll() {
        return service.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create
            (@RequestBody @Valid ProdukDto.Create produk,
             BindingResult result) {
        Map<String, Object> output = new HashMap<>();
        if (result.hasErrors()) {
            output.put("id", null);
            output.put("status", "Create Data Gagal");
            output.put("errors", result.getAllErrors());
            return ResponseEntity.badRequest().body(output);
        } else {
            output.put("id", service.create(produk));
            output.put("status", "Create Data Berhasil");
            return ResponseEntity.ok(output);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> update
            (@RequestBody @Valid ProdukDto.Update produk, BindingResult result) {
        Map<String, Object> output = new HashMap<>();
        if (result.hasErrors()) {
            output.put("status", "Update Data Gagal");
            output.put("errors", result.getAllErrors());
            return ResponseEntity.badRequest().body(output);
        } else {
            try {
                service.findId(produk.getId());
                service.update(produk);
                output.put("status", "Update Data Berhasil");
                return ResponseEntity.ok().body(output);
            } catch (EmptyResultDataAccessException e) {
                output.put("status", "Id Tidak Ditemukan");
                return ResponseEntity.badRequest().body(output);
            }
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> delete
            (@PathVariable Integer id) {
        Map<String, Object> output = new HashMap<>();
        try {
            service.findId(id);
            service.delete(id);
            output.put("status", "Delete Data Berhasil");
            return ResponseEntity.ok(output);
        } catch (EmptyResultDataAccessException e) {
            output.put("status", "Id Tidak Ditemukan");
            return ResponseEntity.badRequest().body(output);
        }
    }
}
