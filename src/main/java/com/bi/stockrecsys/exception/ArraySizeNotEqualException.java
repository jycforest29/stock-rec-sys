package com.bi.stockrecsys.exception;

public class ArraySizeNotEqualException extends RuntimeException{
    public ArraySizeNotEqualException(){
        super("두 벡터의 길이가 달라 코사인 유사도 계산 불가");
    }
}
