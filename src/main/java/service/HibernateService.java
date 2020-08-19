package service;

import configuration.HibernateConfig;
import entity.jpa.Book;
import entity.jpa.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class HibernateService {

    private SessionFactory sessionFactory;

    public HibernateService(){
        this.sessionFactory = HibernateConfig.sessionFactory();
        System.out.println("sie udało");
    }

    public void lazyLoading(){
        Session session = sessionFactory.openSession();
        CriteriaQuery<Student> query = session.getCriteriaBuilder().createQuery(Student.class);
        Root<Student> sRoot = query.from(Student.class);
        sRoot.fetch("books");
        query.select(sRoot);

        TypedQuery<Student> typedQuery = session.createQuery(query);
        List<Student> students = typedQuery.getResultList();
        students.get(0).getBooks().size();
        students.get(0).getComputers().size();
        session.close();
        List<Book> books = new ArrayList<>(students.get(0).getBooks());
    }

    public void testCache() {
        Session session1 = sessionFactory.openSession();
        Student s1 = session1.byId(Student.class).load(15L);
        System.out.println("get 1");
        Student s2 = session1.byId(Student.class).load(15L);
        System.out.println("get 2");
        session1.clear();
        System.out.println("clear session");
        Student s3 = session1.byId(Student.class).load(15L);
        System.out.println("pobrałem czeciego");
    }
}
