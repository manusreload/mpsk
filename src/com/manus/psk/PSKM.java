/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manus.psk;

import java.util.logging.Level;
import java.util.logging.Logger;
import sun.security.pkcs.PKCS10;

/**
 *
 * @author Manus
 */
public class PSKM {

    static long perms = 100000000;
    public static long count = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        byte[] output = new byte[100];
        //printByte(data, data.length);
        byte[] key = new byte[]{
            0, 0, 2,
            114, 12, 15, 8, 3, 120
        };

        byte[] data = new byte[]{
            1,2,3,4,5,6,7,8,9,0
        };

        long t = System.currentTimeMillis();
        printByte(data, data.length);
        int l = PSK3.encode(key, data, output);
        printByte(output, l);
        byte[] b2 = new byte[l];
        for(int i = 0; i < l; i++)
        {
            b2[i] = output[i];
        }
        data = new byte[100];
        l = PSK3.encode(key, b2, data);
        printByte(data, l );
//        while (count < perms) {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(PSKM.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        long time = System.currentTimeMillis() - t;
        System.out.println(time + " ms ");// + perms / time + " ms por permutación");
    }

    public static void printByte(byte[] arr, int leng) {
        for (int i = 0; i < leng; i++) {
            System.out.println(arr[i] & 0xFF);
        }
        System.out.println("");
    }

    public static void printMatrix(byte[] data, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int p = i * cols + j;
                System.out.print(data[p] + "\t");
            }
            System.out.println("");
        }
        System.out.println("");
    }
}
