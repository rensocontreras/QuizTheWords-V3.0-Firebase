package com.contreras.myquizapplication.Entity.ItemEntity;

import com.contreras.myquizapplication.Entity.Nivel;

import java.io.Serializable;

public class NivelCompetidores implements Serializable {

    private int total;
    private int numero_nivel;

    public NivelCompetidores(){
        total=0;
    }

    public int getNumero_nivel() {
        return numero_nivel;
    }

    public void setNumero_nivel(int numero_nivel) {
        this.numero_nivel = numero_nivel;
    }

    public void incrementTotal(){
        total++;
    }

    public int getTotal(){
        return total;
    }

}
