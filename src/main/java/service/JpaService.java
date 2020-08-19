package service;

import configuration.jpa.JpaConfig;
import entity.jpa.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JpaService {

    private final EntityManager entityManager;

    public JpaService(JpaConfig jpaConfig) {
        this.entityManager = jpaConfig.getEntityManager();
    }

    public void test() {
        System.out.println("Test JpaService.");
    }

    public void savePersonInDb() {
        entityManager.getTransaction().begin();
        Person person = new Person();
        person.setAddress(new Address("Warszawa", "Wesoła 12", "12-040"));
        person.setBillingAddress(new Address("Wrocław", "Jana 2", "50-333"));
        person.setName("Marek");
        person.setSurname("Kowalski");
        entityManager.persist(person);
        entityManager.getTransaction().commit();
    }

    public Person getPersonFromDb(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> root = cq.from(Person.class);
        cq.select(root)
                .where(cb.equal(root.get(Person_.id), id));
        return entityManager.createQuery(cq).getSingleResult();
    }

    public Person getPersonFromDbByNativeSQL(Long id) {
        Query query = entityManager.createNativeQuery("select * from person p where p.id = ?1asas", Person.class);
        query.setParameter("1asas", id);
        return (Person) query.getSingleResult();
    }

    public Person getPersonFromDbByJpql(Long id) {
        TypedQuery<Person> query = entityManager.createQuery("select p from testujemyNazwe p where p.id = :idFieldName", Person.class);
        query.setParameter("idFieldName", id);
        return query.getSingleResult();
    }

    public void modifyPerson(Long id) {
        Person existingPerson = getPersonFromDb(id);
        entityManager.getTransaction().begin();
        existingPerson.setName("zmiana2");
        existingPerson = entityManager.merge(existingPerson);
//        entityManager.clear();
        existingPerson.setSurname("Dżubazzz");
        entityManager.getTransaction().commit();
    }

    public void addStudentsIntoDb(List<Student> students) {
        entityManager.getTransaction().begin();
        students.forEach(st -> entityManager.persist(st));
        entityManager.getTransaction().commit();
    }

    public List<Student> prepareStudentData() {
        Computer computer = new Computer();
        computer.setSerialNumber("XYZ123");
        computer.setDeviceName("Asus #1");
        computer.setLocalization("Krakow");
        Computer computer2 = new Computer();
        computer2.setSerialNumber("ABC123");
        computer2.setDeviceName("Asus #2");
        computer2.setLocalization("Warszawa");
        Author author = new Author();
        author.setName("Julian");
        author.setSurname("Tuwim");
        Author author2 = new Author();
        author2.setName("Jan");
        author2.setSurname("Brzechwa");
        Author author3 = new Author();
        author3.setName("Paulo");
        author3.setSurname("Coelho");
        Book book = new Book();
        book.setTitle("Wiersze");
        book.setAuthor(author);
        Book book2 = new Book();
        book2.setTitle("Akademia Pana kleksa");
        book2.setAuthor(author2);
        Book book3 = new Book();
        book3.setTitle("Lepszy nóż w plecy niż...");
        book3.setAuthor(author3);
        Student student = new Student();
        student.setName("Andrzej");
        student.setSurname("Duda");
        student.getBooks().add(book);
        book.setStudent(student);
        student.getComputers().add(computer);
        student.getComputers().add(computer2);
        computer.getStudents().add(student);
        computer2.getStudents().add(student);
        Student student2 = new Student();
        student2.setName("Aleksander");
        student2.setSurname("Kwasniewski");
        student2.getBooks().add(book2);
        book2.setStudent(student2);
        student2.getComputers().add(computer);
        computer.getStudents().add(student2);
        Student student3 = new Student();
        student3.setName("Krzysztof");
        student3.setSurname("Kononowicz");
        student3.getBooks().add(book3);
        book3.setStudent(student3);
        student3.getComputers().add(computer2);
        computer2.getStudents().add(student3);
        return Arrays.asList(student, student2, student3);
    }

    public List<Book> getBooksTakenStudentFromLocationFromDb(String loc) {
        Query query = entityManager.createNativeQuery(
                "SELECT b.* FROM book b " +
                        "join student s on b.student_id = s.id " +
                        "join computer_student cs on cs.student_id = s.id " +
                        "join computer c on cs.computer_id = c.id " +
                        "where c.localization = ?1 ", Book.class);
        query.setParameter(1, loc);
        return query.getResultList();
    }

    public List<BookInfo> getBooksInfoProjectionWithCriteriaBuilder(String loc) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookInfo> cq = cb.createQuery(BookInfo.class);
        Root<Book> bookRoot = cq.from(Book.class);
        Join<Book, Student> students = bookRoot.join("student");
        Join<Student, Computer> computers = students.join("computers");
        cq.select(cb.construct(BookInfo.class,
                bookRoot.get("title"),
                students.get("name"),
                students.get("surname")
        ));
        cq.where(cb.equal(computers.get("localization"), loc));
        return entityManager.createQuery(cq).getResultList();
    }

    public List<Book> getBooksTakenStudentFromLocationFromDbJpa(String loc) {
        TypedQuery<Book> query = entityManager.createQuery(
                "SELECT b FROM Book b " +
                        "join b.student s " +
                        "join s.computers c " +
                        "where c.localization = :location", Book.class);
        query.setParameter("location", loc);
        return query.getResultList();
    }

    public List<BookInfo> getBookInfoProjectionWithJpa(String loc) {
        TypedQuery<BookInfo> query = entityManager.createQuery(
                "SELECT new entity.jpa.BookInfo(b.title, s.name, s.surname)" +
                        "from Book b " +
                        "Join b.student s " +
                        "Join s.computers c " +
                        "where c.localization = :localization", BookInfo.class);
        query.setParameter("localization", loc);
        return query.getResultList();
    }

    public void lazyLoading() {
        entityManager.getTransaction().begin();
        CriteriaQuery<Student> query = entityManager.getCriteriaBuilder().createQuery(Student.class);
        Root<Student> sRoot = query.from(Student.class);
        query.select(sRoot);

        TypedQuery<Student> typedQuery = entityManager.createQuery(query);
        List<Student> students = typedQuery.getResultList();
        entityManager.close();
        List<Book> books = new ArrayList<>(students.get(0).getBooks());
    }

}
