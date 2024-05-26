package com.dna.mockito.mockito_demo.business;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SomeBusinessImplStubTest {
    @Test
    void findTheGreatestFromAllData_basicScenario() {
        SomeBusinessImpl businessImpl = new SomeBusinessImpl(new DataServiceStub1());
        int result = businessImpl.findTheGreatestFromAllData();
        assertEquals(25, result);
    }
    
    @Test
    void findTheGreatestFromAllData_withOneValue() {
        SomeBusinessImpl businessImpl = new SomeBusinessImpl(new DataServiceStub2());
        int result = businessImpl.findTheGreatestFromAllData();
        assertEquals(35, result);
    }
}

class DataServiceStub1 implements DataService {
    @Override
    public int[] retrieveAllData() {
        return new int[] {25, 15, 5};
    }
}

class DataServiceStub2 implements DataService {
    @Override
    public int[] retrieveAllData() {
        return new int[] {35};
    }
}