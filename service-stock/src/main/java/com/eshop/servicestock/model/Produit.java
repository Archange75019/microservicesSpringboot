package com.eshop.servicestock.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "produit")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void setNbJoursEntreCommandeEtLivraison(int nbJoursEntreCommandeEtLivraison) {
        this.nbJoursEntreCommandeEtLivraison = nbJoursEntreCommandeEtLivraison;
    }
}
