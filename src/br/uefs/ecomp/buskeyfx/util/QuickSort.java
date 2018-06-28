package br.uefs.ecomp.buskeyfx.util;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * Classe responsável por criar objetos capazes de ordenar dados.
 *
 * @author Uellington Damasceno e Anésio Sousa
 */
public class QuickSort {

    
    /**
     * Método utilizado para ordenar coleções.
     *
     * @param vetor Comprable[] que será ordenado.
     * @param inicio posição inicial que será utilizada para a ordenação
     * @param fim posição correspondente ao tamanho e ou posição final da ordenação.
     */
    public void quickSort(Comparable[] vetor, int inicio, int fim) {
        if (inicio < fim) {
            int pe = inicio;
            int pivo = fim;
            int pd = fim - 1;
            while (pe <= pd) {
                while (pe <= pd && vetor[pe].compareTo(vetor[pivo]) > 0) {
                    pe++;
                }
                while (pe <= pd && vetor[pd].compareTo(vetor[pivo]) < 0) {
                    pd--;
                }
                if (pe <= pd) {
                    swap(vetor, pe, pd);
                    pe++;
                    pd--;
                }
            }
            swap(vetor, pe, pivo);
            quickSort(vetor, inicio, pe - 1);
            quickSort(vetor, pe + 1, fim);
        }
    }

    public void quickSort(Comparable[] vetor, int inicio, int fim, Comparator comparador) {
        if (inicio < fim) {
            int pe = inicio;
            int pivo = fim;
            int pd = fim - 1;
            while (pe <= pd) {
                while (pe <= pd && comparador.compare(vetor[pe], vetor[pivo]) > 0) {
                    pe++;
                }
                while (pe <= pd && comparador.compare(vetor[pd], vetor[pivo]) < 0) {
                    pd--;
                }
                if (pe <= pd) {
                    swap(vetor, pe, pd);
                    pe++;
                    pd--;
                }
            }
            swap(vetor, pe, pivo);
            quickSort(vetor, inicio, pe - 1);
            quickSort(vetor, pe + 1, fim);
        }
    }

    /**
     * Método capaz de trocar dois objetos de posição em um array.
     *
     * @param vetor Vetor que terá as os objetos trocas.
     * @param p1 Posição do primeiro objeto.
     * @param p2 Posição do segundo objeto.
     */
    private void swap(Object[] vetor, int p1, int p2) {
        Object carta = vetor[p1];
        vetor[p1] = vetor[p2];
        vetor[p2] = carta;
    }
}
