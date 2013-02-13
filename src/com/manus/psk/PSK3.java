/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manus.psk;

/**
 *
 * @author Manus
 */
public class PSK3 {
    
    public static class Chunk
    {
        byte[] data;
        int leng;
    }
    public static int calculate_chunks(int bits, int chunk_size)
    {
        int chunks_count = bits / chunk_size;
        if(bits % chunk_size > 0)
            chunks_count ++;
        return chunks_count;
    }
    public static int chunks(byte[] bits, int rows, int cols, Chunk[] chunks)
    {
        int n = 0;
        int split = (rows * cols);
        int chunks_count = bits.length / split;
        for(int i = 0; i < chunks_count; i++)
        {
            chunks[i] = new Chunk();
            chunks[i].data = new byte[split];
            for(int j = 0; j < split; j++)
            {
                chunks[i].data[j] = bits[n++];
            }
        }
        int rest = bits.length % split;
        if(rest > 0)
        {
            chunks[chunks_count] = new Chunk();
            chunks[chunks_count].data = new byte[split];
            for(int i = 0; i < rest; i++)
            {
                chunks[chunks_count].data[i] = bits[n++];
            }
            for(int i =rest; i < (split); i++)
            {
                chunks[chunks_count].data[i] = 0;
            }
            chunks_count++;
        }
        
        return chunks_count;
    }
    
    public static int getchunks(Chunk[] chunks, byte[] bits)
    {
        int n = 0;
        
        for(int i = 0; i < chunks.length; i++)
        {
            for(int j = 0; j < chunks[i].data.length; j++)
            {
                bits[n++] = chunks[i].data[j];
            }
        }
        return (n + n % 8);
    }
    public static int bitting(byte[] input, int leng, byte[] output)
    {
        for(int i = 0; i < leng; i++)
        {
            byte b = input[i];
            output[(i * 8) + 0] = (byte)((b >> 7) & 0x01);
            output[(i * 8) + 1] = (byte)((b >> 6) & 0x01);
            output[(i * 8) + 2] = (byte)((b >> 5) & 0x01);
            output[(i * 8) + 3] = (byte)((b >> 4) & 0x01);
            output[(i * 8) + 4] = (byte)((b >> 3) & 0x01);
            output[(i * 8) + 5] = (byte)((b >> 2) & 0x01);
            output[(i * 8) + 6] = (byte)((b >> 1) & 0x01);
            output[(i * 8) + 7] = (byte)((b) & 0x01);
        }
        return leng * 8;
    }
    
    public static int unbitting(byte[] input, int leng, byte[] output)
    {
        int t = 0;
        for(int i = 0; i < leng; i+=8)
        {
            byte b = 0;
            b ^= input[(i  + 0)] << 7;
            b ^= input[(i  + 1)] << 6;
            b ^= input[(i  + 2)] << 5;
            b ^= input[(i  + 3)] << 4;
            b ^= input[(i  + 4)] << 3;
            b ^= input[(i  + 5)] << 2;
            b ^= input[(i  + 6)] << 1;
            b ^= input[(i  + 7)];
            output[t++] = b;
        }
        return t;
    }
    
    public static void permutate_row(byte[] matrix, int rows, int cols, int row)
    {
        if(row >= rows)
        {
            row = row % rows;
        }
        int o,n;
        for(int j = 0; j < cols; j++)
        {
            o = row * cols + j;
            n = ((row + 1) % rows) * cols + j;
            byte aux = matrix[o];
            matrix[o] = matrix[n];
            matrix[n] = aux;
            
        }
    }
    public static void permutate_colum(byte[] matrix, int rows, int cols, int colum)
    {
        if(colum >= cols)
        {
            colum = colum % cols;
        }
        int o,n;
        for(int i = 0; i < rows; i++)
        {
            o = i * cols + colum;
            n = i * cols + ((colum + 1) % cols);
            byte aux = matrix[o];
            matrix[o] = matrix[n];
            matrix[n] = aux;
            
        }
    }
    
    public static int encode(byte[] key, byte[] input, byte[] output)
    {
        int k = key[1] << 8;
        k ^= key[2];
        int l = key.length - 3;
        int rows = l / k;
        int cols = k + (l % k);
        byte[] bits = new byte[input.length * 8];
        bitting(input, input.length, bits);
        Chunk[] chunks = new Chunk[calculate_chunks(bits.length,(rows * cols))];
        int chunks_count = chunks(bits,rows, cols,chunks);
        for(int c = 0; c < chunks_count; c++)
        {
            for(int p =0; p < (rows * cols); p++)
            {
                if(p % 2 == 0) //Row
                {
                    permutate_row(chunks[c].data, rows, cols, (p / 2) % rows);
                }
                else //Cols
                {
                    permutate_colum(chunks[c].data, rows, cols, ((p - 1) / 2) % cols);
                }
            }
        }
        bits = new byte[chunks_count * rows * cols + ((rows * cols) % 8)];
        int t = getchunks(chunks, bits);
        return unbitting(bits, t, output);
    }
    
}
