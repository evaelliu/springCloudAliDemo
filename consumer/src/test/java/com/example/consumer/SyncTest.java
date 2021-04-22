package com.example.consumer;

public class SyncTest {

    private int i = 0;
    public void test(Object lock){
        synchronized (lock) {
            System.out.println(System.currentTimeMillis()+"======="+Thread.currentThread().getName()+"==========="+lock.toString()+"=========="+i++);
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
