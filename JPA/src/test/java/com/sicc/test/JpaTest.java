package com.sicc.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.sicc.pojo.Customer;

public class JpaTest {
	@Test
	public void test() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("myJpa");
		EntityManager em = factory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		Customer c = new Customer();
		c.setCustName("SICC");
		em.persist(c);
		tx.commit();
		em.close();
		factory.close();
	}
}
