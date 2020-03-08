package com.inspur.crawler.test;

/**
 * @author wang.ning
 * @create 2020-02-26 18:33
 */
public class Test {


    public static void main(String[] args) {
        HelloThread helloThread = new HelloThread();
        new Thread(helloThread).start();
        new Thread(helloThread).start();
    }
}

class HelloThread implements Runnable{


    int i = 1;

    @Override
    public void run() {
        synchronized (this){
            this.test();
        }
    }

    private void test(){
        for(;i<=200;i++){
            System.out.println(Thread.currentThread()+":"+i);
        }
    }
}
