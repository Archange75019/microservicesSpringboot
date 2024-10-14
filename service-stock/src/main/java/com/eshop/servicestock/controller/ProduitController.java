package com.eshop.servicestock.controller;

import com.eshop.servicestock.dto.ProduitDto;
import com.eshop.servicestock.model.Produit;
import com.eshop.servicestock.service.ProduitService;
import com.eshop.servicestock.util.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    @Autowired
    private DtoConverter dtoConverter;

    @PostMapping
    public ProduitDto createProduit(@RequestBody ProduitDto produitDto) {
        Produit produit = dtoConverter.convertToEntity(produitDto);
        return dtoConverter.convertToDto(produitService.save(produit));
    }

    @GetMapping("/{id}")
    public ProduitDto getProduitById(@PathVariable Long id) {
        Optional<Produit> produit = produitService.findById(id);
        return produit.map(dtoConverter::convertToDto).orElse(null);
    }

    @PutMapping("/{id}")
    public ProduitDto updateProduit(@PathVariable Long id, @RequestBody ProduitDto produitDto) {
        Produit produit = dtoConverter.convertToEntity(produitDto);
        produit.setId(id);
        return dtoConverter.convertToDto(produitService.save(produit));
    }

    @DeleteMapping("/{id}")
    public void deleteProduit(@PathVariable Long id) {
        produitService.deleteById(id);
    }

    @PostMapping("/{id}/acheter")
    public ProduitDto acheterProduit(@PathVariable Long id, @RequestParam int quantite) {
        Produit produit = produitService.acheterProduit(id, quantite);
        return dtoConverter.convertToDto(produit);
    }
}