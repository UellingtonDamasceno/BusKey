/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.util;

import br.uefs.ecomp.buskeyfx.util.AVLTree.Node;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Uellington Damasceno
 */
public class ArvoreAVLTest {

    @Test
    public void testContem() {
        AVLTree arvore = new AVLTree();
        for(int i = 0; i < 100; i++){
            arvore.inserir(i);
        }
        for(int i = 0; i < 100; i++){
            assertTrue(arvore.contem(i));
        }
        
    }

    @Test
    public void testRemover() {
        AVLTree arvore = new AVLTree();
        assertTrue(arvore.estaVazia());
        arvore.inserir(20);
        arvore.inserir(15);
        arvore.inserir(25);
        assertTrue(arvore.contem(20));
        arvore.inserir(23);
        assertTrue(arvore.contem(15));
        arvore.remover(15); // root is now unbalanced rotation performed
        assertFalse(arvore.contem(15));
        assertEquals(0, arvore.getRaiz().getDados().compareTo(23)); // new root
        assertEquals(0, arvore.getRaiz().getDireita().getDados().compareTo(20));
        assertEquals(0, arvore.getRaiz().getEsquerda().getDados().compareTo(25));
    }

    @Test
    public void testInsert() {
        AVLTree arvore = new AVLTree();
        arvore.inserir(20);
        arvore.inserir(15);
        arvore.inserir(25);
        arvore.inserir(22);
        arvore.inserir(21);
        assertEquals(0, arvore.getRaiz().getDados().compareTo(20));
        assertEquals(0, arvore.getRaiz().getEsquerda().getDados().compareTo(15));
        Node filhoDireito = (Node) arvore.getRaiz().getDireita();
        assertEquals(0, filhoDireito.getDados().compareTo(22));
        assertEquals(0, filhoDireito.getDireita().getDados().compareTo(25));
        assertEquals(0, filhoDireito.getEsquerda().getDados().compareTo(21));
    }

}
