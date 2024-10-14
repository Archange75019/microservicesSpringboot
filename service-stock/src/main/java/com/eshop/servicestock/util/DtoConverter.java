package com.eshop.servicestock.util;

import com.eshop.servicestock.dto.ProduitDto;
import com.eshop.servicestock.model.Produit;
import org.springframework.stereotype.Component;

@Component
public class DtoConverter {

    public ProduitDto convertToDto(Produit produit) {
        ProduitDto produitDto = new ProduitDto();
        produitDto.setId(produit.getId());
        produitDto.setNom(produit.getNom());
        produitDto.setDescription(produit.getDescription());
        produitDto.setPrixAchat(produit.getPrixAchat());
        produitDto.setPrixVente(produit.getPrixVente());
        produitDto.setQuantiteStock(produit.getQuantiteStock());
        produitDto.setQuantiteSurface(produit.getQuantiteSurface());
        produitDto.setDateCommande(produit.getDateCommande());
        produitDto.setDateLivraison(produit.getDateLivraison());
        produitDto.setNbJoursEntreCommandeEtLivraison(produit.getNbJoursEntreCommandeEtLivraison());
        return produitDto;
    }

    public Produit convertToEntity(ProduitDto produitDto) {
        Produit produit = new Produit();
        produit.setNom(produitDto.getNom());
        produit.setDescription(produitDto.getDescription());
        produit.setPrixAchat(produitDto.getPrixAchat());
        produit.setPrixVente(produitDto.getPrixVente());
        produit.setQuantiteStock(produitDto.getQuantiteStock());
        produit.setQuantiteSurface(produitDto.getQuantiteSurface());
        produit.setDateCommande(produitDto.getDateCommande());
        produit.setDateLivraison(produitDto.getDateLivraison());
        produit.setNbJoursEntreCommandeEtLivraison(produitDto.getNbJoursEntreCommandeEtLivraison());
        return produit;
    }
}
