package com.eshop.servicestock.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProduitDto {
    private Long id;
    private String nom;
    private String description;
    private double prixAchat;
    private double prixVente;
    private int quantiteStock;
    private int quantiteSurface;
    private LocalDate dateCommande;
    private LocalDate dateLivraison;
    private int nbJoursEntreCommandeEtLivraison;

    public int getNbJoursEntreCommandeEtLivraison() {
        if (dateCommande != null && dateLivraison != null) {
            return Math.toIntExact(java.time.temporal.ChronoUnit.DAYS.between(dateCommande, dateLivraison));
        }
        return 0;
    }
}
