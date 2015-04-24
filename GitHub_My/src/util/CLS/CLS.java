package util.CLS;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zhen
 */
public class CLS {

    public  native void clear();

    static {
        System.loadLibrary("clear");
    }
}
