package com.zxrh.ehomework;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EhomeworkApplicationTests {

	@Test
	public void contextLoads(){
		List<Integer> primes = new ArrayList<>();
		int bound = 100;
		long t1 = System.currentTimeMillis();
		for(int number=2;number<=bound;number++){
			if(primes.size()==0){
				primes.add(number);
			}else{
				for(int i=0;i<primes.size();i++){
					int prime = primes.get(i);
					if(prime*prime<=number){
						if(number%prime==0){
							break;
						}
					}else{
						primes.add(number);
						break;
					}
				}
			}
		}
		long t2 = System.currentTimeMillis();
		System.err.println(t2-t1);
		System.err.println(primes);
		System.err.println((float)primes.size()/bound);
	}
	
}