package com.eshop.servicestock.service;

import com.eshop.servicestock.model.Produit;
import com.eshop.servicestock.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepository;

    public List<Produit> findAll() {
        return produitRepository.findAll();
    }

    public Optional<Produit> findById(Long id) {
        return produitRepository.findById(id);
    }

    public Produit save(Produit produit) {
        return produitRepository.save(produit);
    }

    public void deleteById(Long id) {
        produitRepository.deleteById(id);
    }

    @Transactional
    public Produit acheterProduit(Long id, int quantite) {
        Optional<Produit> produitOptional = produitRepository.findById(id);
        if (produitOptional.isPresent()) {
            Produit produit = produitOptional.get();

            // Vérifiez si la quantité demandée est disponible
            if (produit.getQuantiteStock() >= quantite) {
                produit.setQuantiteStock(produit.getQuantiteStock() - quantite);
                produit.setQuantiteSurface(produit.getQuantiteSurface() - quantite);
                return produitRepository.save(produit);
            } else {
                throw new IllegalArgumentException("Quantité demandée non disponible en stock");
            }
        } else {
            throw new IllegalArgumentException("Produit non trouvé");
        }
    }
}
