/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.util;

import br.uefs.ecomp.buskeyfx.model.Palavra;
import java.util.Comparator;

/**
 *
 * @author Uellington Damasceno
 */
public class PalavraComparatorMaisBuscada implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Palavra aComparar = (Palavra) o1;
        Palavra comparada = (Palavra) o2;
        return (aComparar.getPesquisa() - comparada.getPesquisa());
    }
    
}
