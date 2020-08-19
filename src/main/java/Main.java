import configuration.SpringConfig;
import entity.jpa.Student;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.HibernateService;
import service.JpaService;
import service.SpringService;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello shit");
//
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        JpaService jpaService = context.getBean(JpaService.class);
        HibernateService hibernateService = context.getBean(HibernateService.class);
        SpringService springService = context.getBean(SpringService.class);
//        jpaService.test();
////        jpaService.savePersonInDb();
//        Person p = jpaService.getPersonFromDb(3L);
//        System.out.println(p.toString());
//
//        Person p2 = jpaService.getPersonFromDbByNativeSQL(3L);
//        System.out.println(p2.toString());
//        jpaService.modifyPerson(3L);
//
//    Person p3 = jpaService.getPersonFromDbByJpql(3L);
//        System.out.println(p3.toString());

//        List<Student> students = jpaService.prepareStudentData();
//        List<BookInfo> books = jpaService.getBooksInfoProjectionWithCriteriaBuilder("Krakow");
//        books.forEach(System.out::println);
        Student s = springService.getById(13L);
        Student s2 = new Student();
        s2.setName("Andrzej");
        s2.setSurname("Dziubas");
        springService.addStudent(s2);
        springService.getById(s2.getId());
        s = springService.getById(13L);

    }
}
