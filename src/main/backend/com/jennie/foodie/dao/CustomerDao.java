package com.jennie.foodie.dao;


import com.jennie.foodie.entity.Customer;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import com.jennie.foodie.entity.Authorities;
@Repository
public class CustomerDao {
    @Autowired
    private SessionFactory sessionFactory;
    public void signUp(Customer customer) {
        Session session = null; //用hibernate进行增删改查
        Authorities authorities = new Authorities();
        authorities.setEmail(customer.getEmail());
        authorities.setAuthorities("ROLE_USER");


//保证操作要么成功 要么失败 原子性 unity
        try {//avoid if when we change multiple tables something goes wrong during the whole process
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(authorities);//save the record in the db
            session.save(customer);//save the record in the db
            session.getTransaction().commit();//let db to process the save steps

        } catch (Exception ex) {
            ex.printStackTrace();
            if (session != null) session.getTransaction().rollback();//if sth wrong the transaction will help us to delete the data that is already created
        } finally {
            if (session != null) {
                session.close();//close the session when the process is commit and succeed
            }
        }

    }

    public Customer getCustomer(String email) {
        Customer customer = null;
        try (Session session = sessionFactory.openSession()) {//try with resource
            customer = session.get(Customer.class, email);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return customer;
    }
    //regular way to do it
//    Customer customer = null;
//    Session session = null;
//    try{
//        session = sessionFactory.openSession();
//        customer = session.get(Customer.class, email);
//    }catch (Exception ex) {
//        ex.printStackTrace();;
//
//    }finally {
//        if (session != null) session.close();
//    }
//    return customer;

}


