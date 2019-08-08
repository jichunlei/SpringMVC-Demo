package cn.jicl.converter;

import cn.jicl.pojo.Book;
import cn.jicl.pojo.Person;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/8 08:19
 * @Description: 自定义的类型转换器String->Book
 */
@Component
public class MyConverter implements Converter<String, Book> {
    @Override
    public Book convert(String source) {
        if(source==null){
            return null;
        }
        if(!source.contains("-")){
            return null;
        }
        String[] strings = source.split("-");
        Book book = new Book();
        Person person = new Person();
        person.setName(strings[0]);
        book.setAuthor(person);
        book.setBookName(strings[1]);
        book.setPrice(Double.valueOf(strings[2]));
        return book;
    }
}
