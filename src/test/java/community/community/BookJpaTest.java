package community.community;

import community.community.domain.Book;
import community.community.repository.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookJpaTest {

    Logger logger = LoggerFactory.getLogger(BookJpaTest.class);

    private final static String BOOT_TEST_TITLE = "Spring boot Test Book";

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void book_저장하기_테스트(){
        Book book = Book.builder().title(BOOT_TEST_TITLE)
                                    .publishedAt(LocalDateTime.now()).build();
        testEntityManager.persist(book);
        assertThat(bookRepository.getOne(book.getIdx())).isEqualTo(book);
        logger.info("--------------------repository {}", bookRepository.getOne(book.getIdx()) );
        logger.info("--------------------------book {}", book );
    }

    @Test
    public void BookList_저장하고_검색_테스트(){
        Book book1 = Book.builder().title(BOOT_TEST_TITLE+"1")
                                    .publishedAt(LocalDateTime.now())
                                    .build();

        testEntityManager.persist(book1);
        Book book2 = Book.builder().title(BOOT_TEST_TITLE+"2")
                                    .publishedAt(LocalDateTime.now())
                                    .build();
        testEntityManager.persist(book2);
        Book book3 = Book.builder().title(BOOT_TEST_TITLE+"3")
                                    .publishedAt(LocalDateTime.now())
                                    .build();
        testEntityManager.persist(book3);

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(3);
        assertThat(bookList).contains(book1, book2, book3);
    }

    @Test
    public void BookList_저장하고_삭제_테스트(){
        Book book1 = Book.builder().title(BOOT_TEST_TITLE+"1")
                .publishedAt(LocalDateTime.now())
                .build();

        testEntityManager.persist(book1);
        Book book2 = Book.builder().title(BOOT_TEST_TITLE+"2")
                .publishedAt(LocalDateTime.now())
                .build();
        testEntityManager.persist(book2);

        bookRepository.deleteAll();;
        assertThat(bookRepository.findAll()).isEmpty();


    }
}
