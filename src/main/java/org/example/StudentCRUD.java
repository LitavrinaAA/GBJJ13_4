package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

//2. Подключить hibernate. Реализовать простые запросы: Find(by id), Persist, Merge, Remove
//3. Попробовать написать запрос поиска всех студентов старше 20 лет (session.createQuery)
public class StudentCRUD {
    public static void main(String[] args) {
        Test test = new Test();




        Student studentNew = test.persist(new Student("Wizard", "Potter", 24));

        Student student = test.find(studentNew.getId());
        System.out.println("Нашелся новый студент" +student);

        student.setAge(28);
        student.setSecondName("Just");
        test.merge(student);

        System.out.println("Измененный студент: " + test.find(student.getId()));

        test.remove(student);

        for (int i = 1; i < 11; i++) {
            test.persist(new Student("Wizard" + i, "Magic", 18+i));
        }
        test.selectByAge(20);
    }
}

class Test {
    Configuration configuration = new Configuration().configure()
            .addAnnotatedClass(Student.class);

    SessionFactory sessionFactory = configuration.buildSessionFactory();

    public Student find(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Student student = session.find(Student.class, id);
            session.getTransaction().commit();
            return student;

        }
    }

    public Student persist(Student student) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(student);
            session.getTransaction().commit();
            return student;

        }
    }

    public void merge(Student student) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(student);
            session.getTransaction().commit();

        }
    }

    public void remove(Student student) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(student);
            session.getTransaction().commit();

        }
    }

    public  void selectByAge(int ageSelect) {
        Configuration configuration = new Configuration().configure()
                .addAnnotatedClass(Student.class);

        SessionFactory sessionFactory = configuration.buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Student> query = session.createQuery("select p from Student p where age > :ageParam");
            query.setParameter("ageParam", ageSelect);
            List<Student> students = query.getResultList();
            for (Student student: students) {
                System.out.println(student.getFirstName() + " " + student.getAge());
            }
            session.getTransaction().commit();
        }
    }
}