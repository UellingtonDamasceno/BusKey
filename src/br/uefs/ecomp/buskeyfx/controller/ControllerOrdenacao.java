/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.controller;

import br.uefs.ecomp.buskeyfx.util.PaginaComparatorMaiorOcorrencia;
import br.uefs.ecomp.buskeyfx.util.PaginaComparatorMaisAcessos;
import br.uefs.ecomp.buskeyfx.util.PalavraComparatorMaisBuscada;
import br.uefs.ecomp.buskeyfx.util.QuickSort;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

/**
 *
 * @author Uellington Damasceno
 */
public class ControllerOrdenacao {

    private final QuickSort quick;
    private Comparable[] array;

    public ControllerOrdenacao(){
        quick = new QuickSort();
    }

    public LinkedList ordenar(LinkedList aOrdenar, String ordem){
        Comparator comparador;
        array = new Comparable[aOrdenar.size()];
    
        if(ordem.contains("+")){
            if(ordem.contains("A")){
                comparador =  new PaginaComparatorMaisAcessos();
            }else if(ordem.contains("B")){
                comparador = new PalavraComparatorMaisBuscada();
            }else{
                comparador = new PaginaComparatorMaiorOcorrencia();
            }
            ordem = "+";
        }else{
            if(ordem.contentEquals("A")){
                comparador = new PaginaComparatorMaisAcessos();
            }else if(ordem.contains("B")){
                comparador = new PalavraComparatorMaisBuscada();
            }else{
                comparador = new PaginaComparatorMaiorOcorrencia();
            }
            ordem = "-";
        }
        
        aOrdenar.toArray(array);
        quick.quickSort(array, comparador);
        return arrayToList(array, ordem);
    }
    
    private LinkedList arrayToList(Comparable[] array, String ordem) {
        LinkedList aux = new LinkedList();
        for (Comparable atual : array) {
            switch (ordem) {
                case "+": {
                    aux.addLast(atual);
                    break;
                }
                case "-": {
                    aux.addFirst(atual);
                    break;
                }
            }
        }
        return aux;
    }
}
