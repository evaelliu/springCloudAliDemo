package com.example.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProviderApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        Long lock1 = 111L;
        Long lock2 = 222L;
        for (int i=0;i<10;i++){
            Runnable runnable;
            SyncTest syncTest = new SyncTest();
            if((i&1)==0) {
                runnable = () -> {
                    syncTest.test(lock1);
                };
            }else {
                runnable = () -> {
                    syncTest.test(lock2);
                };
            }
            new Thread(runnable).start();
        }
    }

}
